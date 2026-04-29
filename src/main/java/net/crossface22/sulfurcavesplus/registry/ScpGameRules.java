package net.crossface22.sulfurcavesplus.registry;

import net.crossface22.sulfurcavesplus.SulfurCavesPlus;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public final class ScpGameRules {
    public static final GameRule<Boolean> NOXIOUS_GAS_ON_GEYSERS = registerBoolean(
            "noxious_gas_on_geysers",
            GameRuleCategory.UPDATES,
            false
    );

    public static final GameRule<Boolean> LAVA_GEYSERS_DAMAGE = registerBoolean(
            "lava_geyser_damage",
            GameRuleCategory.PLAYER,
            true
    );

    private ScpGameRules() {
    }

    public static void register() {
    }

    private static GameRule<Boolean> registerBoolean(String id, GameRuleCategory category, boolean defaultValue) {
        return GameRuleBuilder.forBoolean(defaultValue)
                .category(category)
                .buildAndRegister(Identifier.fromNamespaceAndPath(SulfurCavesPlus.MOD_ID, id));
    }
}
