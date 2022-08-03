package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModItems;
import mod.vemerion.runesword.init.ModLootModifiers;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.AlternativeLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModLootModifierProvider extends GlobalLootModifierProvider {

	public ModLootModifierProvider(DataGenerator gen) {
		super(gen, Main.MODID);
	}

	@Override
	protected void start() {
		add("air_rune", ModLootModifiers.AIR_RUNE.get(),
				new RuneLootModifier(
						new LootItemCondition[] { LootItemKilledByPlayerCondition.killedByPlayer().build(),
								LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1f, 0.02f)
										.build(),
								LootTableIdCondition.builder(EntityType.PHANTOM.getDefaultLootTable()).build() },
						ModItems.AIR_RUNE));
		add("blood_rune", ModLootModifiers.BLOOD_RUNE.get(),
				new RuneLootModifier(new LootItemCondition[] { LootItemKilledByPlayerCondition.killedByPlayer().build(),
						LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.002f, 0.001f).build() },
						ModItems.BLOOD_RUNE));
		add("earth_rune", ModLootModifiers.EARTH_RUNE.get(), new RuneLootModifier(
				new LootItemCondition[] { LootItemRandomChanceCondition.randomChance(0.07f).build(),
						AlternativeLootItemCondition
								.alternative(LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON),
										LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT),
										LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR))
								.build() },
				ModItems.EARTH_RUNE));
		add("fire_rune", ModLootModifiers.FIRE_RUNE.get(),
				new RuneLootModifier(
						new LootItemCondition[] {
								LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.002f, 0.001f)
										.build(),
								LootItemKilledByPlayerCondition.killedByPlayer().build(),
								LootTableIdCondition.builder(EntityType.BLAZE.getDefaultLootTable()).build() },
						ModItems.FIRE_RUNE));
		add("water_rune", ModLootModifiers.WATER_RUNE.get(), new RuneLootModifier(
				new LootItemCondition[] { LootItemRandomChanceCondition.randomChance(0.05f).build(),
						AlternativeLootItemCondition
								.alternative(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG),
										LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL),
										LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_SUPPLY),
										LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE))
								.build() },
				ModItems.WATER_RUNE));
	}

}
