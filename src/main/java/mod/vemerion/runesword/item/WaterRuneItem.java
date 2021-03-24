package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;

public class WaterRuneItem extends RuneItem {

	public WaterRuneItem(Properties properties) {
		super(new Color(0, 50, 255).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	public static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.LUCK_OF_THE_SEA,
				Enchantments.LURE, Enchantments.FLAME, Enchantments.EFFICIENCY, Enchantments.RESPIRATION);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public void onKill(ItemStack runeable, PlayerEntity player, LivingEntity target, DamageSource source,
				Set<ItemStack> runes) {
			if (target.getCreatureAttribute() == CreatureAttribute.WATER) {
				double chance = runes.size() * 0.1;
				chance += getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, runes) * 0.03;
				if (!target.isInWater())
					chance += getEnchantmentLevel(Enchantments.LURE, runes) * 0.1;
				if (random.nextDouble() < chance) {
					Block log = Blocks.OAK_LOG;
					if (random.nextDouble() < getEnchantmentLevel(Enchantments.FLAME, runes) * 0.1)
						log = random.nextBoolean() ? Blocks.CRIMSON_HYPHAE : Blocks.WARPED_HYPHAE;
					spawnItem(player.world, target.getPosition(), log.asItem().getDefaultInstance());
				}
			}
		}

		@Override
		public float onBreakSpeedMajor(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos,
				float speed, ItemStack rune) {
			if (isCorrectTool(runeable, state)) {
				if (player.isInWaterRainOrBubbleColumn()) {
					speed += 20 + getEnchantmentLevel(Enchantments.EFFICIENCY, rune) * 2;
				}
			}
			return speed;
		}

		@Override
		public void onBlockBreakMajor(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos,
				ItemStack rune) {
			if (isCorrectTool(runeable, state) && player.isInWaterRainOrBubbleColumn())
				if (random.nextDouble() < getEnchantmentLevel(Enchantments.RESPIRATION, rune) * 0.1)
					player.setAir(Math.min(player.getMaxAir(), player.getAir() + (int) (player.getMaxAir() * 0.1)));
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.AQUA_AFFINITY,
				Enchantments.SHARPNESS, Enchantments.PROTECTION, Enchantments.RESPIRATION,
				Enchantments.LUCK_OF_THE_SEA);

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
			if (player.isInWater() || (getEnchantmentLevel(Enchantments.AQUA_AFFINITY, rune) > 0 && player.isWet())) {
				float damage = 3 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.4f;
				target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
				target.hurtResistantTime = 0;
			}
		}

		@Override
		public float onHurtMajor(ItemStack sword, PlayerEntity player, DamageSource source, float amount,
				ItemStack rune) {
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
		public float onHurt(ItemStack sword, PlayerEntity player, DamageSource source, float amount,
				Set<ItemStack> runes) {
			if (source == DamageSource.DROWN
					&& player.getRNG().nextDouble() < getEnchantmentLevel(Enchantments.LUCK_OF_THE_SEA, runes) * 0.03) {
				player.setAir(Math.min(player.getMaxAir(), player.getAir() + (int) (player.getMaxAir() * 0.1)));
			}
			return super.onHurt(sword, player, source, amount, runes);
		}
	}
}
