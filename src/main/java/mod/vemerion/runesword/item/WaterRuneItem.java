package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WaterRuneItem extends RuneItem {

	public WaterRuneItem(Properties properties) {
		super(new Color(0, 50, 255).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	public static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FISHING_LUCK,
				Enchantments.FISHING_SPEED, Enchantments.FLAMING_ARROWS, Enchantments.BLOCK_EFFICIENCY, Enchantments.RESPIRATION);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public void onKill(ItemStack runeable, Player player, LivingEntity target, DamageSource source,
				Set<ItemStack> runes) {
			if (target.getMobType() == MobType.WATER) {
				double chance = runes.size() * 0.1;
				chance += getEnchantmentLevel(Enchantments.FISHING_LUCK, runes) * 0.03;
				if (!target.isInWater())
					chance += getEnchantmentLevel(Enchantments.FISHING_SPEED, runes) * 0.1;
				if (player.getRandom().nextDouble() < chance) {
					var log = Blocks.OAK_LOG;
					if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.FLAMING_ARROWS, runes) * 0.1)
						log = player.getRandom().nextBoolean() ? Blocks.CRIMSON_HYPHAE : Blocks.WARPED_HYPHAE;
					spawnItem(player.level, target.blockPosition(), log.asItem().getDefaultInstance());
				}
			}
		}

		@Override
		public float onBreakSpeedMajor(ItemStack runeable, Player player, BlockState state, Optional<BlockPos> pos,
				float speed, ItemStack rune) {
			if (isCorrectTool(runeable, state)) {
				if (player.isInWaterRainOrBubble()) {
					speed += 20 + getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, rune) * 2;
				}
			}
			return speed;
		}

		@Override
		public void onBlockBreakMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos,
				ItemStack rune) {
			if (isCorrectTool(runeable, state) && player.isInWaterRainOrBubble())
				if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.RESPIRATION, rune) * 0.1)
					player.setAirSupply(Math.min(player.getMaxAirSupply(), player.getAirSupply() + (int) (player.getMaxAirSupply() * 0.1)));
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.AQUA_AFFINITY,
				Enchantments.SHARPNESS, Enchantments.ALL_DAMAGE_PROTECTION, Enchantments.RESPIRATION,
				Enchantments.FISHING_LUCK);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public void onAttackMajor(ItemStack sword, Player player, Entity target, ItemStack rune) {
			if (player.isInWater() || (getEnchantmentLevel(Enchantments.AQUA_AFFINITY, rune) > 0 && player.isInWaterOrRain())) {
				float damage = 3 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.4f;
				target.hurt(DamageSource.playerAttack(player), damage);
				target.invulnerableTime = 0;
			}
		}

		@Override
		public float onHurtMajor(ItemStack sword, Player player, DamageSource source, float amount,
				ItemStack rune) {
			if (player.isInWater() || (getEnchantmentLevel(Enchantments.AQUA_AFFINITY, rune) > 0 && player.isInWaterOrRain())) {
				amount *= 1 - 0.05f * getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, rune);
			}
			return amount;
		}

		@Override
		public void onKill(ItemStack sword, Player player, LivingEntity entityLiving, DamageSource source,
				Set<ItemStack> runes) {
			int respiration = getEnchantmentLevel(Enchantments.RESPIRATION, runes);
			int air = player.getAirSupply() + (int) ((runes.size() + respiration * 0.34) * ((float) player.getMaxAirSupply() / 10));
			player.setAirSupply(Math.min(player.getMaxAirSupply(), air));
		}

		@Override
		public float onHurt(ItemStack sword, Player player, DamageSource source, float amount,
				Set<ItemStack> runes) {
			if (source == DamageSource.DROWN
					&& player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.FISHING_LUCK, runes) * 0.03) {
				player.setAirSupply(Math.min(player.getMaxAirSupply(),
						player.getAirSupply() + (int) (player.getMaxAirSupply() * 0.1)));
			}
			return super.onHurt(sword, player, source, amount, runes);
		}
	}
}
