package com.iafenvoy.origins.js.binding;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.world.entity.Entity;

/**
 * Fabric-specific PlatformHelper using Cardinal Components API.
 */
public final class PlatformHelper {

    public static OriginComponent getOriginComponent(Entity entity) {
        if (entity == null) return null;
        return ModComponents.ORIGIN.get(entity);
    }

    public static PowerHolderComponent getPowerComponent(Entity entity) {
        if (entity == null) return null;
        return PowerHolderComponent.KEY.get(entity);
    }
}
