package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.core.registries.USPoiTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ServerLevel.class)
public class ServerLevelMixin {

    // Allows for Lightning to target any Oxidized stages of Lightning Rods

    @Inject(method = "findLightningTargetAround", at = @At("TAIL"), cancellable = true)
    private void US$findLightningTargetAround(BlockPos pos, CallbackInfoReturnable<BlockPos> cir) {
        BlockPos position = ((ServerLevel)(Object)this).getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
        Optional<BlockPos> target = this.US$findLightningRod(position);
        cir.setReturnValue(target.orElseGet(cir::getReturnValue));
    }

    private Optional<BlockPos> US$findLightningRod(BlockPos pos) {
        ServerLevel $this = (ServerLevel) (Object) this;
        Optional<BlockPos> target = $this.getPoiManager().findClosest(holder -> holder.is(USPoiTags.LIGHTNING_RODS), blockPos -> blockPos.getY() == $this.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()) - 1, pos, 128, PoiManager.Occupancy.ANY);
        return target.map(blockPos -> blockPos.above(1));
    }

}