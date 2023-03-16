package mod.vemerion.runesword.init;

import mod.vemerion.runesword.Main;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,
			Main.MODID);

	public static final RegistryObject<SoundEvent> GUIDE_CLICK = SOUNDS.register("guide_click",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Main.MODID, "guide_click")));

	public static final RegistryObject<SoundEvent> PROJECTILE_IMPACT = SOUNDS.register("projectile_impact",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Main.MODID, "projectile_impact")));

	public static final RegistryObject<SoundEvent> PROJECTILE_LAUNCH = SOUNDS.register("projectile_launch",
			() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Main.MODID, "projectile_launch")));
}
