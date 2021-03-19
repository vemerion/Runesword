package mod.vemerion.runesword.item;

import java.util.Set;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ToolType;

public abstract class RunePowers implements IRunePowers {

	@Override
	public void onAttack(ItemStack runeable, PlayerEntity player, Entity target, Set<ItemStack> runes) {
	}

	@Override
	public void onAttackMajor(ItemStack runeable, PlayerEntity player, Entity target, ItemStack rune) {
	}

	@Override
	public void onKill(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes) {
	}

	@Override
	public void onKillMajor(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune) {
	}

	@Override
	public float onHurt(ItemStack runeable, PlayerEntity player, DamageSource source, float amount,
			Set<ItemStack> runes) {
		return amount;
	}

	@Override
	public float onHurtMajor(ItemStack runeable, PlayerEntity player, DamageSource source, float amount,
			ItemStack rune) {
		return amount;
	}

	@Override
	public void onRightClick(ItemStack runeable, PlayerEntity player, Set<ItemStack> runes) {
	}

	@Override
	public void onRightClickMajor(ItemStack runeable, PlayerEntity player, ItemStack rune) {
	}

	public static boolean isSword(ItemStack stack) {
		return stack.getItem() instanceof SwordItem;
	}

	public static boolean isAxe(ItemStack stack) {
		return stack.getItem() instanceof AxeItem || stack.getToolTypes().contains(ToolType.AXE);
	}

	public abstract boolean canActivatePowers(ItemStack stack);

	public abstract boolean isBeneficialEnchantment(Enchantment enchantment);

}
