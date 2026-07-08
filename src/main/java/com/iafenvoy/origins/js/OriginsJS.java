package com.iafenvoy.origins.js;

import com.iafenvoy.origins.js.registry.OJSActions;
import com.iafenvoy.origins.js.registry.OJSConditions;
import com.iafenvoy.origins.js.registry.OJSPowers;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public class OriginsJS {
    public static final String MOD_ID = "origins_js";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static boolean initialized = false;

    /**
     * Initialize the mod. Called from platform-specific entrypoints.
     * Safe to call multiple times (only executes once).
     */
    public static void init() {
        if (initialized) return;
        initialized = true;

        LOGGER.info("[OriginsJS] Initializing Origins JS for 1.20.1");

        OJSPowers.register();
        OJSActions.register();
        OJSConditions.register();

        LOGGER.info("[OriginsJS] Initialization complete");
    }
}
