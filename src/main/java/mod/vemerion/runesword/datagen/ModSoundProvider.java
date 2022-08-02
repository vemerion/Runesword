package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

	protected ModSoundProvider(DataGenerator generator, ExistingFileHelper helper) {
		super(generator, Main.MODID, helper);
	}

	@Override
	public void registerSounds() {
		addSimple(Main.GUIDE_CLICK);
		addSimple(Main.PROJECTILE_IMPACT_SOUND);
		addSimple(Main.PROJECTILE_LAUNCH_SOUND);
	}

	private void addSimple(SoundEvent sound) {
		add(sound, definition().with(sound(sound.getLocation())));
	}
}
