package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class WaterRuneItem extends RuneItem {

	public WaterRuneItem(Properties properties) {
		super(new Color(0, 50, 255).getRGB(), properties);
	}

	@Override
	public void onAttackMajor(ItemStack sword, PlayerEntity player, Entity target, ItemStack runes) {
		if (player.isInWater()) {
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), 3);
			target.hurtResistantTime = 0;
		}
	}

	@Override
	public void onKill(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source, Set<ItemStack> runes) {
		player.setAir(Math.min(player.getMaxAir(),
				player.getAir() + (int) (runes.size() * ((float) player.getMaxAir() / 10))));
	}

}
