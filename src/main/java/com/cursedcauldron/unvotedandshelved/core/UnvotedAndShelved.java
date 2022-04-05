package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.api.LightningRodAccess;
import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlock;
import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlockItem;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;

import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import com.cursedcauldron.unvotedandshelved.mixin.ActivityInvoker;
import com.cursedcauldron.unvotedandshelved.mixin.LivingEntityMemoryInvoker;
import com.cursedcauldron.unvotedandshelved.mixin.MemoryInvoker;
import com.google.common.reflect.Reflection;
import com.mojang.serialization.Codec;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;

import static com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry.GLOWBERRY_DUST_STEP;
import static com.cursedcauldron.unvotedandshelved.core.registries.USEntities.COPPER_GOLEM;
import static com.cursedcauldron.unvotedandshelved.core.registries.USEntities.GLARE;
import static net.minecraft.world.level.biome.Biomes.LUSH_CAVES;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";
    public static Activity GOTO_DARKNESS = ActivityInvoker.invokeRegister("goto_darkness");
    public static Activity GOTO_BUTTON = ActivityInvoker.invokeRegister("goto_button");
    public static MemoryModuleType<Integer> GRUMPY_TICKS = MemoryInvoker.invokeRegister("grumpy_ticks", Codec.INT);
    public static MemoryModuleType<Integer> DARK_TICKS_REMAINING = MemoryInvoker.invokeRegister("darkness_ticks", Codec.INT);
    public static MemoryModuleType<Integer> GLOWBERRIES_GIVEN = MemoryInvoker.invokeRegister("glowberries_given", Codec.INT);
    public static MemoryModuleType<LivingEntity> GIVEN_GLOWBERRY = LivingEntityMemoryInvoker.invokeRegister("given_glowberry");
    public static MemoryModuleType<BlockPos> DARK_POS = LivingEntityMemoryInvoker.invokeRegister("dark_pos");
    public static final Item GLARE_SPAWN_EGG = new SpawnEggItem(GLARE, 7837492, 5204011, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    public static final SoundType GLOW = new SoundType(1.0f, 2.0f, SoundEvents.RESPAWN_ANCHOR_CHARGE, GLOWBERRY_DUST_STEP, SoundEvents.RESPAWN_ANCHOR_CHARGE , SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundEvents.RESPAWN_ANCHOR_CHARGE);
    public static GlowberryDustBlock GLOWBERRY_DUST = new GlowberryDustBlock(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0f, 3600000.8f).noDrops().sound(GLOW).lightLevel(GlowberryDustBlock.LIGHT_EMISSION));
    public static final SimpleParticleType GLOWBERRY_DUST_PARTICLES = register("glowberry_dust", false);


    public static SimpleParticleType register(String key, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(MODID, key), new AbstractSimpleParticleType(alwaysShow));
    }



    @Override
    public void onInitialize() {
        SoundRegistry.init();
        USBlocks.init();
        USEntities.init();
        USStructureProcessors.init();
        USStructureProcessors.initProcessors();
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(LUSH_CAVES), MobCategory.UNDERGROUND_WATER_CREATURE, GLARE, 10, 1, 1);

        Reflection.initialize(USEntities.class);
        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "glare_spawn_egg"), GLARE_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation(MODID, "glowberry_dust_bottle"), new GlowberryDustBlockItem(GLOWBERRY_DUST, new FabricItemSettings().tab(CreativeModeTab.TAB_DECORATIONS)));
        GLOWBERRY_DUST = Registry.register(Registry.BLOCK, new ResourceLocation(MODID, "glowberry_dust"), GLOWBERRY_DUST);
        FabricDefaultAttributeRegistry.register(GLARE, GlareEntity.createGlareAttributes());
        FabricDefaultAttributeRegistry.register(COPPER_GOLEM, CopperGolemEntity.createAttributes());
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

    public static ResourceLocation ID(String path)
    {
        return new ResourceLocation(MODID, path);
    }
}