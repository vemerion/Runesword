package mod.vemerion.runesword.particle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;

public class MagicBallParticle extends SpriteTexturedParticle {

	protected MagicBallParticle(ClientWorld world, double x, double y, double z, double motionX, double motionY,
			double motionZ, MagicBallParticleData data) {
		super(world, x, y, z, motionX, motionY, motionZ);
		this.particleRed = data.getRed();
		this.particleGreen = data.getGreen();
		this.particleBlue = data.getBlue();
	}
	
	@Override
	public IParticleRenderType getRenderType() {
		return IParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements IParticleFactory<MagicBallParticleData> {
		private final IAnimatedSprite spriteSet;

		public Factory(IAnimatedSprite spriteSet) {
			this.spriteSet = spriteSet;
		}

		@Override
		public Particle makeParticle(MagicBallParticleData data, ClientWorld worldIn, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			MagicBallParticle particle = new MagicBallParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, data);
			particle.selectSpriteRandomly(spriteSet);
			return particle;
		}
	}
}
