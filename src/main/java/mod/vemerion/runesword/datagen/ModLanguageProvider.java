package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(DataGenerator gen) {
		super(gen, Main.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addGui("no_sword", "Insert sword into runeforge");
		addGui("slot_locked", "Slot locked");
		add(Main.RUNEFORGE_BLOCK, "Runeforge");
		add(RuneItem.AIR_RUNE_ITEM, "Air Rune");
		add(RuneItem.FIRE_RUNE_ITEM, "Fire Rune");
		add(RuneItem.WATER_RUNE_ITEM, "Water Rune");
		add(RuneItem.EARTH_RUNE_ITEM, "Earth Rune");
		add(RuneItem.BLOOD_RUNE_ITEM, "Blood Rune");
		add(RuneItem.FROST_RUNE_ITEM, "Frost Rune");
		add(RuneItem.MAGIC_RUNE_ITEM, "Magic Rune");
		addText("tooltip", "minor", "Minor: ");
		addText("tooltip", "major", "Major: ");
		add(Main.GUIDE_ITEM, "Runesword Guide");
		add("itemGroup." + Main.MODID, "Runesword");
		addGui("guide", "Runesword");
		addGui("home", "Home");
		addText("death.attack", "magicplayer", "%1$s was magicked to death by %2$s");
		addText("death.attack", "magicindirect", "%1$s was magicked to death by %2$s");
		addText("death.attack", "magic", "%1$s was magicked to death");
		add(Main.FROST_GOLEM_ENTITY, "Frost Golem");
		add(Main.FROSTBALL_ENTITY, "Frostball");
		add(Main.MAGIC_BALL_ENTITY, "Magic Ball");
		guide();
	}

	private void guide() {
		addGui("intro",
				"Welcome to the Runesword guide, containing all the information you need to become proficient in runeforging! Click on one of the icons above to learn more about a specific subject.");
		addGui("runeforge1", "The runeforge is the block used to insert runes into swords. Here is how you craft it:");
		addGui("runeforge2", "And here is the runeforge GUI:");
		addGui("runeforge3",
				"You start by inserting a sword into the middle slot (1). You can then insert runes into the sword by placing them in slot 2-5. The higher the quality of the sword, the more runes can be inserted. For example, a stone sword only has the first rune slot unlocked (2), while a netherite sword has all four rune slots unlocked.");
		addGui("runeforge4",
				"The rune slots 2-4 are minor rune slots, while the top slot (5) is a major rune slot. Runes in the minor slots will have a smaller effect, while the rune in the major slot will have a big effect.");
		addGui("enchanting", "Rune Enchanting");
		addGui("enchantingtext",
				"Like other tools, runes can be enchanted in an enchanting table to boost the effect of the runes. Some enchantments increase the power of the major effect, while some enchantments increase the power of the minor effect. You can read about the different possible enchantments for every type of rune in their respective guide chapter.");

		addGui("blood.obtain", "Obtain: Low drop chance from all mobs.");
		addGui("blood.minor", "Minor: Chance to heal on kill.");
		addGui("blood.major", "Major: Attacks cost health, but deal more damage.");
		addGui("minorenchants", "Minor Enchantments");
		addGui("majorenchants", "Major Enchantments");
		addGui("blood.minorenchant1", "Fire Aspect: More lifesteal if enemy on fire.");
		addGui("blood.minorenchant2", "Aqua Affinity: More lifesteal if in water.");
		addGui("blood.minorenchant3", "Mending: chance to repair sword on attack.");
		addGui("blood.majorenchant1", "Protection: Chance to not drain your health when attacking.");
		addGui("blood.majorenchant2", "Sharpness: More damage.");

		addGui("air.obtain", "Obtain: Dropped by phantoms.");
		addGui("air.minor", "Minor: Chance to knock up enemies when attacking.");
		addGui("air.major", "Major: Grants speed boost on kill.");
		addGui("air.minorenchant1", "Knockback: Adds knockback to attacks.");
		addGui("air.minorenchant2", "Punch: Increase knock-up height.");
		addGui("air.majorenchant1", "Infinity: Double speed duration.");
		addGui("air.majorenchant2", "Efficiency: Chance to get speed II.");
		addGui("air.majorenchant3", "Feather Falling: Chance to get Jump Boost.");

		addGui("earth.obtain", "Obtain: Found in treasure chests underground.");
		addGui("earth.minor", "Minor: Mobs have a chance to drop ores on kill.");
		addGui("earth.major", "Major: The sword deals more damage while you are underground.");
		addGui("earth.minorenchant1", "Fortune: Increaes ore drop chance.");
		addGui("earth.minorenchant2", "Looting: Chance that enemy drops rare ore.");
		addGui("earth.minorenchant3", "Fire Aspect: Chance to smelt ore immediately.");
		addGui("earth.majorenchant1", "Shaprness: More damage underground.");
		addGui("earth.majorenchant2", "Protection: Take reduced damage underground.");

		addGui("water.obtain", "Obtain: Found in treasure chests under water.");
		addGui("water.minor", "Minor: Restore breath on kill.");
		addGui("water.major", "Major: The sword deals more damage while under water.");
		addGui("water.minorenchant1", "Respiration: Increase bubble restoration.");
		addGui("water.minorenchant2", "Luck of the Sea: Chance to ignore drowning.");
		addGui("water.majorenchant1", "Aqua Affinity: Major effect also in rain.");
		addGui("water.majorenchant2", "Sharpness: Increase bonus damage chance.");
		addGui("water.majorenchant3", "Protection: Take reduced damage in water.");

		addGui("fire.obtain", "Obtain: Dropped by blazes.");
		addGui("fire.minor", "Minor: Chance to put ground on fire when attacking mob.");
		addGui("fire.major", "Major: The sword deals more damage if the user is on fire.");
		addGui("fire.minorenchant1", "Flame: Increase fire creation chance.");
		addGui("fire.minorenchant2", "Power: Chance to create lava instead of fire.");
		addGui("fire.minorenchant3", "Mending: Repair sword if hit burning mob.");
		addGui("fire.majorenchant1", "Fire Aspect: More Damage.");
		addGui("fire.majorenchant2", "Fire Protection: Chance to block fire damage.");

		addGui("frost.obtain", "Obtain: Created by crafting Air and Water Rune together.");
		addGui("frost.minor", "Minor: Chance to shoot slowing Frostball when attacking.");
		addGui("frost.major", "Major: Chance to summon Frost Golem when killing enemies.");
		addGui("frost.minorenchant1", "Multishot: Increase Frostball chance.");
		addGui("frost.minorenchant2", "Knockback: Increase Frostball knockback.");
		addGui("frost.minorenchant3", "Channeling: Increase slow duration.");
		addGui("frost.majorenchant1", "Fortune: Increase Frost Golem spawn chance.");
		addGui("frost.majorenchant2", "Infinity: Double Frost Golem duration.");
		addGui("frost.majorenchant3", "Efficiency: Frost Golem can shoot Frostballs.");

		addGui("magic.obtain",
				"Obtain: Created by crafting all elemental runes (water, fire, air and earth) together with a blood rune.");
		addGui("magic.minor", "Minor: Boosts the major effect depending on enchantments.");
		addGui("magic.major", "Major: Activate to fire a magic projectile.");
		addGui("magic.text",
				"The magic rune is a special rune, that has an active effect instead of a passive effect. Right-clicking a sword with a magic rune equipped in the major slot will create a magic projectile that deals damage when it hits an entity. The effect of the projectile can be boosted by equipping the sword with minor magic runes.");
		addGui("magic.minorenchant1", "Aqua Affinity: Increase projectile damage underwater.");
		addGui("magic.minorenchant2", "Bane of Arthropods: Increase projectile damage against arthropod mobs.");
		addGui("magic.minorenchant3", "Blast Protection: Chance to explode upon projectile contact.");
		addGui("magic.minorenchant4", "Channeling: Chance to create lightning upon projectile contact.");
		addGui("magic.minorenchant5", "Depth Strider: Increase underwater projectile speed.");
		addGui("magic.minorenchant6", "Efficiency: Increase damage based on distance travelled.");
		addGui("magic.minorenchant7", "Feather Falling: Chance to apply levitation on projectile hit.");
		addGui("magic.minorenchant8", "Fire Aspect: Chance to apply fire on projectile hit.");
		addGui("magic.minorenchant9", "Fire Protection: Projectile leaves trail of fire.");
		addGui("magic.minorenchant10", "Flame: Chance to apply fire on projectile hit.");
		addGui("magic.minorenchant11", "Fortune: Chance to deal double damage.");
		addGui("magic.minorenchant12", "Frost Walker: Projectile leaves trail of snow.");
		addGui("magic.minorenchant13", "Impaling: Increase projectile damage against underwater mobs.");
		addGui("magic.minorenchant14", "Infinity: Increase projectile range.");
		addGui("magic.minorenchant15", "Knockback: Add knockback to projectile.");
		addGui("magic.minorenchant16", "Looting: More experience dropped when killing mobs.");
		addGui("magic.minorenchant17", "Loyalty: Chance for projectile to boomerang back.");
		addGui("magic.minorenchant18", "Luck of the Sea: Temporary underwater breathing when launching projectile.");
		addGui("magic.minorenchant19", "Lure: Pull in nearby enemies at projectile impact.");
		addGui("magic.minorenchant20", "Mending: Chance to heal when projectile hits mob.");
		addGui("magic.minorenchant21", "Multishot: Chance to shoot several projectiles.");
		addGui("magic.minorenchant22", "Piercing: Chance to pass through entities.");
		addGui("magic.minorenchant23", "Power: Increase projectile accuracy.");
		addGui("magic.minorenchant24", "Projectile Protection: Chance to destroy nearby projectiles.");
		addGui("magic.minorenchant25", "Protection: Chance to gain protection effect when projectile hits mob.");
		addGui("magic.minorenchant26", "Punch: Add knock-up to projectile.");
		addGui("magic.minorenchant27", "Quick Charge: Reduce projectile cooldown.");
		addGui("magic.minorenchant28", "Respiration: Restore air when projectile hits mob.");
		addGui("magic.minorenchant29", "Riptide: Launch player towards projectile impact.");
		addGui("magic.minorenchant30", "Sharpness: Increase projectile damage.");
		addGui("magic.minorenchant31", "Silk Touch: Small chance to drop spawn egg when projectile kills mob.");
		addGui("magic.minorenchant32", "Smite: Increase damage against undead mobs.");
		addGui("magic.minorenchant33", "Soul Speed: Increase projectile speed.");
		addGui("magic.minorenchant34", "Sweeping Edge: AoE damage at projectile impact.");
		addGui("magic.minorenchant35", "Thorns: Chance to apply poison when projectile hits mob.");
		addGui("magic.minorenchant36", "Unbreaking: Chance to ignore armor when projectile hits mob.");
	}

	private void addGui(String suffix, String text) {
		addText("gui", suffix, text);
	}

	private void addText(String prefix, String suffix, String text) {
		add(prefix + "." + Main.MODID + "." + suffix, text);
	}
}
