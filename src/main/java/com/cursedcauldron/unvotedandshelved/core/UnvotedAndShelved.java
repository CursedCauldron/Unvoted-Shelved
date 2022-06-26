package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.api.LightningRodAccess;
import com.cursedcauldron.unvotedandshelved.core.registries.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class UnvotedAndShelved implements ModInitializer {

    public static final String MODID = "unvotedandshelved";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    @Override
    public void onInitialize() {

        // Registry:

        USActivities.ACTIVITIES.register();
        USBlocks.BLOCKS.register();
        USEntities.ENTITIES.register();
        USFeatures.init();
        USGameEvents.init();
        USItems.ITEMS.register();
        USMemoryModules.MEMORY_MODULES.register();
        USParticles.PARTICLES.register();
        USSounds.SOUNDS.register();
        USStructureProcessors.PROCESSORS.register();
        USTags.init();

        LOGGER.info("Thank you for downloading Unvoted & Shelved! :)");

        Util.make(Maps.newLinkedHashMap(), map -> {
            map.put(USBlocks.COPPER_BUTTON, USBlocks.WAXED_COPPER_BUTTON);
            map.put(USBlocks.EXPOSED_COPPER_BUTTON, USBlocks.WAXED_EXPOSED_COPPER_BUTTON);
            map.put(USBlocks.WEATHERED_COPPER_BUTTON, USBlocks.WAXED_WEATHERED_COPPER_BUTTON);
            map.put(USBlocks.OXIDIZED_COPPER_BUTTON, USBlocks.WAXED_OXIDIZED_COPPER_BUTTON);

            map.put(USBlocks.COPPER_PILLAR, USBlocks.WAXED_COPPER_PILLAR);
            map.put(USBlocks.EXPOSED_COPPER_PILLAR, USBlocks.WAXED_EXPOSED_COPPER_PILLAR);
            map.put(USBlocks.WEATHERED_COPPER_PILLAR, USBlocks.WAXED_WEATHERED_COPPER_PILLAR);
            map.put(USBlocks.OXIDIZED_COPPER_PILLAR, USBlocks.WAXED_OXIDIZED_COPPER_PILLAR);

            map.put(Blocks.LIGHTNING_ROD, USBlocks.WAXED_LIGHTNING_ROD);
            map.put(USBlocks.EXPOSED_LIGHTNING_ROD, USBlocks.WAXED_EXPOSED_LIGHTNING_ROD);
            map.put(USBlocks.WEATHERED_LIGHTNING_ROD, USBlocks.WAXED_WEATHERED_LIGHTNING_ROD);
            map.put(USBlocks.OXIDIZED_LIGHTNING_ROD, USBlocks.WAXED_OXIDIZED_LIGHTNING_ROD);

        }).forEach((unwaxed, waxed) -> {
            assert unwaxed != null;
            assert waxed != null;
            OxidizableBlocksRegistry.registerWaxableBlockPair((Block) unwaxed, (Block) waxed);
        });

        List<Block> list = Lists.newLinkedList();

        list.add(USBlocks.COPPER_PILLAR);
        list.add(USBlocks.EXPOSED_COPPER_PILLAR);
        list.add(USBlocks.WEATHERED_COPPER_PILLAR);
        list.add(USBlocks.OXIDIZED_COPPER_PILLAR);

        for (int i = 0; i < list.size() - 1; i++) {
            OxidizableBlocksRegistry.registerOxidizableBlockPair(list.get(i), list.get(i + 1));
        }

        List<Block> list2 = Lists.newLinkedList();

        list2.add(USBlocks.COPPER_BUTTON);
        list2.add(USBlocks.EXPOSED_COPPER_BUTTON);
        list2.add(USBlocks.WEATHERED_COPPER_BUTTON);
        list2.add(USBlocks.OXIDIZED_COPPER_BUTTON);

        for (int i = 0; i < list.size() - 1; i++) {
            OxidizableBlocksRegistry.registerOxidizableBlockPair(list2.get(i), list2.get(i + 1));
        }

        List<Block> list3 = Lists.newLinkedList();

        list3.add(Blocks.LIGHTNING_ROD);
        list3.add(USBlocks.EXPOSED_LIGHTNING_ROD);
        list3.add(USBlocks.WEATHERED_LIGHTNING_ROD);
        list3.add(USBlocks.OXIDIZED_LIGHTNING_ROD);

        for (int i = 0; i < list.size() - 1; i++) {
            OxidizableBlocksRegistry.registerOxidizableBlockPair(list3.get(i), list3.get(i + 1));
        }

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.LUSH_CAVES), MobCategory.UNDERGROUND_WATER_CREATURE, USEntities.GLARE, 10, 1, 1);

        USEntities.registerAttributes();

        DispenserBlock.registerBehavior(Blocks.LIGHTNING_ROD, new OptionalDispenseItemBehavior() {
            @Override
            protected ItemStack execute(@NotNull BlockSource pointer, @NotNull ItemStack stack) {
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
}