package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public class FireRuneItem extends RuneItem {
	


	public FireRuneItem(Properties properties) {
		super(new Color(255, 100, 0).getRGB(), ImmutableList.of(new SwordPowers()), properties);
	}
	
	private static class SwordPowers extends RunePowers {
		
		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FIRE_PROTECTION, Enchantments.FIRE_ASPECT,
				Enchantments.FLAME, Enchantments.POWER, Enchantments.MENDING);


		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}
		
		@Override
		public void onAttackMajor(ItemStack sword, PlayerEntity player, Entity target, ItemStack rune) {
			if (player.getFireTimer() > 0) {
				float damage = 4 + getEnchantmentLevel(Enchantments.FIRE_ASPECT, rune) * 0.5f;
				target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
				target.hurtResistantTime = 0;
			}
		}
		
		@Override
		public float onHurtMajor(ItemStack sword, PlayerEntity player, DamageSource source, float amount, ItemStack rune) {
			if (source.isFireDamage() && player.getRNG().nextDouble() < getEnchantmentLevel(Enchantments.FIRE_PROTECTION, rune) * 0.05) {
				amount = 0;
			}
			return amount;
		}

		@Override
		public void onAttack(ItemStack sword, PlayerEntity player, Entity target, Set<ItemStack> runes) {
			if (player.getRNG().nextDouble() < runes.size() * 0.1 + getEnchantmentLevel(Enchantments.FLAME, runes) * 0.05) {
				BlockPos targetPos = target.getPosition();
				if (player.world.isAirBlock(targetPos)) {
					BlockState fire = Blocks.FIRE.getDefaultState();
					if (player.getRNG().nextDouble() < getEnchantmentLevel(Enchantments.POWER, runes) * 0.01)
						fire = Blocks.LAVA.getDefaultState();
					player.world.setBlockState(targetPos, fire);
				}
			}
			if (target.getFireTimer() > 0)
				sword.setDamage(sword.getDamage() - getEnchantmentLevel(Enchantments.MENDING, runes));
		}

	}
}
