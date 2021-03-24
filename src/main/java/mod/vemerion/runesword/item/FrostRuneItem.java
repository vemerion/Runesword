package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome.RainType;

public class FrostRuneItem extends RuneItem {

	public FrostRuneItem(Properties properties) {
		super(new Color(40, 120, 150).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	private static class AxePowers extends RunePowers {
		
		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.EFFICIENCY, Enchantments.FEATHER_FALLING,
				Enchantments.FORTUNE, Enchantments.INFINITY, Enchantments.POWER, Enchantments.FROST_WALKER);


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
		public float onBreakSpeed(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos, float speed,
				Set<ItemStack> runes) {
			if (isCorrectTool(runeable, state)) {
				if (player.world.getBiome(pos).getTemperature() < 0.5) {
					speed += runes.size() * 5 + getEnchantmentLevel(Enchantments.EFFICIENCY, runes) * 0.8;

					if (player.world.isRaining() && player.world.getBiome(pos).getPrecipitation() == RainType.SNOW) {
						speed += getEnchantmentLevel(Enchantments.FEATHER_FALLING, runes) * 2;
					}
				}
			}
			return speed;
		}

		@Override
		public void onBlockBreakMajor(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos,
				ItemStack rune) {
			if (random.nextDouble() < 0.1 + getEnchantmentLevel(Enchantments.FORTUNE, rune) * 0.05
					&& state.getBlock() == Blocks.SPRUCE_LOG) {
				int duration = BASE_DURATION * (getEnchantmentLevel(Enchantments.INFINITY, rune) > 0 ? 2 : 1);
				int level = random.nextDouble() < getEnchantmentLevel(Enchantments.POWER, rune) * 0.02 ? 1 : 0;

				player.addPotionEffect(new EffectInstance(Effects.HASTE, duration, level));

				if (random.nextDouble() < getEnchantmentLevel(Enchantments.FROST_WALKER, rune) * 0.1)
					player.addPotionEffect(new EffectInstance(Effects.SPEED, duration, level));

			}
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final int MAX_DURATION = 20 * 30;

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FORTUNE, Enchantments.INFINITY,
				Enchantments.EFFICIENCY, Enchantments.MULTISHOT, Enchantments.KNOCKBACK, Enchantments.CHANNELING);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public void onKillMajor(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
				ItemStack rune) {
			if (player.getRNG().nextDouble() < 0.1 + getEnchantmentLevel(Enchantments.FORTUNE, rune) * 0.05) {
				int duration = MAX_DURATION;
				if (getEnchantmentLevel(Enchantments.INFINITY, rune) > 0)
					duration *= 2;
				FrostGolemEntity snowman = new FrostGolemEntity(Main.FROST_GOLEM_ENTITY, player.world, duration,
						getEnchantmentLevel(Enchantments.EFFICIENCY, rune));
				snowman.setPositionAndRotation(entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(),
						entityLiving.rotationYaw, entityLiving.rotationPitch);
				player.world.addEntity(snowman);
			}
		}

		@Override
		public void onAttack(ItemStack sword, PlayerEntity player, Entity target, Set<ItemStack> runes) {
			if (player.getRNG().nextDouble() < 0.1 * runes.size()
					+ getEnchantmentLevel(Enchantments.MULTISHOT, runes) * 0.05) {
				int knockback = getEnchantmentLevel(Enchantments.KNOCKBACK, runes);
				int duration = FrostballEntity.SLOW_DURATION
						* (1 + getEnchantmentLevel(Enchantments.CHANNELING, runes));
				FrostballEntity snowball = new FrostballEntity(player.world, player, knockback, duration);
				snowball.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5f, 1); // Shoot
				player.world.addEntity(snowball);
			}
		}

	}
}
