package mod.vemerion.runesword.effect;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.helpers.Helper;
import mod.vemerion.runesword.init.ModParticles;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BleedEffect extends MobEffect {

	public static final DamageSource BLEED = new DamageSource(Main.MODID + ".bleed");

	public BleedEffect() {
		super(MobEffectCategory.HARMFUL, Helper.color(150, 0, 0, 255));
	}

	@Override
	public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
		entityLivingBaseIn.hurt(BLEED, 1);

		if (entityLivingBaseIn.level.isClientSide)
			addBleedingParticles(entityLivingBaseIn);
	}

	public static void addBleedingParticles(LivingEntity entity) {
		Random rand = entity.getRandom();
		for (int i = 0; i < 5; i++) {
			Vec3 position = Helper.randomInBox(rand, entity.getBoundingBox());
			entity.level.addParticle(ModParticles.BLEED.get(), position.x, position.y, position.z, 0, 0, 0);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
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
