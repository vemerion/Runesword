package mod.vemerion.runesword.init;

import com.mojang.serialization.Codec;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.lootmodifier.RuneLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
	public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister
			.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Main.MODID);

	public static final RegistryObject<Codec<RuneLootModifier>> RUNE = LOOT_MODIFIERS.register("rune",
			() -> RuneLootModifier.CODEC);
}
