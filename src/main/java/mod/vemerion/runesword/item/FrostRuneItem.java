package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class FrostRuneItem extends RuneItem {

	private static final Set<Enchantment> ENCHANTS = ImmutableSet.of();

	public FrostRuneItem(Properties properties) {
		super(new Color(40, 120, 150).getRGB(), properties);
	}

	@Override
	public void onAttackMajor(ItemStack sword, PlayerEntity player, Entity target, ItemStack rune) {
		SnowballEntity snowball = new FrostSnowballEntity(player.world, player);
		snowball.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5f, 1); // Shoot
		player.world.addEntity(snowball);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ENCHANTS.contains(enchantment);
	}
	
	private static class FrostSnowballEntity extends SnowballEntity {

		public FrostSnowballEntity(World worldIn, LivingEntity throwerIn) {
			super(worldIn, throwerIn);
		}
		
		protected void onEntityHit(EntityRayTraceResult result) {
			super.onEntityHit(result);
			Entity entity = result.getEntity();
			Vector3d direction = getMotion();
			entity.addVelocity(direction.x * 0.3, 0.1, direction.z * 0.3);
		}
		
	}

}
