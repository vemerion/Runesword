package mod.vemerion.runesword.lootmodifier.lootcondition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BelowDepth implements LootItemCondition {

	private int depth;

	public BelowDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public boolean test(LootContext t) {
		if (t.hasParam(LootContextParams.ORIGIN)) {
			return depth > t.getParamOrNull(LootContextParams.ORIGIN).y();
		}
		return false;
	}

	@Override
	public LootItemConditionType getType() {
		return LootConditions.BELOW_DEPTH;
	}

	public static class ConditionSerializer implements Serializer<BelowDepth> {
		public void serialize(JsonObject json, BelowDepth instance, JsonSerializationContext p_230424_3_) {
			json.addProperty("depth", instance.depth);
		}

		public BelowDepth deserialize(JsonObject json, JsonDeserializationContext p_230423_2_) {
			int depth = GsonHelper.getAsInt(json, "depth");
			return new BelowDepth(depth);
		}
	}

}
