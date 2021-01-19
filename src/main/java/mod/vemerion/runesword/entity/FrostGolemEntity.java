package mod.vemerion.runesword.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FrostGolemEntity extends SnowGolemEntity {

	private int duration;
	private int efficiency;
	
	public FrostGolemEntity(EntityType<? extends FrostGolemEntity> type, World worldIn) {
		super(type, worldIn);
		duration = 100;
	}

	public FrostGolemEntity(EntityType<? extends FrostGolemEntity> type, World worldIn, int duration, int efficiency) {
		this(type, worldIn);
		this.duration = duration;
		this.efficiency = efficiency;
	}

	@Override
	public void tick() {
		super.tick();
		duration--;
		if (!world.isRemote && duration <= 0) {
			remove();
			((ServerWorld) world).spawnParticle(ParticleTypes.ITEM_SNOWBALL, getPosX(), getPosY() + 1, getPosZ(), 20,
					0.3, 0.3, 0.3, 0.5);
			this.playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
		}
	}

	@Override
	public void attackEntityWithRangedAttack(LivingEntity target, float distanceFactor) {
		if (getRNG().nextDouble() < efficiency * 0.2) {
			FrostballEntity frostball = new FrostballEntity(world, this);
			double x = target.getPosX() - getPosX();
			double y = target.getPosYEye() - 1.1f - frostball.getPosY();
			double z = target.getPosZ() - this.getPosZ();
			double height = MathHelper.sqrt(x * x + z * z) * 0.2;
			frostball.shoot(x, y + height, z, 1.6F, 12.0F);
			playSound(SoundEvents.ENTITY_SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (getRNG().nextFloat() * 0.4F + 0.8F));
			world.addEntity(frostball);
		} else {
			super.attackEntityWithRangedAttack(target, distanceFactor);
		}
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		if (nbt.contains("duration"))
			duration = nbt.getInt("duration");
		if (nbt.contains("efficiency"))
			efficiency = nbt.getInt("efficiency");
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.putInt("duration", duration);
		nbt.putInt("efficiency", efficiency);
		return nbt;
	}

}
