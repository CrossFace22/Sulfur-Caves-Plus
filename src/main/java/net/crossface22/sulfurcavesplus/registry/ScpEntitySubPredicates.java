package net.crossface22.sulfurcavesplus.registry;

import com.mojang.serialization.Codec;
import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.advancement.predicate.InGeyserPredicate;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public final class ScpEntitySubPredicates {

    public static final Codec<InGeyserPredicate> IN_GEYSER = Registry.register(
            BuiltInRegistries.ENTITY_SUB_PREDICATE_TYPE,
            Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "in_geyser"),
            InGeyserPredicate.CODEC
    );

    public static void register() {}

    private ScpEntitySubPredicates() {
    }
}