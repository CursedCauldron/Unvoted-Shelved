package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Mixin to make Drowned attack Copper Golems

@Mixin(Drowned.class)
public class DrownedMixin extends Zombie {

    protected DrownedMixin(EntityType<? extends Drowned> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "addBehaviourGoals()V", at = @At("TAIL"))
    protected void US$addBehaviourGoals(CallbackInfo ci) {
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, CopperGolemEntity.class, true));
    }
}