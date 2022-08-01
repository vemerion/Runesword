package mod.vemerion.runesword.guide;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import mod.vemerion.runesword.api.IGuideChapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class GuideChapter implements IGuideChapter {

	private static final int ICON_SIZE = 16;
	private static final int COMPONENT_PADDING = 6;

	private ItemLike itemIcon;
	private ResourceLocation rlIcon;
	private HeaderComponent title;
	private List<GuideChapter> children = new ArrayList<>();
	private List<ChapterComponent> components = new ArrayList<>();

	public GuideChapter(ItemLike icon, Component title) {
		this.itemIcon = icon;
		this.title = new HeaderComponent(title);
	}

	public GuideChapter(ResourceLocation icon, Component title) {
		this.rlIcon = icon;
		this.title = new HeaderComponent(title);
	}

	public static void throwIfInvalidChapter(IGuideChapter chapter) {
		if (!(chapter instanceof GuideChapter))
			throw new IllegalArgumentException("openGuide() parameter is not of type GuideChapter");

	}

	@Override
	public GuideChapter addChild(IGuideChapter child) {
		throwIfInvalidChapter(child);
		children.add((GuideChapter) child);
		return this;
	}

	@Override
	public GuideChapter addText(String translationKey) {
		components.add(new TextComponent(new TranslatableComponent(translationKey)));
		return this;
	}

	@Override
	public GuideChapter addHeader(String translationKey) {
		components.add(new HeaderComponent(new TranslatableComponent(translationKey)));
		return this;
	}

	@Override
	public GuideChapter addImage(ResourceLocation image, int imgWidth, int imgHeight) {
		components.add(new ImageComponent(image, imgWidth, imgHeight));
		return this;
	}

	public Component getTitle() {
		return title.text;
	}

	public void renderTitle(PoseStack poseStack, Minecraft mc, int x, int y, int width, int height) {
		title.render(poseStack, mc, x, y, y, width, height);
	}

	public boolean mouseClicked(int x, int y, int top, int width, int height, double mouseX, double mouseY, int button,
			Consumer<GuideChapter> changeChapter) {
		int left = x;
		for (int i = 0; i < children.size(); i++) {
			if (isInside(x, y, ICON_SIZE, mouseX, mouseY) && button == 0 && isInsideScreen(y, ICON_SIZE, top, height)) {
				changeChapter.accept(children.get(i));
				return true;
			}
			x += ICON_SIZE;
			if (x + ICON_SIZE > left + width && i + 1 != children.size()) {
				x = left;
				y += ICON_SIZE;
			}
		}
		return false;
	}

	public int renderComponents(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height,
			int mouseX, int mouseY) {
		int left = x;
		for (int i = 0; i < children.size(); i++) {
			GuideChapter c = children.get(i);
			if (isInsideScreen(y, ICON_SIZE, top, height))
				c.renderIcon(poseStack, mc, x, y, width, height, mouseX, mouseY);
			x += ICON_SIZE;
			if (x + ICON_SIZE > left + width && i + 1 != children.size()) {
				x = left;
				y += ICON_SIZE;
			}
		}

		x = left;
		y += children.isEmpty() ? COMPONENT_PADDING : ICON_SIZE + COMPONENT_PADDING;
		for (ChapterComponent comp : components) {
			y = comp.render(poseStack, mc, x, y, top, width, height) + COMPONENT_PADDING;
		}
		return y;
	}

	private void renderIcon(PoseStack poseStack, Minecraft mc, int x, int y, int width, int height, int mouseX,
			int mouseY) {
		if (isInside(x, y, ICON_SIZE, mouseX, mouseY)) {
			mc.screen.renderTooltip(poseStack, title.text, mouseX, mouseY);
		}

		if (itemIcon != null) {
			mc.getItemRenderer().renderAndDecorateItem(new ItemStack(itemIcon), x, y);
		} else {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, rlIcon);
			Screen.blit(poseStack, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
		}
	}

	private boolean isInside(int left, int top, int size, double x, double y) {
		return x > left && x < left + size && y > top && y < top + size;
	}

	private static boolean isInsideScreen(int y, int size, int top, int height) {
		return y >= top && y + size < top + height;
	}

	private static interface ChapterComponent {
		int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height);
	}

	private static class TextComponent implements ChapterComponent {

		private static final int COLOR = Color.BLACK.getRGB();

		private Component text;

		private TextComponent(Component text) {
			this.text = text;
		}

		@Override
		public int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height) {
			var font = mc.font;
			var lines = font.split(text, width);
			for (var line : lines) {
				if (isInsideScreen(y, font.lineHeight, top, height))
					font.draw(poseStack, line, x, y, COLOR);
				y += font.lineHeight;
			}
			return y;
		}

	}

	private static class HeaderComponent implements ChapterComponent {

		private static final int COLOR = Color.BLACK.getRGB();
		private static final float SCALE = 1.3f;

		private Component text;

		private HeaderComponent(Component text) {
			this.text = text;
		}

		@Override
		public int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height) {
			var font = mc.font;

			var lines = font.split(text, (int) (width / SCALE));
			for (var line : lines) {
				float textWidth = font.width(line) * SCALE;
				poseStack.pushPose();
				poseStack.scale(SCALE, SCALE, 1);
				if (isInsideScreen(y, (int) (font.lineHeight * SCALE), top, height))
					font.draw(poseStack, line, (x + width / 2 - textWidth / 2) / SCALE, y / SCALE, COLOR);
				y += font.lineHeight * SCALE;
				poseStack.popPose();
			}
			return y + 2;
		}

	}

	private static class ImageComponent implements ChapterComponent {

		private static final float SCALE = 0.6f;

		private ResourceLocation image;
		private int imgWidth;
		private int imgHeight;

		private ImageComponent(ResourceLocation image, int imgWidth, int imgHeight) {
			this.image = image;
			this.imgWidth = imgWidth;
			this.imgHeight = imgHeight;
		}

		@Override
		public int render(PoseStack poseStack, Minecraft mc, int x, int y, int top, int width, int height) {
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderColor(1, 1, 1, 1);
			RenderSystem.setShaderTexture(0, image);
			float scale = (float) width / imgWidth * SCALE;
			int drawWidth = (int) (imgWidth * scale);
			int drawHeight = (int) (imgHeight * scale);
			int offset = drawHeight + 2;

			// Do not draw if outside guide screen
			if (y + drawHeight <= top || y >= top + height)
				return y + offset;

			int vHeight = imgHeight;
			int v = 0;
			int drawY = y;

			// Cut of image top
			if (y < top && y + drawHeight > top) {
				float ratio = (y + drawHeight - top) / (float) drawHeight;
				v += imgHeight * (1 - ratio);
				drawY += drawHeight * (1 - ratio);
				vHeight = (int) (ratio * vHeight);
				drawHeight = (int) (ratio * drawHeight);
			}

			// Cut of image bottom
			if (y + drawHeight > top + height) {
				float ratio = 1 - ((y + drawHeight - (top + height)) / (float) drawHeight);
				drawHeight = (int) (drawHeight * ratio);
				vHeight = (int) (vHeight * ratio);
			}
			Screen.blit(poseStack, x + width / 2 - drawWidth / 2, drawY, drawWidth, drawHeight, 0, v, imgWidth,
					vHeight, imgWidth, imgHeight);
			return y + offset;
		}

	}
}