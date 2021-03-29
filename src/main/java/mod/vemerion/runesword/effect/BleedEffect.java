package mod.vemerion.runesword.effect;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.helpers.Helper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;

public class BleedEffect extends Effect {

	public static final DamageSource BLEED = new DamageSource(Main.MODID + ".bleed");

	public BleedEffect() {
		super(EffectType.HARMFUL, Helper.color(150, 0, 0, 255));
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		entityLivingBaseIn.attackEntityFrom(BLEED, 1);

		if (entityLivingBaseIn.world.isRemote)
			addBleedingParticles(entityLivingBaseIn);
	}

	public static void addBleedingParticles(LivingEntity entity) {
		Random rand = entity.getRNG();
		for (int i = 0; i < 5; i++) {
			Vector3d position = Helper.randomInBox(rand, entity.getBoundingBox());
			entity.world.addParticle(Main.BLEED_PARTICLE, position.x, position.y, position.z, 0, 0, 0);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int interval = 25 >> amplifier;
		if (interval > 0) {
			return duration % interval == 0;
		} else {
			return true;
		}
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return ImmutableList.of();
	}
}
