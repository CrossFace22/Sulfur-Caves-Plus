package net.crossface22.sulfurcavesplus.registry;

import com.mojang.serialization.Codec;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class ScpGameRules {

    public static final Identifier LAVA_GEYSERS_DAMAGE_ID = Identifier.withDefaultNamespace("lava_geyser_damage");

    public static final GameRule<Boolean> LAVA_GEYSERS_DAMAGE = new GameRule<>(
            GameRuleCategory.PLAYER,
            GameRuleType.BOOL,
            BoolArgumentType.bool(),
            GameRuleTypeVisitor::visitBoolean,
            Codec.BOOL,
            value -> value ? 1 : 0
            ,
            true,
            FeatureFlags.VANILLA_SET
    );

    public static void register(RegisterEvent event) {
        event.register(Registries.GAME_RULE, helper -> helper.register(LAVA_GEYSERS_DAMAGE_ID, LAVA_GEYSERS_DAMAGE));
    }

    private ScpGameRules() {
    }

    @FunctionalInterface
    private interface GameRuleTypeVisitor extends GameRules.VisitorCaller<Boolean> {
        static void visitBoolean(net.minecraft.world.level.gamerules.GameRuleTypeVisitor visitor, GameRule<Boolean> rule) {
            visitor.visitBoolean(rule);
        }
    }
}
