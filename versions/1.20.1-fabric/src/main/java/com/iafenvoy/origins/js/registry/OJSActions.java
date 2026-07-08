package com.iafenvoy.origins.js.registry;

import com.iafenvoy.origins.js.OriginsJS;
import com.iafenvoy.origins.js.data.action.JSBiEntityAction;
import com.iafenvoy.origins.js.data.action.JSBlockAction;
import com.iafenvoy.origins.js.data.action.JSEntityAction;
import com.iafenvoy.origins.js.data.action.JSItemAction;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.core.Registry;

public final class OJSActions {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        Registry.register(ApoliRegistries.ENTITY_ACTION,
                JSEntityAction.FACTORY.getSerializerId(),
                JSEntityAction.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered entity action: js_entity_action");

        Registry.register(ApoliRegistries.BLOCK_ACTION,
                JSBlockAction.FACTORY.getSerializerId(),
                JSBlockAction.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered block action: js_block_action");

        Registry.register(ApoliRegistries.ITEM_ACTION,
                JSItemAction.FACTORY.getSerializerId(),
                (ActionFactory) JSItemAction.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered item action: js_item_action");

        Registry.register(ApoliRegistries.BIENTITY_ACTION,
                JSBiEntityAction.FACTORY.getSerializerId(),
                (ActionFactory) JSBiEntityAction.FACTORY);
        OriginsJS.LOGGER.info("[OriginsJS] Registered bi-entity action: js_bientity_action");
    }
}
