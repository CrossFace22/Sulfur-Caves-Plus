package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.effect.CorrosionEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffect;

public final class ScpEffects {

    public static Holder<MobEffect> CORROSION;

    public static void register() {
        ResourceKey<MobEffect> key = ResourceKey.create(
                Registries.MOB_EFFECT,
                Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "corrosion")
        );
        CORROSION = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, key, new CorrosionEffect());
    }

    private ScpEffects() {}
}
