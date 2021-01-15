package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class BloodRuneItem extends RuneItem {

	private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.PROTECTION, Enchantments.SHARPNESS,
			Enchantments.MENDING, Enchantments.FIRE_ASPECT, Enchantments.AQUA_AFFINITY);

	public BloodRuneItem(Properties properties) {
		super(new Color(210, 20, 20).getRGB(), properties);
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
			sword.setDamage(sword.getDamage() - getEnchantmentLevel(Enchantments.MENDING, runes));
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ENCHANTS.contains(enchantment);
	}
}
