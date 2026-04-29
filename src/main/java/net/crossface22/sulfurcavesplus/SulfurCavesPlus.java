package net.crossface22.sulfurcavesplus;

import net.crossface22.sulfurcavesplus.entity.SulfurSpiderEntity;
import net.crossface22.sulfurcavesplus.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SulfurCavesPlus implements ModInitializer {

    public static final String MOD_ID = "sulfurcavesplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ResourceKey<Biome> SULFUR_CAVES_BIOME = ResourceKey.create(
            Registries.BIOME,
            Identifier.fromNamespaceAndPath("minecraft", "sulfur_caves"));

    @Override
    public void onInitialize() {
        ScpConfig cfg = ScpConfig.INSTANCE;
        LOGGER.info("[SCP] Config — corrosionInterval={} durabilityPerTick={} " +
                        "spiderNormal={}t spiderHard={}t potentSulfur={}t",
                cfg.corrosionTickInterval, cfg.durabilityDamagePerTick,
                cfg.spiderCorrosionDurationNormal, cfg.spiderCorrosionDurationHard,
                cfg.potentSulfurEffectDuration);

        ScpEffects.register();
        ScpSounds.register();
        ScpEntities.register();
        ScpBlocks.register();
        ScpPotions.register();
        ScpItems.register();
        ScpFeatures.register();
        ScpGameRules.register();

        BiomeModifications.addFeature(
                BiomeSelectors.includeByKey(Biomes.BASALT_DELTAS),
                GenerationStep.Decoration.UNDERGROUND_ORES,
                ScpPlacedFeatures.SULFUR_VEIN_PLACED
        );

        FabricDefaultAttributeRegistry.register(
                ScpEntities.SULFUR_SPIDER,
                SulfurSpiderEntity.createAttributes()
        );

        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.SULFUR), ScpPotions.CORROSION);
        });
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(ScpPotions.CORROSION, Ingredient.of(Items.REDSTONE), ScpPotions.LONG_CORROSION);
        });
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(ScpPotions.CORROSION, Ingredient.of(Items.GLOWSTONE_DUST), ScpPotions.STRONG_CORROSION);
        });

        LOGGER.info("[SCP] Sulfur Caves Plus initialized.");
    }
}
