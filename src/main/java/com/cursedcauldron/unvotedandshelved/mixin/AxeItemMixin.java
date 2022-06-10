package com.cursedcauldron.unvotedandshelved.mixin;

import com.cursedcauldron.unvotedandshelved.mixin.access.AxeItemAccessor;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.WeatheringCopper;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Optional;
import static net.minecraft.world.level.block.ButtonBlock.POWERED;

// Mixin to access Axe scraping for Copper related things

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Inject(at = @At("HEAD"), method = "useOn(Lnet/minecraft/world/item/context/UseOnContext;)Lnet/minecraft/world/InteractionResult;", cancellable = true)
    public void US$useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Level level = useOnContext.getLevel();
        BlockPos blockPos = useOnContext.getClickedPos();
        Player player2 = useOnContext.getPlayer();
        BlockState blockState = level.getBlockState(blockPos);
        Optional<BlockState> optional = ((AxeItemAccessor) this).callGetStripped(blockState);
        Optional<BlockState> optional2 = WeatheringCopper.getPrevious(blockState);
        Optional<BlockState> optional3 = Optional.ofNullable(HoneycombItem.WAX_OFF_BY_BLOCK.get().get(blockState.getBlock())).map(block -> (blockState.getBlock() instanceof ButtonBlock) ? block.withPropertiesOf(blockState.setValue(POWERED, false)) : block.withPropertiesOf(blockState));
        ItemStack itemStack = useOnContext.getItemInHand();
        Optional<BlockState> optional4 = Optional.empty();
        if (optional.isPresent()) {
            level.playSound(player2, blockPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f);
            optional4 = optional;
        } else if (optional2.isPresent() && optional2.get().getBlock() instanceof ButtonBlock) {
            level.playSound(player2, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.levelEvent(player2, 3005, blockPos, 0);
            optional4 = optional2;
        } else if (optional3.isPresent() && optional3.get().getBlock() instanceof ButtonBlock) {
            level.playSound(player2, blockPos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f);
            level.levelEvent(player2, 3004, blockPos, 0);
            optional4 = optional3;
        }
        if (optional4.isPresent()) {
            if (player2 instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player2, blockPos, itemStack);
            }
            if (optional4.get().getBlock() instanceof ButtonBlock) {
                level.setBlock(blockPos, optional4.get().setValue(POWERED, false), 11);
                if (player2 != null) {
                    itemStack.hurtAndBreak(1, player2, player -> player.broadcastBreakEvent(useOnContext.getHand()));
                }
                cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
                cir.cancel();
            }
        }
    }
}
