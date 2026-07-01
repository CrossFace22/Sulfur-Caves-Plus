package net.crossface22.sulfurcavesplus.registry;

import com.mojang.serialization.Codec;
import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.advancement.predicate.InGeyserPredicate;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ScpEntitySubPredicates {

    public static final Identifier IN_GEYSER_ID = Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "in_geyser");
    public static final Codec<InGeyserPredicate> IN_GEYSER = InGeyserPredicate.CODEC;

    public static void register(RegisterEvent event) {
        event.register(Registries.ENTITY_SUB_PREDICATE_TYPE, helper -> helper.register(IN_GEYSER_ID, IN_GEYSER));
    }

    private ScpEntitySubPredicates() {
    }
}
