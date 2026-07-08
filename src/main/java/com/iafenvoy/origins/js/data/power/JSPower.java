package com.iafenvoy.origins.js.data.power;

import com.google.gson.JsonObject;
import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.util.JSUtil;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class JSPower extends Power {
    private static final Map<String, Map<String, BiConsumer<LivingEntity, JsonObject>>> CALLBACKS = new ConcurrentHashMap<>();
    private static final Map<String, BiPredicate<LivingEntity, JsonObject>> IS_ACTIVE = new ConcurrentHashMap<>();

    private final String callbackId;
    private final JsonObject params;

    public JSPower(PowerType<?> type, LivingEntity entity, String callbackId, JsonObject params) {
        super(type, entity);
        this.callbackId = callbackId;
        this.params = params;
    }

    public String getCallbackId() {
        return callbackId;
    }

    public JsonObject getParams() {
        return params;
    }

    @Override
    public void onGained() {
        Map<String, BiConsumer<LivingEntity, JsonObject>> m = CALLBACKS.get(this.callbackId);
        BiConsumer<LivingEntity, JsonObject> cb = m != null ? m.get("grant") : null;
        if (cb != null) {
            try {
                cb.accept(this.entity, this.params);
            } catch (Exception e) {
                OriginsJS.LOGGER.error("[OriginsJS] Error in JS power '{}' grant", this.callbackId, e);
            }
        }
    }

    @Override
    public void onLost() {
        Map<String, BiConsumer<LivingEntity, JsonObject>> m = CALLBACKS.get(this.callbackId);
        BiConsumer<LivingEntity, JsonObject> cb = m != null ? m.get("revoke") : null;
        if (cb != null) {
            try {
                cb.accept(this.entity, this.params);
            } catch (Exception e) {
                OriginsJS.LOGGER.error("[OriginsJS] Error in JS power '{}' revoke", this.callbackId, e);
            }
        }
    }

    @Override
    public void tick() {
        Map<String, BiConsumer<LivingEntity, JsonObject>> m = CALLBACKS.get(this.callbackId);
        BiConsumer<LivingEntity, JsonObject> cb = m != null ? m.get("tick") : null;
        if (cb != null) {
            try {
                cb.accept(this.entity, this.params);
            } catch (Exception e) {
                OriginsJS.LOGGER.error("[OriginsJS] Error in JS power '{}' tick", this.callbackId, e);
            }
        }
    }

    @Override
    public boolean isActive() {
        BiPredicate<LivingEntity, JsonObject> cb = IS_ACTIVE.get(this.callbackId);
        if (cb != null) {
            try {
                return cb.test(this.entity, this.params);
            } catch (Exception e) {
                OriginsJS.LOGGER.error("[OriginsJS] Error in JS power '{}' isActive", this.callbackId, e);
            }
        }
        return super.isActive();
    }

    /**
     * Called when the power's active key is pressed (not an override in 1.20.1).
     * Call this externally for key-binding based active powers.
     */
    public void onActive() {
        Map<String, BiConsumer<LivingEntity, JsonObject>> m = CALLBACKS.get(this.callbackId);
        BiConsumer<LivingEntity, JsonObject> cb = m != null ? m.get("active") : null;
        if (cb != null) {
            try {
                cb.accept(this.entity, this.params);
            } catch (Exception e) {
                OriginsJS.LOGGER.error("[OriginsJS] Error in JS power '{}' active", this.callbackId, e);
            }
        }
    }

    // Note: 1.20.1 Apoli doesn't have a direct "inactive" callback like 1.21.1.
    // We use onLost as the closest equivalent, but also allow explicit call.

    /**
     * Called when the power becomes inactive (key released).
     * Not a direct override in 1.20.1; call manually from key handling.
     */
    public void onInactive() {
        Map<String, BiConsumer<LivingEntity, JsonObject>> m = CALLBACKS.get(this.callbackId);
        BiConsumer<LivingEntity, JsonObject> cb = m != null ? m.get("inactive") : null;
        if (cb != null) {
            try {
                cb.accept(this.entity, this.params);
            } catch (Exception e) {
                OriginsJS.LOGGER.error("[OriginsJS] Error in JS power '{}' inactive", this.callbackId, e);
            }
        }
    }

    // ========== Factory ==========

    public static final PowerFactory<JSPower> FACTORY = new PowerFactory<>(
            new ResourceLocation(OriginsJS.MOD_ID, "js_power"),
            new SerializableData()
                    .add("id", SerializableDataTypes.STRING)
                    .add("params", JSUtil.JSON_OBJECT, new JsonObject()),
            data -> (PowerType<JSPower> type, LivingEntity entity) -> new JSPower(type, entity,
                    data.getString("id"),
                    (JsonObject) data.get("params"))
    ).allowCondition();

    // ========== Builder / Registration ==========

    private static void putCallback(String id, String type, BiConsumer<LivingEntity, JsonObject> cb) {
        CALLBACKS.computeIfAbsent(id, k -> new ConcurrentHashMap<>()).put(type, cb);
    }

    public static void clear() {
        CALLBACKS.clear();
        IS_ACTIVE.clear();
    }

    public static PowerBuilder forId(String id) {
        return new PowerBuilder(id);
    }

    @SuppressWarnings("unused")
    public static final class PowerBuilder {
        private final String id;
        private BiConsumer<LivingEntity, JsonObject> grant, revoke, tick, active, inactive;
        private BiPredicate<LivingEntity, JsonObject> isActive;

        private PowerBuilder(String id) {
            this.id = id;
        }

        public PowerBuilder grant(BiConsumer<LivingEntity, JsonObject> cb) {
            this.grant = cb;
            return this;
        }

        public PowerBuilder revoke(BiConsumer<LivingEntity, JsonObject> cb) {
            this.revoke = cb;
            return this;
        }

        public PowerBuilder tick(BiConsumer<LivingEntity, JsonObject> cb) {
            this.tick = cb;
            return this;
        }

        public PowerBuilder active(BiConsumer<LivingEntity, JsonObject> cb) {
            this.active = cb;
            return this;
        }

        public PowerBuilder inactive(BiConsumer<LivingEntity, JsonObject> cb) {
            this.inactive = cb;
            return this;
        }

        public PowerBuilder isActive(BiPredicate<LivingEntity, JsonObject> cb) {
            this.isActive = cb;
            return this;
        }

        public void register() {
            if (this.grant != null) putCallback(this.id, "grant", this.grant);
            if (this.revoke != null) putCallback(this.id, "revoke", this.revoke);
            if (this.tick != null) putCallback(this.id, "tick", this.tick);
            if (this.active != null) putCallback(this.id, "active", this.active);
            if (this.inactive != null) putCallback(this.id, "inactive", this.inactive);
            if (this.isActive != null) IS_ACTIVE.put(this.id, this.isActive);
            OriginsJS.LOGGER.info("[OriginsJS] Registered power: {}", this.id);
        }
    }
}
