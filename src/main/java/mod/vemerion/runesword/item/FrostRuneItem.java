package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class FrostRuneItem extends RuneItem {

	public FrostRuneItem(Properties properties) {
		super(new Color(40, 120, 150).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	private static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.BLOCK_EFFICIENCY,
				Enchantments.FALL_PROTECTION, Enchantments.BLOCK_FORTUNE, Enchantments.INFINITY_ARROWS,
				Enchantments.POWER_ARROWS, Enchantments.FROST_WALKER);

		private static final int BASE_DURATION = 20 * 10;

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public float onBreakSpeed(ItemStack runeable, Player player, BlockState state, BlockPos pos, float speed,
				Set<ItemStack> runes) {
			if (isCorrectTool(runeable, state)) {
				if (player.level.getBiome(pos).value().coldEnoughToSnow(pos)) {
					speed += runes.size() * 5 + getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, runes) * 0.8;

					if (player.level.isRaining()
							&& player.level.getBiome(pos).value().getPrecipitation() == Biome.Precipitation.SNOW) {
						speed += getEnchantmentLevel(Enchantments.FALL_PROTECTION, runes) * 2;
					}
				}
			}
			return speed;
		}

		@Override
		public void onBlockBreakMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos,
				ItemStack rune) {
			if (player.getRandom().nextDouble() < 0.1 + getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, rune) * 0.05
					&& state.getBlock() == Blocks.SPRUCE_LOG) {
				int duration = BASE_DURATION * (getEnchantmentLevel(Enchantments.INFINITY_ARROWS, rune) > 0 ? 2 : 1);
				int level = player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.POWER_ARROWS, rune)
						* 0.02 ? 1 : 0;

				player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, duration, level));

				if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.FROST_WALKER, rune) * 0.1)
					player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, level));

			}
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final int MAX_DURATION = 20 * 30;

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.BLOCK_FORTUNE,
				Enchantments.INFINITY_ARROWS, Enchantments.BLOCK_EFFICIENCY, Enchantments.MULTISHOT,
				Enchantments.KNOCKBACK, Enchantments.CHANNELING);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public void onKillMajor(ItemStack sword, Player player, LivingEntity entityLiving, DamageSource source,
				ItemStack rune) {
			if (player.getRandom().nextDouble() < 0.1 + getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, rune) * 0.05) {
				int duration = MAX_DURATION;
				if (getEnchantmentLevel(Enchantments.INFINITY_ARROWS, rune) > 0)
					duration *= 2;
				FrostGolemEntity snowman = new FrostGolemEntity(Main.FROST_GOLEM_ENTITY, player.level, duration,
						getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, rune));
				snowman.absMoveTo(entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), entityLiving.getYRot(),
						entityLiving.getXRot());
				player.level.addFreshEntity(snowman);
			}
		}

		@Override
		public void onAttack(ItemStack sword, Player player, Entity target, Set<ItemStack> runes) {
			if (player.getRandom().nextDouble() < 0.1 * runes.size()
					+ getEnchantmentLevel(Enchantments.MULTISHOT, runes) * 0.05) {
				int knockback = getEnchantmentLevel(Enchantments.KNOCKBACK, runes);
				int duration = FrostballEntity.SLOW_DURATION
						* (1 + getEnchantmentLevel(Enchantments.CHANNELING, runes));
				FrostballEntity snowball = new FrostballEntity(player.level, player, knockback, duration);
				snowball.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5f, 1); // Shoot
				player.level.addFreshEntity(snowball);
			}
		}

	}
}
