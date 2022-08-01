package mod.vemerion.runesword.lootmodifier.lootcondition;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.registries.ForgeRegistries;

public class IsEntityType implements LootItemCondition {

	private EntityType<?> entityType;

	public IsEntityType(EntityType<?> entityType) {
		this.entityType = entityType;
	}

	@Override
	public boolean test(LootContext t) {
		return t.hasParam(LootContextParams.THIS_ENTITY) && t.getParamOrNull(LootContextParams.THIS_ENTITY).getType() == entityType;
	}

	@Override
	public LootItemConditionType getType() {
		return LootConditions.IS_ENTITY_TYPE;
	}

	public static class ConditionSerializer implements Serializer<IsEntityType> {
		public void serialize(JsonObject json, IsEntityType instance, JsonSerializationContext p_230424_3_) {
			json.addProperty("type", ForgeRegistries.ENTITIES.getKey(instance.entityType).toString());
		}

		public IsEntityType deserialize(JsonObject json, JsonDeserializationContext p_230423_2_) {
			var rl = new ResourceLocation(GsonHelper.getAsString(json, "type"));
			var type = ForgeRegistries.ENTITIES.getValue(rl);
			if (type == null) {
				throw new JsonSyntaxException("Unknown type '" + rl + "'");
			}

			return new IsEntityType(type);
		}
	}

}
