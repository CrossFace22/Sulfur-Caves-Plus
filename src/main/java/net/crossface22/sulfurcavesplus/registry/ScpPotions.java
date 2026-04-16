package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public final class ScpPotions {

    public static Holder<Potion> CORROSION;
    public static Holder<Potion> LONG_CORROSION;
    public static Holder<Potion> STRONG_CORROSION;

    public static void register() {
        CORROSION = registerPotion("corrosion",
                new MobEffectInstance(ScpEffects.CORROSION, 900, 0));

        LONG_CORROSION = registerPotion("long_corrosion",
                new MobEffectInstance(ScpEffects.CORROSION, 1800, 0));

        STRONG_CORROSION = registerPotion("strong_corrosion",
                new MobEffectInstance(ScpEffects.CORROSION, 432, 1));
    }

    private static Holder<Potion> registerPotion(String name, MobEffectInstance effect) {
        ResourceKey<Potion> key = ResourceKey.create(
                Registries.POTION,
                Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, name));
        return Registry.registerForHolder(BuiltInRegistries.POTION, key,
                new Potion(name, effect));
    }

    private ScpPotions() {}
}
