package com.iafenvoy.origins.js;

import com.iafenvoy.origins.js.binding.OriginsJSBindings;
import net.fabricmc.api.ModInitializer;

/**
 * Fabric entrypoint for Origins JS.
 */
public class OriginsJSFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        OriginsJS.init();
    }
}
