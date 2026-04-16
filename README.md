**Sulfur Caves Plus** breathes life into the Sulfur Caves biome introduced in Minecraft 26.2.
It adds a **new status effect**, **Sulfur Spiders**, **Corrosion potions**, and **Sulfur Ore variants** that generate naturally inside sulfur caves terrain.

---

## Corrosion Effect

A new **harmful status effect** that breaks your armor over time.

- Damages the durability of every piece of worn armor
- **Unbreaking** enchantment partially resists the damage (higher levels = higher block chance)

---

## Sulfur Spider

![Sulfur Spider](https://cdn.modrinth.com/data/cached_images/f438a3dfad58cb9ffa8a719bd653945a61d26e6f.png)

A venomous spider variant that lurks exclusively in the Sulfur Caves biome.
It carries a **sulfur spike on its tail** and injects **Corrosion** on every bite.

| Difficulty | Corrosion Duration | Also applies |
|---|---|---|
| Easy | - | Nausea |
| Normal | **7s** | Nausea |
| Hard | **14s** | Nausea |

---

## Potions of Corrosion

Three new potions:

| Recipe | Result |
|---|---|
| Awkward Potion + Sulfur | **Potion of Corrosion** |
| Potion of Corrosion + Redstone | **Potion of Corrosion (Extended)** |
| Potion of Corrosion + Glowstone Dust | **Potion of Corrosion (Strong)** |

---

## Sulfur Ore Variants

Eight new ore blocks generate inside **sulfur** terrain.

---

## 🔧 Configuration

A `sulfurcavesplus.json` config file is generated in your config folder on first launch.

| Option | Default | Description |
|---|---|---|
| `corrosionTickInterval` | `20` | Ticks between each armor damage tick |
| `durabilityDamagePerTick` | `1` | Durability removed per interval |
| `spiderCorrosionDurationEasy` | `0` | Corrosion ticks applied on Easy |
| `spiderCorrosionDurationNormal` | `100` | Corrosion ticks applied on Normal |
| `spiderCorrosionDurationHard` | `200` | Corrosion ticks applied on Hard |
| `potentSulfurEffectDuration` | `200` | Duration of Potent Sulfur block effect |

---

## 📦 Requirements

- **Minecraft** 26.2-snapshot-3 (Java Edition)
- **Fabric Loader** 0.16+
- **Fabric API**
