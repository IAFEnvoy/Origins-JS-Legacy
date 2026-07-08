package com.iafenvoy.origins.js.registry;

import com.iafenvoy.origins.js.OriginsJS;

public final class OJSActions {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void register() {
        // On Forge 1.20.1, actions are registered through the edwinmindcraft
        // ApoliRegistries which is handled by the shared code's init sequence.
        // The action classes themselves use apace100.ActionFactory which exists
        // in the Forge Apoli jar as a compatibility wrapper.
        OriginsJS.LOGGER.info("[OriginsJS] Forge actions registered via edwinmindcraft bridge");
    }
}
