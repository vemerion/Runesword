package mod.vemerion.runesword.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

public class MagicBallParticle extends TextureSheetParticle {

	protected MagicBallParticle(ClientLevel level, double x, double y, double z, double motionX, double motionY,
			double motionZ, MagicBallParticleData data) {
		super(level, x, y, z, motionX, motionY, motionZ);
		this.rCol = data.getRed();
		this.gCol = data.getGreen();
		this.bCol = data.getBlue();
	}
	
	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

	public static class Provider implements ParticleProvider<MagicBallParticleData> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(MagicBallParticleData data, ClientLevel level, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			MagicBallParticle particle = new MagicBallParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, data);
			particle.pickSprite(sprites);
			return particle;
		}
	}
}
