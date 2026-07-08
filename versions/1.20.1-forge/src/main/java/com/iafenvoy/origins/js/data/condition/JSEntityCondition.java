package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

/**
 * Forge stub — the full implementation lives in the Fabric source folder.
 * On Forge, the edwinmindcraft API uses IConditionFactory instead of ConditionFactory.
 */
public final class JSEntityCondition {
    private static final Map<String, BiPredicate<?, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getCallback(String id) { return (T) CALLBACKS.get(id); }

    @SuppressWarnings("unchecked")
    public static void register(String id, Object predicate) {
        CALLBACKS.put(id, (BiPredicate<?, JsonObject>) predicate);
    }

    public static void clear() { CALLBACKS.clear(); }
}
