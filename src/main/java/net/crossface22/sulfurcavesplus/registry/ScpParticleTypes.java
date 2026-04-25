package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ScpParticleTypes {

    public static SimpleParticleType GEYSER_WATER;

    public static void register() {
        GEYSER_WATER = Registry.register(
                BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "geyser_water"),
                FabricParticleTypes.simple()
        );
    }

    private ScpParticleTypes() {}
}
