package mod.vemerion.runesword.lootmodifier.lootcondition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;

public class BelowDepth implements ILootCondition {

	private int depth;

	public BelowDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public boolean test(LootContext t) {
		System.out.println("LOOT");
		if (t.has(LootParameters.field_237457_g_)) {
			return depth > t.get(LootParameters.field_237457_g_).getY();
		}
		return false;
	}

	@Override
	public LootConditionType func_230419_b_() {
		return LootConditions.BELOW_DEPTH;
	}

	public static class Serializer implements ILootSerializer<BelowDepth> {
		public void serialize(JsonObject json, BelowDepth instance, JsonSerializationContext p_230424_3_) {
			json.addProperty("depth", instance.depth);
		}

		public BelowDepth deserialize(JsonObject json, JsonDeserializationContext p_230423_2_) {
			int depth = JSONUtils.getInt(json, "depth");
			return new BelowDepth(depth);
		}
	}

}
