package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public class FireRuneItem extends RuneItem {

	public FireRuneItem(Properties properties) {
		super(new Color(255, 100, 0).getRGB(), properties);
	}

	@Override
	public void onAttack(PlayerEntity player, Entity target, Set<ItemStack> runes, boolean major) {
		if (major) {
			if (player.getFireTimer() > 0) {
				target.attackEntityFrom(DamageSource.causePlayerDamage(player), 4);
				target.hurtResistantTime = 0;
			}
		} else {
			if (player.getRNG().nextDouble() < runes.size() * 0.1) {
				BlockPos targetPos = target.getPosition();
				if (player.world.isAirBlock(targetPos)) {
					player.world.setBlockState(targetPos, Blocks.FIRE.getDefaultState());
				}
			}
		}
	}

}
