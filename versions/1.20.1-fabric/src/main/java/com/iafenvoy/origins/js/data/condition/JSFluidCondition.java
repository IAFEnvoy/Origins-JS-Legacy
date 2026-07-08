package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FluidState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

public final class JSFluidCondition {
    private static final Map<String, BiPredicate<FluidState, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    public static final ConditionFactory<FluidState> FACTORY = new ConditionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_fluid_condition"),
            new SerializableData().add("id", SerializableDataTypes.STRING).add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, state) -> {
                String callbackId = data.getString("id");
                BiPredicate<FluidState, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) try { return cb.test(state, data.get("params")); } catch (Exception e) { OriginsJS.LOGGER.error("[OriginsJS] Error in JS fluid condition '{}'", callbackId, e); }
                return false;
            }
    );

    public static void register(String id, BiPredicate<FluidState, JsonObject> predicate) { CALLBACKS.put(id, predicate); }
    public static void clear() { CALLBACKS.clear(); }
}
