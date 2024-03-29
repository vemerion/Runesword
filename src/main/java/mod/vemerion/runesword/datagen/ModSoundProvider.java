package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModSounds;
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
		addSimple(ModSounds.GUIDE_CLICK.get());
		addSimple(ModSounds.PROJECTILE_IMPACT.get());
		addSimple(ModSounds.PROJECTILE_LAUNCH.get());
	}

	private void addSimple(SoundEvent sound) {
		add(sound, definition().with(sound(sound.getLocation())));
	}
}
