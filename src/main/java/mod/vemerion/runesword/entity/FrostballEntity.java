package mod.vemerion.runesword.entity;

import mod.vemerion.runesword.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FrostballEntity extends SnowballEntity {

	public static final int SLOW_DURATION = 20 * 5;

	private int knockback;
	private int slowDuration = SLOW_DURATION;

	public FrostballEntity(EntityType<? extends FrostballEntity> type, World world) {
		super(type, world);
	}

	public FrostballEntity(World world, LivingEntity shooter, int knockback, int slowDuration) {
		this(Main.FROSTBALL_ENTITY, world);
		this.setShooter(shooter);
		this.setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.1, shooter.getPosZ());
		this.knockback = knockback;
		this.slowDuration = slowDuration;
	}

	public FrostballEntity(World world, LivingEntity shooter) {
		this(world, shooter, 0, SLOW_DURATION);
	}

	protected void onEntityHit(EntityRayTraceResult result) {
		super.onEntityHit(result);
		Entity entity = result.getEntity();
		Vector3d direction = getMotion();
		entity.addVelocity(direction.x * 0.3 * (1 + knockback * 0.1), 0.1, direction.z * 0.3 * (1 + knockback * 0.1));
		if (entity instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) entity;
			living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, slowDuration));
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		if (nbt.contains("duration"))
			knockback = nbt.getInt("knockback");
		if (nbt.contains("slowDuration"))
			slowDuration = nbt.getInt("slowDuration");
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.putInt("knockback", knockback);
		nbt.putInt("slowDuration", slowDuration);
		return nbt;
	}

}