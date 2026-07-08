package com.iafenvoy.origins.js.data.condition;

import com.google.gson.JsonObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class JSBiomeCondition {
    private static final Map<String, Object> CALLBACKS = new ConcurrentHashMap<>();

    public static <T> T getCallback(String id) { return (T) CALLBACKS.get(id); }

    public static void register(String id, Object predicate) { CALLBACKS.put(id, predicate); }

    public static void clear() { CALLBACKS.clear(); }
}
