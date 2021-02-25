package mod.vemerion.runesword.guide;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.runesword.api.IGuideChapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class GuideChapter implements IGuideChapter {

	private static final int ICON_SIZE = 16;
	private static final int COMPONENT_PADDING = 6;

	private IItemProvider itemIcon;
	private ResourceLocation rlIcon;
	private HeaderComponent title;
	private List<GuideChapter> children = new ArrayList<>();
	private List<ChapterComponent> components = new ArrayList<>();

	public GuideChapter(IItemProvider icon, ITextComponent title) {
		this.itemIcon = icon;
		this.title = new HeaderComponent(title);
	}

	public GuideChapter(ResourceLocation icon, ITextComponent title) {
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
		components.add(new TextComponent(new TranslationTextComponent(translationKey)));
		return this;
	}

	@Override
	public GuideChapter addHeader(String translationKey) {
		components.add(new HeaderComponent(new TranslationTextComponent(translationKey)));
		return this;
	}

	@Override
	public GuideChapter addImage(ResourceLocation image, int imgWidth, int imgHeight) {
		components.add(new ImageComponent(image, imgWidth, imgHeight));
		return this;
	}

	public ITextComponent getTitle() {
		return title.text;
	}

	public void renderTitle(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height) {
		title.render(matrix, mc, x, y, y, width, height);
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

	public int renderComponents(MatrixStack matrix, Minecraft mc, int x, int y, int top, int width, int height,
			int mouseX, int mouseY) {
		int left = x;
		for (int i = 0; i < children.size(); i++) {
			GuideChapter c = children.get(i);
			if (isInsideScreen(y, ICON_SIZE, top, height))
				c.renderIcon(matrix, mc, x, y, width, height, mouseX, mouseY);
			x += ICON_SIZE;
			if (x + ICON_SIZE > left + width && i + 1 != children.size()) {
				x = left;
				y += ICON_SIZE;
			}
		}

		x = left;
		y += children.isEmpty() ? COMPONENT_PADDING : ICON_SIZE + COMPONENT_PADDING;
		for (ChapterComponent comp : components) {
			y = comp.render(matrix, mc, x, y, top, width, height) + COMPONENT_PADDING;
		}
		return y;
	}

	private void renderIcon(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height, int mouseX,
			int mouseY) {
		if (isInside(x, y, ICON_SIZE, mouseX, mouseY)) {
			GuiUtils.drawHoveringText(matrix, Arrays.asList(title.text), mouseX, mouseY, width, height, -1,
					mc.fontRenderer);
		}

		if (itemIcon != null) {
			mc.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(itemIcon), x, y);
		} else {
			mc.getTextureManager().bindTexture(rlIcon);
			AbstractGui.blit(matrix, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
		}
	}

	private boolean isInside(int left, int top, int size, double x, double y) {
		return x > left && x < left + size && y > top && y < top + size;
	}

	private static boolean isInsideScreen(int y, int size, int top, int height) {
		return y >= top && y + size < top + height;
	}

	private static interface ChapterComponent {
		int render(MatrixStack matrix, Minecraft mc, int x, int y, int top, int width, int height);
	}

	private static class TextComponent implements ChapterComponent {

		private static final int COLOR = Color.BLACK.getRGB();

		private ITextComponent text;

		private TextComponent(ITextComponent text) {
			this.text = text;
		}

		@Override
		public int render(MatrixStack matrix, Minecraft mc, int x, int y, int top, int width, int height) {
			FontRenderer font = mc.fontRenderer;
			List<IReorderingProcessor> lines = font.trimStringToWidth(text, width);
			for (IReorderingProcessor line : lines) {
				if (isInsideScreen(y, font.FONT_HEIGHT, top, height))
					font.func_238422_b_(matrix, line, x, y, COLOR);
				y += font.FONT_HEIGHT;
			}
			return y;
		}

	}

	private static class HeaderComponent implements ChapterComponent {

		private static final int COLOR = Color.BLACK.getRGB();
		private static final float SCALE = 1.3f;

		private ITextComponent text;

		private HeaderComponent(ITextComponent text) {
			this.text = text;
		}

		@Override
		public int render(MatrixStack matrix, Minecraft mc, int x, int y, int top, int width, int height) {
			FontRenderer font = mc.fontRenderer;

			List<IReorderingProcessor> lines = font.trimStringToWidth(text, (int) (width / SCALE));
			for (IReorderingProcessor line : lines) {
				float textWidth = font.func_243245_a(line) * SCALE;
				matrix.push();
				matrix.scale(SCALE, SCALE, 1);
				if (isInsideScreen(y, (int) (font.FONT_HEIGHT * SCALE), top, height))
					font.func_238422_b_(matrix, line, (x + width / 2 - textWidth / 2) / SCALE, y / SCALE, COLOR);
				y += font.FONT_HEIGHT * SCALE;
				matrix.pop();
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
		public int render(MatrixStack matrix, Minecraft mc, int x, int y, int top, int width, int height) {
			mc.getTextureManager().bindTexture(image);
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
			AbstractGui.blit(matrix, x + width / 2 - drawWidth / 2, drawY, drawWidth, drawHeight, 0, v, imgWidth,
					vHeight, imgWidth, imgHeight);
			return y + offset;
		}

	}
}