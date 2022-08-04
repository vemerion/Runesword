package mod.vemerion.runesword.guide;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mod.vemerion.runesword.Main;
import mod.vemerion.runesword.api.RuneswordAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

public class GuideReloadListener extends SimpleJsonResourceReloadListener {
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	public static final String FOLDER_NAME = Main.MODID + "guides";

	public GuideReloadListener() {
		super(GSON, FOLDER_NAME);
	}

	@Override
	protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn,
			ProfilerFiller profilerIn) {
		Map<String, GuideChapter> guides = new HashMap<>();
		for (Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
			JsonObject json = GsonHelper.convertToJsonObject(entry.getValue(), "top element");

			var chapter = GuideChapter.read(json);

			guides.put(entry.getKey().toString(), chapter);
		}

		for (var guide : guides.entrySet())
			RuneswordAPI.guide.registerGuide(guide.getKey(), guide.getValue());
	}
}
