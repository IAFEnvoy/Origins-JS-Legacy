package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

public final class JSEntityCondition {
    private static final Map<String, BiPredicate<Entity, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    public static final ConditionFactory<Entity> FACTORY = new ConditionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_entity_condition"),
            new SerializableData()
                    .add("id", SerializableDataTypes.STRING)
                    .add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, entity) -> {
                String callbackId = data.getString("id");
                JsonObject params = data.get("params");
                BiPredicate<Entity, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) {
                    try {
                        return cb.test(entity, params);
                    } catch (Exception e) {
                        OriginsJS.LOGGER.error("[OriginsJS] Error in JS entity condition '{}'", callbackId, e);
                    }
                } else {
                    OriginsJS.LOGGER.warn("[OriginsJS] Unknown JS entity condition '{}'", callbackId);
                }
                return false;
            }
    );

    public static void register(String id, BiPredicate<Entity, JsonObject> predicate) {
        CALLBACKS.put(id, predicate);
    }

    public static void clear() {
        CALLBACKS.clear();
    }
}
