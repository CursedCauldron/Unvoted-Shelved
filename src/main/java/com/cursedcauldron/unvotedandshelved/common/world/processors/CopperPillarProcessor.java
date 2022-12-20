package com.cursedcauldron.unvotedandshelved.common.world.processors;

import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USStructureProcessors;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CopperPillarProcessor extends StructureProcessor {
    public static final Codec<CopperPillarProcessor> CODEC = RecordCodecBuilder.create(codec -> codec.group(Codec.BOOL.fieldOf("twigs_compat").forGetter(pillarProcessor -> pillarProcessor.twigsCompat)).apply(codec, CopperPillarProcessor::new));
    private final boolean twigsCompat;
    private static final Map<Block, Block> MAP = Util.make(Maps.newHashMap(), map -> {
        map.put(USBlocks.COPPER_PILLAR, getTwigsBlock("copper_pillar"));
        map.put(USBlocks.EXPOSED_COPPER_PILLAR, getTwigsBlock("exposed_copper_pillar"));
        map.put(USBlocks.WEATHERED_COPPER_PILLAR, getTwigsBlock("weathered_copper_pillar"));
        map.put(USBlocks.OXIDIZED_COPPER_PILLAR, getTwigsBlock("oxidized_copper_pillar"));
    });

    public CopperPillarProcessor(boolean twigsCompat) {
        this.twigsCompat = twigsCompat;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo2, StructurePlaceSettings structurePlaceSettings) {
        if (this.twigsCompat && !FabricLoader.getInstance().isModLoaded("twigs")) {
            return structureBlockInfo2;
        }
        for (Block block : MAP.keySet()) {
            if (structureBlockInfo2.state.is(block)) {
                structureBlockInfo2 = new StructureTemplate.StructureBlockInfo(structureBlockInfo2.pos, MAP.get(block).defaultBlockState().setValue(RotatedPillarBlock.AXIS, structureBlockInfo2.state.getValue(RotatedPillarBlock.AXIS)), structureBlockInfo2.nbt);
            }
        }
        return structureBlockInfo2;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return USStructureProcessors.COPPER_PILLAR_PROCESSOR;
    }

    public static Block getTwigsBlock(String name) {
        return Registry.BLOCK.get(new ResourceLocation("twigs", name));
    }

}
