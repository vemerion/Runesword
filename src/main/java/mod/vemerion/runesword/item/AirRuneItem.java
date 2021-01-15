package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;

public class AirRuneItem extends RuneItem {

	public AirRuneItem(Properties properties) {
		super(new Color(170, 220, 220).getRGB(), properties);
	}

	@Override
	public void onAttack(PlayerEntity player, Entity target, Set<ItemStack> runes) {

		if (player.getRNG().nextDouble() < runes.size() * 0.1) {
			target.addVelocity(0, 0.8, 0);
			target.setOnGround(false);
		}
	}

	@Override
	public void onKillMajor(PlayerEntity player, LivingEntity entityLiving, DamageSource source, ItemStack rune) {
		player.addPotionEffect(new EffectInstance(Effects.SPEED, 20 * 10));
	}

}
