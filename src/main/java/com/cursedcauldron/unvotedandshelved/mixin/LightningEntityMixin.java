package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import net.minecraft.entity.LightningEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LightningEntity.class)
public class LightningEntityMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LightningEntity;cleanOxidation(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"), method = "tick")
    public void tick(CallbackInfo ci) {
        LightningEntity $this = (LightningEntity) (Object) this;
        List<CopperGolemEntity> list = $this.getWorld().getNonSpectatingEntities(CopperGolemEntity.class, $this.getBoundingBox().expand(2.0D));
        for (CopperGolemEntity copperGolemEntity : list) {
            if (copperGolemEntity.getOxidationStage() == CopperGolemEntity.OxidationStage.OXIDIZED) {
                CopperGolemEntity.OxidationStage oxidationStage = CopperGolemEntity.getRandomStage($this.getEntityWorld().random);
                copperGolemEntity.setOxidationStage(oxidationStage);
            }
        }
    }

}
