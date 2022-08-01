package mod.vemerion.runesword.lootmodifier.lootcondition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class FromChest implements LootItemCondition {
	private static final FromChest INSTANCE = new FromChest();

	public FromChest() {
	}

	@Override
	public boolean test(LootContext t) {
		if (t.hasParam(LootContextParams.ORIGIN)) {
			var pos = new BlockPos(t.getParamOrNull(LootContextParams.ORIGIN));
			var te = pos == null ? null : t.getLevel().getBlockEntity(pos);
			return te instanceof RandomizableContainerBlockEntity;
		}
		return false;
	}

	@Override
	public LootItemConditionType getType() {
		return LootConditions.FROM_CHEST;
	}

	public static class ConditionSerializer implements Serializer<FromChest> {
		public void serialize(JsonObject json, FromChest instance, JsonSerializationContext p_230424_3_) {
		}

		public FromChest deserialize(JsonObject json, JsonDeserializationContext p_230423_2_) {
			return INSTANCE;
		}
	}

}
