package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RedStoneOreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class ScpBlocks {

    public static Block SULFUR_COAL_ORE;
    public static Block SULFUR_IRON_ORE;
    public static Block SULFUR_GOLD_ORE;
    public static Block SULFUR_COPPER_ORE;
    public static Block SULFUR_REDSTONE_ORE;
    public static Block SULFUR_DIAMOND_ORE;
    public static Block SULFUR_LAPIS_ORE;
    public static Block SULFUR_EMERALD_ORE;

    public static void register() {
        SULFUR_COAL_ORE     = ore("sulfur_coal_ore",     UniformInt.of(0, 2),  Blocks.COAL_ORE);
        SULFUR_IRON_ORE     = ore("sulfur_iron_ore",     ConstantInt.of(0),    Blocks.IRON_ORE);
        SULFUR_GOLD_ORE     = ore("sulfur_gold_ore",     ConstantInt.of(0),    Blocks.GOLD_ORE);
        SULFUR_COPPER_ORE   = ore("sulfur_copper_ore",   ConstantInt.of(0),    Blocks.COPPER_ORE);
        SULFUR_REDSTONE_ORE = redstoneOre("sulfur_redstone_ore");
        SULFUR_LAPIS_ORE    = ore("sulfur_lapis_ore",    UniformInt.of(1, 5),  Blocks.LAPIS_ORE);
        SULFUR_DIAMOND_ORE  = ore("sulfur_diamond_ore",  UniformInt.of(3, 7),  Blocks.DIAMOND_ORE);
        SULFUR_EMERALD_ORE  = ore("sulfur_emerald_ore",  UniformInt.of(3, 7),  Blocks.EMERALD_ORE);
    }

    private static Block ore(String name, IntProvider xp, Block baseOre) {
        Identifier id = Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        ResourceKey<Item>  itemKey  = ResourceKey.create(Registries.ITEM,  id);

        Block block = Registry.register(
                BuiltInRegistries.BLOCK, id,
                new DropExperienceBlock(xp,
                        BlockBehaviour.Properties
                                .ofLegacyCopy(baseOre)
                                .setId(blockKey)
                                .mapColor(MapColor.COLOR_YELLOW)
                                .strength(3.5F, 6.0F)
                                .sound(SoundType.SULFUR)
                )
        );

        Registry.register(
                BuiltInRegistries.ITEM, id,
                new BlockItem(block, new Item.Properties().setId(itemKey))
        );

        return block;
    }

    private static Block redstoneOre(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, name);
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, id);
        ResourceKey<Item>  itemKey  = ResourceKey.create(Registries.ITEM,  id);

        Block block = Registry.register(
                BuiltInRegistries.BLOCK, id,
                new RedStoneOreBlock(
                        BlockBehaviour.Properties
                                .ofLegacyCopy(Blocks.REDSTONE_ORE)
                                .setId(blockKey)
                                .mapColor(MapColor.COLOR_YELLOW)
                                .strength(3.5F, 6.0F)
                                .sound(SoundType.SULFUR)
                )
        );

        Registry.register(
                BuiltInRegistries.ITEM, id,
                new BlockItem(block, new Item.Properties().setId(itemKey))
        );

        return block;
    }

    private ScpBlocks() {}
}
