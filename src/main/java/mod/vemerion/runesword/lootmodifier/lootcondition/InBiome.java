package mod.vemerion.runesword.lootmodifier.lootcondition;

import java.util.Optional;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.JsonOps;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

public class InBiome implements ILootCondition {

	private Biome.Category biome;

	public InBiome(Biome.Category biome) {
		this.biome = biome;
	}

	@Override
	public boolean test(LootContext t) {
		if (t.has(LootParameters.field_237457_g_)) {
			return t.getWorld().getBiome(new BlockPos(t.get(LootParameters.field_237457_g_))).getCategory() == biome;
		}
		return false;
	}

	@Override
	public LootConditionType func_230419_b_() {
		return LootConditions.IN_BIOME;
	}

	public static class Serializer implements ILootSerializer<InBiome> {
		public void serialize(JsonObject json, InBiome instance, JsonSerializationContext p_230424_3_) {
			Biome.Category.CODEC.encodeStart(JsonOps.INSTANCE, instance.biome).result().ifPresent(elem -> {
				json.add("category", elem);
			});
		}

		public InBiome deserialize(JsonObject json, JsonDeserializationContext p_230423_2_) {
			JsonElement elem = json.get("category");
			if (elem == null) {
				throw new JsonSyntaxException("Missing biome category");
			}
			Optional<Biome.Category> maybeBiome = Biome.Category.CODEC.parse(JsonOps.INSTANCE, json.get("category"))
					.result();

			if (!maybeBiome.isPresent()) {
				throw new JsonSyntaxException("Unknown biome category");
			}

			return new InBiome(maybeBiome.get());
		}
	}

}
