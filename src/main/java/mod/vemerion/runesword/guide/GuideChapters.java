package mod.vemerion.runesword.guide;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.api.IGuide;
import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.api.RuneswordAPI;
import mod.vemerion.runesword.init.ModBlocks;
import mod.vemerion.runesword.init.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class GuideChapters {

	private static IGuideChapter runeforge;
	private static IGuideChapter enchant;
	private static IGuideChapter blood;
	private static IGuideChapter air;
	private static IGuideChapter earth;
	private static IGuideChapter water;
	private static IGuideChapter fire;
	private static IGuideChapter frost;
	private static IGuideChapter magic;
	private static IGuideChapter start;

	private static IGuideChapter bloodSword;
	private static IGuideChapter airSword;
	private static IGuideChapter earthSword;
	private static IGuideChapter waterSword;
	private static IGuideChapter fireSword;
	private static IGuideChapter frostSword;
	private static IGuideChapter magicSword;

	private static IGuideChapter bloodAxe;
	private static IGuideChapter airAxe;
	private static IGuideChapter earthAxe;
	private static IGuideChapter waterAxe;
	private static IGuideChapter fireAxe;
	private static IGuideChapter frostAxe;
	private static IGuideChapter magicAxe;

	public static IGuideChapter getStartChapter() {
		if (start == null) {
			var swordPowers = Component.translatable(transKey("swordpowers"));
			var axePowers = Component.translatable(transKey("axepowers"));

			IGuide guide = RuneswordAPI.guide;
			runeforge = guide.createGuideChapter(ModBlocks.RUNEFORGE.get(), ModBlocks.RUNEFORGE.get().getName());
			enchant = guide.createGuideChapter(Blocks.ENCHANTING_TABLE,
					Component.translatable(transKey("enchanting")));
			blood = guide.createGuideChapter(ModItems.BLOOD_RUNE.get(), ModItems.BLOOD_RUNE.get().getDescription());
			air = guide.createGuideChapter(ModItems.AIR_RUNE.get(), ModItems.AIR_RUNE.get().getDescription());
			earth = guide.createGuideChapter(ModItems.EARTH_RUNE.get(), ModItems.EARTH_RUNE.get().getDescription());
			water = guide.createGuideChapter(ModItems.WATER_RUNE.get(), ModItems.WATER_RUNE.get().getDescription());
			fire = guide.createGuideChapter(ModItems.FIRE_RUNE.get(), ModItems.FIRE_RUNE.get().getDescription());
			frost = guide.createGuideChapter(ModItems.FROST_RUNE.get(), ModItems.FROST_RUNE.get().getDescription());
			magic = guide.createGuideChapter(ModItems.MAGIC_RUNE.get(), ModItems.MAGIC_RUNE.get().getDescription());
			start = guide.createGuideChapter(ModItems.GUIDE.get(), Component.translatable(transKey("guide")));

			bloodSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			airSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			earthSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			waterSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			fireSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			frostSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);
			magicSword = guide.createGuideChapter(Items.NETHERITE_SWORD, swordPowers);

			bloodAxe = guide.createGuideChapter(Items.NETHERITE_AXE, axePowers);
			airAxe = guide.createGuideChapter(Items.NETHERITE_AXE, axePowers);
			earthAxe = guide.createGuideChapter(Items.NETHERITE_AXE, axePowers);
			waterAxe = guide.createGuideChapter(Items.NETHERITE_AXE, axePowers);
			fireAxe = guide.createGuideChapter(Items.NETHERITE_AXE, axePowers);
			frostAxe = guide.createGuideChapter(Items.NETHERITE_AXE, axePowers);
			magicAxe = guide.createGuideChapter(Items.NETHERITE_AXE, axePowers);

			runeforge.addText(transKey("runeforge1")).addImage(image("runeforge_crafting"), 518, 265)
					.addText(transKey("runeforge2")).addImage(image("runeforge"), 176, 166)
					.addText(transKey("runeforge3")).addText(transKey("runeforge4"));
			enchant.addText(transKey("enchantingtext"));
			blood.addChild(bloodSword).addChild(bloodAxe).addText(transKey("blood.obtain"));
			powers(bloodSword, "blood.sword", 3, 2);
			powers(bloodAxe, "blood.axe", 3, 2);
			air.addChild(airSword).addChild(airAxe).addText(transKey("air.obtain"));
			powers(airSword, "air.sword", 2, 3);
			powers(airAxe, "air.axe", 3, 2);
			earth.addChild(earthSword).addChild(earthAxe).addText(transKey("earth.obtain"));
			powers(earthSword, "earth.sword", 3, 2);
			powers(earthAxe, "earth.axe", 3, 2);
			water.addChild(waterSword).addChild(waterAxe).addText(transKey("water.obtain"));
			powers(waterSword, "water.sword", 2, 3);
			powers(waterAxe, "water.axe", 3, 2);
			fire.addChild(fireSword).addChild(fireAxe).addText(transKey("fire.obtain"));
			powers(fireSword, "fire.sword", 3, 2);
			powers(fireAxe, "fire.axe", 3, 2);
			frost.addChild(frostSword).addChild(frostAxe).addText(transKey("frost.obtain"));
			powers(frostSword, "frost.sword", 3, 3);
			powers(frostAxe, "frost.axe", 2, 4);
			magic.addChild(magicSword).addChild(magicAxe).addText(transKey("magic.obtain"))
					.addText(transKey("magic.text"));
			powers(magicSword, "magic.sword", 36, 0);
			powers(magicAxe, "magic.axe", 36, 0);
			start.addChild(runeforge).addChild(enchant).addChild(blood).addChild(air).addChild(earth).addChild(water)
					.addChild(fire).addChild(frost).addChild(magic).addText(transKey("intro"));
		}
		return start;
	}

	private static void powers(IGuideChapter chapter, String prefix, int minorEnchants, int majorEnchants) {
		chapter.addText(transKey(prefix + ".minor")).addText(transKey(prefix + ".major"));
		if (minorEnchants > 0) {
			chapter.addHeader(transKey("minorenchants"));
			for (int i = 1; i <= minorEnchants; i++)
				chapter.addText(transKey(prefix + ".minorenchant" + i));
		}
		if (majorEnchants > 0) {
			chapter.addHeader(transKey("majorenchants"));
			for (int i = 1; i <= majorEnchants; i++)
				chapter.addText(transKey(prefix + ".majorenchant" + i));
		}
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
