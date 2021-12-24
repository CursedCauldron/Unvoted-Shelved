package com.cursedcauldron.unvotedandshelved.common.blocks;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class GlowberryDustBlockItem extends BlockItem {
    public GlowberryDustBlockItem(Block block, Settings properties) {
        super(block, properties);
    }

    public ActionResult place(ItemPlacementContext blockPlaceContext) {
        if (!blockPlaceContext.canPlace()) {
            return ActionResult.FAIL;
        }
        ItemPlacementContext blockPlaceContext2 = this.getPlacementContext(blockPlaceContext);
        if (blockPlaceContext2 == null) {
            return ActionResult.FAIL;
        }
        BlockState blockState = this.getPlacementState(blockPlaceContext2);
        if (blockState == null) {
            return ActionResult.FAIL;
        }
        if (!this.place(blockPlaceContext2, blockState)) {
            return ActionResult.FAIL;
        }
        BlockPos blockPos = blockPlaceContext2.getBlockPos();
        World level = blockPlaceContext2.getWorld();
        PlayerEntity player = blockPlaceContext2.getPlayer();
        ItemStack itemStack = blockPlaceContext2.getStack();
        BlockState blockState2 = level.getBlockState(blockPos);
        Hand hand = blockPlaceContext2.getHand();
        if (blockState2.isOf(blockState.getBlock())) {
            blockState2 = placeFromTag(blockPos, level, itemStack, blockState2);
            this.postPlacement(blockPos, level, player, itemStack, blockState2);
            blockState2.getBlock().onPlaced(level, blockPos, blockState2, player, itemStack);
            if (player instanceof ServerPlayerEntity) {
                Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)player, blockPos, itemStack);
            }
        }
        BlockSoundGroup soundType = blockState2.getSoundGroup();
        level.playSound(player, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0f) / 2.0f, soundType.getPitch() * 0.8f);
        level.emitGameEvent(player, GameEvent.BLOCK_PLACE, blockPos);
        if (player == null || !player.getAbilities().creativeMode) {
            assert player != null;
            itemStack.decrement(1);
            player.giveItemStack(Items.GLASS_BOTTLE.getDefaultStack());
        }
        return ActionResult.success(level.isClient);
    }


    private BlockState placeFromTag(BlockPos blockPos, World level, ItemStack itemStack, BlockState blockState) {
        BlockState blockState2 = blockState;
        NbtCompound compoundTag = itemStack.getNbt();
        if (compoundTag != null) {
            NbtCompound compoundTag2 = compoundTag.getCompound(BLOCK_STATE_TAG_KEY);
            StateManager<Block, BlockState> stateDefinition = blockState2.getBlock().getStateManager();
            for (String string : compoundTag2.getKeys()) {
                Property<?> property = stateDefinition.getProperty(string);
                if (property == null) continue;
                String string2 = compoundTag2.get(string).asString();
                blockState2 = with(blockState2, property, string2);
            }
        }
        if (blockState2 != blockState) {
            level.setBlockState(blockPos, blockState2, 2);
        }
        return blockState2;
    }
    private static <T extends Comparable<T>> BlockState with(BlockState blockState, Property<T> property, String string) {
        return property.parse(string).map(comparable -> blockState.with(property, comparable)).orElse(blockState);
    }
}
