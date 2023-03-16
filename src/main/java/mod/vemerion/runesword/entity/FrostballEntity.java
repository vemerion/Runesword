package mod.vemerion.runesword.entity;

import mod.vemerion.runesword.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class FrostballEntity extends Snowball {

	public static final int SLOW_DURATION = 20 * 5;

	private int knockback;
	private int slowDuration = SLOW_DURATION;

	public FrostballEntity(EntityType<? extends FrostballEntity> type, Level level) {
		super(type, level);
	}

	public FrostballEntity(Level level, LivingEntity shooter, int knockback, int slowDuration) {
		this(ModEntities.FROSTBALL.get(), level);
		this.setOwner(shooter);
		this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
		this.knockback = knockback;
		this.slowDuration = slowDuration;
	}

	public FrostballEntity(Level level, LivingEntity shooter) {
		this(level, shooter, 0, SLOW_DURATION);
	}

	protected void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		Entity entity = result.getEntity();
		Vec3 direction = getDeltaMovement();
		entity.push(direction.x * 0.3 * (1 + knockback * 0.1), 0.1, direction.z * 0.3 * (1 + knockback * 0.1));
		if (entity instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) entity;
			living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, slowDuration));
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		if (nbt.contains("duration"))
			knockback = nbt.getInt("knockback");
		if (nbt.contains("slowDuration"))
			slowDuration = nbt.getInt("slowDuration");
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();
		nbt.putInt("knockback", knockback);
		nbt.putInt("slowDuration", slowDuration);
		return nbt;
	}

}