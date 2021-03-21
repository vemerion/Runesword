package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class AirRuneItem extends RuneItem {

	public AirRuneItem(Properties properties) {
		super(new Color(170, 220, 220).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	public static class AxePowers extends RunePowers {
		
		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.SILK_TOUCH,
				Enchantments.EFFICIENCY, Enchantments.FEATHER_FALLING, Enchantments.FORTUNE, Enchantments.INFINITY);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public float onBreakSpeed(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos, float speed,
				Set<ItemStack> runes) {
			boolean isLeave = state.isIn(BlockTags.LEAVES);
			boolean hasSilkTouch = getEnchantmentLevel(Enchantments.SILK_TOUCH, runes) > 0;
			if (runeable.getToolTypes().contains(state.getHarvestTool()) || (isLeave && hasSilkTouch)) {
				if (!player.isOnGround())
					speed += 7 + getEnchantmentLevel(Enchantments.EFFICIENCY, runes);

				if (player.isPotionActive(Effects.LEVITATION))
					speed += getEnchantmentLevel(Enchantments.FEATHER_FALLING, runes);
			}

			return speed;
		}

		@Override
		public void onBlockBreakMajor(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos,
				ItemStack rune) {
			if (runeable.getToolTypes().contains(state.getHarvestTool())) {
				if (random.nextDouble() < 0.2 + getEnchantmentLevel(Enchantments.FORTUNE, rune) * 0.1) {
					int duration = 20 * 10;
					if (getEnchantmentLevel(Enchantments.INFINITY, rune) > 0)
						duration *= 2;
					player.addPotionEffect(new EffectInstance(Effects.LEVITATION, duration, 0));
				}
			}
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.INFINITY,
				Enchantments.FEATHER_FALLING, Enchantments.EFFICIENCY, Enchantments.PUNCH, Enchantments.KNOCKBACK);

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public void onAttack(ItemStack sword, PlayerEntity player, Entity target, Set<ItemStack> runes) {

			if (player.getRNG().nextDouble() < runes.size() * 0.1) {
				Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw())
						.scale(getEnchantmentLevel(Enchantments.KNOCKBACK, runes) * 0.1);
				target.addVelocity(direction.x, 0.8 + getEnchantmentLevel(Enchantments.PUNCH, runes) * 0.03,
						direction.z);
				target.setOnGround(false);
			}
		}

		@Override
		public void onKillMajor(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
				ItemStack rune) {
			int duration = 20 * 10;
			int level = 0;
			if (getEnchantmentLevel(Enchantments.INFINITY, rune) > 0)
				duration *= 2;
			if (player.getRNG().nextDouble() < getEnchantmentLevel(Enchantments.EFFICIENCY, rune) * 0.8)
				level = 1;

			player.addPotionEffect(new EffectInstance(Effects.SPEED, duration, level));

			if (player.getRNG().nextDouble() < getEnchantmentLevel(Enchantments.FEATHER_FALLING, rune) * 0.1)
				player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, duration, level));

		}
	}
}
