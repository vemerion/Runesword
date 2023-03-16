package mod.vemerion.runesword.datagen;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.init.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Main.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		runeforge();
	}

	private void runeforge() {
		var name = ForgeRegistries.BLOCKS.getKey(ModBlocks.RUNEFORGE.get()).getPath();
		var top = modLoc("block/runeforge_top");
		var side = modLoc("block/runeforge_side");
		var model = models().withExistingParent(name, mcLoc("block/block")).texture("particle", top)
				.texture("bottom", top).texture("top", top).texture("side", side).renderType("cutout");
		model.element().from(0, 0, 0).to(16, 15, 16).face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#bottom")
				.cullface(Direction.DOWN).end().face(Direction.UP).uvs(0, 0, 16, 16).texture("#top").end()
				.face(Direction.NORTH).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.WEST).end()
				.face(Direction.EAST).uvs(0, 1, 16, 16).texture("#side").cullface(Direction.EAST).end().end();
		simpleBlock(ModBlocks.RUNEFORGE.get(), model);
		itemModels().getBuilder(name).parent(model);
	}
}
