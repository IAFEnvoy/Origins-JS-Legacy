package com.iafenvoy.origins.js;

import com.iafenvoy.origins.js.binding.OriginsJSBindings;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

/**
 * KubeJS Plugin for Origins integration.
 * <p>
 * Registered via src/main/resources/kubejs.plugins.txt
 */
public class OriginsJSKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void init() {
        OriginsJS.init();
        OriginsJS.LOGGER.info("[OriginsJS] KubeJS plugin initialized");
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("OriginsJS", OriginsJSBindings.class);
    }

    @Override
    public void clearCaches() {
        OriginsJSBindings.clearAll();
    }
}
