package com.iafenvoy.origins.js.registry;

import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.data.power.JSPower;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.core.Registry;

public final class OJSPowers {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        Registry.register(ApoliRegistries.POWER_FACTORY,
                JSPower.FACTORY.getSerializerId(),
                JSPower.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered power type: js_power");
    }
}
