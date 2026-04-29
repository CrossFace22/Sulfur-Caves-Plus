package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class ScpSounds {

    public static SoundEvent CORROSION_TICK;

    public static void register() {
        Identifier corrosionId = Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "corrosion_tick");
        CORROSION_TICK = SoundEvent.createVariableRangeEvent(corrosionId);
        Registry.register(BuiltInRegistries.SOUND_EVENT, corrosionId, CORROSION_TICK);
    }

    private ScpSounds() {}
}
