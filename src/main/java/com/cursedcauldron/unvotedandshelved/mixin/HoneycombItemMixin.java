package com.cursedcauldron.unvotedandshelved.mixin;

import com.google.common.collect.BiMap;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.ButtonBlock.POWERED;

@Mixin(HoneycombItem.class)
public class HoneycombItemMixin {

    @Shadow @Final public static Supplier<BiMap<Block, Block>> WAXABLES;

    @Inject(at = @At("HEAD"), method = "getWaxed(Lnet/minecraft/world/level/block/state/BlockState;)Ljava/util/Optional;", cancellable = true)
    private static void US$getWaxed(BlockState blockState, CallbackInfoReturnable<Optional<BlockState>> cir) {
        cir.setReturnValue(Optional.ofNullable((Block)((BiMap<?, ?>)WAXABLES.get()).get(blockState.getBlock())).map((block) -> {
            if (block.withPropertiesOf(blockState).hasProperty(POWERED)) {
                return block.withPropertiesOf(blockState).setValue(POWERED, false);
            } else return block.withPropertiesOf(blockState);
        }));
    }
}
