package net.crossface22.sulfurcavesplus.entity;

import net.crossface22.sulfurcavesplus.ScpConfig;
import net.crossface22.sulfurcavesplus.registry.ScpEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.spider.CaveSpider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SulfurSpiderEntity extends CaveSpider {

    public SulfurSpiderEntity(EntityType<? extends CaveSpider> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return CaveSpider.createCaveSpider();
    }

    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean hit = target.hurtServer(level, this.damageSources().mobAttack(this), damage);

        if (hit) {
            this.setLastHurtMob(target);

            if (target instanceof LivingEntity living) {
                ScpConfig cfg = ScpConfig.INSTANCE;
                int duration = switch (level.getDifficulty()) {
                    case EASY   -> cfg.spiderCorrosionDurationEasy;
                    case NORMAL -> cfg.spiderCorrosionDurationNormal;
                    case HARD   -> cfg.spiderCorrosionDurationHard;
                    default     -> 0;
                };
                if (duration > 0) {
                    living.addEffect(new MobEffectInstance(ScpEffects.CORROSION, duration, 0), this);
                    living.addEffect(new MobEffectInstance(MobEffects.NAUSEA, duration, 0), this);
                }
            }
        }

        return hit;
    }
}
