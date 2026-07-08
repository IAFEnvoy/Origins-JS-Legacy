package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import com.iafenvoy.origins.js.util.function.TriPredicate;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JSBiomeCondition {
    private static final Map<String, TriPredicate<Holder<Biome>, BlockPos, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    public static final ConditionFactory<Holder<Biome>> FACTORY = new ConditionFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_biome_condition"),
            new SerializableData().add("id", SerializableDataTypes.STRING).add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            (data, biome) -> {
                String callbackId = data.getString("id");
                TriPredicate<Holder<Biome>, BlockPos, JsonObject> cb = CALLBACKS.get(callbackId);
                if (cb != null) try { return cb.test(biome, new BlockPos(0, 0, 0), data.get("params")); } catch (Exception e) { OriginsJS.LOGGER.error("[OriginsJS] Error in JS biome condition '{}'", callbackId, e); }
                return false;
            }
    );

    public static void register(String id, TriPredicate<Holder<Biome>, BlockPos, JsonObject> predicate) { CALLBACKS.put(id, predicate); }
    public static void clear() { CALLBACKS.clear(); }
}
