package mod.vemerion.runesword.init;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.effect.BleedEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
	
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Main.MODID);

	public static final RegistryObject<MobEffect> BLEED = EFFECTS.register("bleed",
			() -> new BleedEffect());
}
