package net.crossface22.sulfurcavesplus.advancement.predicate;

import com.mojang.serialization.Codec;
import net.crossface22.sulfurcavesplus.util.ScpGeyserTracker;
import net.minecraft.advancements.predicates.entity.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public record InGeyserPredicate(boolean expected) implements EntitySubPredicate {

    public static final Codec<InGeyserPredicate> CODEC = Codec.BOOL.xmap(
            InGeyserPredicate::new,
            InGeyserPredicate::expected
    );

    @Override
    public boolean matches(Entity entity, ServerLevel level, @Nullable Vec3 position) {
        return ScpGeyserTracker.isInsideGeyser(entity) == this.expected;
    }
}
