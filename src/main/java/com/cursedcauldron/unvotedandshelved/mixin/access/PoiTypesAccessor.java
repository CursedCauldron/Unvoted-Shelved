package com.cursedcauldron.unvotedandshelved.mixin.access;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PoiTypes.class)
public interface PoiTypesAccessor {
    @Invoker
    static PoiType callRegister(Registry<PoiType> registry, ResourceKey<PoiType> resourceKey, Set<BlockState> set, int i, int j) {
        throw new UnsupportedOperationException();
    }
}
