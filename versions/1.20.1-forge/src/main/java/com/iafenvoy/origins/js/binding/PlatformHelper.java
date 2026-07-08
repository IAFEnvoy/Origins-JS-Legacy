package com.iafenvoy.origins.js.binding;

import io.github.apace100.origins.component.OriginComponent;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import net.minecraft.world.entity.Entity;

/**
 * Forge-specific PlatformHelper.
 */
public final class PlatformHelper {

    public static OriginComponent getOriginComponent(Entity entity) {
        if (entity == null) return null;
        return IOriginContainer.get(entity)
                .map(IOriginContainer::asLegacyComponent)
                .orElse(null);
    }

    public static IPowerContainer getPowerComponent(Entity entity) {
        if (entity == null) return null;
        return IPowerContainer.get(entity).orElse(null);
    }
}
