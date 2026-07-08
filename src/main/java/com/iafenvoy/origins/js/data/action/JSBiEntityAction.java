package com.iafenvoy.origins.js.data.action;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import com.iafenvoy.origins.js.util.function.TriConsumer;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JSBiEntityAction {
    private static final Map<String, TriConsumer<Entity, Entity, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static final ActionFactory<Pair<Entity, Entity>> FACTORY = new ActionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_bientity_action"),
            new SerializableData()
                    .add("id", SerializableDataTypes.STRING)
                    .add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, pair) -> {
                String callbackId = data.getString("id");
                JsonObject params = data.get("params");
                TriConsumer<Entity, Entity, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) {
                    try {
                        cb.accept(pair.getLeft(), pair.getRight(), params);
                    } catch (Exception e) {
                        OriginsJS.LOGGER.error("[OriginsJS] Error in JS bi-entity action '{}'", callbackId, e);
                    }
                } else {
                    OriginsJS.LOGGER.warn("[OriginsJS] Unknown JS bi-entity action '{}'", callbackId);
                }
            }
    );

    public static void register(String id, TriConsumer<Entity, Entity, JsonObject> callback) {
        CALLBACKS.put(id, callback);
    }

    public static void clear() {
        CALLBACKS.clear();
    }
}
