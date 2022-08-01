package mod.vemerion.runesword.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class BleedParticle extends TextureSheetParticle {

	protected BleedParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed);
		this.lifetime = 25;
		this.gravity = 1;
		this.xd = 0;
		this.zd = 0;
		this.yd = -0.1;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		@Override
		public Particle createParticle(SimpleParticleType typeIn, ClientLevel level, double x, double y, double z,
				double xSpeed, double ySpeed, double zSpeed) {
			BleedParticle particle = new BleedParticle(level, x, y, z, ySpeed, zSpeed, xSpeed);
			particle.pickSprite(sprites);
			return particle;
		}
	}
}
