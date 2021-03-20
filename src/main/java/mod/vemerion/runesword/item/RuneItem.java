package mod.vemerion.runesword.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Iterables;

import mod.vemerion.runesword.Main;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Main.MODID)
public abstract class RuneItem extends Item implements IRunePowers {

	public static final Item FIRE_RUNE_ITEM = null;
	public static final Item WATER_RUNE_ITEM = null;
	public static final Item EARTH_RUNE_ITEM = null;
	public static final Item AIR_RUNE_ITEM = null;
	public static final Item BLOOD_RUNE_ITEM = null;
	public static final Item FROST_RUNE_ITEM = null;
	public static final Item MAGIC_RUNE_ITEM = null;

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
	public void onAttack(ItemStack runeable, PlayerEntity player, Entity target, Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onAttack(runeable, player, target, runes);
	}

	@Override
	public void onAttackMajor(ItemStack runeable, PlayerEntity player, Entity target, ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onAttackMajor(runeable, player, target, rune);
	}

	@Override
	public void onKill(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onKill(runeable, player, entityLiving, source, runes);
	}

	@Override
	public void onKillMajor(ItemStack runeable, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onKillMajor(runeable, player, entityLiving, source, rune);
	}

	@Override
	public float onHurt(ItemStack runeable, PlayerEntity player, DamageSource source, float amount,
			Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				amount = p.onHurt(runeable, player, source, amount, runes);
		return amount;
	}

	@Override
	public float onHurtMajor(ItemStack runeable, PlayerEntity player, DamageSource source, float amount,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				amount = p.onHurtMajor(runeable, player, source, amount, rune);
		return amount;
	}

	@Override
	public void onRightClick(ItemStack runeable, PlayerEntity player, Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onRightClick(runeable, player, runes);
	}

	@Override
	public void onRightClickMajor(ItemStack runeable, PlayerEntity player, ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				p.onRightClickMajor(runeable, player, rune);
	}

	@Override
	public float onBreakSpeed(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos, float speed,
			Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				speed = p.onBreakSpeed(runeable, player, state, pos, speed, runes);
		return speed;
	}

	@Override
	public float onBreakSpeedMajor(ItemStack runeable, PlayerEntity player, BlockState state, BlockPos pos, float speed,
			ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				speed = p.onBreakSpeedMajor(runeable, player, state, pos, speed, rune);
		return speed;
	}

	@Override
	public boolean onHarvestCheck(ItemStack runeable, PlayerEntity player, BlockState state, boolean canHarvest, Set<ItemStack> runes) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				canHarvest = p.onHarvestCheck(runeable, player, state, canHarvest, runes);
		return canHarvest;
	}

	@Override
	public boolean onHarvestCheckMajor(ItemStack runeable, PlayerEntity player, BlockState state,
			boolean canHarvest, ItemStack rune) {
		for (RunePowers p : powers)
			if (p.canActivatePowers(runeable))
				canHarvest = p.onHarvestCheckMajor(runeable, player, state, canHarvest, rune);
		return canHarvest;
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
	public int getItemEnchantability() {
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
