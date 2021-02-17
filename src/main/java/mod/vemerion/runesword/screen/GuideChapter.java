package mod.vemerion.runesword.screen;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

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

public class GuideChapter {

	private static final int ICON_SIZE = 16;
	private static final int COMPONENT_PADDING = 6;

	private IItemProvider icon;
	private HeaderComponent title;
	private List<GuideChapter> children;
	private List<ChapterComponent> components;

	public GuideChapter(IItemProvider icon, ITextComponent title) {
		this.icon = icon;
		this.title = new HeaderComponent(title);
		this.children = new ArrayList<>();
		this.components = new ArrayList<>();
	}

	public GuideChapter addChild(GuideChapter child) {
		children.add(child);
		return this;
	}

	public GuideChapter addText(String translationKey) {
		components.add(new TextComponent(new TranslationTextComponent(translationKey)));
		return this;
	}
	
	public GuideChapter addHeader(String translationKey) {
		components.add(new HeaderComponent(new TranslationTextComponent(translationKey)));
		return this;
	}
	
	public GuideChapter addImage(ResourceLocation image, int imgWidth, int imgHeight) {
		components.add(new ImageComponent(image, imgWidth, imgHeight));
		return this;
	}

	public ITextComponent getTitle() {
		return title.text;
	}

	public void renderTitle(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height) {
		title.render(matrix, mc, x, y, width, height);
	}

	public void renderComponents(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height, int mouseX,
			int mouseY) {
		int left = x;
		for (int i = 0; i < children.size(); i++) {
			GuideChapter c = children.get(i);
			c.renderIcon(matrix, mc, x, y, width, height, mouseX, mouseY);
			x += ICON_SIZE;
			if (x + ICON_SIZE > left + width && i + 1 != children.size()) {
				x = left;
				y += ICON_SIZE;
			}
		}

		x = left;
		y += ICON_SIZE + COMPONENT_PADDING;
		for (ChapterComponent comp : components) {
			y = comp.render(matrix, mc, x, y, width, height) + COMPONENT_PADDING;
		}
	}

	private void renderIcon(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height, int mouseX,
			int mouseY) {
		if (mouseX > x && mouseX < x + ICON_SIZE && mouseY > y && mouseY < y + ICON_SIZE) {
			GuiUtils.drawHoveringText(matrix, Arrays.asList(title.text), mouseX, mouseY, width, height, -1,
					mc.fontRenderer);
		}

		mc.getItemRenderer().renderItemAndEffectIntoGUI(new ItemStack(icon), x, y);
	}

	private static interface ChapterComponent {
		int render(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height);
	}

	private static class TextComponent implements ChapterComponent {

		private static final int COLOR = Color.BLACK.getRGB();

		private ITextComponent text;

		private TextComponent(ITextComponent text) {
			this.text = text;
		}

		@Override
		public int render(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height) {
			FontRenderer font = mc.fontRenderer;
			List<IReorderingProcessor> lines = font.trimStringToWidth(text, width);
			for (IReorderingProcessor line : lines) {
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
		public int render(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height) {
			FontRenderer font = mc.fontRenderer;

			List<IReorderingProcessor> lines = font.trimStringToWidth(text, (int) (width / SCALE));
			for (IReorderingProcessor line : lines) {
				float textWidth = font.func_243245_a(line) * SCALE;
				matrix.push();
				matrix.scale(SCALE, SCALE, 1);
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
		public int render(MatrixStack matrix, Minecraft mc, int x, int y, int width, int height) {
			mc.getTextureManager().bindTexture(image);
			float scale = (float) width / imgWidth * SCALE;
			int drawWidth = (int) (imgWidth * scale);
			int drawHeight = (int) (imgHeight * scale);
			AbstractGui.blit(matrix, x + width / 2 - drawWidth / 2, y, drawWidth, drawHeight, 0, 0, imgWidth, imgHeight, imgWidth, imgHeight);
			return y + drawHeight + 2;
		}

	}
}