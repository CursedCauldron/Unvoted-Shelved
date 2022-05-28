package com.cursedcauldron.unvotedandshelved.mixin.access;

import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructureFeature.class)
public interface StructureFeatureAccessor {
    @Invoker
    static <F extends StructureFeature<?>> F callRegister(String string, F structureFeature, GenerationStep.Decoration decoration) {
        throw new UnsupportedOperationException();
    }
}
