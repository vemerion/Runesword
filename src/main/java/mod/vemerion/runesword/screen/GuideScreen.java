package mod.vemerion.runesword.screen;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class GuideScreen extends Screen {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/table.png");
	private static final int X_SIZE = 230;
	private static final int Y_SIZE = 230;
	private static final int TEX_SIZE = 256;
	private static final int Y_OFFSET = 30;
	private static final int X_OFFSET = 10;

	private GuideChapter current;

	public GuideScreen(GuideChapter startChapter) {
		super(startChapter.getTitle());
		this.current = startChapter;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		InputMappings.Input mouseKey = InputMappings.getInputByCode(keyCode, scanCode);
		if (super.keyPressed(keyCode, scanCode, modifiers)) {
			return true;
		} else if (minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey)) {
			closeScreen();
			return true;
		}
		return false;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		minecraft.getTextureManager().bindTexture(BACKGROUND);
		int x = (width - X_SIZE) / 2;
		int y = (height - Y_SIZE) / 2;
		blit(matrixStack, x, y, X_SIZE, Y_SIZE, 0, 0, TEX_SIZE, TEX_SIZE, TEX_SIZE, TEX_SIZE);

		current.renderComponents(matrixStack, minecraft, x + X_OFFSET, y + Y_OFFSET, X_SIZE - X_OFFSET * 2,
				Y_SIZE - Y_OFFSET * 2, mouseX, mouseY);
	}

	private static String transKey(String suffix) {
		return transKey("gui", suffix);
	}

	private static String transKey(String prefix, String suffix) {
		return prefix + "." + Main.MODID + "." + suffix;
	}

	public static final GuideChapter BLOOD_CHAPTER = new GuideChapter(() -> RuneItem.BLOOD_RUNE_ITEM,
			RuneItem.BLOOD_RUNE_ITEM.getName()).addText(transKey("text1"));

	public static final GuideChapter START_CHAPTER = new GuideChapter(() -> Main.GUIDE_ITEM,
			new TranslationTextComponent(transKey("guide"))).addChild(BLOOD_CHAPTER).addText(transKey("text1"))
					.addText(transKey("text2"));
}
