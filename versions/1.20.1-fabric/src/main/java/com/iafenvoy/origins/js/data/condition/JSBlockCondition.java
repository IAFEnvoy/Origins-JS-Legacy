package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import com.iafenvoy.origins.js.util.function.TriPredicate;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JSBlockCondition {
    private static final Map<String, TriPredicate<Level, BlockPos, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    public static final ConditionFactory<BlockInWorld> FACTORY = new ConditionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_block_condition"),
            new SerializableData().add("id", SerializableDataTypes.STRING).add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, biw) -> {
                String callbackId = data.getString("id");
                JsonObject params = data.get("params");
                TriPredicate<Level, BlockPos, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) try { return cb.test((Level) biw.getLevel(), biw.getPos(), params); } catch (Exception e) { OriginsJS.LOGGER.error("[OriginsJS] Error in JS block condition '{}'", callbackId, e); }
                return false;
            }
    );

    public static void register(String id, TriPredicate<Level, BlockPos, JsonObject> predicate) { CALLBACKS.put(id, predicate); }
    public static void clear() { CALLBACKS.clear(); }
}
