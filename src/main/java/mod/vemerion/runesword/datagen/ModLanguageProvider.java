package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(DataGenerator gen) {
		super(gen, Main.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addGui("no_sword", "Insert sword into runeforge");
		addGui("slot_locked", "Slot locked");
		add(Main.RUNEFORGE_BLOCK, "Runeforge");
		add(RuneItem.AIR_RUNE_ITEM, "Air Rune");
		add(RuneItem.FIRE_RUNE_ITEM, "Fire Rune");
		add(RuneItem.WATER_RUNE_ITEM, "Water Rune");
		add(RuneItem.EARTH_RUNE_ITEM, "Earth Rune");
		add(RuneItem.BLOOD_RUNE_ITEM, "Blood Rune");
		add(RuneItem.FROST_RUNE_ITEM, "Frost Rune");
		addText("tooltip", "minor", "Minor: ");
		addText("tooltip", "major", "Major: ");
		add(Main.GUIDE_ITEM, "Runesword Guide");
		add("itemGroup." + Main.MODID, "Runesword");
		addGui("guide", "Runesword");
		addGui("home", "Home");
		addGui("text1", "This is some text! This is another sentence. More more more. This should be enough.");
		addGui("text2", "EVENT MORE TEXT!");
	}
	
	private void addGui(String suffix, String text) {
		addText("gui", suffix, text);
	}
	
	private void addText(String prefix, String suffix, String text) {
		add(prefix + "." + Main.MODID + "." + suffix, text);
	}
}
