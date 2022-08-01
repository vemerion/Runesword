package mod.vemerion.runesword.lootmodifier.lootcondition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class InBiome implements LootItemCondition {

	private TagKey<Biome> biome;

	public InBiome(TagKey<Biome> biome) {
		this.biome = biome;
	}

	@Override
	public boolean test(LootContext t) {
		if (t.hasParam(LootContextParams.ORIGIN)) {
			return t.getLevel().getBiome(new BlockPos(t.getParamOrNull(LootContextParams.ORIGIN))).containsTag(biome);
		}
		return false;
	}

	@Override
	public LootItemConditionType getType() {
		return LootConditions.IN_BIOME;
	}

	public static class ConditionSerializer implements Serializer<InBiome> {
		public void serialize(JsonObject json, InBiome instance, JsonSerializationContext p_230424_3_) {
			json.addProperty("biome", instance.biome.location().toString());

		}

		public InBiome deserialize(JsonObject json, JsonDeserializationContext p_230423_2_) {
			var biome = GsonHelper.getAsString(json, "biome");

			var biomeTag = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(biome));

			return new InBiome(biomeTag);
		}
	}

}
