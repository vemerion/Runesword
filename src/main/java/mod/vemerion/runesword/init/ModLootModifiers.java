package mod.vemerion.runesword.init;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
	public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister
			.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, Main.MODID);

	public static final RegistryObject<GlobalLootModifierSerializer<RuneLootModifier>> FIRE_RUNE = LOOT_MODIFIERS.register("fire_rune",
			() -> new RuneLootModifier.Serializer(ModItems.FIRE_RUNE));

	public static final RegistryObject<GlobalLootModifierSerializer<RuneLootModifier>> WATER_RUNE = LOOT_MODIFIERS
			.register("water_rune", () -> new RuneLootModifier.Serializer(ModItems.WATER_RUNE));

	public static final RegistryObject<GlobalLootModifierSerializer<RuneLootModifier>> EARTH_RUNE = LOOT_MODIFIERS
			.register("earth_rune", () -> new RuneLootModifier.Serializer(ModItems.EARTH_RUNE));

	public static final RegistryObject<GlobalLootModifierSerializer<RuneLootModifier>> BLOOD_RUNE = LOOT_MODIFIERS
			.register("blood_rune", () -> new RuneLootModifier.Serializer(ModItems.BLOOD_RUNE));

	public static final RegistryObject<GlobalLootModifierSerializer<RuneLootModifier>> AIR_RUNE = LOOT_MODIFIERS.register("air_rune",
			() -> new RuneLootModifier.Serializer(ModItems.AIR_RUNE));
}
