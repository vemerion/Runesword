package mod.vemerion.runesword.init;

import com.mojang.serialization.Codec;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.particle.MagicBallParticleData;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Main.MODID);

	public static final RegistryObject<ParticleType<MagicBallParticleData>> MAGIC_BALL = PARTICLES.register("magic_ball",
			() -> new ParticleType<MagicBallParticleData>(true, new MagicBallParticleData.Deserializer()) {

				@Override
				public Codec<MagicBallParticleData> codec() {
					return MagicBallParticleData.CODEC;
				}
			});
	
	public static final RegistryObject<SimpleParticleType> BLEED = PARTICLES.register("bleed",
			() -> new SimpleParticleType(true));
}
