package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import mod.vemerion.runesword.Main;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;

public class BloodRuneItem extends RuneItem {

	public BloodRuneItem(Properties properties) {
		super(new Color(210, 20, 20).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	public static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FORTUNE, Enchantments.INFINITY,
				Enchantments.POWER, Enchantments.SHARPNESS, Enchantments.LOYALTY);

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
		public void onAttack(ItemStack runeable, PlayerEntity player, Entity target, Set<ItemStack> runes) {
			int fortune = getEnchantmentLevel(Enchantments.FORTUNE, runes);
			if (target instanceof LivingEntity && random.nextDouble() < runes.size() * 0.1 + fortune * 0.03) {
				int duration = BASE_DURATION + getEnchantmentLevel(Enchantments.INFINITY, runes) * INF_DURATION;
				int level = random.nextDouble() < getEnchantmentLevel(Enchantments.POWER, runes) * 0.01 ? 1 : 0;
				((LivingEntity) target)
						.addPotionEffect(new EffectInstance(Main.BLEED_EFFECT, duration, level, false, false, true));
			}
		}

		@Override
		public void onAttackMajor(ItemStack runeable, PlayerEntity player, Entity target, ItemStack rune) {
			if (target instanceof LivingEntity && ((LivingEntity) target).isPotionActive(Main.BLEED_EFFECT)) {
				float damage = 4 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.5f;
				attack(player, target, damage);

				if (random.nextDouble() < getEnchantmentLevel(Enchantments.LOYALTY, rune) * 0.1)
					player.heal(0.5f);
			}
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.PROTECTION,
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
		public void onAttackMajor(ItemStack sword, PlayerEntity player, Entity target, ItemStack rune) {
			float damage = 4 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.2f;
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
			if (!(player.getRNG().nextDouble() < getEnchantmentLevel(Enchantments.PROTECTION, rune) * 0.05))
				player.attackEntityFrom(DamageSource.MAGIC, 2);
			target.hurtResistantTime = 0;
		}

		@Override
		public void onKill(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
				Set<ItemStack> runes) {
			if (player.getRNG().nextDouble() < runes.size() * 0.05) {
				float heal = 2;
				if (entityLiving.getFireTimer() > 0)
					heal += getEnchantmentLevel(Enchantments.FIRE_ASPECT, runes) * 0.5f;
				if (player.isInWater())
					heal += getEnchantmentLevel(Enchantments.AQUA_AFFINITY, runes);
				player.heal(heal);
				mendItem(sword, getEnchantmentLevel(Enchantments.MENDING, runes));
			}
		}
	}
}
