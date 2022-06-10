package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.common.blocks.WeatheringRotatedPillarBlock;
import com.cursedcauldron.unvotedandshelved.common.blocks.WeatheringCopperButtonBlock;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Mixin to allow for lightning to unoxidize any copper related things

@Mixin(LightningBolt.class)
public class LightningBoltMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LightningBolt;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V"), method = "tick")
    private void US$tick(CallbackInfo ci) {
        LightningBolt $this = (LightningBolt) (Object) this;
        List<CopperGolemEntity> golems = $this.getLevel().getEntitiesOfClass(CopperGolemEntity.class, new AABB($this.getX() - 15.0, $this.getY() - 15.0, $this.getZ() - 15.0, $this.getX() + 15.0, $this.getY() + 6.0 + 15.0, $this.getZ() + 15.0));
        for (CopperGolemEntity copperGolem : golems) {
            if (copperGolem.isAlive() && copperGolem.getStage() != CopperGolemEntity.Stage.UNAFFECTED) {
                copperGolem.setStage(CopperGolemEntity.Stage.values()[copperGolem.getStage().getId() - 1]);
                copperGolem.level.playLocalSound(copperGolem.getX(), copperGolem.getY(), copperGolem.getZ(), SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 2.0f, 0.5f + copperGolem.getRandom().nextFloat() * 0.2f, false);
                copperGolem.level.levelEvent(3004, copperGolem.blockPosition(), 0);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "randomStepCleaningCopper", cancellable = true)
    private static void US$randomStepCleaningCopper(Level world, BlockPos pos, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        for(BlockPos blockpos : BlockPos.randomInCube(world.random, 10, pos, 1)) {
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.getBlock() instanceof WeatheringCopperButtonBlock || blockstate.getBlock() instanceof WeatheringCopper) {
                WeatheringCopper.getPrevious(blockstate).ifPresent(state -> world.setBlockAndUpdate(blockpos, state));
                WeatheringCopperButtonBlock.getPreviousState(blockstate).ifPresent(state -> world.setBlockAndUpdate(blockpos, state));
                WeatheringRotatedPillarBlock.getPreviousState(blockstate).ifPresent(state -> world.setBlockAndUpdate(blockpos, state));
                world.levelEvent(3002, blockpos, -1);
                cir.setReturnValue(Optional.of(blockpos));
            }
        }

        cir.setReturnValue(Optional.empty());
    }

}
