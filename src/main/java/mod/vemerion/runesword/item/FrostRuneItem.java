package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class FrostRuneItem extends RuneItem {

	private static final int MAX_DURATION = 20 * 30;

	private static final Set<Enchantment> ENCHANTS = ImmutableSet.of(Enchantments.FORTUNE, Enchantments.INFINITY,
			Enchantments.EFFICIENCY, Enchantments.MULTISHOT, Enchantments.KNOCKBACK, Enchantments.CHANNELING);

	public FrostRuneItem(Properties properties) {
		super(new Color(40, 120, 150).getRGB(), properties);
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
			int duration = FrostballEntity.SLOW_DURATION * (1 + getEnchantmentLevel(Enchantments.CHANNELING, runes));
			FrostballEntity snowball = new FrostballEntity(player.world, player, knockback, duration);
			snowball.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5f, 1); // Shoot
			player.world.addEntity(snowball);
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ENCHANTS.contains(enchantment);
	}
}
