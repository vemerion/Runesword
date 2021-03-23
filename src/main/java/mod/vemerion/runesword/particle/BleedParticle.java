package mod.vemerion.runesword.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

public class BleedParticle extends SpriteTexturedParticle {

	protected BleedParticle(ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.maxAge = 25;
		this.particleGravity = 1;
		this.motionX = 0;
		this.motionZ = 0;
		this.motionY = -0.1;
	}

	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_LIT;
	}

	public static class Factory implements IParticleFactory<BasicParticleType> {
		private final IAnimatedSprite sprites;

		public Factory(IAnimatedSprite sprite) {
			this.sprites = sprite;
		}

		@Override
		public Particle makeParticle(BasicParticleType typeIn, ClientWorld worldIn, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			BleedParticle particle = new BleedParticle(worldIn, x, y, z, ySpeed, zSpeed, xSpeed);
			particle.selectSpriteRandomly(sprites);
			return particle;
		}
	}
}
