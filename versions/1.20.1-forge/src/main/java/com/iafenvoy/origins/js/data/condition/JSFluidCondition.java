package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;

public final class JSFluidCondition {
    private static final Map<String, BiPredicate<?, JsonObject>> CALLBACKS = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getCallback(String id) { return (T) CALLBACKS.get(id); }

    @SuppressWarnings("unchecked")
    public static void register(String id, Object predicate) {
        CALLBACKS.put(id, (BiPredicate<?, JsonObject>) predicate);
    }

    public static void clear() { CALLBACKS.clear(); }
}
