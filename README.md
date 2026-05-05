**Sulfur Caves Plus** breathes life into the Sulfur Caves biome introduced in Minecraft 26.2.
It adds a **new status effect**, **Sulfur Spiders**, **Corrosion potions**, volcanic **Lava Geysers**, and **more ore variants** that generate naturally inside sulfur caves terrain.

---

## ![Corrosion Effect](https://cdn.modrinth.com/data/cached_images/8707750c8e830d8fbbc650972e9683484859563c.png) Corrosion Effect

A new **harmful status effect** that breaks your armor over time.

- Damages the durability of every piece of worn armor
- **Unbreaking** enchantment partially resists the damage (higher levels = higher block chance)

---

## ![Sulfur Spider](https://cdn.modrinth.com/data/cached_images/bcbf356592f42358d98d260c78ce4091b9fa07b5.png) Sulfur Spider

A venomous spider variant that lurks exclusively in the Sulfur Caves biome.
It carries a **sulfur spike on its tail** and injects **Corrosion** on every bite.

| Difficulty | Corrosion Duration | Also applies |
|---|---|---|
| Easy | - | Nausea |
| Normal | **7s** | Nausea |
| Hard | **14s** | Nausea |

---

## ![Corrosion Potion](https://cdn.modrinth.com/data/cached_images/9675dbe49bf9161a6e3ebaf3ec39db3500188a7f.png) Potions of Corrosion

Three new potions:

| Recipe | Result |
|---|---|
| Awkward Potion + Sulfur | **Potion of Corrosion** |
| Potion of Corrosion + Redstone | **Potion of Corrosion (Extended)** |
| Potion of Corrosion + Glowstone Dust | **Potion of Corrosion (Strong)** |

---

## ![Sulfur Geyser](https://cdn.modrinth.com/data/cached_images/931c91ab59173eed395bbf4ac8f2662327f80333_0.webp) Sulfur Geysers

Sulfur Geysers are now part of **Vanilla Minecraft**.

**Sulfur Caves Plus enhances their behavior:**
- When powered by **Redstone**, geysers remain active indefinitely.
- Geysers no longer emit noxious gas or apply negative effects by default, focusing purely on their propulsion behavior.
- Noxious gas particles and negative effects can be restored with a gamerule.

### Lava Geysers

A volcanic geyser variant created when **Potent Sulfur** has **lava above it** and a **Magma Block below it**.

Lava Geysers:
- Launch entities upward like regular geysers.
- Deal fire damage and ignite entities.
- Emit dark volcanic particles with subtle ashy-orange magma tones.
- Spawn lava pops that scale with the geyser height.
- Generate naturally in **Basalt Deltas**.

---

## ![Cinnabar Diamond Ore](https://cdn.modrinth.com/data/cached_images/8c6bbbf25b1e91f04f77aa044853089ab90a1ae6_0.webp) Sulfur & Cinnabar Ore Variants

**16 new ore blocks** generate inside Sulfur Caves terrain.

---

## WANT MORE?

Expand your *Chaos Cubed* experience with **Better Sulfur Cubes**
-> https://modrinth.com/mod/better-sulfur-cubes

---

## Configuration

A `sulfurcavesplus.json` config file is generated in your config folder on first launch.

| Option | Default | Description |
|---|---|---|
| `corrosionTickInterval` | `20` | Ticks between each armor damage tick |
| `durabilityDamagePerTick` | `1` | Durability removed per interval |
| `spiderCorrosionDurationEasy` | `0` | Corrosion ticks applied on Easy |
| `spiderCorrosionDurationNormal` | `100` | Corrosion ticks applied on Normal |
| `spiderCorrosionDurationHard` | `200` | Corrosion ticks applied on Hard |
| `potentSulfurEffectDuration` | `200` | Duration of Potent Sulfur block effect |
| `sulfurVeinsGenerateUnderLava` | `true` | Enables natural Lava Geyser generation in Basalt Deltas |

### Gamerules

| Gamerule | Default | Description |
|---|---|---|
| `sulfurcavesplus:noxious_gas_on_geysers` | `false` | Enables noxious gas particles and negative effects on geysers |
| `sulfurcavesplus:lava_geysers_damage` | `true` | Enables fire damage and ignition from Lava Geysers |

---

## Requirements

- **Minecraft** 26.2-snapshot-6
- **Fabric Loader** >= 0.19.2
- **Fabric API**

## Found a bug?

-> [Issues Page](https://github.com/CrossFace22/Sulfur-Caves-Plus/issues)
