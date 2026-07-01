package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScpSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SulfurCavesPlus.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CORROSION_TICK = SOUND_EVENTS.register(
            "corrosion_tick",
            registryName -> SoundEvent.createVariableRangeEvent(registryName)
    );

    public static void register(IEventBus modBus) {
        SOUND_EVENTS.register(modBus);
    }

    private ScpSounds() {}
}
