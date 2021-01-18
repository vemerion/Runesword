package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.entity.FrostGolemEntity;
import mod.vemerion.runesword.entity.FrostballEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class FrostRuneItem extends RuneItem {

	private static final Set<Enchantment> ENCHANTS = ImmutableSet.of();

	public FrostRuneItem(Properties properties) {
		super(new Color(40, 120, 150).getRGB(), properties);
	}

	@Override
	public void onKillMajor(ItemStack sword, PlayerEntity player, LivingEntity entityLiving, DamageSource source,
			ItemStack rune) {
		if (player.getRNG().nextDouble() < 0.1) {
			FrostGolemEntity snowman = new FrostGolemEntity(Main.FROST_GOLEM_ENTITY, player.world);
			snowman.setPositionAndRotation(entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(),
					entityLiving.rotationYaw, entityLiving.rotationPitch);
			player.world.addEntity(snowman);
		}
	}

	@Override
	public void onAttack(ItemStack sword, PlayerEntity player, Entity target, Set<ItemStack> runes) {
		if (player.getRNG().nextDouble() < 0.1 * runes.size()) {
			FrostballEntity snowball = new FrostballEntity(player.world, player);
			snowball.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5f, 1); // Shoot
			player.world.addEntity(snowball);
		}
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return ENCHANTS.contains(enchantment);
	}
}
