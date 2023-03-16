package mod.vemerion.runesword.helpers;

import mod.vemerion.runesword.Main;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class Helper {

	public static int color(int r, int g, int b, int a) {
		a = (a << 24) & 0xFF000000;
		r = (r << 16) & 0x00FF0000;
		g = (g << 8) & 0x0000FF00;
		b &= 0x000000FF;

		return a | r | g | b;
	}

	public static DamageSource magicDamage() {
		return (new DamageSource(Main.MODID + ".magic")).setMagic();
	}

	public static DamageSource magicDamage(Player player) {
		return new EntityDamageSource(Main.MODID + ".magicplayer", player).setMagic();
	}

	public static DamageSource magicDamage(Entity source, Entity shooter) {
		return new IndirectEntityDamageSource(Main.MODID + ".magicindirect", source, shooter).setMagic();
	}

	public static float soundPitch(RandomSource rand) {
		return 0.8f + rand.nextFloat() * 0.4f;
	}

	public static Vec3 randomInBox(RandomSource rand, AABB box) {
		return new Vec3(Mth.nextDouble(rand, box.minX, box.maxX), Mth.nextDouble(rand, box.minY, box.maxY),
				Mth.nextDouble(rand, box.minZ, box.maxZ));
	}
}
