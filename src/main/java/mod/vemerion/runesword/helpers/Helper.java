package mod.vemerion.runesword.helpers;

import mod.vemerion.runesword.Main;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;

public class Helper {

	public static int color(int r, int g, int b, int a) {
		a = (a << 24) & 0xFF000000;
		r = (r << 16) & 0x00FF0000;
		g = (g << 8) & 0x0000FF00;
		b &= 0x000000FF;

		return a | r | g | b;
	}
	
	private static final DamageSource MAGIC = (new DamageSource(Main.MODID + ".magic")).setMagicDamage();

	public static DamageSource magicDamage() {
		return MAGIC;
	}

	public static DamageSource magicDamage(PlayerEntity player) {
		return new EntityDamageSource(Main.MODID + ".magicplayer", player).setMagicDamage();
	}

	public static DamageSource magicDamage(Entity source, PlayerEntity player) {
		return new IndirectEntityDamageSource(Main.MODID + ".magicindirect", source, player).setMagicDamage();
	}
}
