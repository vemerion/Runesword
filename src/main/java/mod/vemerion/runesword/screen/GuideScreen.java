package mod.vemerion.runesword.screen;

import java.util.Arrays;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
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
	private static final int Y_OFFSET = 25;
	private static final int X_OFFSET = 10;
	private static final int CHAPTER_WIDTH = X_SIZE - X_OFFSET * 2;
	private static final int CHAPTER_HEIGHT = Y_SIZE - Y_OFFSET * 2;
	private static final int SCROLL_LENGTH = (int) (CHAPTER_HEIGHT * 0.3);
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
				b -> page = Math.max(0, page - 1)) {
			@Override
			public void playDownSound(SoundHandler handler) {
				handler.play(SimpleSound.master(Main.GUIDE_CLICK, 1.0F));
			}
		});
		addButton(new ImageButton(x, y + BUTTON_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, DOWN_ARROW, b -> {
			if (canPageDown)
				page++;
		}) {
			@Override
			public void playDownSound(SoundHandler handler) {
				handler.play(SimpleSound.master(Main.GUIDE_CLICK, 1.0F));
			}
		});
		addButton(new ImageButton(x + 2, y - Y_SIZE / 2, BUTTON_SIZE, BUTTON_SIZE, 0, 0, 32, HOME_BUTTON, 256, 256,
				b -> gotoChapter(startChapter), (b, m, mouseX, mouseY) -> GuiUtils.drawHoveringText(m,
						Arrays.asList(b.getMessage()), mouseX, mouseY, width, height, -1, minecraft.fontRenderer),
				new TranslationTextComponent(transKey("home"))) {
			@Override
			public void playDownSound(SoundHandler handler) {
				handler.play(SimpleSound.master(Main.GUIDE_CLICK, 1.0F));
			}
		});
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
		} else if (current.mouseClicked(left + X_OFFSET, top + Y_OFFSET - SCROLL_LENGTH * page, top + Y_OFFSET,
				CHAPTER_WIDTH, CHAPTER_HEIGHT, mouseX, mouseY, button, chapter -> {
					current = chapter;
					Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(Main.GUIDE_CLICK, 1.0F));
				})) {
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

		current.renderTitle(matrixStack, minecraft, x, top + 5, CHAPTER_WIDTH, CHAPTER_HEIGHT);
		y -= SCROLL_LENGTH * page;
		y = current.renderComponents(matrixStack, minecraft, x, y, top + Y_OFFSET, CHAPTER_WIDTH, CHAPTER_HEIGHT,
				mouseX, mouseY);
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

	public static final GuideChapter RUNEFORGE_CHAPTER = new GuideChapter(Main.RUNEFORGE_BLOCK,
			Main.RUNEFORGE_BLOCK.getTranslatedName()).addText(transKey("runeforge1"))
					.addImage(image("runeforge_crafting"), 518, 265).addText(transKey("runeforge2"))
					.addImage(image("runeforge"), 176, 166).addText(transKey("runeforge3"))
					.addText(transKey("runeforge4"));

	public static final GuideChapter ENCHANT_CHAPTER = new GuideChapter(Blocks.ENCHANTING_TABLE,
			new TranslationTextComponent(transKey("enchanting"))).addText(transKey("enchantingtext"));

	public static final GuideChapter BLOOD_CHAPTER = new GuideChapter(RuneItem.BLOOD_RUNE_ITEM,
			RuneItem.BLOOD_RUNE_ITEM.getName()).addText(transKey("blood.obtain")).addText(transKey("blood.minor"))
					.addText(transKey("blood.major")).addHeader(transKey("minorenchants"))
					.addText(transKey("blood.minorenchant1")).addText(transKey("blood.minorenchant2"))
					.addText(transKey("blood.minorenchant3")).addHeader(transKey("majorenchants"))
					.addText(transKey("blood.majorenchant1")).addText(transKey("blood.majorenchant2"));

	public static final GuideChapter AIR_CHAPTER = new GuideChapter(RuneItem.AIR_RUNE_ITEM,
			RuneItem.AIR_RUNE_ITEM.getName()).addText(transKey("air.obtain")).addText(transKey("air.minor"))
					.addText(transKey("air.major")).addHeader(transKey("minorenchants"))
					.addText(transKey("air.minorenchant1")).addText(transKey("air.minorenchant2"))
					.addHeader(transKey("majorenchants")).addText(transKey("air.majorenchant1"))
					.addText(transKey("air.majorenchant2")).addText(transKey("air.majorenchant3"));

	public static final GuideChapter EARTH_CHAPTER = new GuideChapter(RuneItem.EARTH_RUNE_ITEM,
			RuneItem.EARTH_RUNE_ITEM.getName()).addText(transKey("earth.obtain")).addText(transKey("earth.minor"))
					.addText(transKey("earth.major")).addHeader(transKey("minorenchants"))
					.addText(transKey("earth.minorenchant1")).addText(transKey("earth.minorenchant2"))
					.addText(transKey("earth.minorenchant3")).addHeader(transKey("majorenchants"))
					.addText(transKey("earth.majorenchant1")).addText(transKey("earth.majorenchant2"));

	public static final GuideChapter WATER_CHAPTER = new GuideChapter(RuneItem.WATER_RUNE_ITEM,
			RuneItem.WATER_RUNE_ITEM.getName()).addText(transKey("water.obtain")).addText(transKey("water.minor"))
					.addText(transKey("water.major")).addHeader(transKey("minorenchants"))
					.addText(transKey("water.minorenchant1")).addText(transKey("water.minorenchant2"))
					.addHeader(transKey("majorenchants")).addText(transKey("water.majorenchant1"))
					.addText(transKey("water.majorenchant2")).addText(transKey("water.majorenchant3"));

	public static final GuideChapter FIRE_CHAPTER = new GuideChapter(RuneItem.FIRE_RUNE_ITEM,
			RuneItem.FIRE_RUNE_ITEM.getName()).addText(transKey("fire.obtain")).addText(transKey("fire.minor"))
					.addText(transKey("fire.major")).addHeader(transKey("minorenchants"))
					.addText(transKey("fire.minorenchant1")).addText(transKey("fire.minorenchant2"))
					.addText(transKey("fire.minorenchant3")).addHeader(transKey("majorenchants"))
					.addText(transKey("fire.majorenchant1")).addText(transKey("fire.majorenchant2"));

	public static final GuideChapter FROST_CHAPTER = new GuideChapter(RuneItem.FROST_RUNE_ITEM,
			RuneItem.FROST_RUNE_ITEM.getName()).addText(transKey("frost.obtain")).addText(transKey("frost.minor"))
					.addText(transKey("frost.major")).addHeader(transKey("minorenchants"))
					.addText(transKey("frost.minorenchant1")).addText(transKey("frost.minorenchant2"))
					.addText(transKey("frost.minorenchant3")).addHeader(transKey("majorenchants"))
					.addText(transKey("frost.majorenchant1")).addText(transKey("frost.majorenchant2"))
					.addText(transKey("frost.majorenchant3"));

	public static final GuideChapter START_CHAPTER = new GuideChapter(() -> Main.GUIDE_ITEM,
			new TranslationTextComponent(transKey("guide"))).addChild(RUNEFORGE_CHAPTER).addChild(ENCHANT_CHAPTER)
					.addChild(BLOOD_CHAPTER).addChild(AIR_CHAPTER).addChild(EARTH_CHAPTER).addChild(WATER_CHAPTER)
					.addChild(FIRE_CHAPTER).addChild(FROST_CHAPTER).addText(transKey("intro"));
}
