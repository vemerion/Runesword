package mod.vemerion.runesword.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public interface IRunePowers {

	void onAttack(ItemStack runeable, PlayerEntity player, Entity target, Set<ItemStack> runes);

	void onAttackMajor(ItemStack runeable, PlayerEntity player, Entity target, ItemStack rune);

	void onKill(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes);

	void onKillMajor(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune);

	float onHurt(ItemStack runeable, PlayerEntity player, DamageSource source, float amount, Set<ItemStack> runes);

	float onHurtMajor(ItemStack runeable, PlayerEntity player, DamageSource source, float amount, ItemStack rune);

	void onRightClick(ItemStack runeable, PlayerEntity player, Set<ItemStack> runes);

	void onRightClickMajor(ItemStack runeable, PlayerEntity player, ItemStack rune);

	default int getEnchantmentLevel(Enchantment enchantment, Set<ItemStack> stacks) {
		int level = 0;
		for (ItemStack stack : stacks)
			level += EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
		return level;
	}

	default int getEnchantmentLevel(Enchantment enchantment, ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
	}

	default Map<Enchantment, Integer> getEnchantments(Set<ItemStack> stacks) {
		Map<Enchantment, Integer> enchantments = new HashMap<>();

		for (ItemStack stack : stacks) {
			EnchantmentHelper.getEnchantments(stack)
					.forEach((ench, level) -> enchantments.merge(ench, level, (l1, l2) -> l1 + l2));
		}
		return enchantments;
	}
}
