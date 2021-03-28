package mod.vemerion.runesword.guide;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.api.IGuide;
import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.api.RuneswordAPI;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class GuideChapters {

	private static IGuideChapter runeforge;
	private static IGuideChapter enchant;
	private static IGuideChapter blood;
	private static IGuideChapter bloodSword;
	private static IGuideChapter air;
	private static IGuideChapter airSword;
	private static IGuideChapter earth;
	private static IGuideChapter earthSword;
	private static IGuideChapter water;
	private static IGuideChapter waterSword;
	private static IGuideChapter fire;
	private static IGuideChapter fireSword;
	private static IGuideChapter frost;
	private static IGuideChapter frostSword;
	private static IGuideChapter magic;
	private static IGuideChapter magicSword;
	private static IGuideChapter start;

	public static IGuideChapter getStartChapter() {
		if (start == null) {
			TranslationTextComponent swordPowers = new TranslationTextComponent(transKey("swordpowers"));

			IGuide guide = RuneswordAPI.guide;
			runeforge = guide.createGuideChapter(Main.RUNEFORGE_BLOCK, Main.RUNEFORGE_BLOCK.getTranslatedName());
			enchant = guide.createGuideChapter(Blocks.ENCHANTING_TABLE,
					new TranslationTextComponent(transKey("enchanting")));
			blood = guide.createGuideChapter(RuneItem.BLOOD_RUNE_ITEM, RuneItem.BLOOD_RUNE_ITEM.getName());
			air = guide.createGuideChapter(RuneItem.AIR_RUNE_ITEM, RuneItem.AIR_RUNE_ITEM.getName());
			earth = guide.createGuideChapter(RuneItem.EARTH_RUNE_ITEM, RuneItem.EARTH_RUNE_ITEM.getName());
			water = guide.createGuideChapter(RuneItem.WATER_RUNE_ITEM, RuneItem.WATER_RUNE_ITEM.getName());
			fire = guide.createGuideChapter(RuneItem.FIRE_RUNE_ITEM, RuneItem.FIRE_RUNE_ITEM.getName());
			frost = guide.createGuideChapter(RuneItem.FROST_RUNE_ITEM, RuneItem.FROST_RUNE_ITEM.getName());
			magic = guide.createGuideChapter(RuneItem.MAGIC_RUNE_ITEM, RuneItem.MAGIC_RUNE_ITEM.getName());
			start = guide.createGuideChapter(Main.GUIDE_ITEM, new TranslationTextComponent(transKey("guide")));

			bloodSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			airSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			earthSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			waterSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			fireSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			frostSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			magicSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);

			runeforge.addText(transKey("runeforge1")).addImage(image("runeforge_crafting"), 518, 265)
					.addText(transKey("runeforge2")).addImage(image("runeforge"), 176, 166)
					.addText(transKey("runeforge3")).addText(transKey("runeforge4"));
			enchant.addText(transKey("enchantingtext"));
			blood.addChild(bloodSword).addText(transKey("blood.obtain"));
			bloodSword.addText(transKey("blood.sword.minor")).addText(transKey("blood.sword.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("blood.sword.minorenchant1"))
					.addText(transKey("blood.sword.minorenchant2")).addText(transKey("blood.sword.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("blood.sword.majorenchant1"))
					.addText(transKey("blood.sword.majorenchant2"));
			air.addChild(airSword).addText(transKey("air.obtain"));
			airSword.addText(transKey("air.sword.minor")).addText(transKey("air.sword.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("air.sword.minorenchant1"))
					.addText(transKey("air.sword.minorenchant2")).addHeader(transKey("majorenchants"))
					.addText(transKey("air.sword.majorenchant1")).addText(transKey("air.sword.majorenchant2"))
					.addText(transKey("air.sword.majorenchant3"));
			earth.addChild(earthSword).addText(transKey("earth.obtain"));
			earthSword.addText(transKey("earth.sword.minor")).addText(transKey("earth.sword.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("earth.sword.minorenchant1"))
					.addText(transKey("earth.sword.minorenchant2")).addText(transKey("earth.sword.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("earth.sword.majorenchant1"))
					.addText(transKey("earth.sword.majorenchant2"));
			water.addChild(waterSword).addText(transKey("water.obtain"));
			waterSword.addText(transKey("water.sword.minor")).addText(transKey("water.sword.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("water.sword.minorenchant1"))
					.addText(transKey("water.sword.minorenchant2")).addHeader(transKey("majorenchants"))
					.addText(transKey("water.sword.majorenchant1")).addText(transKey("water.sword.majorenchant2"))
					.addText(transKey("water.sword.majorenchant3"));
			fire.addChild(fireSword).addText(transKey("fire.obtain"));
			fireSword.addText(transKey("fire.sword.minor")).addText(transKey("fire.sword.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("fire.sword.minorenchant1"))
					.addText(transKey("fire.sword.minorenchant2")).addText(transKey("fire.sword.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("fire.sword.majorenchant1"))
					.addText(transKey("fire.sword.majorenchant2"));
			frost.addChild(frostSword).addText(transKey("frost.obtain"));
			frostSword.addText(transKey("frost.sword.minor")).addText(transKey("frost.sword.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("frost.sword.minorenchant1"))
					.addText(transKey("frost.sword.minorenchant2")).addText(transKey("frost.sword.minorenchant3"))
					.addHeader(transKey("majorenchants")).addText(transKey("frost.sword.majorenchant1"))
					.addText(transKey("frost.sword.majorenchant2")).addText(transKey("frost.sword.majorenchant3"));
			magic.addChild(magicSword).addText(transKey("magic.obtain")).addText(transKey("magic.text"));
			magicSword.addText(transKey("magic.sword.minor")).addText(transKey("magic.sword.major"))
					.addHeader(transKey("minorenchants")).addText(transKey("magic.sword.minorenchant1"))
					.addText(transKey("magic.sword.minorenchant2")).addText(transKey("magic.sword.minorenchant3"))
					.addText(transKey("magic.sword.minorenchant4")).addText(transKey("magic.sword.minorenchant5"))
					.addText(transKey("magic.sword.minorenchant6")).addText(transKey("magic.sword.minorenchant7"))
					.addText(transKey("magic.sword.minorenchant8")).addText(transKey("magic.sword.minorenchant9"))
					.addText(transKey("magic.sword.minorenchant10")).addText(transKey("magic.sword.minorenchant1"))
					.addText(transKey("magic.sword.minorenchant12")).addText(transKey("magic.sword.minorenchant13"))
					.addText(transKey("magic.sword.minorenchant14")).addText(transKey("magic.sword.minorenchant15"))
					.addText(transKey("magic.sword.minorenchant16")).addText(transKey("magic.sword.minorenchant17"))
					.addText(transKey("magic.sword.minorenchant18")).addText(transKey("magic.sword.minorenchant19"))
					.addText(transKey("magic.sword.minorenchant20")).addText(transKey("magic.sword.minorenchant21"))
					.addText(transKey("magic.sword.minorenchant22")).addText(transKey("magic.sword.minorenchant23"))
					.addText(transKey("magic.sword.minorenchant24")).addText(transKey("magic.sword.minorenchant25"))
					.addText(transKey("magic.sword.minorenchant26")).addText(transKey("magic.sword.minorenchant27"))
					.addText(transKey("magic.sword.minorenchant28")).addText(transKey("magic.sword.minorenchant29"))
					.addText(transKey("magic.sword.minorenchant30")).addText(transKey("magic.sword.minorenchant31"))
					.addText(transKey("magic.sword.minorenchant32")).addText(transKey("magic.sword.minorenchant33"))
					.addText(transKey("magic.sword.minorenchant34")).addText(transKey("magic.sword.minorenchant35"))
					.addText(transKey("magic.sword.minorenchant36"));
			start.addChild(runeforge).addChild(enchant).addChild(blood).addChild(air).addChild(earth).addChild(water)
					.addChild(fire).addChild(frost).addChild(magic).addText(transKey("intro"));
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
