package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScpBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, SulfurCavesPlus.MOD_ID);

    public static final DeferredHolder<Block, Block> SULFUR_COAL_ORE = sulfurOre("sulfur_coal_ore", UniformInt.of(0, 2), Blocks.COAL_ORE);
    public static final DeferredHolder<Block, Block> SULFUR_IRON_ORE = sulfurOre("sulfur_iron_ore", ConstantInt.of(0), Blocks.IRON_ORE);
    public static final DeferredHolder<Block, Block> SULFUR_GOLD_ORE = sulfurOre("sulfur_gold_ore", ConstantInt.of(0), Blocks.GOLD_ORE);
    public static final DeferredHolder<Block, Block> SULFUR_COPPER_ORE = sulfurOre("sulfur_copper_ore", ConstantInt.of(0), Blocks.COPPER_ORE);
    public static final DeferredHolder<Block, Block> SULFUR_REDSTONE_ORE = sulfurRedstoneOre("sulfur_redstone_ore");
    public static final DeferredHolder<Block, Block> SULFUR_LAPIS_ORE = sulfurOre("sulfur_lapis_ore", UniformInt.of(1, 5), Blocks.LAPIS_ORE);
    public static final DeferredHolder<Block, Block> SULFUR_DIAMOND_ORE = sulfurOre("sulfur_diamond_ore", UniformInt.of(3, 7), Blocks.DIAMOND_ORE);
    public static final DeferredHolder<Block, Block> SULFUR_EMERALD_ORE = sulfurOre("sulfur_emerald_ore", UniformInt.of(3, 7), Blocks.EMERALD_ORE);

    public static final DeferredHolder<Block, Block> CINNABAR_COAL_ORE = cinnabarOre("cinnabar_coal_ore", UniformInt.of(0, 2), Blocks.COAL_ORE);
    public static final DeferredHolder<Block, Block> CINNABAR_IRON_ORE = cinnabarOre("cinnabar_iron_ore", ConstantInt.of(0), Blocks.IRON_ORE);
    public static final DeferredHolder<Block, Block> CINNABAR_GOLD_ORE = cinnabarOre("cinnabar_gold_ore", ConstantInt.of(0), Blocks.GOLD_ORE);
    public static final DeferredHolder<Block, Block> CINNABAR_COPPER_ORE = cinnabarOre("cinnabar_copper_ore", ConstantInt.of(0), Blocks.COPPER_ORE);
    public static final DeferredHolder<Block, Block> CINNABAR_REDSTONE_ORE = cinnabarRedstoneOre("cinnabar_redstone_ore");
    public static final DeferredHolder<Block, Block> CINNABAR_LAPIS_ORE = cinnabarOre("cinnabar_lapis_ore", UniformInt.of(1, 5), Blocks.LAPIS_ORE);
    public static final DeferredHolder<Block, Block> CINNABAR_DIAMOND_ORE = cinnabarOre("cinnabar_diamond_ore", UniformInt.of(3, 7), Blocks.DIAMOND_ORE);
    public static final DeferredHolder<Block, Block> CINNABAR_EMERALD_ORE = cinnabarOre("cinnabar_emerald_ore", UniformInt.of(3, 7), Blocks.EMERALD_ORE);

    public static void register(IEventBus modBus) {
        BLOCKS.register(modBus);
    }

    private static DeferredHolder<Block, Block> sulfurOre(String name, IntProvider xp, Block baseOre) {
        return BLOCKS.register(name, registryName -> new DropExperienceBlock(
                xp,
                BlockBehaviour.Properties.ofLegacyCopy(baseOre)
                        .setId(ResourceKey.create(Registries.BLOCK, registryName))
                        .mapColor(MapColor.COLOR_YELLOW)
                        .strength(3.5F, 6.0F)
                        .sound(SoundType.SULFUR)
        ));
    }

    private static DeferredHolder<Block, Block> cinnabarOre(String name, IntProvider xp, Block baseOre) {
        return BLOCKS.register(name, registryName -> new DropExperienceBlock(
                xp,
                BlockBehaviour.Properties.ofLegacyCopy(baseOre)
                        .setId(ResourceKey.create(Registries.BLOCK, registryName))
                        .mapColor(MapColor.COLOR_RED)
                        .strength(3.5F, 6.0F)
                        .sound(SoundType.SULFUR)
        ));
    }

    private static DeferredHolder<Block, Block> cinnabarRedstoneOre(String name) {
        return BLOCKS.register(name, registryName -> new RedStoneOreBlock(
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.REDSTONE_ORE)
                        .setId(ResourceKey.create(Registries.BLOCK, registryName))
                        .mapColor(MapColor.COLOR_RED)
                        .strength(3.5F, 6.0F)
                        .sound(SoundType.CINNABAR)
        ));
    }

    private static DeferredHolder<Block, Block> sulfurRedstoneOre(String name) {
        return BLOCKS.register(name, registryName -> new RedStoneOreBlock(
                BlockBehaviour.Properties.ofLegacyCopy(Blocks.REDSTONE_ORE)
                        .setId(ResourceKey.create(Registries.BLOCK, registryName))
                        .mapColor(MapColor.COLOR_YELLOW)
                        .strength(3.5F, 6.0F)
                        .sound(SoundType.SULFUR)
        ));
    }

    private ScpBlocks() {}
}
