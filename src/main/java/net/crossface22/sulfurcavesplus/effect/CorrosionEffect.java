package net.crossface22.sulfurcavesplus.effect;

import net.crossface22.sulfurcavesplus.ScpConfig;
import net.crossface22.sulfurcavesplus.registry.ScpSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

import static net.minecraft.world.Difficulty.*;

public class CorrosionEffect extends MobEffect {

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET
    };

    private static final int EFFECT_COLOR = 0xB5C400;

    public CorrosionEffect() {
        super(MobEffectCategory.HARMFUL, EFFECT_COLOR);
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity entity, int amplifier) {
        ScpConfig cfg = ScpConfig.INSTANCE;
        int tickInterval = switch (serverLevel.getDifficulty()) {
            case EASY, PEACEFUL -> cfg.corrosionTickInterval + 6;
            case NORMAL -> cfg.corrosionTickInterval + 3;
            case HARD -> cfg.corrosionTickInterval;
        };

        if (entity.tickCount % tickInterval != 0) {
            return true;
        }

        Holder<Enchantment> unbreaking = serverLevel.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(Enchantments.UNBREAKING);

        boolean anythingDamaged = false;

        for (EquipmentSlot slot : ARMOR_SLOTS) {
            ItemStack armor = entity.getItemBySlot(slot);
            if (armor.isEmpty() || !armor.isDamageableItem()) continue;

            int unbreakingLevel = EnchantmentHelper.getItemEnchantmentLevel(unbreaking, armor);
            if (unbreakingLevel > 0) {
                int blockChance = switch (unbreakingLevel) {
                    case 1  -> 15;
                    case 2  -> 30;
                    default -> 50;
                };
                if (entity.getRandom().nextInt(100) < blockChance) continue;
            }

            armor.hurtAndBreak(cfg.durabilityDamagePerTick + amplifier, entity, slot);
            anythingDamaged = true;
        }

        if (anythingDamaged) {
            playCorrosionSound(serverLevel, entity);
        }

        spawnGasParticles(serverLevel, entity);

        return true;
    }

    private void playCorrosionSound(ServerLevel level, LivingEntity entity) {
        level.playSound(
                null,
                entity.getX(), entity.getY(), entity.getZ(),
                ScpSounds.CORROSION_TICK,
                SoundSource.PLAYERS,
                0.4F,
                0.9F + entity.getRandom().nextFloat() * 0.2F
        );
    }

    private void spawnGasParticles(ServerLevel level, LivingEntity entity) {
        double cx = entity.getX();
        double cy = entity.getY() + entity.getBbHeight() * 0.5;
        double cz = entity.getZ();
        double spread = entity.getBbWidth() * 0.6;

        level.sendParticles(
                ParticleTypes.NOXIOUS_GAS,
                cx, cy, cz,
                4,
                spread,
                0.2,
                spread,
                0.02
        );
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
