package mod.vemerion.runesword.lootmodifier.lootcondition;

import mod.vemerion.runesword.Main;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootConditions {
	
	protected static LootItemConditionType IN_BIOME;
	protected static LootItemConditionType IS_ENTITY_TYPE;
	protected static LootItemConditionType FROM_CHEST;
	protected static LootItemConditionType BELOW_DEPTH;


	public static void register() {
		IN_BIOME = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Main.MODID, "in_biome"),
				new LootItemConditionType(new InBiome.ConditionSerializer()));
		IS_ENTITY_TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE,
				new ResourceLocation(Main.MODID, "is_entity_type"),
				new LootItemConditionType(new IsEntityType.ConditionSerializer()));
		FROM_CHEST = Registry.register(Registry.LOOT_CONDITION_TYPE,
				new ResourceLocation(Main.MODID, "from_chest"),
				new LootItemConditionType(new FromChest.ConditionSerializer()));
		BELOW_DEPTH = Registry.register(Registry.LOOT_CONDITION_TYPE,
				new ResourceLocation(Main.MODID, "below_depth"),
				new LootItemConditionType(new BelowDepth.ConditionSerializer()));

	}
}
