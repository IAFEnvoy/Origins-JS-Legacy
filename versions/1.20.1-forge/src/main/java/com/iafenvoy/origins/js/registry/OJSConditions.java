package com.iafenvoy.origins.js.registry;

import com.iafenvoy.origins.js.OriginsJS;

public final class OJSConditions {
    public static void register() {
        OriginsJS.LOGGER.info("[OriginsJS] Forge conditions: JS conditions store callbacks only (no FACTORY registration on Forge 1.20.1)");
    }
}
