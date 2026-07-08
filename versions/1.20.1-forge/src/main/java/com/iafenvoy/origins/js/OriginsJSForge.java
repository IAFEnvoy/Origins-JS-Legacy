package com.iafenvoy.origins.js;

import net.minecraftforge.fml.common.Mod;

/**
 * Forge entrypoint for Origins JS.
 */
@Mod(OriginsJS.MOD_ID)
public class OriginsJSForge {
    public OriginsJSForge() {
        OriginsJS.init();
    }
}
