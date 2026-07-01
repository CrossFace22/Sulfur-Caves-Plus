package net.crossface22.sulfurcavesplus;

import net.crossface22.sulfurcavesplus.registry.*;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(SulfurCavesPlus.MOD_ID)
public class SulfurCavesPlus {

    public static final String MOD_ID = "sulfurcavesplus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public SulfurCavesPlus(IEventBus modBus) {
        ScpEffects.register(modBus);
        ScpSounds.register(modBus);
        ScpEntities.register(modBus);
        ScpBlocks.register(modBus);
        ScpPotions.register(modBus);
        ScpItems.register(modBus);
        ScpFeatures.register(modBus);
        modBus.addListener(this::registerEntitySubPredicates);
        modBus.addListener(this::registerGameRules);

        modBus.addListener(this::registerAttributes);
        modBus.addListener(this::buildCreativeTabs);
        NeoForge.EVENT_BUS.addListener(this::registerBrewingRecipes);
    }

    private void registerAttributes(EntityAttributeCreationEvent event) {
        ScpEntities.registerAttributes(event);
    }

    private void registerEntitySubPredicates(RegisterEvent event) {
        ScpEntitySubPredicates.register(event);
    }

    private void registerGameRules(RegisterEvent event) {
        ScpGameRules.register(event);
    }

    private void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addMix(Potions.AWKWARD, Items.SULFUR, ScpPotions.CORROSION);
        event.getBuilder().addMix(ScpPotions.CORROSION, Items.REDSTONE, ScpPotions.LONG_CORROSION);
        event.getBuilder().addMix(ScpPotions.CORROSION, Items.GLOWSTONE_DUST, ScpPotions.STRONG_CORROSION);
    }

    private void buildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS || event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            ScpItems.addCreativeTabEntries(event);
        }
    }
}
