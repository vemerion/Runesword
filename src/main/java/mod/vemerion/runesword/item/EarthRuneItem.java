package mod.vemerion.runesword.item;

import java.awt.Color;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;

public class EarthRuneItem extends RuneItem {

	private static final List<Item> DROPS = ImmutableList.of(Items.IRON_ORE, Items.COAL_ORE);

	public EarthRuneItem(Properties properties) {
		super(new Color(100, 50, 0).getRGB(), properties);
	}

	@Override
	public void onAttack(PlayerEntity player, Entity target, Set<ItemStack> runes, boolean major) {

		if (major && player.getPosY() < 30) {
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), 3);
			target.hurtResistantTime = 0;
		}
	}

	@Override
	public void onKill(PlayerEntity player, LivingEntity entityLiving, DamageSource source, Set<ItemStack> runes,
			boolean major) {
		if (!major && player.getRNG().nextDouble() < runes.size() * 0.1
				+ getEnchantmentLevel(Enchantments.FORTUNE, runes) * 0.02) {
			ItemEntity dirt = new ItemEntity(player.world, entityLiving.getPosX(), entityLiving.getPosY(),
					entityLiving.getPosZ(), new ItemStack(getDrop()));
			player.world.addEntity(dirt);
		}
	}

	private Item getDrop() {
		return DROPS.get(random.nextInt(DROPS.size()));
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment == Enchantments.FORTUNE;
	}

}
