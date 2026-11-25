# Origins JS (Legacy)

This is an **official 1.20.1 backport**
of [Origins JS](https://github.com/IAFEnvoy/Origins-JS) ([CurseForge](https://www.curseforge.com/projects/1538032) | [Modrinth](https://modrinth.com/project/zDlbRi3N)) —
a KubeJS integration for Origins (NeoForge), allowing you to manage origins and powers via JavaScript.

## Features

- **Origin &amp; Power Management** — Grant, revoke, and check origins/powers from JS scripts.
- **Custom Actions** — Define `EntityAction`, `BlockAction`, `ItemAction`, `BiEntityAction` in JavaScript.
- **Custom Conditions** — Define `EntityCondition`, `BlockCondition`, `ItemCondition`, `BiEntityCondition`,
  `BiomeCondition`, `DamageCondition`, `FluidCondition` in JavaScript.
- **Custom Powers** — Create fully scripted Power types with JS callbacks (`grant`, `revoke`, `tick`, `active`,
  `inactive`).
- **Data-Driven** — All custom types can be referenced in JSON datapacks using the `origins_js:` namespace.
- **Parameter Support** — Pass custom parameters from JSON to your JS callbacks for dynamic behavior.

## Dependencies

- **Minecraft 1.20.1**
- [KubeJS](https://modrinth.com/mod/kubejs)
- **Origins 1.20.1** — [Fabric](https://modrinth.com/mod/origins) or [Forge](https://modrinth.com/mod/origins) variant

## Quick Start

### 1. Install all dependencies, then place your `.js` scripts in `kubejs/server_scripts/`.

### 2. Basic Usage

```js
// Register a custom entity action
OriginsJS.registerEntityAction("heal_half", (entity, params) => {
    if (entity.isLiving()) {
        entity.heal(entity.getMaxHealth() / 2);
    }
});

// Create a custom power
OriginsJS.powerBuilder("regeneration_power")
    .tick((entity, params) => {
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(0.5);
        }
    })
    .register();
```

Create `kubejs/server_scripts/origins.js`:

```js
// Set avian origins by default
PlayerEvents.loggedIn(event => {
    let holder = OriginsJS.getHolder(event.player);
    if (holder && !holder.hasAllOrigins()) {
        holder.setOrigin("origins:origin", "origins:avian");
    }
});
```

### 3. Reference in JSON Datapacks

```json
{
  "type": "origins_js:js_entity_action",
  "id": "heal_half"
}
```

See [examples](https://docs.iafenvoy.com/docs/mod/origins-js/examples/js-powers) for more examples including custom actions, conditions, and powers.

## Documentation

- [API Reference](https://docs.iafenvoy.com/docs/mod/origins-js/api/global-api/) — full type table and static shortcut methods
- [Examples](https://docs.iafenvoy.com/docs/mod/origins-js/examples/js-powers) — code examples for origins/powers, custom actions, conditions, and powers
