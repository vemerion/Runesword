package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModBlocks;
import mod.vemerion.runesword.init.ModEffects;
import mod.vemerion.runesword.init.ModEntities;
import mod.vemerion.runesword.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(DataGenerator gen) {
		super(gen, Main.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addGui("no_sword", "Insert valid item into runeforge");
		addGui("slot_locked", "Slot locked");
		add(ModBlocks.RUNEFORGE.get(), "Runeforge");
		add(ModItems.AIR_RUNE.get(), "Air Rune");
		add(ModItems.FIRE_RUNE.get(), "Fire Rune");
		add(ModItems.WATER_RUNE.get(), "Water Rune");
		add(ModItems.EARTH_RUNE.get(), "Earth Rune");
		add(ModItems.BLOOD_RUNE.get(), "Blood Rune");
		add(ModItems.FROST_RUNE.get(), "Frost Rune");
		add(ModItems.MAGIC_RUNE.get(), "Magic Rune");
		addText("tooltip", "minor", "Minor: ");
		addText("tooltip", "major", "Major: ");
		add(ModItems.GUIDE.get(), "Runesword Guide");
		add("itemGroup." + Main.MODID, "Runesword");
		add(ModEffects.BLEED.get(), "Bleed");
		addGui("guide", "Runesword");
		addGui("home", "Home");
		addGui("mute", "Mute Guide Sounds");
		addGui("unmute", "Unmute Guide Sounds");
		addGui("back", "Back");
		addText("death.attack", "magicplayer", "%1$s was magicked to death by %2$s");
		addText("death.attack", "magicindirect", "%1$s was magicked to death by %2$s");
		addText("death.attack", "magic", "%1$s was magicked to death");
		addText("death.attack", "bleed", "%1$s bled to death");
		add(ModEntities.FROST_GOLEM.get(), "Frost Golem");
		add(ModEntities.FROSTBALL.get(), "Frostball");
		add(ModEntities.MAGIC_BALL.get(), "Magic Ball");
		guide();
	}

	private void guide() {
		addGui("intro",
				"Welcome to the Runesword guide, containing all the information you need to become proficient in runeforging! Click on one of the icons above to learn more about a specific subject.");
		addGui("runeforge1",
				"The runeforge is the block used to insert runes into swords and axes. Here is how you craft it:");
		addGui("runeforge2", "And here is the runeforge GUI:");
		addGui("runeforge3",
				"You start by inserting a sword or axe into the middle slot (1). You can then insert runes into the sword/axe by placing them in slot 2-5. The higher the quality of the sword/axe, the more runes can be inserted. For example, a stone tool only has the first rune slot unlocked (2), while a netherite tool has all four rune slots unlocked.");
		addGui("runeforge4",
				"The rune slots 2-4 are minor rune slots, while the top slot (5) is a major rune slot. Runes in the minor slots will have a smaller effect, while the rune in the major slot will have a big effect.");
		addGui("enchanting", "Rune Enchanting");
		addGui("enchantingtext",
				"Like other tools, runes can be enchanted in an enchanting table to boost the effect of the runes. Some enchantments increase the power of the major effect, while some enchantments increase the power of the minor effect. You can read about the different possible enchantments for every type of rune in their respective guide chapter.");

		addGui("minorenchants", "Minor Enchantments");
		addGui("majorenchants", "Major Enchantments");

		addGui("swordpowers", "Sword Powers");
		addGui("axepowers", "Axe Powers");

		addGui("blood.obtain", "Obtain: Low drop chance from all mobs.");

		addGui("blood.sword.minor", "Minor: Chance to heal on kill.");
		addGui("blood.sword.major", "Major: Attacks cost health, but deal more damage.");
		addGui("blood.sword.minorenchant1", "Fire Aspect: More lifesteal if enemy on fire.");
		addGui("blood.sword.minorenchant2", "Aqua Affinity: More lifesteal if in water.");
		addGui("blood.sword.minorenchant3", "Mending: chance to repair sword on attack.");
		addGui("blood.sword.majorenchant1", "Protection: Chance to not drain your health when attacking.");
		addGui("blood.sword.majorenchant2", "Sharpness: More damage.");

		addGui("blood.axe.minor", "Minor: Chance to apply bleed when attacking.");
		addGui("blood.axe.major", "Major: Deal more damage to bleeding enemies.");
		addGui("blood.axe.minorenchant1", "Fortune: Increase bleed chance.");
		addGui("blood.axe.minorenchant2", "Infinity: Increase bleed duration.");
		addGui("blood.axe.minorenchant3", "Power: Chance to apply stronger bleed.");
		addGui("blood.axe.majorenchant1", "Sharpness: Further increase damage to bleeding enemy.");
		addGui("blood.axe.majorenchant2", "Loyalty: Chance to heal yourself when attacking bleeding enemy.");

		addGui("air.obtain", "Obtain: Dropped by phantoms.");

		addGui("air.sword.minor", "Minor: Chance to knock up enemies when attacking.");
		addGui("air.sword.major", "Major: Grants speed boost on kill.");
		addGui("air.sword.minorenchant1", "Knockback: Adds knockback to attacks.");
		addGui("air.sword.minorenchant2", "Punch: Increase knock-up height.");
		addGui("air.sword.majorenchant1", "Infinity: Double speed duration.");
		addGui("air.sword.majorenchant2", "Efficiency: Chance to get speed II.");
		addGui("air.sword.majorenchant3", "Feather Falling: Chance to get Jump Boost.");

		addGui("air.axe.minor", "Minor: Increase block break speed when you are airborne.");
		addGui("air.axe.major", "Major: Chance to gain levitation when chopping wood.");
		addGui("air.axe.minorenchant1", "Silk Touch: Also break leaves faster.");
		addGui("air.axe.minorenchant2", "Efficiency: Further increase break speed.");
		addGui("air.axe.minorenchant3", "Feather Falling: Increase break speed if levitating.");
		addGui("air.axe.majorenchant1", "Fortune: Increase chance to gain levitation.");
		addGui("air.axe.majorenchant2", "Infinity: Increase levitation duration.");

		addGui("earth.obtain", "Obtain: Found in treasure chests underground.");

		addGui("earth.sword.minor", "Minor: Mobs have a chance to drop ores on kill.");
		addGui("earth.sword.major", "Major: The sword deals more damage while you are underground.");
		addGui("earth.sword.minorenchant1", "Fortune: Increase ore drop chance.");
		addGui("earth.sword.minorenchant2", "Looting: Chance that enemy drops rare ore.");
		addGui("earth.sword.minorenchant3", "Fire Aspect: Chance to smelt ore immediately.");
		addGui("earth.sword.majorenchant1", "Shaprness: More damage underground.");
		addGui("earth.sword.majorenchant2", "Protection: Take reduced damage underground.");

		addGui("earth.axe.minor", "Minor: Break stone type blocks faster.");
		addGui("earth.axe.major", "Major: Axe now functions as pickaxe as well.");
		addGui("earth.axe.minorenchant1", "Efficiency: Further increase mining speed.");
		addGui("earth.axe.minorenchant2", "Flame: Increase mining speed in the Nether.");
		addGui("earth.axe.minorenchant3", "Fire Aspect: Further increase mining speed if you are on fire.");
		addGui("earth.axe.majorenchant1", "Fortune: Chance to gain extra drop when mining stone.");
		addGui("earth.axe.majorenchant2", "Mending: Chance to repair axe when mining.");

		addGui("water.obtain", "Obtain: Found in treasure chests under water.");

		addGui("water.sword.minor", "Minor: Restore breath on kill.");
		addGui("water.sword.major", "Major: The sword deals more damage while under water.");
		addGui("water.sword.minorenchant1", "Respiration: Increase bubble restoration.");
		addGui("water.sword.minorenchant2", "Luck of the Sea: Chance to ignore drowning.");
		addGui("water.sword.majorenchant1", "Aqua Affinity: Major effect also in rain.");
		addGui("water.sword.majorenchant2", "Sharpness: Increase bonus damage chance.");
		addGui("water.sword.majorenchant3", "Protection: Take reduced damage in water.");

		addGui("water.axe.minor", "Minor: Fish has chance to drop oak log when killed with axe.");
		addGui("water.axe.major", "Major: Increase block break speed when in water or rain.");
		addGui("water.axe.minorenchant1", "Luck of the Sea: Increase oak log drop chance.");
		addGui("water.axe.minorenchant2", "Lure: Greatly increase oak log drop chance if fish is on land.");
		addGui("water.axe.minorenchant3", "Flame: Chance to gain Nether type log instead of oak log.");
		addGui("water.axe.majorenchant1", "Efficiency: Further increase break speed.");
		addGui("water.axe.majorenchant2", "Respiration: Chance to regain air bubbles when chopping wood underwater.");

		addGui("fire.obtain", "Obtain: Dropped by blazes.");

		addGui("fire.sword.minor", "Minor: Chance to put ground on fire when attacking mob.");
		addGui("fire.sword.major", "Major: The sword deals more damage if the user is on fire.");
		addGui("fire.sword.minorenchant1", "Flame: Increase fire creation chance.");
		addGui("fire.sword.minorenchant2", "Power: Chance to create lava instead of fire.");
		addGui("fire.sword.minorenchant3", "Mending: Repair sword if hit burning mob.");
		addGui("fire.sword.majorenchant1", "Fire Aspect: More Damage.");
		addGui("fire.sword.majorenchant2", "Fire Protection: Chance to block fire damage.");

		addGui("fire.axe.minor", "Minor: Increase block break speed, but chance to catch fire when chopping wood.");
		addGui("fire.axe.major", "Major: The axe is more effective in the Nether, but ONLY works in the Nether.");
		addGui("fire.axe.minorenchant1",
				"Fire Aspect: Further increase break speed, but also increase chance to catch fire.");
		addGui("fire.axe.minorenchant2", "Flame: Further increase break speed if you are on fire.");
		addGui("fire.axe.minorenchant3", "Aqua Affinity: Reduce chance to catch fire.");
		addGui("fire.axe.majorenchant1", "Efficiency: Further increase break speed if in the Nether.");
		addGui("fire.axe.majorenchant2", "Sharpness: Increase damage to Piglins and Hoglins.");

		addGui("frost.obtain", "Obtain: Created by crafting Air and Water Rune together.");

		addGui("frost.sword.minor", "Minor: Chance to shoot slowing Frostball when attacking.");
		addGui("frost.sword.major", "Major: Chance to summon Frost Golem when killing enemies.");
		addGui("frost.sword.minorenchant1", "Multishot: Increase Frostball chance.");
		addGui("frost.sword.minorenchant2", "Knockback: Increase Frostball knockback.");
		addGui("frost.sword.minorenchant3", "Channeling: Increase slow duration.");
		addGui("frost.sword.majorenchant1", "Fortune: Increase Frost Golem spawn chance.");
		addGui("frost.sword.majorenchant2", "Infinity: Double Frost Golem duration.");
		addGui("frost.sword.majorenchant3", "Efficiency: Frost Golem can shoot Frostballs.");

		addGui("frost.axe.minor", "Minor: Increase block break speed in cold biomes.");
		addGui("frost.axe.major", "Major: Chance to gain haste when chopping spruce logs.");
		addGui("frost.axe.minorenchant1", "Efficiency: Further increase break speed in cold biomes.");
		addGui("frost.axe.minorenchant2", "Feather Falling: Further increase break speed if it is snowing.");
		addGui("frost.axe.majorenchant1", "Fortune: Increase chance to gain haste.");
		addGui("frost.axe.majorenchant2", "Infinity: Increase haste duration.");
		addGui("frost.axe.majorenchant3", "Power: Chance to gain more powerful haste.");
		addGui("frost.axe.majorenchant4", "Frost Walker: Chance to also gain speed.");

		addGui("magic.obtain",
				"Obtain: Created by crafting all elemental runes (water, fire, air and earth) together with a blood rune.");
		addGui("magic.text",
				"The magic rune is a special rune, that has an active effect instead of a passive effect. Right-clicking a sword or axe with a magic rune equipped in the major slot will create a magic effect. The power of the effect can be boosted by equipping the sword or axe with minor magic runes.");

		addGui("magic.sword.minor", "Minor: Boost the major effect depending on enchantments.");
		addGui("magic.sword.major", "Major: Activate to fire a magic projectile.");
		addGui("magic.sword.minorenchant1", "Aqua Affinity: Increase projectile damage underwater.");
		addGui("magic.sword.minorenchant2", "Bane of Arthropods: Increase projectile damage against arthropod mobs.");
		addGui("magic.sword.minorenchant3", "Blast Protection: Chance to explode upon projectile contact.");
		addGui("magic.sword.minorenchant4", "Channeling: Chance to create lightning upon projectile contact.");
		addGui("magic.sword.minorenchant5", "Depth Strider: Increase underwater projectile speed.");
		addGui("magic.sword.minorenchant6", "Efficiency: Increase damage based on distance travelled.");
		addGui("magic.sword.minorenchant7", "Feather Falling: Chance to apply levitation on projectile hit.");
		addGui("magic.sword.minorenchant8", "Fire Aspect: Chance to apply fire on projectile hit.");
		addGui("magic.sword.minorenchant9", "Fire Protection: Projectile leaves trail of fire.");
		addGui("magic.sword.minorenchant10", "Flame: Chance to apply fire on projectile hit.");
		addGui("magic.sword.minorenchant11", "Fortune: Chance to deal double damage.");
		addGui("magic.sword.minorenchant12", "Frost Walker: Projectile leaves trail of snow.");
		addGui("magic.sword.minorenchant13", "Impaling: Increase projectile damage against underwater mobs.");
		addGui("magic.sword.minorenchant14", "Infinity: Increase projectile range.");
		addGui("magic.sword.minorenchant15", "Knockback: Add knockback to projectile.");
		addGui("magic.sword.minorenchant16", "Looting: More experience dropped when killing mobs.");
		addGui("magic.sword.minorenchant17", "Loyalty: Chance for projectile to boomerang back.");
		addGui("magic.sword.minorenchant18",
				"Luck of the Sea: Temporary underwater breathing when launching projectile.");
		addGui("magic.sword.minorenchant19", "Lure: Pull in nearby enemies at projectile impact.");
		addGui("magic.sword.minorenchant20", "Mending: Chance to heal when projectile hits mob.");
		addGui("magic.sword.minorenchant21", "Multishot: Chance to shoot several projectiles.");
		addGui("magic.sword.minorenchant22", "Piercing: Chance to pass through entities.");
		addGui("magic.sword.minorenchant23", "Power: Increase projectile accuracy.");
		addGui("magic.sword.minorenchant24", "Projectile Protection: Chance to destroy nearby projectiles.");
		addGui("magic.sword.minorenchant25", "Protection: Chance to gain protection effect when projectile hits mob.");
		addGui("magic.sword.minorenchant26", "Punch: Add knock-up to projectile.");
		addGui("magic.sword.minorenchant27", "Quick Charge: Reduce projectile cooldown.");
		addGui("magic.sword.minorenchant28", "Respiration: Restore air when projectile hits mob.");
		addGui("magic.sword.minorenchant29", "Riptide: Launch player towards projectile impact.");
		addGui("magic.sword.minorenchant30", "Sharpness: Increase projectile damage.");
		addGui("magic.sword.minorenchant31", "Silk Touch: Small chance to drop spawn egg when projectile kills mob.");
		addGui("magic.sword.minorenchant32", "Smite: Increase damage against undead mobs.");
		addGui("magic.sword.minorenchant33", "Soul Speed: Increase projectile speed.");
		addGui("magic.sword.minorenchant34", "Sweeping Edge: AoE damage at projectile impact.");
		addGui("magic.sword.minorenchant35", "Thorns: Chance to apply poison when projectile hits mob.");
		addGui("magic.sword.minorenchant36", "Unbreaking: Chance to ignore armor when projectile hits mob.");

		addGui("magic.axe.minor", "Minor: Boost the major effect depending on enchantments.");
		addGui("magic.axe.major", "Major: Activate to damage nearby entities with a magic area effect.");
		addGui("magic.axe.minorenchant1", "Aqua Affinity: Increase damage underwater.");
		addGui("magic.axe.minorenchant2", "Bane of Arthropods: Increase damage against arthropod mobs.");
		addGui("magic.axe.minorenchant3", "Blast Protection: Chance to create explosion at player.");
		addGui("magic.axe.minorenchant4", "Channeling: Deal more damage the further away the enemy is.");
		addGui("magic.axe.minorenchant5", "Depth Strider: Larger radius if in water.");
		addGui("magic.axe.minorenchant6", "Efficiency: Chance to chop nearby wood.");
		addGui("magic.axe.minorenchant7", "Feather Falling: Propel yourself upwards.");
		addGui("magic.axe.minorenchant8", "Fire Aspect: Chance to apply fire to entities.");
		addGui("magic.axe.minorenchant9", "Fire Protection: Chance to set nearby ground on fire.");
		addGui("magic.axe.minorenchant10", "Flame: Deal more damage in the Nether.");
		addGui("magic.axe.minorenchant11", "Fortune: Chance to deal double damage.");
		addGui("magic.axe.minorenchant12", "Frost Walker: Chance to create snow on ground nearby.");
		addGui("magic.axe.minorenchant13", "Impaling: Increase damage against underwater mobs.");
		addGui("magic.axe.minorenchant14", "Infinity: Increase damage, but at the cost of longer cooldown.");
		addGui("magic.axe.minorenchant15", "Knockback: Add more knockback.");
		addGui("magic.axe.minorenchant16",
				"Looting: More experience dropped when killing mobs with magic area effect.");
		addGui("magic.axe.minorenchant17", "Loyalty: Larger radius if riding horse.");
		addGui("magic.axe.minorenchant18", "Luck of the Sea: Chance to create seagrass if in water.");
		addGui("magic.axe.minorenchant19", "Lure: Pull in nearby enemies.");
		addGui("magic.axe.minorenchant20", "Mending: Chance to heal when activating magic power.");
		addGui("magic.axe.minorenchant21", "Multishot: Chance to also shoot magic projectile in random direction.");
		addGui("magic.axe.minorenchant22", "Piercing: Deal more damage if enemy is wearing armor.");
		addGui("magic.axe.minorenchant23", "Power: More damage if attack recharge meter is full.");
		addGui("magic.axe.minorenchant24", "Projectile Protection: Chance to destroy nearby projectiles.");
		addGui("magic.axe.minorenchant25", "Protection: Chance to activate magic area effect when taking damage.");
		addGui("magic.axe.minorenchant26", "Punch: Add knock-up.");
		addGui("magic.axe.minorenchant27", "Quick Charge: Reduce cooldown.");
		addGui("magic.axe.minorenchant28", "Respiration: Restore air when activating magic power.");
		addGui("magic.axe.minorenchant29", "Riptide: Launch player forward when activating magic power.");
		addGui("magic.axe.minorenchant30", "Sharpness: Increase damage.");
		addGui("magic.axe.minorenchant31", "Silk Touch: Chance to bonemeal nearby plants.");
		addGui("magic.axe.minorenchant32", "Smite: Increase damage against undead mobs.");
		addGui("magic.axe.minorenchant33", "Soul Speed: Increase damage if you have speed potion effect.");
		addGui("magic.axe.minorenchant34", "Sweeping Edge: Larger radius.");
		addGui("magic.axe.minorenchant35", "Thorns: Chance to spawn oak saplings nearby.");
		addGui("magic.axe.minorenchant36", "Unbreaking: Chance to ignore armor.");
	}

	private void addGui(String suffix, String text) {
		addText("gui", suffix, text);
	}

	private void addText(String prefix, String suffix, String text) {
		add(prefix + "." + Main.MODID + "." + suffix, text);
	}
}
