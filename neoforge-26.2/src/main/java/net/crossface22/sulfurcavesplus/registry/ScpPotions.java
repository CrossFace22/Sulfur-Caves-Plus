package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScpPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, SulfurCavesPlus.MOD_ID);

    public static final DeferredHolder<Potion, Potion> CORROSION = registerPotion("corrosion", new MobEffectInstance(ScpEffects.CORROSION, 900, 0));
    public static final DeferredHolder<Potion, Potion> LONG_CORROSION = registerPotion("long_corrosion", new MobEffectInstance(ScpEffects.CORROSION, 1800, 0));
    public static final DeferredHolder<Potion, Potion> STRONG_CORROSION = registerPotion("strong_corrosion", new MobEffectInstance(ScpEffects.CORROSION, 432, 1));

    public static void register(IEventBus modBus) {
        POTIONS.register(modBus);
    }

    private static DeferredHolder<Potion, Potion> registerPotion(String name, MobEffectInstance effect) {
        return POTIONS.register(name, () -> new Potion(name, effect));
    }

    private ScpPotions() {}
}
