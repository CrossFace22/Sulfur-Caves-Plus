package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.worldgen.SulfurVeinUnderLavaFeature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScpFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, SulfurCavesPlus.MOD_ID);

    public static final DeferredHolder<Feature<?>, SulfurVeinUnderLavaFeature> SULFUR_VEIN_UNDER_LAVA = FEATURES.register(
            "sulfur_vein_under_lava",
            () -> new SulfurVeinUnderLavaFeature(NoneFeatureConfiguration.CODEC)
    );

    public static void register(IEventBus modBus) {
        FEATURES.register(modBus);
    }

    private ScpFeatures() {}
}
