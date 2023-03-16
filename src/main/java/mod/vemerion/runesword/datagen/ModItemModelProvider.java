package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModItems;
import mod.vemerion.runesword.item.RuneItem;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItemModelProvider extends ItemModelProvider {

	public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Main.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		singleTexture("template_rune", mcLoc(ITEM_FOLDER + "/generated"), "layer0", modLoc(ITEM_FOLDER + "/rune"))
				.texture("layer1", modLoc(ITEM_FOLDER + "/rune_overlay"));
		for (RuneItem rune : RuneItem.getRunes())
			rune(rune);

		singleTexture(ForgeRegistries.ITEMS.getKey(ModItems.GUIDE.get()).getPath(), mcLoc(ITEM_FOLDER + "/generated"),
				"layer0", modLoc(ITEM_FOLDER + "/table"));

	}

	private void rune(Item item) {
		withExistingParent(ForgeRegistries.ITEMS.getKey(item).getPath(), modLoc(ITEM_FOLDER + "/template_rune"));
	}
}
