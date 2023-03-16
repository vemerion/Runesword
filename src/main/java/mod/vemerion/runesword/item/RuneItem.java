package mod.vemerion.runesword.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Iterables;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RuneItem extends Item implements IRunePowers {

	private static final List<RuneItem> RUNES = new ArrayList<>();

	private final int color;
	private final List<RunePowers> powers;

	public RuneItem(int color, List<RunePowers> powers, Properties properties) {
		super(properties);
		this.color = color;
		this.powers = powers;
		RUNES.add(this);
	}

	@Override
	public void onAttack(ItemStack runeable, Player player, Entity target, Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onAttack(runeable, player, target, runes);
	}

	@Override
	public void onAttackMajor(ItemStack runeable, Player player, Entity target, ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onAttackMajor(runeable, player, target, rune);
	}

	@Override
	public void onKill(ItemStack runeable, Player player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onKill(runeable, player, entityLiving, source, runes);
	}

	@Override
	public void onKillMajor(ItemStack runeable, Player player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onKillMajor(runeable, player, entityLiving, source, rune);
	}

	@Override
	public float onHurt(ItemStack runeable, Player player, DamageSource source, float amount,
			Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				amount = p.onHurt(runeable, player, source, amount, runes);
		return amount;
	}

	@Override
	public float onHurtMajor(ItemStack runeable, Player player, DamageSource source, float amount,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				amount = p.onHurtMajor(runeable, player, source, amount, rune);
		return amount;
	}

	@Override
	public void onRightClick(ItemStack runeable, Player player, Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onRightClick(runeable, player, runes);
	}

	@Override
	public void onRightClickMajor(ItemStack runeable, Player player, ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onRightClickMajor(runeable, player, rune);
	}

	@Override
	public float onBreakSpeed(ItemStack runeable, Player player, BlockState state, Optional<BlockPos> pos, float speed,
			Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				speed = p.onBreakSpeed(runeable, player, state, pos, speed, runes);
		return speed;
	}

	@Override
	public float onBreakSpeedMajor(ItemStack runeable, Player player, BlockState state, Optional<BlockPos> pos, float speed,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				speed = p.onBreakSpeedMajor(runeable, player, state, pos, speed, rune);
		return speed;
	}

	@Override
	public boolean onHarvestCheckMajor(ItemStack runeable, Player player, BlockState state, boolean canHarvest,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				canHarvest = p.onHarvestCheckMajor(runeable, player, state, canHarvest, rune);
		return canHarvest;
	}

	@Override
	public void onBlockBreak(ItemStack runeable, Player player, BlockState state, BlockPos pos,
			Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onBlockBreak(runeable, player, state, pos, runes);
	}

	@Override
	public void onBlockBreakMajor(ItemStack runeable, Player player, BlockState state, BlockPos pos,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onBlockBreakMajor(runeable, player, state, pos, rune);
	}

	public int getColor() {
		return color;
	}

	public static Iterable<RuneItem> getRunes() {
		return Iterables.unmodifiableIterable(RUNES);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return true;
	}

	@Override
	public int getEnchantmentValue() {
		return 10;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		for (RunePowers p : powers)
			if (p.isBeneficialEnchantment(enchantment))
				return true;
		return false;
	}
}
