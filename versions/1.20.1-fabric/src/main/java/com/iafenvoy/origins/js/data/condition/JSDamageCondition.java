package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import com.iafenvoy.origins.js.util.function.TriPredicate;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JSDamageCondition {
    private static final Map<String, TriPredicate<DamageSource, Float, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    public static final ConditionFactory<Pair<DamageSource, Float>> FACTORY = new ConditionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_damage_condition"),
            new SerializableData().add("id", SerializableDataTypes.STRING).add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, pair) -> {
                String callbackId = data.getString("id");
                TriPredicate<DamageSource, Float, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) try { return cb.test(pair.getLeft(), pair.getRight(), data.get("params")); } catch (Exception e) { OriginsJS.LOGGER.error("[OriginsJS] Error in JS damage condition '{}'", callbackId, e); }
                return false;
            }
    );

    public static void register(String id, TriPredicate<DamageSource, Float, JsonObject> predicate) { CALLBACKS.put(id, predicate); }
    public static void clear() { CALLBACKS.clear(); }
}
