package mod.vemerion.runesword.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public abstract class RunePowers implements IRunePowers {

	@Override
	public void onAttack(ItemStack runeable, PlayerEntity player, Entity target, Set<ItemStack> runes) {
	}

	@Override
	public void onAttackMajor(ItemStack runeable, PlayerEntity player, Entity target, ItemStack rune) {
	}

	@Override
	public void onKill(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes) {
	}

	@Override
	public void onKillMajor(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune) {
	}

	@Override
	public float onHurt(ItemStack runeable, PlayerEntity player, DamageSource source, float amount,
			Set<ItemStack> runes) {
		return amount;
	}

	@Override
	public float onHurtMajor(ItemStack runeable, PlayerEntity player, DamageSource source, float amount,
			ItemStack rune) {
		return amount;
	}

	@Override
	public void onRightClick(ItemStack runeable, PlayerEntity player, Set<ItemStack> runes) {
	}

	@Override
	public void onRightClickMajor(ItemStack runeable, PlayerEntity player, ItemStack rune) {
	}

	@Override
	public float onBreakSpeed(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos, float speed,
			Set<ItemStack> runes) {
		return speed;
	}

	@Override
	public float onBreakSpeedMajor(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos, float speed,
			ItemStack rune) {
		return speed;
	}

	@Override
	public boolean onHarvestCheckMajor(ItemStack runeable, PlayerEntity player, BlockState state, boolean canHarvest,
			ItemStack rune) {
		return canHarvest;
	}

	@Override
	public void onBlockBreak(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos,
			Set<ItemStack> runes) {
	}

	@Override
	public void onBlockBreakMajor(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos,
			ItemStack rune) {
	}

	public static boolean isSword(ItemStack stack) {
		return stack.getItem() instanceof SwordItem;
	}

	public static boolean isAxe(ItemStack stack) {
		return stack.getItem() instanceof AxeItem || stack.getToolTypes().contains(ToolType.AXE);
	}

	public abstract boolean canActivatePowers(ItemStack stack);

	public abstract boolean isBeneficialEnchantment(Enchantment enchantment);

	// Helper methods
	protected final int getEnchantmentLevel(Enchantment enchantment, Set<ItemStack> stacks) {
		int level = 0;
		for (ItemStack stack : stacks)
			level += EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
		return level;
	}

	protected final int getEnchantmentLevel(Enchantment enchantment, ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
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
		item.setDamage(item.getDamage() - amount);
	}

	protected final void attack(PlayerEntity player, Entity target, float damage) {
		target.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
		target.hurtResistantTime = 0;
	}

	protected final boolean isCorrectTool(ItemStack stack, BlockState state) {
		return stack.getToolTypes().contains(state.getHarvestTool());
	}

	protected final void spawnItem(World world, BlockPos pos, ItemStack stack) {
		Vector3d position = Vector3d.copyCentered(pos);
		ItemEntity entity = new ItemEntity(world, position.getX(), position.getY(), position.getZ(), stack);
		world.addEntity(entity);

	}
}
