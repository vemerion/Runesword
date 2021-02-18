package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Main.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		runeforge();
	}

	private void runeforge() {
		String name = Main.RUNEFORGE_BLOCK.getRegistryName().getPath();
		ResourceLocation top = modLoc("block/runeforge_top");
		ResourceLocation side = modLoc("block/runeforge_side");
		BlockModelBuilder model = models().withExistingParent(name, mcLoc("block/block")).texture("particle", top)
				.texture("bottom", top).texture("top", top).texture("side", side);
		model.element().from(0, 0, 0).to(16, 15, 16).face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom")
				.cullface(Direction.DOWN).end().face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").end()
				.face(Direction.NORTH).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.WEST).end()
				.face(Direction.EAST).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.EAST).end().end();
		simpleBlock(Main.RUNEFORGE_BLOCK, model);
        itemModels().getBuilder(Main.RUNEFORGE_BLOCK.getRegistryName().getPath() + "_item").parent(model);
	}
}
