package com.iafenvoy.origins.js.binding;

import com.iafenvoy.origins.js.OriginsJS;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper around {@link OriginComponent} exposed to JavaScript.
 * Provides a safe, user-friendly API for managing origins and powers in 1.20.1.
 */
@SuppressWarnings("unused")
public class HolderWrapper {
    private final OriginComponent originComp;
    private final PowerHolderComponent powerComp;
    private final Entity entity;

    public HolderWrapper(OriginComponent originComp, PowerHolderComponent powerComp, Entity entity) {
        this.originComp = originComp;
        this.powerComp = powerComp;
        this.entity = entity;
    }

    public OriginComponent getOriginComponent() {
        return this.originComp;
    }

    public PowerHolderComponent getPowerComponent() {
        return this.powerComp;
    }

    public Entity getEntity() {
        return this.entity;
    }

    // ========== Origin Management ==========

    public void setOrigin(String layerId, String originId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        Origin origin = OriginRegistry.get(new ResourceLocation(originId));
        if (layer != null && origin != null) {
            this.originComp.setOrigin(layer, origin);
        } else {
            OriginsJS.LOGGER.warn("[OriginsJS] Cannot set origin: layer={}, origin={}", layerId, originId);
        }
    }

    public void clearOrigin(String layerId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        if (layer != null) {
            this.originComp.setOrigin(layer, Origin.EMPTY);
        }
    }

    public boolean hasOrigin(String originId) {
        Origin origin = OriginRegistry.get(new ResourceLocation(originId));
        if (origin == null) return false;
        return this.originComp.getOrigins().containsValue(origin);
    }

    public boolean hasOriginInLayer(String layerId, String originId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        Origin origin = OriginRegistry.get(new ResourceLocation(originId));
        if (layer == null || origin == null) return false;
        return origin.equals(this.originComp.getOrigin(layer));
    }

    public boolean hasLayer(String layerId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        if (layer == null) return false;
        return this.originComp.hasOrigin(layer);
    }

    public String getOriginId(String layerId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        if (layer == null) return null;
        Origin origin = this.originComp.getOrigin(layer);
        if (origin == null || origin == Origin.EMPTY) return null;
        return origin.getIdentifier().toString();
    }

    public List<List<String>> getAllOrigins() {
        List<List<String>> result = new ArrayList<>();
        for (Map.Entry<OriginLayer, Origin> entry : this.originComp.getOrigins().entrySet()) {
            String layerId = entry.getKey().getIdentifier().toString();
            String originId = entry.getValue().getIdentifier().toString();
            result.add(List.of(layerId, originId));
        }
        return result;
    }

    public boolean hasAllOrigins() {
        return this.originComp.hasAllOrigins();
    }

    // ========== Power Management (via PowerHolderComponent) ==========

    public void grantPower(String source, String powerId) {
        if (this.powerComp == null) return;
        PowerType<?> type = PowerTypeRegistry.get(new ResourceLocation(powerId));
        if (type == null) {
            OriginsJS.LOGGER.warn("[OriginsJS] Unknown power type: {}", powerId);
            return;
        }
        ResourceLocation sourceRL = new ResourceLocation(source);
        try {
            this.powerComp.addPower(type, sourceRL);
        } catch (Exception e) {
            OriginsJS.LOGGER.error("[OriginsJS] Failed to grant power '{}' from source '{}'", powerId, source, e);
        }
    }

    public void revokePower(String source, String powerId) {
        if (this.powerComp == null) return;
        PowerType<?> type = PowerTypeRegistry.get(new ResourceLocation(powerId));
        if (type == null) return;
        ResourceLocation sourceRL = new ResourceLocation(source);
        try {
            this.powerComp.removePower(type, sourceRL);
        } catch (Exception e) {
            OriginsJS.LOGGER.error("[OriginsJS] Failed to revoke power '{}' from source '{}'", powerId, source, e);
        }
    }

    public void revokeAllPowers(String source) {
        if (this.powerComp == null) return;
        ResourceLocation sourceRL = new ResourceLocation(source);
        for (PowerType<?> type : new ArrayList<>(this.powerComp.getPowerTypes(false))) {
            try {
                this.powerComp.removePower(type, sourceRL);
            } catch (Exception ignored) {
            }
        }
    }

    public boolean hasPower(String powerId) {
        if (this.powerComp == null) return false;
        PowerType<?> type = PowerTypeRegistry.get(new ResourceLocation(powerId));
        return type != null && this.powerComp.hasPower(type);
    }

    public List<String> getAllPowerIds() {
        if (this.powerComp == null) return List.of();
        List<String> result = new ArrayList<>();
        for (PowerType<?> type : this.powerComp.getPowerTypes(false)) {
            result.add(type.getIdentifier().toString());
        }
        return result;
    }

    // ========== Resource ==========

    public int getResource(String powerId) {
        return 0;
    }

    public void setResource(String powerId, int value) {
        OriginsJS.LOGGER.debug("[OriginsJS] setResource not directly supported in 1.20.1");
    }

    public void addResource(String powerId, int delta) {
        OriginsJS.LOGGER.debug("[OriginsJS] addResource not directly supported in 1.20.1");
    }

    // ========== Cooldown ==========

    public int getCooldown(String powerId) {
        return 0;
    }

    public void startCooldown(String powerId) {
        OriginsJS.LOGGER.debug("[OriginsJS] startCooldown not directly supported in 1.20.1");
    }

    public boolean canUseCooldown(String powerId) {
        return false;
    }
}
