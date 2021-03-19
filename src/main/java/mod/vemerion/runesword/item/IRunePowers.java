package mod.vemerion.runesword.item;

import java.util.Set;

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
}
