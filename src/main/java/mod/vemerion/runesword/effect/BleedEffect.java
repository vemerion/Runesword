package mod.vemerion.runesword.effect;

import java.util.List;

import com.google.common.collect.ImmutableList;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.helpers.Helper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

public class BleedEffect extends Effect {

	public static final DamageSource BLEED = new DamageSource(Main.MODID + ".bleed");

	public BleedEffect() {
		super(EffectType.HARMFUL, Helper.color(150, 0, 0, 255));
	}

	@Override
	public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
		entityLivingBaseIn.attackEntityFrom(BLEED, 1);
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		int j = 25 >> amplifier;
		if (j > 0) {
			return duration % j == 0;
		} else {
			return true;
		}
	}
	
	@Override
	public List<ItemStack> getCurativeItems() {
		return ImmutableList.of();
	}
}
