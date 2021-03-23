package mod.vemerion.runesword.helpers;

import java.util.Random;

import mod.vemerion.runesword.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class Helper {

	public static int color(int r, int g, int b, int a) {
		a = (a << 24) & 0xFF000000;
		r = (r << 16) & 0x00FF0000;
		g = (g << 8) & 0x0000FF00;
		b &= 0x000000FF;

		return a | r | g | b;
	}

	public static DamageSource magicDamage() {
		return (new DamageSource(Main.MODID + ".magic")).setMagicDamage();
	}

	public static DamageSource magicDamage(PlayerEntity player) {
		return new EntityDamageSource(Main.MODID + ".magicplayer", player).setMagicDamage();
	}

	public static DamageSource magicDamage(Entity source, Entity shooter) {
		return new IndirectEntityDamageSource(Main.MODID + ".magicindirect", source, shooter).setMagicDamage();
	}

	public static float soundPitch(Random rand) {
		return 0.8f + rand.nextFloat() * 0.4f;
	}

	public static Vector3d randomInBox(Random rand, AxisAlignedBB box) {
		return new Vector3d(MathHelper.nextDouble(rand, box.minX, box.maxX),
				MathHelper.nextDouble(rand, box.minY, box.maxY), MathHelper.nextDouble(rand, box.minZ, box.maxZ));
	}
}
