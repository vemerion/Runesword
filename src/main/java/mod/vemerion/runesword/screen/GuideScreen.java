package mod.vemerion.runesword.screen;

import java.util.Arrays;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class GuideScreen extends Screen {

	private static final ResourceLocation BACKGROUND = new ResourceLocation(Main.MODID, "textures/gui/table.png");
	private static final ResourceLocation UP_ARROW = new ResourceLocation(Main.MODID, "textures/gui/up_arrow.png");
	private static final ResourceLocation DOWN_ARROW = new ResourceLocation(Main.MODID, "textures/gui/down_arrow.png");
	private static final ResourceLocation HOME_BUTTON = new ResourceLocation(Main.MODID,
			"textures/gui/home_button.png");
	private static final int X_SIZE = 230;
	private static final int Y_SIZE = 230;
	private static final int TEX_SIZE = 256;
	private static final int Y_OFFSET = 30;
	private static final int X_OFFSET = 10;
	private static final int CHAPTER_WIDTH = X_SIZE - X_OFFSET * 2;
	private static final int CHAPTER_HEIGHT = Y_SIZE - Y_OFFSET * 2;
	private static final int BUTTON_SIZE = 32;

	private GuideChapter current;
	private int page = 0;
	private boolean canPageDown;
	private final GuideChapter startChapter;
	private int left, top;

	public GuideScreen(GuideChapter startChapter) {
		super(startChapter.getTitle());
		this.startChapter = startChapter;
		this.current = startChapter;
	}

	private void gotoChapter(GuideChapter chapter) {
		current = chapter;
		page = 0;
	}

	@Override
	protected void init() {
		left = (width - X_SIZE) / 2;
		top = (height - Y_SIZE) / 2;
		int x = left + X_SIZE;
		int y = top + Y_SIZE / 2;
		addButton(new ImageButton(x, y - BUTTON_SIZE - BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, UP_ARROW,
				b -> page = Math.max(0, page - 1)));
		addButton(new ImageButton(x, y + BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, DOWN_ARROW, b -> {
			if (canPageDown)
				page++;
		}));
		addButton(new ImageButton(x + 2, y - Y_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, HOME_BUTTON, 256, 256,
				b -> gotoChapter(startChapter), (b, m, mouseX, mouseY) -> GuiUtils.drawHoveringText(m,
						Arrays.asList(b.getMessage()), mouseX, mouseY, width, height, -1, minecraft.fontRenderer),
				new TranslationTextComponent(transKey("home"))));
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
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) {
			return true;
		} else if (current.mouseClicked(left + X_OFFSET, top + Y_OFFSET - CHAPTER_HEIGHT * page, CHAPTER_WIDTH,
				CHAPTER_HEIGHT, mouseX, mouseY, button, chapter -> current = chapter)) {
			return true;
		}
		return false;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);

		minecraft.getTextureManager().bindTexture(BACKGROUND);

		blit(matrixStack, left, top, X_SIZE, Y_SIZE, 0, 0, TEX_SIZE, TEX_SIZE, TEX_SIZE, TEX_SIZE);

		int x = left + X_OFFSET;
		int y = top + Y_OFFSET;

		current.renderTitle(matrixStack, minecraft, x, y - 25, CHAPTER_WIDTH, CHAPTER_HEIGHT);
		y -= CHAPTER_HEIGHT * page;
		y = current.renderComponents(matrixStack, minecraft, x, y, CHAPTER_WIDTH, CHAPTER_HEIGHT, mouseX, mouseY);
		canPageDown = y > top + CHAPTER_HEIGHT;

		for (Widget b : buttons)
			b.renderButton(matrixStack, mouseX, mouseY, partialTicks);
	}

	private static String transKey(String suffix) {
		return transKey("gui", suffix);
	}

	private static String transKey(String prefix, String suffix) {
		return prefix + "." + Main.MODID + "." + suffix;
	}

	private static ResourceLocation image(String name) {
		return new ResourceLocation(Main.MODID, "textures/guide/" + name + ".png");
	}

	public static final GuideChapter BLOOD_CHAPTER = new GuideChapter(() -> RuneItem.BLOOD_RUNE_ITEM,
			RuneItem.BLOOD_RUNE_ITEM.getName()).addText(transKey("text1"));

	public static final GuideChapter START_CHAPTER = new GuideChapter(() -> Main.GUIDE_ITEM,
			new TranslationTextComponent(transKey("guide"))).addChild(BLOOD_CHAPTER).addText(transKey("text1"))
					.addText(transKey("text2")).addHeader(transKey("text1")).addText(transKey("text2"))
					.addImage(image("runeforge_crafting"), 518, 264).addText(transKey("text2"))
					.addHeader(transKey("text2")).addText(transKey("text2"));
}
