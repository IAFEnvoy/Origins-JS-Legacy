package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import com.iafenvoy.origins.js.util.function.TriPredicate;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JSItemCondition {
    private static final Map<String, TriPredicate<Level, ItemStack, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    public static final ConditionFactory<ItemStack> FACTORY = new ConditionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_item_condition"),
            new SerializableData().add("id", SerializableDataTypes.STRING).add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, stack) -> {
                String callbackId = data.getString("id");
                TriPredicate<Level, ItemStack, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) try { return cb.test(null, stack, data.get("params")); } catch (Exception e) { OriginsJS.LOGGER.error("[OriginsJS] Error in JS item condition '{}'", callbackId, e); }
                return false;
            }
    );

    public static void register(String id, TriPredicate<Level, ItemStack, JsonObject> predicate) { CALLBACKS.put(id, predicate); }
    public static void clear() { CALLBACKS.clear(); }
}
