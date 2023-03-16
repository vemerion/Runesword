package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FireRuneItem extends RuneItem {

	public FireRuneItem(Properties properties) {
		super(new Color(255, 100, 0).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	public static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FIRE_ASPECT, Enchantments.FLAMING_ARROWS,
				Enchantments.AQUA_AFFINITY, Enchantments.BLOCK_EFFICIENCY, Enchantments.SHARPNESS);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public float onBreakSpeed(ItemStack runeable, Player player, BlockState state, Optional<BlockPos> pos, float speed,
				Set<ItemStack> runes) {
			if (isCorrectTool(runeable, state)) {
				speed += runes.size() * 4;

				speed += getEnchantmentLevel(Enchantments.FIRE_ASPECT, runes) * 2;

				if (player.isOnFire())
					speed += getEnchantmentLevel(Enchantments.FLAMING_ARROWS, runes) * 4;
			}
			return speed;
		}

		@Override
		public void onBlockBreak(ItemStack runeable, Player player, BlockState state, BlockPos pos,
				Set<ItemStack> runes) {
			if (!isCorrectTool(runeable, state))
				return;

			if (player.getRandom().nextDouble() < runes.size() * 0.1 + getEnchantmentLevel(Enchantments.FIRE_ASPECT, runes) * 0.5
					- getEnchantmentLevel(Enchantments.AQUA_AFFINITY, runes) * 0.5) {
				player.setSecondsOnFire(4);
			}
		}

		@Override
		public boolean onHarvestCheckMajor(ItemStack runeable, Player player, BlockState state,
				boolean canHarvest, ItemStack rune) {
			return canHarvest && player.level.dimension() == Level.NETHER;
		}

		@Override
		public float onBreakSpeedMajor(ItemStack runeable, Player player, BlockState state, Optional<BlockPos> pos,
				float speed, ItemStack rune) {
			if (player.level.dimension() == Level.NETHER && isCorrectTool(runeable, state)) {
				speed += 20;

				speed += getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, rune);
			}

			return speed;
		}

		@Override
		public void onAttackMajor(ItemStack runeable, Player player, Entity target, ItemStack rune) {
			EntityType<?> type = target.getType();
			if (type == EntityType.PIGLIN || type == EntityType.HOGLIN) {
				int sharpness = getEnchantmentLevel(Enchantments.SHARPNESS, rune);
				if (sharpness > 0)
					attack(player, target, sharpness * 0.5f);
			}
		}
	}

	private static class SwordPowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FIRE_PROTECTION,
				Enchantments.FIRE_ASPECT, Enchantments.FLAMING_ARROWS, Enchantments.POWER_ARROWS, Enchantments.MENDING);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public void onAttackMajor(ItemStack sword, Player player, Entity target, ItemStack rune) {
			if (player.getRemainingFireTicks() > 0) {
				float damage = 4 + getEnchantmentLevel(Enchantments.FIRE_ASPECT, rune) * 0.5f;
				target.hurt(DamageSource.playerAttack(player), damage);
				target.invulnerableTime = 0;
			}
		}

		@Override
		public float onHurtMajor(ItemStack sword, Player player, DamageSource source, float amount,
				ItemStack rune) {
			if (source.isFire()
					&& player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.FIRE_PROTECTION, rune) * 0.05) {
				amount = 0;
			}
			return amount;
		}

		@Override
		public void onAttack(ItemStack sword, Player player, Entity target, Set<ItemStack> runes) {
			if (player.getRandom().nextDouble() < runes.size() * 0.1
					+ getEnchantmentLevel(Enchantments.FLAMING_ARROWS, runes) * 0.05) {
				BlockPos targetPos = target.blockPosition();
				if (player.level.isEmptyBlock(targetPos)) {
					BlockState fire = Blocks.FIRE.defaultBlockState();
					if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.POWER_ARROWS, runes) * 0.01)
						fire = Blocks.LAVA.defaultBlockState();
					player.level.setBlockAndUpdate(targetPos, fire);
				}
			}
			if (target.getRemainingFireTicks() > 0)
				sword.setDamageValue(sword.getDamageValue() - getEnchantmentLevel(Enchantments.MENDING, runes));
		}

	}
}
