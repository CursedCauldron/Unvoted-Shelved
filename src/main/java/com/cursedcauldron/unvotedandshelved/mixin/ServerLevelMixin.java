package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.core.registries.USPoiTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level {
    protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, holder, supplier, bl, bl2, l, i);
    }

    @Shadow public abstract PoiManager getPoiManager();

    @Inject(method = "findLightningTargetAround", at = @At("TAIL"), cancellable = true)
    private void us$findLightningTarget(BlockPos pos, CallbackInfoReturnable<BlockPos> cir) {
        BlockPos position = this.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
        Optional<BlockPos> target = this.us$findLightningRod(position);
        cir.setReturnValue(target.orElseGet(cir::getReturnValue));
    }

    private Optional<BlockPos> us$findLightningRod(BlockPos pos) {
        Optional<BlockPos> target = this.getPoiManager().findClosest(holder -> holder.is(USPoiTags.LIGHTNING_RODS), blockPos -> blockPos.getY() == this.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()) - 1, pos, 128, PoiManager.Occupancy.ANY);
        return target.map(blockPos -> blockPos.above(1));
    }
}