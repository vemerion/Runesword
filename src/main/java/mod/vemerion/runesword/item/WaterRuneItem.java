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

public class WaterRuneItem extends RuneItem {

	private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.AQUA_AFFINITY, Enchantments.SHARPNESS,
			Enchantments.PROTECTION, Enchantments.RESPIRATION, Enchantments.LUCK_OF_THE_SEA);

	public WaterRuneItem(Properties properties) {
		super(new Color(0, 50, 255).getRGB(), properties);
	}

	@Override
	public void onAttackMajor(ItemStack sword, PlayerEntity player, Entity target, ItemStack rune) {
		if (player.isInWater() || (getEnchantmentLevel(Enchantments.AQUA_AFFINITY, rune) > 0 && player.isWet())) {
			float damage = 3 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.4f;
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
			target.hurtResistantTime = 0;
		}
	}

	@Override
	public float onHurtMajor(ItemStack sword, PlayerEntity player, DamageSource source, float amount, ItemStack rune) {
		if (player.isInWater() || (getEnchantmentLevel(Enchantments.AQUA_AFFINITY, rune) > 0 && player.isWet())) {
			amount *= 1 - 0.05f * getEnchantmentLevel(Enchantments.PROTECTION, rune);
		}
		return amount;
	}

	@Override
	public void onKill(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes) {
		int respiration = getEnchantmentLevel(Enchantments.RESPIRATION, runes);
		int air = player.getAir() + (int) ((runes.size() + respiration * 0.34) * ((float) player.getMaxAir() / 10));
		player.setAir(Math.min(player.getMaxAir(), air));
	}

	@Override
	public float onHurt(ItemStack sword, PlayerEntity player, DamageSource source, float amount, Set<ItemStack> runes) {
		if (source == DamageSource.DROWN
				&& player.getRNG().nextDouble() < getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, runes) * 0.03) {
			player.setAir(Math.min(player.getMaxAir(), player.getAir() + (int) (player.getMaxAir() * 0.1)));
		}
		return super.onHurt(sword, player, source, amount, runes);
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ENCHANTS.contains(enchantment);
	}

}
