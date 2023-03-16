package mod.vemerion.runesword.datagen;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.guide.GuideChapter;
import mod.vemerion.runesword.guide.GuideReloadListener;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public class GuideProvider implements DataProvider {
	public static final String FOLDER_NAME = GuideReloadListener.FOLDER_NAME;

	protected final DataGenerator generator;
	private final String modid;
	private final String guideId;
	private final GuideChapter startChapter;

	public GuideProvider(DataGenerator generator, String modid, String guideId, IGuideChapter startChapter) {
		this.generator = generator;
		this.modid = modid;
		this.guideId = guideId;
		this.startChapter = (GuideChapter) startChapter;
	}

	@Override
	public String getName() {
		return "Guides";
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		Path folder = generator.getPackOutput().getOutputFolder();
		Path path = folder.resolve("assets/" + modid + "/" + FOLDER_NAME + "/" + guideId + ".json");
		return DataProvider.saveStable(cache, startChapter.write(), path);
	}
}
