package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModItems;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, Main.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		singleTexture("template_rune", mcLoc(ITEM_FOLDER + "/generated"), "layer0", modLoc(ITEM_FOLDER + "/rune"))
				.texture("layer1", modLoc(ITEM_FOLDER + "/rune_overlay"));
		for (RuneItem rune : RuneItem.getRunes())
			rune(rune);
		singleTexture(ModItems.GUIDE.get().getRegistryName().getPath(), mcLoc(ITEM_FOLDER + "/generated"), "layer0",
				modLoc(ITEM_FOLDER + "/table"));

	}

	private void rune(Item item) {
		withExistingParent(item.getRegistryName().getPath(), modLoc(ITEM_FOLDER + "/template_rune"));
	}
}
