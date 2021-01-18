package mod.vemerion.runesword.entity;

import mod.vemerion.runesword.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FrostballEntity extends SnowballEntity {

	public FrostballEntity(EntityType<? extends FrostballEntity> type, World world) {
		super(type, world);
	}

	public FrostballEntity(World world, PlayerEntity shooter) {
		this(Main.FROSTBALL_ENTITY, world);
		this.setShooter(shooter);
		this.setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.1, shooter.getPosZ());
	}

	protected void onEntityHit(EntityRayTraceResult result) {
		super.onEntityHit(result);
		Entity entity = result.getEntity();
		Vector3d direction = getMotion();
		entity.addVelocity(direction.x * 0.3, 0.1, direction.z * 0.3);
		if (entity instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) entity;
			living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20 * 10));
		}
	}
	
	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}