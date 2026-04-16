package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class ScpItems {

    public static Item SULFUR_SPIDER_SPAWN_EGG;

    public static void register() {
        Identifier id = Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "sulfur_spider_spawn_egg");
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id);
        SULFUR_SPIDER_SPAWN_EGG = Registry.register(
                BuiltInRegistries.ITEM,
                id,
                new SpawnEggItem(new Item.Properties().setId(key).spawnEgg(ScpEntities.SULFUR_SPIDER))
        );

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.SPAWN_EGGS).register(entries ->
                entries.accept(SULFUR_SPIDER_SPAWN_EGG));

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_COAL_ORE.asItem()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_REDSTONE_ORE.asItem()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_LAPIS_ORE.asItem()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_DIAMOND_ORE.asItem()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_COPPER_ORE.asItem()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_EMERALD_ORE.asItem()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_IRON_ORE.asItem()));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries ->
                entries.accept(ScpBlocks.SULFUR_GOLD_ORE.asItem()));
    }

    private ScpItems() {}
}
