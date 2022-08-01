package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class AirRuneItem extends RuneItem {

	public AirRuneItem(Properties properties) {
		super(new Color(170, 220, 220).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	public static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.SILK_TOUCH,
				Enchantments.BLOCK_EFFICIENCY, Enchantments.FALL_PROTECTION, Enchantments.BLOCK_FORTUNE,
				Enchantments.INFINITY_ARROWS);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public float onBreakSpeed(ItemStack runeable, Player player, BlockState state, BlockPos pos, float speed,
				Set<ItemStack> runes) {
			boolean isLeave = state.is(BlockTags.LEAVES);
			boolean hasSilkTouch = getEnchantmentLevel(Enchantments.SILK_TOUCH, runes) > 0;
			if (runeable.isCorrectToolForDrops(state) || (isLeave && hasSilkTouch)) {
				if (!player.isOnGround())
					speed += 7 + getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, runes);

				if (player.hasEffect(MobEffects.LEVITATION))
					speed += getEnchantmentLevel(Enchantments.FALL_PROTECTION, runes);
			}

			return speed;
		}

		@Override
		public void onBlockBreakMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos,
				ItemStack rune) {
			if (runeable.isCorrectToolForDrops(state)) {
				if (player.getRandom().nextDouble() < 0.2
						+ getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, rune) * 0.1) {
					int duration = 20 * 10;
					if (getEnchantmentLevel(Enchantments.INFINITY_ARROWS, rune) > 0)
						duration *= 2;
					player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, duration, 0));
				}
			}
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.INFINITY_ARROWS,
				Enchantments.FALL_PROTECTION, Enchantments.BLOCK_EFFICIENCY, Enchantments.PUNCH_ARROWS,
				Enchantments.KNOCKBACK);

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public void onAttack(ItemStack sword, Player player, Entity target, Set<ItemStack> runes) {

			if (player.getRandom().nextDouble() < runes.size() * 0.1) {
				Vec3 direction = Vec3.directionFromRotation(player.getRotationVector())
						.scale(getEnchantmentLevel(Enchantments.KNOCKBACK, runes) * 0.1);
				target.push(direction.x, 0.8 + getEnchantmentLevel(Enchantments.PUNCH_ARROWS, runes) * 0.03,
						direction.z);
				target.setOnGround(false);
			}
		}

		@Override
		public void onKillMajor(ItemStack sword, Player player, LivingEntity entityLiving, DamageSource source,
				ItemStack rune) {
			int duration = 20 * 10;
			int level = 0;
			if (getEnchantmentLevel(Enchantments.INFINITY_ARROWS, rune) > 0)
				duration *= 2;
			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, rune) * 0.8)
				level = 1;

			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, level));

			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.FALL_PROTECTION, rune) * 0.1)
				player.addEffect(new MobEffectInstance(MobEffects.JUMP, duration, level));

		}
	}
}
