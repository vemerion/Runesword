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
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;

public class AirRuneItem extends RuneItem {

	private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.INFINITY,
			Enchantments.FEATHER_FALLING, Enchantments.EFFICIENCY, Enchantments.PUNCH, Enchantments.KNOCKBACK);

	public AirRuneItem(Properties properties) {
		super(new Color(170, 220, 220).getRGB(), properties);
	}

	@Override
	public void onAttack(ItemStack sword, PlayerEntity player, Entity target, Set<ItemStack> runes) {

		if (player.getRNG().nextDouble() < runes.size() * 0.1) {
			Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw())
					.scale(getEnchantmentLevel(Enchantments.KNOCKBACK, runes) * 0.1);
			target.addVelocity(direction.x, 0.8 + getEnchantmentLevel(Enchantments.PUNCH, runes) * 0.03, direction.z);
			target.setOnGround(false);
		}
	}

	@Override
	public void onKillMajor(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source, ItemStack rune) {
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

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ENCHANTS.contains(enchantment);
	}

}
