package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.entity.SulfurSpiderEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ScpEntities {

    public static EntityType<SulfurSpiderEntity> SULFUR_SPIDER;

    public static void register() {

        SULFUR_SPIDER = Registry.register(
                BuiltInRegistries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "sulfur_spider"),
                EntityType.Builder.<SulfurSpiderEntity>of(SulfurSpiderEntity::new, MobCategory.MONSTER)
                        .sized(0.7F, 0.5F)
                        .eyeHeight(0.45F)
                        .clientTrackingRange(8)
                        .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "sulfur_spider")))
        );
    }

    private ScpEntities() {}
}