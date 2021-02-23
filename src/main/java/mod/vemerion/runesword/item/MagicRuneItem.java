package mod.vemerion.runesword.item;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.entity.MagicBallEntity;
import mod.vemerion.runesword.helpers.Helper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class MagicRuneItem extends RuneItem {

	public MagicRuneItem(Properties properties) {
		super(Helper.color(100, 0, 100, 255), properties);
	}

	@Override
	public void onRightClickMajor(ItemStack sword, PlayerEntity player, ItemStack rune) {
		Runes.getRunes(sword).ifPresent(runes -> {
			World world = player.world;
			Map<Enchantment, Integer> enchantments = getEnchantments(minorMagicRunes(runes));
			int multishot = enchantments.getOrDefault(Enchantments.MULTISHOT, 0);
			float inaccuracy = 1 - enchantments.getOrDefault(Enchantments.POWER, 0) / 15f;
			float speed = 0.5f + enchantments.getOrDefault(Enchantments.SOUL_SPEED, 0) * 0.05f;
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
}
