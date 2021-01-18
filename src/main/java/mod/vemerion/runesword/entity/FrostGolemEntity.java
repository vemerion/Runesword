package mod.vemerion.runesword.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class FrostGolemEntity extends SnowGolemEntity {

	private static final int MAX_DURATION = 20 * 30;

	private int duration;

	public FrostGolemEntity(EntityType<? extends FrostGolemEntity> type, World worldIn) {
		super(type, worldIn);
		this.duration = MAX_DURATION;
	}

	@Override
	public void tick() {
		super.tick();
		duration--;
		if (!world.isRemote && duration <= 0) {
			remove();
			((ServerWorld) world).spawnParticle(ParticleTypes.ITEM_SNOWBALL, getPosX(), getPosY() + 1, getPosZ(), 20, 0.3, 0.3,
					0.3, 0.5);
            this.playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
		}
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		if (nbt.contains("duration"))
			duration = nbt.getInt("duration");
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.putInt("duration", duration);
		return nbt;
	}

}
