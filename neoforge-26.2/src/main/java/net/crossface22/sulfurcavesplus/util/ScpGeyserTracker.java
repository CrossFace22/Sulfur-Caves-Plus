package net.crossface22.sulfurcavesplus.util;

import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class ScpGeyserTracker {

    private static final Map<UUID, Long> ENTITIES_IN_GEYSER = new HashMap<>();
    private static final long VALID_TICKS = 5L;

    public static void markInsideGeyser(Entity entity) {
        if (entity.level().isClientSide()) {
            return;
        }

        ENTITIES_IN_GEYSER.put(entity.getUUID(), entity.level().getGameTime());
    }

    public static boolean isInsideGeyser(Entity entity) {
        if (entity.level().isClientSide()) {
            return false;
        }

        long gameTime = entity.level().getGameTime();
        Long lastTick = ENTITIES_IN_GEYSER.get(entity.getUUID());

        if (lastTick == null) {
            return false;
        }

        if (gameTime - lastTick > VALID_TICKS) {
            ENTITIES_IN_GEYSER.remove(entity.getUUID());
            return false;
        }

        return true;
    }

    public static void cleanup(long gameTime) {
        Iterator<Map.Entry<UUID, Long>> iterator = ENTITIES_IN_GEYSER.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            if (gameTime - entry.getValue() > 100L) {
                iterator.remove();
            }
        }
    }

    private ScpGeyserTracker() {
    }
}
