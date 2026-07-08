package com.iafenvoy.origins.js.binding;

import com.iafenvoy.origins.js.OriginsJS;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class HolderWrapper {
    private final OriginComponent originComp;
    private final IPowerContainer powerComp;
    private final Entity entity;

    public HolderWrapper(OriginComponent originComp, IPowerContainer powerComp, Entity entity) {
        this.originComp = originComp;
        this.powerComp = powerComp;
        this.entity = entity;
    }

    public OriginComponent getOriginComponent() { return this.originComp; }
    public IPowerContainer getPowerComponent() { return this.powerComp; }
    public Entity getEntity() { return this.entity; }

    public void setOrigin(String layerId, String originId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        Origin origin = OriginRegistry.get(new ResourceLocation(originId));
        if (layer != null && origin != null) this.originComp.setOrigin(layer, origin);
        else OriginsJS.LOGGER.warn("[OriginsJS] Cannot set origin: layer={}, origin={}", layerId, originId);
    }

    public void clearOrigin(String layerId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        if (layer != null) this.originComp.setOrigin(layer, Origin.EMPTY);
    }

    public boolean hasOrigin(String originId) {
        Origin origin = OriginRegistry.get(new ResourceLocation(originId));
        return origin != null && this.originComp.getOrigins().containsValue(origin);
    }

    public boolean hasOriginInLayer(String layerId, String originId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        Origin origin = OriginRegistry.get(new ResourceLocation(originId));
        if (layer == null || origin == null) return false;
        return origin.equals(this.originComp.getOrigin(layer));
    }

    public boolean hasLayer(String layerId) {
        OriginLayer layer = OriginLayers.getLayer(new ResourceLocation(layerId));
        return layer != null && this.originComp.hasOrigin(layer);
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
        for (Map.Entry<OriginLayer, Origin> e : this.originComp.getOrigins().entrySet())
            result.add(List.of(e.getKey().getIdentifier().toString(), e.getValue().getIdentifier().toString()));
        return result;
    }

    public boolean hasAllOrigins() { return this.originComp.hasAllOrigins(); }

    // Power Management — Forge IPowerContainer uses ResourceLocation directly
    public void grantPower(String source, String powerId) {
        if (powerComp == null) return;
        try { powerComp.addPower(new ResourceLocation(powerId), new ResourceLocation(source)); }
        catch (Exception e) { OriginsJS.LOGGER.error("[OriginsJS] grantPower failed: {}", powerId, e); }
    }

    public void revokePower(String source, String powerId) {
        if (powerComp == null) return;
        try { powerComp.removePower(new ResourceLocation(powerId), new ResourceLocation(source)); }
        catch (Exception e) { OriginsJS.LOGGER.error("[OriginsJS] revokePower failed: {}", powerId, e); }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void revokeAllPowers(String source) {
        if (powerComp == null) return;
        ResourceLocation src = new ResourceLocation(source);
        for (Object key : new ArrayList(powerComp.getPowerNames()))
            try { powerComp.removePower((net.minecraft.resources.ResourceKey) key, src); } catch (Exception ignored) {}
    }

    public boolean hasPower(String powerId) {
        return powerComp != null && powerComp.hasPower(new ResourceLocation(powerId));
    }

    public List<String> getAllPowerIds() {
        if (powerComp == null) return List.of();
        List<String> r = new ArrayList<>();
        for (var k : powerComp.getPowerNames()) r.add(k.location().toString());
        return r;
    }

    public int getResource(String powerId) { return 0; }
    public void setResource(String powerId, int value) {}
    public void addResource(String powerId, int delta) {}
    public int getCooldown(String powerId) { return 0; }
    public void startCooldown(String powerId) {}
    public boolean canUseCooldown(String powerId) { return false; }
}
