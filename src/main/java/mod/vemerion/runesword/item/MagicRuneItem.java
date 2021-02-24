package mod.vemerion.runesword.item;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.entity.MagicBallEntity;
import mod.vemerion.runesword.helpers.Helper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MagicRuneItem extends RuneItem {

	private static final int COOLDOWN = 20 * 10;

	public MagicRuneItem(Properties properties) {
		super(Helper.color(100, 0, 100, 255), properties);
	}

	@Override
	public void onRightClickMajor(ItemStack sword, PlayerEntity player, ItemStack rune) {
		CooldownTracker cdTracker = player.getCooldownTracker();
		if (cdTracker.hasCooldown(rune.getItem()))
			return;

		Runes.getRunes(sword).ifPresent(runes -> {
			World world = player.world;
			Map<Enchantment, Integer> enchantments = getEnchantments(minorMagicRunes(runes));
			int multishot = enchantments.getOrDefault(Enchantments.MULTISHOT, 0);
			float inaccuracy = 1 - enchantments.getOrDefault(Enchantments.POWER, 0) / 15f;
			float speed = 0.5f + enchantments.getOrDefault(Enchantments.SOUL_SPEED, 0) * 0.05f;

			int quickCharge = enchantments.getOrDefault(Enchantments.QUICK_CHARGE, 0);
			cdTracker.setCooldown(rune.getItem(), (int) (COOLDOWN * (1 - quickCharge * 0.05)));

			world.playSound(null, player.getPosition(), Main.PROJECTILE_LAUNCH_SOUND, SoundCategory.PLAYERS, 1,
					Helper.soundPitch(random));

			if (multishot <= 0) {
				Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw());
				Vector3d position = player.getPositionVec().add(direction.getX() * 1, 1.2, direction.getZ() * 1);
				shoot(player, world, enchantments, position, player.rotationPitch, player.rotationYaw, inaccuracy,
						speed);
			} else { // Multishot
				int count = random.nextInt(multishot) + 1;
				for (int i = 0; i < count; i++) {
					Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw())
							.rotateYaw((random.nextFloat() - 0.5f) * 0.1f);
					Vector3d position = player.getPositionVec().add(direction.getX() * 1,
							1.2 + (random.nextDouble() - 0.5) * 0.5, direction.getZ() * 1);
					shoot(player, world, enchantments, position, player.rotationPitch + random.nextInt(30) - 15,
							player.rotationYaw + random.nextInt(30) - 15, inaccuracy, speed);
				}
			}

			// Underwater breathing buff
			int luckOfTheSea = enchantments.getOrDefault(Enchantments.LUCK_OF_THE_SEA, 0);
			if (luckOfTheSea > 0)
				player.addPotionEffect(new EffectInstance(Effects.WATER_BREATHING, luckOfTheSea * 3 * 20));

		});
	}

	private void shoot(PlayerEntity player, World world, Map<Enchantment, Integer> enchantments, Vector3d position,
			float pitch, float yaw, float inaccuracy, float speed) {
		MagicBallEntity ball = new MagicBallEntity(position.getX(), position.getY(), position.getZ(), world,
				enchantments);
		ball.setShooter(player);
		ball.func_234612_a_(player, pitch, yaw, 0, speed, inaccuracy); // shoot()
		world.addEntity(ball);
	}

	private Set<ItemStack> minorMagicRunes(Runes runes) {
		Set<ItemStack> minorRunes = new HashSet<>();
		for (int i = Runes.FIRST_MINOR_SLOT; i < Runes.RUNES_COUNT; i++) {
			ItemStack rune = runes.getStackInSlot(i);
			if (rune.getItem() instanceof MagicRuneItem)
				minorRunes.add(rune);
		}
		return minorRunes;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment.getRegistryName().getNamespace().equals("minecraft");
	}
}
