package mod.vemerion.runesword.item;

import java.util.HashSet;
import java.util.Set;

import mod.vemerion.runesword.capability.Runes;
import mod.vemerion.runesword.entity.MagicBallEntity;
import mod.vemerion.runesword.helpers.Helper;
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
			Vector3d direction = Vector3d.fromPitchYaw(player.getPitchYaw());
			Vector3d position = player.getPositionVec().add(direction.getX() * 1, 1.2, direction.getZ() * 1);
			MagicBallEntity ball = new MagicBallEntity(position.getX(), position.getY(), position.getZ(), world,
					getEnchantments(minorRunes(runes)));
			ball.setShooter(player);
			ball.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0, 1f, 0); // shoot()
			world.addEntity(ball);
		});
	}

	private Set<ItemStack> minorRunes(Runes runes) {
		Set<ItemStack> minorRunes = new HashSet<>();
		for (int i = Runes.FIRST_MINOR_SLOT; i < Runes.RUNES_COUNT; i++) {
			minorRunes.add(runes.getStackInSlot(i));
		}
		return minorRunes;
	}
}
