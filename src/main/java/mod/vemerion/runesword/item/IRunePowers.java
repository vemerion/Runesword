package mod.vemerion.runesword.item;

import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface IRunePowers {

	void onAttack(ItemStack runeable, Player player, Entity target, Set<ItemStack> runes);

	void onAttackMajor(ItemStack runeable, Player player, Entity target, ItemStack rune);

	void onKill(ItemStack runeable, Player player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes);

	void onKillMajor(ItemStack runeable, Player player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune);

	float onHurt(ItemStack runeable, Player player, DamageSource source, float amount, Set<ItemStack> runes);

	float onHurtMajor(ItemStack runeable, Player player, DamageSource source, float amount, ItemStack rune);

	void onRightClick(ItemStack runeable, Player player, Set<ItemStack> runes);

	void onRightClickMajor(ItemStack runeable, Player player, ItemStack rune);

	float onBreakSpeed(ItemStack runeable, Player player, BlockState state, BlockPos pos, float speed,
			Set<ItemStack> runes);

	float onBreakSpeedMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos, float speed,
			ItemStack rune);

	boolean onHarvestCheckMajor(ItemStack runeable, Player player, BlockState state, boolean canHarvest,
			ItemStack rune);

	void onBlockBreak(ItemStack runeable, Player player, BlockState state, BlockPos pos, Set<ItemStack> runes);

	void onBlockBreakMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos, ItemStack rune);
}
