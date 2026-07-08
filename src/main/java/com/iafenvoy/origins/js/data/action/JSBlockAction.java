package com.iafenvoy.origins.js.data.action;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import com.iafenvoy.origins.js.util.function.QuadConsumer;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class JSBlockAction {
    private static final Map<String, QuadConsumer<Level, BlockPos, Optional<Direction>, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    /**
     * Apoli block action uses Triple(World, BlockPos, Direction).
     */
    @SuppressWarnings("unchecked")
    public static final ActionFactory<Triple<Level, BlockPos, Direction>> FACTORY = new ActionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_block_action"),
            new SerializableData()
                    .add("id", SerializableDataTypes.STRING)
                    .add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, triple) -> {
                String callbackId = data.getString("id");
                JsonObject params = data.get("params");
                QuadConsumer<Level, BlockPos, Optional<Direction>, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) {
                    try {
                        cb.accept(triple.getLeft(), triple.getMiddle(), Optional.ofNullable(triple.getRight()), params);
                    } catch (Exception e) {
                        OriginsJS.LOGGER.error("[OriginsJS] Error in JS block action '{}'", callbackId, e);
                    }
                } else {
                    OriginsJS.LOGGER.warn("[OriginsJS] Unknown JS block action '{}'", callbackId);
                }
            }
    );

    public static void register(String id, QuadConsumer<Level, BlockPos, Optional<Direction>, JsonObject> callback) {
        CALLBACKS.put(id, callback);
    }

    public static void clear() {
        CALLBACKS.clear();
    }
}
