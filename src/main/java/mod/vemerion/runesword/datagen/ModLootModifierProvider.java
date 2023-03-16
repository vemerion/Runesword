package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModItems;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import net.minecraft.data.PackOutput;
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

	public ModLootModifierProvider(PackOutput output) {
		super(output, Main.MODID);
	}

	@Override
	protected void start() {
		add("air_rune",
				new RuneLootModifier(
						new LootItemCondition[] { LootItemKilledByPlayerCondition.killedByPlayer().build(),
								LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1f, 0.02f)
										.build(),
								LootTableIdCondition.builder(EntityType.PHANTOM.getDefaultLootTable()).build() },
						ModItems.AIR_RUNE.get()));
		add("blood_rune",
				new RuneLootModifier(new LootItemCondition[] { LootItemKilledByPlayerCondition.killedByPlayer().build(),
						LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.002f, 0.001f).build() },
						ModItems.BLOOD_RUNE.get()));
		add("earth_rune", new RuneLootModifier(
				new LootItemCondition[] { LootItemRandomChanceCondition.randomChance(0.07f).build(),
						AlternativeLootItemCondition
								.alternative(LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON),
										LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT),
										LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR))
								.build() },
				ModItems.EARTH_RUNE.get()));
		add("fire_rune",
				new RuneLootModifier(
						new LootItemCondition[] {
								LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.02f, 0.01f)
										.build(),
								LootItemKilledByPlayerCondition.killedByPlayer().build(),
								LootTableIdCondition.builder(EntityType.BLAZE.getDefaultLootTable()).build() },
						ModItems.FIRE_RUNE.get()));
		add("water_rune", new RuneLootModifier(
				new LootItemCondition[] { LootItemRandomChanceCondition.randomChance(0.05f).build(),
						AlternativeLootItemCondition
								.alternative(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG),
										LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL),
										LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_SUPPLY),
										LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE))
								.build() },
				ModItems.WATER_RUNE.get()));
	}

}
