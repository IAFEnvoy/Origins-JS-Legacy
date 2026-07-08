package com.iafenvoy.origins.js.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.apace100.calio.data.SerializableDataType;

public final class JSUtil {
    /**
     * SerializableDataType for JsonObject (Gson).
     * In 1.20.1 Calio, there's no built-in JSON_OBJECT type.
     * We create a custom one that serializes via string.
     */
    public static final SerializableDataType<JsonObject> JSON_OBJECT = new SerializableDataType<>(
            JsonObject.class,
            (buf, obj) -> buf.writeUtf(obj.toString()),
            buf -> JsonParser.parseString(buf.readUtf()).getAsJsonObject(),
            json -> json.getAsJsonObject()
    );
}

