package mod.vemerion.runesword.entity;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.helpers.Helper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class MagicBallEntity extends AbstractArrowEntity {

	public MagicBallEntity(EntityType<? extends MagicBallEntity> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
		this.setDamage(4);
		this.setNoGravity(true);
	}

	public MagicBallEntity(double x, double y, double z, World world) {
		super(Main.MAGIC_BALL_ENTITY, x, y, z, world);
		this.setDamage(4);
		this.setNoGravity(true);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		if (!world.isRemote) {
			remove();
		}
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult result) {
		if (!world.isRemote) {
			Entity target = result.getEntity();
			if (func_234616_v_() != null && func_234616_v_() instanceof PlayerEntity) { // getShooter()
				target.attackEntityFrom(Helper.magicDamage(this, (PlayerEntity) func_234616_v_()), (float) getDamage());
			} else {
				target.attackEntityFrom(Helper.magicDamage(), (float) getDamage());
			}
		}
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected ItemStack getArrowStack() {
		return ItemStack.EMPTY;
	}

}
