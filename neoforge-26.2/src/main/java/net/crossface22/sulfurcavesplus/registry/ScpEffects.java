package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.effect.CorrosionEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScpEffects {

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, SulfurCavesPlus.MOD_ID);

    public static final DeferredHolder<MobEffect, CorrosionEffect> CORROSION = MOB_EFFECTS.register("corrosion", CorrosionEffect::new);

    public static void register(IEventBus modBus) {
        MOB_EFFECTS.register(modBus);
    }

    private ScpEffects() {}
}
