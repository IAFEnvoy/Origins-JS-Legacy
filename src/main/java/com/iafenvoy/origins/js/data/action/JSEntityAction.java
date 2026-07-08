package com.iafenvoy.origins.js.data.action;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public final class JSEntityAction {
    private static final Map<String, BiConsumer<Entity, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    public static final ActionFactory<Entity> FACTORY = new ActionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_entity_action"),
            new SerializableData()
                    .add("id", SerializableDataTypes.STRING)
                    .add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, entity) -> {
                String callbackId = data.getString("id");
                JsonObject params = data.get("params");
                BiConsumer<Entity, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) {
                    try {
                        cb.accept(entity, params);
                    } catch (Exception e) {
                        OriginsJS.LOGGER.error("[OriginsJS] Error in JS entity action '{}'", callbackId, e);
                    }
                } else {
                    OriginsJS.LOGGER.warn("[OriginsJS] Unknown JS entity action '{}'", callbackId);
                }
            }
    );

    public static void register(String id, BiConsumer<Entity, JsonObject> callback) {
        CALLBACKS.put(id, callback);
    }

    public static void clear() {
        CALLBACKS.clear();
    }
}
