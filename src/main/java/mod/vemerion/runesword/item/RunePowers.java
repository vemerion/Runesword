package mod.vemerion.runesword.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class RunePowers implements IRunePowers {

	@Override
	public void onAttack(ItemStack runeable, Player player, Entity target, Set<ItemStack> runes) {
	}

	@Override
	public void onAttackMajor(ItemStack runeable, Player player, Entity target, ItemStack rune) {
	}

	@Override
	public void onKill(ItemStack runeable, Player player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes) {
	}

	@Override
	public void onKillMajor(ItemStack runeable, Player player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune) {
	}

	@Override
	public float onHurt(ItemStack runeable, Player player, DamageSource source, float amount,
			Set<ItemStack> runes) {
		return amount;
	}

	@Override
	public float onHurtMajor(ItemStack runeable, Player player, DamageSource source, float amount,
			ItemStack rune) {
		return amount;
	}

	@Override
	public void onRightClick(ItemStack runeable, Player player, Set<ItemStack> runes) {
	}

	@Override
	public void onRightClickMajor(ItemStack runeable, Player player, ItemStack rune) {
	}

	@Override
	public float onBreakSpeed(ItemStack runeable, Player player, BlockState state, BlockPos pos, float speed,
			Set<ItemStack> runes) {
		return speed;
	}

	@Override
	public float onBreakSpeedMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos, float speed,
			ItemStack rune) {
		return speed;
	}

	@Override
	public boolean onHarvestCheckMajor(ItemStack runeable, Player player, BlockState state, boolean canHarvest,
			ItemStack rune) {
		return canHarvest;
	}

	@Override
	public void onBlockBreak(ItemStack runeable, Player player, BlockState state, BlockPos pos,
			Set<ItemStack> runes) {
	}

	@Override
	public void onBlockBreakMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos,
			ItemStack rune) {
	}

	public static boolean isSword(ItemStack stack) {
		return true; // TODO
	}

	public static boolean isAxe(ItemStack stack) {
		return true; // TODO
	}

	public abstract boolean canActivatePowers(ItemStack stack);

	public abstract boolean isBeneficialEnchantment(Enchantment enchantment);

	// Helper methods
	protected final int getEnchantmentLevel(Enchantment enchantment, Set<ItemStack> stacks) {
		int level = 0;
		for (ItemStack stack : stacks)
			level += EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
		return level;
	}

	protected final int getEnchantmentLevel(Enchantment enchantment, ItemStack stack) {
		return EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
	}
	
	protected final int getEnchantmentLevel(Enchantment enchantment, Map<Enchantment, Integer> enchants) {
		return enchants.getOrDefault(enchantment, 0);
	}

	protected final Map<Enchantment, Integer> getEnchantments(Set<ItemStack> stacks) {
		Map<Enchantment, Integer> enchantments = new HashMap<>();

		for (ItemStack stack : stacks) {
			EnchantmentHelper.getEnchantments(stack)
					.forEach((ench, level) -> enchantments.merge(ench, level, (l1, l2) -> l1 + l2));
		}
		return enchantments;
	}

	protected final void mendItem(ItemStack item, int amount) {
		item.setDamageValue(item.getDamageValue() - amount);
	}

	protected final void attack(Player player, Entity target, float damage) {
		target.hurt(DamageSource.playerAttack(player), damage);
		target.invulnerableTime = 0;
	}

	protected final boolean isCorrectTool(ItemStack stack, BlockState state) {
		return stack.isCorrectToolForDrops(state);
	}

	protected final void spawnItem(Level level, BlockPos pos, ItemStack stack) {
		var position = Vec3.atCenterOf(pos);
		var entity = new ItemEntity(level, position.x(), position.y(), position.z(), stack);
		level.addFreshEntity(entity);
	}
	
	protected final void restoreAir(Player player, float fraction) {
		int air = (int) (player.getAirSupply() + fraction * player.getMaxAirSupply());
		player.setAirSupply(Math.min(player.getMaxAirSupply(), air));
	}
}
