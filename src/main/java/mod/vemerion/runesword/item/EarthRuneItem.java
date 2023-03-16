package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EarthRuneItem extends RuneItem {

	public EarthRuneItem(Properties properties) {
		super(new Color(100, 50, 0).getRGB(), ImmutableList.of(new SwordPowers(), new AxePowers()), properties);
	}

	private static class AxePowers extends RunePowers {

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.BLOCK_FORTUNE,
				Enchantments.MENDING, Enchantments.BLOCK_EFFICIENCY, Enchantments.FLAMING_ARROWS,
				Enchantments.FIRE_ASPECT);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isAxe(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public float onBreakSpeed(ItemStack runeable, Player player, BlockState state, Optional<BlockPos> pos, float speed,
				Set<ItemStack> runes) {
			if (state.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
				speed += runes.size();

				speed += getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, runes);

				if (player.level.dimension() == Level.NETHER)
					speed += getEnchantmentLevel(Enchantments.FLAMING_ARROWS, runes) * 3;

				if (player.getRemainingFireTicks() > 0)
					speed += getEnchantmentLevel(Enchantments.FIRE_ASPECT, runes) * 2;
			}

			return speed;
		}

		@Override
		public boolean onHarvestCheckMajor(ItemStack runeable, Player player, BlockState state, boolean canHarvest,
				ItemStack rune) {
			return canHarvest || state.is(BlockTags.MINEABLE_WITH_PICKAXE);
		}

		@Override
		public void onBlockBreakMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos,
				ItemStack rune) {
			if (getEnchantmentLevel(Enchantments.MENDING, rune) > 0 && player.getRandom().nextDouble() < 0.3)
				mendItem(runeable, 2);

			if (state.getBlock() == Blocks.STONE && !player.isCreative()
					&& player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, rune) * 0.2)
				spawnItem(player.level, pos, Blocks.COBBLESTONE.asItem().getDefaultInstance());
		}

	}

	private static class SwordPowers extends RunePowers {

		private static final List<Item> DROPS = ImmutableList.of(Items.IRON_ORE, Items.COAL_ORE);
		private static final List<Item> LOOTING_DROPS = ImmutableList.of(Items.DIAMOND_ORE, Items.GOLD_ORE,
				Items.EMERALD_ORE);

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.BLOCK_FORTUNE,
				Enchantments.FIRE_ASPECT, Enchantments.MOB_LOOTING, Enchantments.SHARPNESS,
				Enchantments.ALL_DAMAGE_PROTECTION);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public float onHurtMajor(ItemStack sword, Player player, DamageSource source, float amount, ItemStack rune) {

			if (player.getY() < 30 && !source.isBypassArmor()) {
				amount *= 1 - 0.05f * getEnchantmentLevel(Enchantments.ALL_DAMAGE_PROTECTION, rune);
			}

			return amount;
		}

		@Override
		public void onAttackMajor(ItemStack sword, Player player, Entity target, ItemStack rune) {
			if (player.getY() < 30) {
				float damage = 3 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.2f;
				target.hurt(DamageSource.playerAttack(player), damage);
				target.invulnerableTime = 0;
			}
		}

		@Override
		public void onKill(ItemStack sword, Player player, LivingEntity entityLiving, DamageSource source,
				Set<ItemStack> runes) {
			if (player.getRandom().nextDouble() < runes.size() * 0.1
					+ getEnchantmentLevel(Enchantments.BLOCK_FORTUNE, runes) * 0.02) {
				ItemEntity dirt = new ItemEntity(player.level, entityLiving.getX(), entityLiving.getY(),
						entityLiving.getZ(), getDrop(player, runes));
				player.level.addFreshEntity(dirt);
			}
		}

		private ItemStack getDrop(Player player, Set<ItemStack> runes) {
			Level level = player.level;
			ItemStack drop = new ItemStack(DROPS.get(player.getRandom().nextInt(DROPS.size())));

			// Rare ores
			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.MOB_LOOTING, runes) * 0.005) {
				drop = new ItemStack(LOOTING_DROPS.get(player.getRandom().nextInt(LOOTING_DROPS.size())));
			}

			// Auto-smelt
			if (player.getRandom().nextDouble() < getEnchantmentLevel(Enchantments.FIRE_ASPECT, runes) * 0.05) {
				var inv = new SimpleContainer(drop);
				Optional<ItemStack> smelted = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, inv, level)
						.map(r -> r.assemble(inv));
				if (smelted.isPresent())
					drop = smelted.get();
			}

			return drop;
		}

	}
}
