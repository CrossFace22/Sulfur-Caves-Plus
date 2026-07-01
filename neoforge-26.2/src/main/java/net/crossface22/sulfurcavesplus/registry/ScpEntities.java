package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.crossface22.sulfurcavesplus.entity.SulfurSpiderEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ScpEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE, SulfurCavesPlus.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<SulfurSpiderEntity>> SULFUR_SPIDER = ENTITY_TYPES.register(
            "sulfur_spider",
            () -> EntityType.Builder.<SulfurSpiderEntity>of(SulfurSpiderEntity::new, MobCategory.MONSTER)
                    .sized(0.7F, 0.5F)
                    .eyeHeight(0.45F)
                    .clientTrackingRange(8)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, "sulfur_spider")))
    );

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SULFUR_SPIDER.get(), SulfurSpiderEntity.createAttributes().build());
    }

    private ScpEntities() {}
}
