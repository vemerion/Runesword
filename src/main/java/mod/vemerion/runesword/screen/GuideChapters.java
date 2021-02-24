package mod.vemerion.runesword.screen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class GuideChapters {

	private static GuideChapter runeforge;
	private static GuideChapter enchant;
	private static GuideChapter blood;
	private static GuideChapter air;
	private static GuideChapter earth;
	private static GuideChapter water;
	private static GuideChapter fire;
	private static GuideChapter frost;
	private static GuideChapter magic;
	private static GuideChapter start;

	public static GuideChapter getStartChapter() {
		if (start == null) {
			runeforge = new GuideChapter(Main.RUNEFORGE_BLOCK, Main.RUNEFORGE_BLOCK.getTranslatedName())
					.addText(transKey("runeforge1")).addImage(image("runeforge_crafting"), 518, 265)
					.addText(transKey("runeforge2")).addImage(image("runeforge"), 176, 166)
					.addText(transKey("runeforge3")).addText(transKey("runeforge4"));
			enchant = new GuideChapter(Blocks.ENCHANTING_TABLE, new TranslationTextComponent(transKey("enchanting")))
					.addText(transKey("enchantingtext"));
			blood = new GuideChapter(RuneItem.BLOOD_RUNE_ITEM, RuneItem.BLOOD_RUNE_ITEM.getName())
					.addText(transKey("blood.obtain")).addText(transKey("blood.minor")).addText(transKey("blood.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("blood.minorenchant1"))
					.addText(transKey("blood.minorenchant2")).addText(transKey("blood.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("blood.majorenchant1"))
					.addText(transKey("blood.majorenchant2"));
			air = new GuideChapter(RuneItem.AIR_RUNE_ITEM, RuneItem.AIR_RUNE_ITEM.getName())
					.addText(transKey("air.obtain")).addText(transKey("air.minor")).addText(transKey("air.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("air.minorenchant1"))
					.addText(transKey("air.minorenchant2")).addHeader(transKey("majorenchants"))
					.addText(transKey("air.majorenchant1")).addText(transKey("air.majorenchant2"))
					.addText(transKey("air.majorenchant3"));
			earth = new GuideChapter(RuneItem.EARTH_RUNE_ITEM, RuneItem.EARTH_RUNE_ITEM.getName())
					.addText(transKey("earth.obtain")).addText(transKey("earth.minor")).addText(transKey("earth.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("earth.minorenchant1"))
					.addText(transKey("earth.minorenchant2")).addText(transKey("earth.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("earth.majorenchant1"))
					.addText(transKey("earth.majorenchant2"));
			water = new GuideChapter(RuneItem.WATER_RUNE_ITEM, RuneItem.WATER_RUNE_ITEM.getName())
					.addText(transKey("water.obtain")).addText(transKey("water.minor")).addText(transKey("water.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("water.minorenchant1"))
					.addText(transKey("water.minorenchant2")).addHeader(transKey("majorenchants"))
					.addText(transKey("water.majorenchant1")).addText(transKey("water.majorenchant2"))
					.addText(transKey("water.majorenchant3"));
			fire = new GuideChapter(RuneItem.FIRE_RUNE_ITEM, RuneItem.FIRE_RUNE_ITEM.getName())
					.addText(transKey("fire.obtain")).addText(transKey("fire.minor")).addText(transKey("fire.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("fire.minorenchant1"))
					.addText(transKey("fire.minorenchant2")).addText(transKey("fire.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("fire.majorenchant1"))
					.addText(transKey("fire.majorenchant2"));
			frost = new GuideChapter(RuneItem.FROST_RUNE_ITEM, RuneItem.FROST_RUNE_ITEM.getName())
					.addText(transKey("frost.obtain")).addText(transKey("frost.minor")).addText(transKey("frost.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("frost.minorenchant1"))
					.addText(transKey("frost.minorenchant2")).addText(transKey("frost.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("frost.majorenchant1"))
					.addText(transKey("frost.majorenchant2")).addText(transKey("frost.majorenchant3"));
			magic = new GuideChapter(RuneItem.MAGIC_RUNE_ITEM, RuneItem.MAGIC_RUNE_ITEM.getName())
					.addText(transKey("magic.obtain")).addText(transKey("magic.minor")).addText(transKey("magic.major"))
					.addText(transKey("magic.text")).addHeader(transKey("minorenchants"))
					.addText(transKey("magic.minorenchant1")).addText(transKey("magic.minorenchant2"))
					.addText(transKey("magic.minorenchant3")).addText(transKey("magic.minorenchant4"))
					.addText(transKey("magic.minorenchant5")).addText(transKey("magic.minorenchant6"))
					.addText(transKey("magic.minorenchant7")).addText(transKey("magic.minorenchant8"))
					.addText(transKey("magic.minorenchant9")).addText(transKey("magic.minorenchant10"))
					.addText(transKey("magic.minorenchant1")).addText(transKey("magic.minorenchant12"))
					.addText(transKey("magic.minorenchant13")).addText(transKey("magic.minorenchant14"))
					.addText(transKey("magic.minorenchant15")).addText(transKey("magic.minorenchant16"))
					.addText(transKey("magic.minorenchant17")).addText(transKey("magic.minorenchant18"))
					.addText(transKey("magic.minorenchant19")).addText(transKey("magic.minorenchant20"))
					.addText(transKey("magic.minorenchant21")).addText(transKey("magic.minorenchant22"))
					.addText(transKey("magic.minorenchant23")).addText(transKey("magic.minorenchant24"))
					.addText(transKey("magic.minorenchant25")).addText(transKey("magic.minorenchant26"))
					.addText(transKey("magic.minorenchant27")).addText(transKey("magic.minorenchant28"))
					.addText(transKey("magic.minorenchant29")).addText(transKey("magic.minorenchant30"))
					.addText(transKey("magic.minorenchant31")).addText(transKey("magic.minorenchant32"))
					.addText(transKey("magic.minorenchant33")).addText(transKey("magic.minorenchant34"))
					.addText(transKey("magic.minorenchant35")).addText(transKey("magic.minorenchant36"));
			start = new GuideChapter(() -> Main.GUIDE_ITEM, new TranslationTextComponent(transKey("guide")))
					.addChild(() -> runeforge).addChild(() -> enchant).addChild(() -> blood).addChild(() -> air)
					.addChild(() -> earth).addChild(() -> water).addChild(() -> fire).addChild(() -> frost)
					.addChild(() -> magic).addText(transKey("intro"));
		}
		return start;
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
}
