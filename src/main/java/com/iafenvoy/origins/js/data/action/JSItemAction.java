package com.iafenvoy.origins.js.data.action;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import com.iafenvoy.origins.js.util.function.QuadConsumer;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JSItemAction {
    private static final Map<String, QuadConsumer<Level, Entity, SlotAccess, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    /**
     * Apoli item action uses Tuple(World, ItemStack).
     * We keep SlotAccess in the callback but only ItemStack in the factory.
     */
    @SuppressWarnings("unchecked")
    public static final ActionFactory<Pair<Level, ItemStack>> FACTORY = new ActionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_item_action"),
            new SerializableData()
                    .add("id", SerializableDataTypes.STRING)
                    .add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, pair) -> {
                String callbackId = data.getString("id");
                JsonObject params = data.get("params");
                QuadConsumer<Level, Entity, SlotAccess, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) {
                    try {
                        // 1.20.1 item action: Tuple(World, ItemStack) — no entity/SlotAccess context
                        cb.accept(pair.getLeft(), null, null, params);
                    } catch (Exception e) {
                        OriginsJS.LOGGER.error("[OriginsJS] Error in JS item action '{}'", callbackId, e);
                    }
                } else {
                    OriginsJS.LOGGER.warn("[OriginsJS] Unknown JS item action '{}'", callbackId);
                }
            }
    );

    public static void register(String id, QuadConsumer<Level, Entity, SlotAccess, JsonObject> callback) {
        CALLBACKS.put(id, callback);
    }

    public static void clear() {
        CALLBACKS.clear();
    }
}
