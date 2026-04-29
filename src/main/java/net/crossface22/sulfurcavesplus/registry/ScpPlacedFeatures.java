package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public final class ScpPlacedFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_VEIN_CONFIGURED =
            ResourceKey.create(Registries.CONFIGURED_FEATURE,
                    Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "sulfur_vein_under_lava"));

    public static final ResourceKey<PlacedFeature> SULFUR_VEIN_PLACED =
            ResourceKey.create(Registries.PLACED_FEATURE,
                    Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "sulfur_vein_under_lava"));

    private ScpPlacedFeatures() {}
}