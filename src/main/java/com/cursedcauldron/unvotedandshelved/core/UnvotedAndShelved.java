package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.api.LightningRodAccess;
import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlock;
import com.cursedcauldron.unvotedandshelved.common.blocks.GlowberryDustBlockItem;
import com.cursedcauldron.unvotedandshelved.common.entity.CopperGolemEntity;
import com.cursedcauldron.unvotedandshelved.common.entity.GlareEntity;
import com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USConfiguredStructures;
import com.cursedcauldron.unvotedandshelved.core.registries.USEntities;
import com.cursedcauldron.unvotedandshelved.core.registries.USGeoEntities;
import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import com.cursedcauldron.unvotedandshelved.core.registries.USStructures;
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
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.LightningRodBlock;
import net.minecraft.block.Material;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.event.GameEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.resource.GeckoLibCache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static com.cursedcauldron.unvotedandshelved.core.registries.SoundRegistry.GLOWBERRY_DUST_STEP;
import static com.cursedcauldron.unvotedandshelved.core.registries.USEntities.GLARE;
import static com.cursedcauldron.unvotedandshelved.core.registries.USGeoEntities.COPPER_GOLEM;
import static net.minecraft.world.biome.BiomeKeys.LUSH_CAVES;
import static software.bernie.geckolib3.GeckoLib.hasInitialized;

//<>

public class UnvotedAndShelved implements ModInitializer {
    public static final String MODID = "unvotedandshelved";
    public static Activity GOTO_DARKNESS = ActivityInvoker.invokeRegister("goto_darkness");
    public static MemoryModuleType<Integer> GRUMPY_TICKS = MemoryInvoker.invokeRegister("grumpy_ticks", Codec.INT);
    public static MemoryModuleType<Integer> DARK_TICKS_REMAINING = MemoryInvoker.invokeRegister("darkness_ticks", Codec.INT);
    public static MemoryModuleType<Integer> GLOWBERRIES_GIVEN = MemoryInvoker.invokeRegister("glowberries_given", Codec.INT);
    public static MemoryModuleType<LivingEntity> GIVEN_GLOWBERRY = LivingEntityMemoryInvoker.invokeRegister("given_glowberry");
    public static MemoryModuleType<BlockPos> DARK_POS = LivingEntityMemoryInvoker.invokeRegister("dark_pos");
    public static final Item GLARE_SPAWN_EGG = new SpawnEggItem(GLARE, 7837492, 5204011, new Item.Settings().group(ItemGroup.MISC));
    public static final BlockSoundGroup GLOW = new BlockSoundGroup(1.0f, 2.0f, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, GLOWBERRY_DUST_STEP, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE , SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE);
    public static GlowberryDustBlock GLOWBERRY_DUST = new GlowberryDustBlock(AbstractBlock.Settings.of(Material.AIR).strength(-1.0f, 3600000.8f).dropsNothing().sounds(GLOW).luminance(GlowberryDustBlock.LIGHT_EMISSION));
    public static final DefaultParticleType GLOWBERRY_DUST_PARTICLES = register("glowberry_dust", false);


    public static DefaultParticleType register(String key, boolean alwaysShow) {
        return Registry.register(Registry.PARTICLE_TYPE, new Identifier(MODID, key), new AbstractSimpleParticleType(alwaysShow));
    }


    public static void initialize() {
        if (!hasInitialized) {
            ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES)
                    .registerReloadListener(new IdentifiableResourceReloadListener() {
                        @Override
                        public Identifier getFabricId() {
                            return new Identifier(UnvotedAndShelved.MODID, "models");
                        }

                        @Override
                        public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager,
                                                              Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor,
                                                              Executor applyExecutor) {
                            return GeckoLibCache.getInstance().reload(synchronizer, manager, prepareProfiler,
                                    applyProfiler, prepareExecutor, applyExecutor);
                        }
                    });
        }
        hasInitialized = true;
    }

    @Override
    public void onInitialize() {
        USStructures.setupAndRegisterStructureFeatures();
        USConfiguredStructures.registerConfiguredStructures();
        addStructureSpawningToDimensionAndBiomes();
        USStructureProcessors.initProcessors();
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(LUSH_CAVES), SpawnGroup.UNDERGROUND_WATER_CREATURE, GLARE, 10, 1, 1);
        SoundRegistry.init();
        USBlocks.register();
        GeckoLib.initialize();
        Reflection.initialize(USEntities.class);
        Registry.register(Registry.ITEM, new Identifier(MODID, "glare_spawn_egg"), GLARE_SPAWN_EGG);
        Registry.register(Registry.ITEM, new Identifier(MODID, "glowberry_dust_bottle"), new GlowberryDustBlockItem(GLOWBERRY_DUST, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        GLOWBERRY_DUST = Registry.register(Registry.BLOCK, new Identifier(MODID, "glowberry_dust"), GLOWBERRY_DUST);
        FabricDefaultAttributeRegistry.register(GLARE, GlareEntity.createGlareAttributes());
        new USGeoEntities();
        FabricDefaultAttributeRegistry.register(COPPER_GOLEM, CopperGolemEntity.createGolemAttributes());
        DispenserBlock.registerBehavior(Blocks.LIGHTNING_ROD, new FallibleItemDispenserBehavior() {
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                World world = pointer.getWorld();
                BlockPos blockPos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
                LightningRodBlock block = (LightningRodBlock) Blocks.LIGHTNING_ROD;
                if (world.isAir(blockPos) && ((LightningRodAccess)block).canDispense(world, blockPos)) {
                    if (!world.isClient) {
                        world.setBlockState(blockPos, block.getDefaultState(), 3);
                        world.emitGameEvent(null, GameEvent.BLOCK_PLACE, blockPos);
                    }

                    stack.decrement(1);
                    this.setSuccess(true);
                } else {
                    this.setSuccess(ArmorItem.dispenseArmor(pointer, stack));
                }

                return stack;
            }
        });

    }

    public static Identifier ID(String path)
    {
        return new Identifier(MODID, path);
    }

    public static void addStructureSpawningToDimensionAndBiomes() {
        BiomeModifications.addStructure(
                BiomeSelectors.categories(
                        Biome.Category.DESERT,
                        Biome.Category.EXTREME_HILLS,
                        Biome.Category.FOREST,
                        Biome.Category.ICY,
                        Biome.Category.JUNGLE,
                        Biome.Category.PLAINS,
                        Biome.Category.TAIGA,
                        Biome.Category.SAVANNA),
                RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY, BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE.getId(USConfiguredStructures.CONFIGURED_RUINED_CAPITAL))
        );
    }

}