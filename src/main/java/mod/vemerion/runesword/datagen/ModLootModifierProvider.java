package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import mod.vemerion.runesword.lootmodifier.lootcondition.BelowDepth;
import mod.vemerion.runesword.lootmodifier.lootcondition.FromChest;
import mod.vemerion.runesword.lootmodifier.lootcondition.InBiome;
import mod.vemerion.runesword.lootmodifier.lootcondition.IsEntityType;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.KilledByPlayer;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.RandomChanceWithLooting;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModLootModifierProvider extends GlobalLootModifierProvider {

	public ModLootModifierProvider(DataGenerator gen) {
		super(gen, Main.MODID);
	}

	@Override
	protected void start() {
		add("air_rune_loot_modifier", Main.AIR_RUNE_LOOT_MODIFIER,
				new RuneLootModifier(new ILootCondition[] { KilledByPlayer.builder().build(),
						RandomChanceWithLooting.builder(0.1f, 0.02f).build(), new IsEntityType(EntityType.PHANTOM) },
						RuneItem.AIR_RUNE_ITEM));
		add("blood_rune_loot_modifier", Main.BLOOD_RUNE_LOOT_MODIFIER,
				new RuneLootModifier(new ILootCondition[] { KilledByPlayer.builder().build(),
						RandomChanceWithLooting.builder(0.002f, 0.001f).build() }, RuneItem.BLOOD_RUNE_ITEM));
		add("earth_rune_loot_modifier", Main.EARTH_RUNE_LOOT_MODIFIER, new RuneLootModifier(
				new ILootCondition[] { RandomChance.builder(0.07f).build(), new BelowDepth(30), new FromChest() },
				RuneItem.EARTH_RUNE_ITEM));
		add("fire_rune_loot_modifier", Main.FIRE_RUNE_LOOT_MODIFIER,
				new RuneLootModifier(
						new ILootCondition[] { RandomChanceWithLooting.builder(0.002f, 0.001f).build(),
								KilledByPlayer.builder().build(), new IsEntityType(EntityType.BLAZE) },
						RuneItem.FIRE_RUNE_ITEM));
		add("water_rune_loot_modifier", Main.WATER_RUNE_LOOT_MODIFIER,
				new RuneLootModifier(new ILootCondition[] { RandomChance.builder(0.05f).build(),
						new InBiome(Biome.Category.OCEAN), new FromChest() }, RuneItem.WATER_RUNE_ITEM));
	}

}
