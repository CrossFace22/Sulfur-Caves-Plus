package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScpItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, SulfurCavesPlus.MOD_ID);

    public static final DeferredHolder<Item, BlockItem> SULFUR_COAL_ORE = registerBlockItem("sulfur_coal_ore", ScpBlocks.SULFUR_COAL_ORE);
    public static final DeferredHolder<Item, BlockItem> SULFUR_IRON_ORE = registerBlockItem("sulfur_iron_ore", ScpBlocks.SULFUR_IRON_ORE);
    public static final DeferredHolder<Item, BlockItem> SULFUR_GOLD_ORE = registerBlockItem("sulfur_gold_ore", ScpBlocks.SULFUR_GOLD_ORE);
    public static final DeferredHolder<Item, BlockItem> SULFUR_COPPER_ORE = registerBlockItem("sulfur_copper_ore", ScpBlocks.SULFUR_COPPER_ORE);
    public static final DeferredHolder<Item, BlockItem> SULFUR_REDSTONE_ORE = registerBlockItem("sulfur_redstone_ore", ScpBlocks.SULFUR_REDSTONE_ORE);
    public static final DeferredHolder<Item, BlockItem> SULFUR_LAPIS_ORE = registerBlockItem("sulfur_lapis_ore", ScpBlocks.SULFUR_LAPIS_ORE);
    public static final DeferredHolder<Item, BlockItem> SULFUR_DIAMOND_ORE = registerBlockItem("sulfur_diamond_ore", ScpBlocks.SULFUR_DIAMOND_ORE);
    public static final DeferredHolder<Item, BlockItem> SULFUR_EMERALD_ORE = registerBlockItem("sulfur_emerald_ore", ScpBlocks.SULFUR_EMERALD_ORE);

    public static final DeferredHolder<Item, BlockItem> CINNABAR_COAL_ORE = registerBlockItem("cinnabar_coal_ore", ScpBlocks.CINNABAR_COAL_ORE);
    public static final DeferredHolder<Item, BlockItem> CINNABAR_IRON_ORE = registerBlockItem("cinnabar_iron_ore", ScpBlocks.CINNABAR_IRON_ORE);
    public static final DeferredHolder<Item, BlockItem> CINNABAR_GOLD_ORE = registerBlockItem("cinnabar_gold_ore", ScpBlocks.CINNABAR_GOLD_ORE);
    public static final DeferredHolder<Item, BlockItem> CINNABAR_COPPER_ORE = registerBlockItem("cinnabar_copper_ore", ScpBlocks.CINNABAR_COPPER_ORE);
    public static final DeferredHolder<Item, BlockItem> CINNABAR_REDSTONE_ORE = registerBlockItem("cinnabar_redstone_ore", ScpBlocks.CINNABAR_REDSTONE_ORE);
    public static final DeferredHolder<Item, BlockItem> CINNABAR_LAPIS_ORE = registerBlockItem("cinnabar_lapis_ore", ScpBlocks.CINNABAR_LAPIS_ORE);
    public static final DeferredHolder<Item, BlockItem> CINNABAR_DIAMOND_ORE = registerBlockItem("cinnabar_diamond_ore", ScpBlocks.CINNABAR_DIAMOND_ORE);
    public static final DeferredHolder<Item, BlockItem> CINNABAR_EMERALD_ORE = registerBlockItem("cinnabar_emerald_ore", ScpBlocks.CINNABAR_EMERALD_ORE);

    public static final DeferredHolder<Item, SpawnEggItem> SULFUR_SPIDER_SPAWN_EGG = ITEMS.register(
            "sulfur_spider_spawn_egg",
            registryName -> new SpawnEggItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))
                    .spawnEgg(ScpEntities.SULFUR_SPIDER.get()))
    );

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }

    public static void addCreativeTabEntries(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(SULFUR_SPIDER_SPAWN_EGG.get());
        }

        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            accept(event, ScpBlocks.SULFUR_COAL_ORE.get());
            accept(event, ScpBlocks.SULFUR_REDSTONE_ORE.get());
            accept(event, ScpBlocks.SULFUR_LAPIS_ORE.get());
            accept(event, ScpBlocks.SULFUR_DIAMOND_ORE.get());
            accept(event, ScpBlocks.SULFUR_COPPER_ORE.get());
            accept(event, ScpBlocks.SULFUR_EMERALD_ORE.get());
            accept(event, ScpBlocks.SULFUR_IRON_ORE.get());
            accept(event, ScpBlocks.SULFUR_GOLD_ORE.get());

            accept(event, ScpBlocks.CINNABAR_COAL_ORE.get());
            accept(event, ScpBlocks.CINNABAR_REDSTONE_ORE.get());
            accept(event, ScpBlocks.CINNABAR_LAPIS_ORE.get());
            accept(event, ScpBlocks.CINNABAR_DIAMOND_ORE.get());
            accept(event, ScpBlocks.CINNABAR_COPPER_ORE.get());
            accept(event, ScpBlocks.CINNABAR_EMERALD_ORE.get());
            accept(event, ScpBlocks.CINNABAR_IRON_ORE.get());
            accept(event, ScpBlocks.CINNABAR_GOLD_ORE.get());
        }
    }

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredHolder<net.minecraft.world.level.block.Block, ? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(name, registryName -> new BlockItem(
                block.get(),
                new Item.Properties()
                        .setId(ResourceKey.create(Registries.ITEM, registryName))
                        .useBlockDescriptionPrefix()
        ));
    }

    private static void accept(BuildCreativeModeTabContentsEvent event, ItemLike item) {
        event.accept(item);
    }

    private ScpItems() {}
}
