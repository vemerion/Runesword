package mod.vemerion.runesword.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;

public class FrostGolemEntity extends SnowGolem {

	private int duration;
	private int efficiency;

	public FrostGolemEntity(EntityType<? extends FrostGolemEntity> type, Level level) {
		super(type, level);
		duration = 100;
	}

	public FrostGolemEntity(EntityType<? extends FrostGolemEntity> type, Level level, int duration, int efficiency) {
		this(type, level);
		this.duration = duration;
		this.efficiency = efficiency;
	}

	@Override
	public void tick() {
		super.tick();
		duration--;
		if (!level.isClientSide && duration <= 0) {
			discard();
			((ServerLevel) level).sendParticles(ParticleTypes.ITEM_SNOWBALL, getX(), getY() + 1, getZ(), 20, 0.3, 0.3,
					0.3, 0.5);
			this.playSound(getDeathSound(), getSoundVolume(), getVoicePitch());
		}
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor) {
		if (getRandom().nextDouble() < efficiency * 0.2) {
			FrostballEntity frostball = new FrostballEntity(level, this);
			double x = target.getX() - getX();
			double y = target.getEyeY() - 1.1f - frostball.getY();
			double z = target.getZ() - this.getZ();
			double height = Math.sqrt(x * x + z * z) * 0.2;
			frostball.shoot(x, y + height, z, 1.6F, 12.0F);
			playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (getRandom().nextFloat() * 0.4F + 0.8F));
			level.addFreshEntity(frostball);
		} else {
			super.performRangedAttack(target, distanceFactor);
		}
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		if (nbt.contains("duration"))
			duration = nbt.getInt("duration");
		if (nbt.contains("efficiency"))
			efficiency = nbt.getInt("efficiency");
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();
		nbt.putInt("duration", duration);
		nbt.putInt("efficiency", efficiency);
		return nbt;
	}

}
