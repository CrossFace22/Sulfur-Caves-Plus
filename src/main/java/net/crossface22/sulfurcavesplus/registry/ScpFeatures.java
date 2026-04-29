package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.worldgen.SulfurVeinUnderLavaFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class ScpFeatures {

    public static Feature<NoneFeatureConfiguration> SULFUR_VEIN_UNDER_LAVA;

    public static void register() {
        SULFUR_VEIN_UNDER_LAVA = Registry.register(
                BuiltInRegistries.FEATURE,
                Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "sulfur_vein_under_lava"),
                new SulfurVeinUnderLavaFeature(NoneFeatureConfiguration.CODEC)
        );
    }

    private ScpFeatures() {}
}