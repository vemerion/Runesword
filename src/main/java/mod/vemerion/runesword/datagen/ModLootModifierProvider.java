package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
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
		add("air_rune_loot_modifier", Main.AIR_RUNE_LOOT_MODIFIER,
				new RuneLootModifier(
						new LootItemCondition[] { LootItemKilledByPlayerCondition.killedByPlayer().build(),
								LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.1f, 0.02f)
										.build(),
								LootTableIdCondition.builder(EntityType.PHANTOM.getDefaultLootTable()).build() },
						RuneItem.AIR_RUNE_ITEM));
		add("blood_rune_loot_modifier", Main.BLOOD_RUNE_LOOT_MODIFIER,
				new RuneLootModifier(new LootItemCondition[] { LootItemKilledByPlayerCondition.killedByPlayer().build(),
						LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.002f, 0.001f).build() },
						RuneItem.BLOOD_RUNE_ITEM));
		add("earth_rune_loot_modifier", Main.EARTH_RUNE_LOOT_MODIFIER, new RuneLootModifier(
				new LootItemCondition[] { LootItemRandomChanceCondition.randomChance(0.07f).build(),
						AlternativeLootItemCondition
								.alternative(LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON),
										LootTableIdCondition.builder(BuiltInLootTables.ABANDONED_MINESHAFT),
										LootTableIdCondition.builder(BuiltInLootTables.STRONGHOLD_CORRIDOR))
								.build() },
				RuneItem.EARTH_RUNE_ITEM));
		add("fire_rune_loot_modifier", Main.FIRE_RUNE_LOOT_MODIFIER,
				new RuneLootModifier(
						new LootItemCondition[] {
								LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.002f, 0.001f)
										.build(),
								LootItemKilledByPlayerCondition.killedByPlayer().build(),
								LootTableIdCondition.builder(EntityType.BLAZE.getDefaultLootTable()).build() },
						RuneItem.FIRE_RUNE_ITEM));
		add("water_rune_loot_modifier", Main.WATER_RUNE_LOOT_MODIFIER, new RuneLootModifier(
				new LootItemCondition[] { LootItemRandomChanceCondition.randomChance(0.05f).build(),
						AlternativeLootItemCondition
								.alternative(LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_BIG),
										LootTableIdCondition.builder(BuiltInLootTables.UNDERWATER_RUIN_SMALL),
										LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_SUPPLY),
										LootTableIdCondition.builder(BuiltInLootTables.SHIPWRECK_TREASURE))
								.build() },
				RuneItem.WATER_RUNE_ITEM));
	}

}
