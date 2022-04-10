package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.api.LightningRodAccess;
import com.cursedcauldron.unvotedandshelved.core.registries.USActivities;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.core.registries.USItems;
import com.cursedcauldron.unvotedandshelved.core.registries.USMemoryModules;
import com.cursedcauldron.unvotedandshelved.core.registries.USParticles;
import com.cursedcauldron.unvotedandshelved.core.registries.USSounds;
import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.gameevent.GameEvent;

import static com.cursedcauldron.unvotedandshelved.core.registries.USEntities.GLARE;
import static net.minecraft.world.level.biome.Biomes.LUSH_CAVES;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";

    @Override
    public void onInitialize() {
        USActivities.ACTIVITIES.register();
        USBlocks.BLOCKS.register();
        USEntities.ENTITIES.register();
        USItems.ITEMS.register();
        USMemoryModules.MEMORY_MODULES.register();
        USParticles.PARTICLES.register();
        USSounds.SOUNDS.register();
        USStructureProcessors.PROCESSORS.register();

        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(LUSH_CAVES), MobCategory.UNDERGROUND_WATER_CREATURE, GLARE, 10, 1, 1);

        USEntities.registerAttributes();

        DispenserBlock.registerBehavior(Blocks.LIGHTNING_ROD, new OptionalDispenseItemBehavior() {
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
}