package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EarthRuneItem extends RuneItem {

	public EarthRuneItem(Properties properties) {
		super(new Color(100, 50, 0).getRGB(), ImmutableList.of(new SwordPowers()), properties);
	}

	private static class SwordPowers extends RunePowers {

		private static final List<Item> DROPS = ImmutableList.of(Items.IRON_ORE, Items.COAL_ORE);
		private static final List<Item> LOOTING_DROPS = ImmutableList.of(Items.DIAMOND_ORE, Items.GOLD_ORE,
				Items.EMERALD_ORE);

		private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FORTUNE, Enchantments.FIRE_ASPECT,
				Enchantments.LOOTING, Enchantments.SHARPNESS, Enchantments.PROTECTION);

		@Override
		public boolean canActivatePowers(ItemStack stack) {
			return isSword(stack);
		}

		@Override
		public boolean isBeneficialEnchantment(Enchantment enchantment) {
			return ENCHANTS.contains(enchantment);
		}

		@Override
		public float onHurtMajor(ItemStack sword, PlayerEntity player, DamageSource source, float amount,
				ItemStack rune) {

			if (player.getPosY() < 30 && !source.isUnblockable()) {
				amount *= 1 - 0.05f * getEnchantmentLevel(Enchantments.PROTECTION, rune);
			}

			return amount;
		}

		@Override
		public void onAttackMajor(ItemStack sword, PlayerEntity player, Entity target, ItemStack rune) {
			if (player.getPosY() < 30) {
				float damage = 3 + getEnchantmentLevel(Enchantments.SHARPNESS, rune) * 0.2f;
				target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
				target.hurtResistantTime = 0;
			}
		}

		@Override
		public void onKill(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
				Set<ItemStack> runes) {
			if (player.getRNG().nextDouble() < runes.size() * 0.1
					+ getEnchantmentLevel(Enchantments.FORTUNE, runes) * 0.02) {
				ItemEntity dirt = new ItemEntity(player.world, entityLiving.getPosX(), entityLiving.getPosY(),
						entityLiving.getPosZ(), getDrop(player, runes));
				player.world.addEntity(dirt);
			}
		}

		private ItemStack getDrop(PlayerEntity player, Set<ItemStack> runes) {
			World world = player.world;
			ItemStack drop = new ItemStack(DROPS.get(random.nextInt(DROPS.size())));

			// Rare ores
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.LOOTING, runes) * 0.01) {
				drop = new ItemStack(LOOTING_DROPS.get(random.nextInt(LOOTING_DROPS.size())));
			}

			// Auto-smelt
			if (random.nextDouble() < getEnchantmentLevel(Enchantments.FIRE_ASPECT, runes) * 0.05) {
				IInventory inv = new Inventory(drop);
				Optional<ItemStack> smelted = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, inv, world)
						.map(r -> r.getCraftingResult(inv));
				if (smelted.isPresent())
					drop = smelted.get();
			}

			return drop;
		}

	}
}
