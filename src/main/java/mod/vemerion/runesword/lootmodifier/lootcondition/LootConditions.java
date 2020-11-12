package mod.vemerion.runesword.lootmodifier.lootcondition;

import mod.vemerion.runesword.Main;
import net.minecraft.loot.LootConditionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class LootConditions {
	
	protected static LootConditionType IN_BIOME;
	protected static LootConditionType IS_ENTITY_TYPE;
	protected static LootConditionType FROM_CHEST;


	public static void register() {
		IN_BIOME = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(Main.MODID, "in_biome"),
				new LootConditionType(new InBiome.Serializer()));
		IS_ENTITY_TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE,
				new ResourceLocation(Main.MODID, "is_entity_type"),
				new LootConditionType(new IsEntityType.Serializer()));
		FROM_CHEST = Registry.register(Registry.LOOT_CONDITION_TYPE,
				new ResourceLocation(Main.MODID, "from_chest"),
				new LootConditionType(new FromChest.Serializer()));

	}
}
