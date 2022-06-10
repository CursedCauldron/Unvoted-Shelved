package com.cursedcauldron.unvotedandshelved.common.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Objects;

public class GlowberryDustBlockItem extends BlockItem {
    public GlowberryDustBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public InteractionResult place(BlockPlaceContext blockPlaceContext) {
        if (!blockPlaceContext.canPlace()) {
            return InteractionResult.FAIL;
        }
        BlockPlaceContext blockPlaceContext2 = this.updatePlacementContext(blockPlaceContext);
        if (blockPlaceContext2 == null) {
            return InteractionResult.FAIL;
        }
        BlockState blockState = this.getPlacementState(blockPlaceContext2);
        if (blockState == null) {
            return InteractionResult.FAIL;
        }
        if (!this.placeBlock(blockPlaceContext2, blockState)) {
            return InteractionResult.FAIL;
        }
        BlockPos blockPos = blockPlaceContext2.getClickedPos();
        Level level = blockPlaceContext2.getLevel();
        Player player = blockPlaceContext2.getPlayer();
        ItemStack itemStack = blockPlaceContext2.getItemInHand();
        BlockState blockState2 = level.getBlockState(blockPos);
        InteractionHand hand = blockPlaceContext2.getHand();
        if (blockState2.is(blockState.getBlock())) {
            blockState2 = updateBlockStateFromTag(blockPos, level, itemStack, blockState2);
            this.updateCustomBlockEntityTag(blockPos, level, player, itemStack, blockState2);
            blockState2.getBlock().setPlacedBy(level, blockPos, blockState2, player, itemStack);
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockPos, itemStack);
            }
        }
        SoundType soundType = blockState2.getSoundType();
        level.playSound(player, blockPos, this.getPlaceSound(blockState2), SoundSource.BLOCKS, (soundType.getVolume() + 1.0f) / 2.0f, soundType.getPitch() * 0.8f);
        level.gameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
        if (player == null || !player.getAbilities().instabuild) {
            assert player != null;
            itemStack.shrink(1);
            player.addItem(Items.GLASS_BOTTLE.getDefaultInstance());
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    private BlockState updateBlockStateFromTag(BlockPos blockPos, Level level, ItemStack itemStack, BlockState blockState) {
        BlockState blockState2 = blockState;
        CompoundTag compoundTag = itemStack.getTag();
        if (compoundTag != null) {
            CompoundTag compoundTag2 = compoundTag.getCompound(BLOCK_STATE_TAG);
            StateDefinition<Block, BlockState> stateDefinition = blockState2.getBlock().getStateDefinition();
            for (String string : compoundTag2.getAllKeys()) {
                Property<?> property = stateDefinition.getProperty(string);
                if (property == null) continue;
                String string2 = Objects.requireNonNull(compoundTag2.get(string)).getAsString();
                blockState2 = updateState(blockState2, property, string2);
            }
        }
        if (blockState2 != blockState) {
            level.setBlock(blockPos, blockState2, 2);
        }
        return blockState2;
    }
    private static <T extends Comparable<T>> BlockState updateState(BlockState blockState, Property<T> property, String string) {
        return property.getValue(string).map(comparable -> blockState.setValue(property, comparable)).orElse(blockState);
    }
}
