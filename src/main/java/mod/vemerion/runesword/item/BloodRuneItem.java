package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import mod.vemerion.runesword.init.ModEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class BloodRuneItem extends RuneItem {

	public BloodRuneItem(Properties properties) {
		super(new Color(210, 20, 20).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	public static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.BLOCK_FORTUNE, Enchantments.INFINITY_ARROWS,
				Enchantments.POWER_ARROWS, Enchantments.SHARPNESS, Enchantments.LOYALTY);

		private static final int BASE_DURATION = 20 * 6;
		private static final int INF_DURATION = 20 * 2;

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public void onAttack(ItemStack runeable, Player player, Entity target, Set<ItemStack> runes) {
			int fortune = getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, runes);
			if (target instanceof LivingEntity && player.getRandom().nextDouble() < runes.size() * 0.1 + fortune * 0.03) {
				int duration = BASE_DURATION + getEnchantmentLevel(Enchantments.INFINITY_ARROWS, runes) * INF_DURATION;
				int level = player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.POWER_ARROWS, runes) * 0.01 ? 1 : 0;
				((LivingEntity) target)
						.addEffect(new MobEffectInstance(ModEffects.BLEED.get(), duration, level, false, false, true));
			}
		}

		@Override
		public void onAttackMajor(ItemStack runeable, Player player, Entity target, ItemStack rune) {
			if (target instanceof LivingEntity && ((LivingEntity) target).hasEffect(ModEffects.BLEED.get())) {
				float damage = 4 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.5f;
				attack(player, target, damage);

				if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.LOYALTY, rune) * 0.1)
					player.heal(0.5f);
			}
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.ALL_DAMAGE_PROTECTION,
				Enchantments.SHARPNESS, Enchantments.MENDING, Enchantments.FIRE_ASPECT, Enchantments.AQUA_AFFINITY);

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
			float damage = 4 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.2f;
			target.hurt(DamageSource.playerAttack(player), damage);
			if (!(player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, rune) * 0.05))
				player.hurt(DamageSource.MAGIC, 2);
			target.invulnerableTime = 0;
		}

		@Override
		public void onKill(ItemStack sword, Player player, LivingEntity entityLiving, DamageSource source,
				Set<ItemStack> runes) {
			if (player.getRandom().nextDouble() < runes.size() * 0.05) {
				float heal = 2;
				if (entityLiving.getRemainingFireTicks() > 0)
					heal += getEnchantmentLevel(Enchantments.FIRE_ASPECT, runes) * 0.5f;
				if (player.isInWater())
					heal += getEnchantmentLevel(Enchantments.AQUA_AFFINITY, runes);
				player.heal(heal);
				mendItem(sword, getEnchantmentLevel(Enchantments.MENDING, runes));
			}
		}
	}
}
