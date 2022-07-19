package com.cursedcauldron.unvotedandshelved.events;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.api.IWaxableObject;
import com.cursedcauldron.unvotedandshelved.api.IWeatheringObject;
import com.cursedcauldron.unvotedandshelved.api.LightningRodAccess;
import com.cursedcauldron.unvotedandshelved.block.CopperButtonBlock;
import com.google.common.base.Suppliers;
import com.google.common.collect.BiMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MiscEvents {

    @SubscribeEvent
    public void onTagsUpdated(TagsUpdatedEvent event) {
        DispenserBlock.registerBehavior(Blocks.LIGHTNING_ROD, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(BlockSource pointer, ItemStack stack) {
                Level world = pointer.getLevel();
                BlockPos blockPos = pointer.getPos().relative(pointer.getBlockState().getValue(DispenserBlock.FACING));
                LightningRodBlock block = (LightningRodBlock) Blocks.LIGHTNING_ROD;
                if (world.isEmptyBlock(blockPos) && ((LightningRodAccess)block).canDispense(world, blockPos)) {
                    if (!world.isClientSide) {
                        world.setBlock(blockPos, block.defaultBlockState(), 3);
                        world.gameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                    }

                    stack.shrink(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(ArmorItem.dispenseArmor(pointer, stack));
                }

                return stack;
            }
        });
    }

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockPos blockPos = event.getPos();
        Level world = event.getWorld();
        BlockState state = world.getBlockState(blockPos);
        Player player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        InteractionHand hand = event.getHand();
        if (stack.getItem() == Items.HONEYCOMB && state.getBlock() instanceof IWaxableObject iWaxable) {
            final BiMap<Block, Block> blockBlockBiMap = iWaxable.getWaxables().get();
            Optional<BlockState> waxables = Optional.ofNullable(blockBlockBiMap.get(state.getBlock())).map((blockState) -> blockState.withPropertiesOf(state));
            if (waxables.isPresent()) {
                event.setCanceled(true);
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockPos, stack);
                }
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                BlockState waxedState = waxables.get();
                if (waxables.get().getBlock() instanceof CopperButtonBlock) {
                    waxedState = waxedState.setValue(CopperButtonBlock.POWERED, false);
                }
                world.setBlock(blockPos, waxedState, 1);
                world.levelEvent(player, 3003, blockPos, 0);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
        if (stack.getItem() instanceof AxeItem && state.getBlock() instanceof IWaxableObject iWaxableObject) {
            Optional<BlockState> finalState = Optional.empty();
            if (state.getBlock() instanceof IWeatheringObject iWeatheringObject) {
                Optional<BlockState> previous = iWeatheringObject.getPrevState(state);
                if (previous.isPresent()) {
                    world.playSound(player, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    world.levelEvent(player, 3005, blockPos, 0);
                    finalState = previous;
                }
            }

            final BiMap<Block, Block> blockBlockBiMap = Suppliers.memoize(() -> iWaxableObject.getWaxables().get().inverse()).get();
            Optional<BlockState> previousWaxed = Optional.ofNullable(blockBlockBiMap.get(state.getBlock())).map((blockState) -> blockState.withPropertiesOf(state));
            if (previousWaxed.isPresent()) {
                world.playSound(player, blockPos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                world.levelEvent(player, 3004, blockPos, 0);
                finalState = previousWaxed;
            }
            if (finalState.isPresent()) {
                event.setCanceled(true);
                BlockState scrapedState = finalState.get();
                if (scrapedState.getBlock() instanceof CopperButtonBlock) {
                    scrapedState = scrapedState.setValue(CopperButtonBlock.POWERED, false);
                }
                world.setBlock(blockPos, scrapedState, 11);
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                player.swing(hand);
                event.setCancellationResult(InteractionResult.SUCCESS);
            }
        }
    }
}