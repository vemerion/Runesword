package mod.vemerion.runesword.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Iterables;

import mod.vemerion.runesword.Main;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Main.MODID)
public class RuneItem extends Item {

	public static final Item FIRE_RUNE_ITEM = null;
	public static final Item WATER_RUNE_ITEM = null;
	public static final Item EARTH_RUNE_ITEM = null;
	public static final Item AIR_RUNE_ITEM = null;
	public static final Item BLOOD_RUNE_ITEM = null;

	private static final List<RuneItem> RUNES = new ArrayList<>();

	private final int color;

	public RuneItem(int color, Properties properties) {
		super(properties);
		this.color = color;
		RUNES.add(this);
	}
	
	public void onAttack(ItemStack sword, PlayerEntity player, Entity target, Set<ItemStack> runes) {
		
	}
	
	public void onAttackMajor(ItemStack sword, PlayerEntity player, Entity target, ItemStack rune) {
		
	}
	
	public void onKill(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source, Set<ItemStack> runes) {
		
	}
	
	public void onKillMajor(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source, ItemStack rune) {
		
	}
	
	public float onHurt(ItemStack sword, PlayerEntity player, DamageSource source, float amount, Set<ItemStack> runes) {
		return amount;
	}
	
	public float onHurtMajor(ItemStack sword, PlayerEntity player, DamageSource source, float amount, ItemStack rune) {
		return amount;
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
	
	protected int getEnchantmentLevel(Enchantment enchantment, Set<ItemStack> stacks) {
		int level = 0;
		for (ItemStack stack : stacks)
			level += EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
		return level;
	}
	
	protected int getEnchantmentLevel(Enchantment enchantment, ItemStack stack) {
		return EnchantmentHelper.getEnchantmentLevel(enchantment, stack);
	}
}
