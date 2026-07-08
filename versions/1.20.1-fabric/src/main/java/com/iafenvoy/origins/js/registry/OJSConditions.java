package com.iafenvoy.origins.js.registry;

import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.data.condition.*;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.core.Registry;

public final class OJSConditions {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_CONDITION,
                JSEntityCondition.FACTORY.getSerializerId(),
                JSEntityCondition.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered entity condition: js_entity_condition");

        Registry.register(ApoliRegistries.BLOCK_CONDITION,
                JSBlockCondition.FACTORY.getSerializerId(),
                JSBlockCondition.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered block condition: js_block_condition");

        Registry.register(ApoliRegistries.ITEM_CONDITION,
                JSItemCondition.FACTORY.getSerializerId(),
                JSItemCondition.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered item condition: js_item_condition");

        Registry.register(ApoliRegistries.BIENTITY_CONDITION,
                JSBiEntityCondition.FACTORY.getSerializerId(),
                (ConditionFactory) JSBiEntityCondition.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered bi-entity condition: js_bientity_condition");

        Registry.register(ApoliRegistries.BIOME_CONDITION,
                JSBiomeCondition.FACTORY.getSerializerId(),
                JSBiomeCondition.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered biome condition: js_biome_condition");

        Registry.register(ApoliRegistries.DAMAGE_CONDITION,
                JSDamageCondition.FACTORY.getSerializerId(),
                (ConditionFactory) JSDamageCondition.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered damage condition: js_damage_condition");

        Registry.register(ApoliRegistries.FLUID_CONDITION,
                JSFluidCondition.FACTORY.getSerializerId(),
                JSFluidCondition.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered fluid condition: js_fluid_condition");
    }
}
