package mod.vemerion.runesword.datagen;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mod.vemerion.runesword.api.IGuideChapter;
import mod.vemerion.runesword.guide.GuideChapter;
import mod.vemerion.runesword.guide.GuideReloadListener;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;

public class GuideProvider implements DataProvider {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Logger LOGGER = LogManager.getLogger();
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
	public void run(HashCache cache) throws IOException {
		Path folder = generator.getOutputFolder();
		Path path = folder.resolve("assets/" + modid + "/" + FOLDER_NAME + "/" + guideId + ".json");
		try {
			DataProvider.save(GSON, cache, startChapter.write(), path);
		} catch (IOException e) {
			LOGGER.error("Could not save guide {}", path, e);
		}
	}

	@Override
	public String getName() {
		return "Guides";
	}
}
