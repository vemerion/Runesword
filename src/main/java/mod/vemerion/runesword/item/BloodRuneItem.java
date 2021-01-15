package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class BloodRuneItem extends RuneItem {

	public BloodRuneItem(Properties properties) {
		super(new Color(210, 20, 20).getRGB(), properties);
	}

	@Override
	public void onAttackMajor(PlayerEntity player, Entity target, ItemStack rune) {
		target.attackEntityFrom(DamageSource.causePlayerDamage(player), 4);
		player.attackEntityFrom(DamageSource.MAGIC, 2);
		target.hurtResistantTime = 0;
	}

	@Override
	public void onKill(PlayerEntity player, LivingEntity entityLiving, DamageSource source, Set<ItemStack> runes) {
		if (player.getRNG().nextDouble() < runes.size() * 0.05) {
			player.heal(2);
		}
	}
}
