package com.cursedcauldron.unvotedandshelved.core.registries;

import com.cursedcauldron.unvotedandshelved.common.entity.ai.glare.GlareBrain;
import com.cursedcauldron.unvotedandshelved.core.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.mixin.SensorTypeAccessor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class USSensorTypes {

    public static final SensorType<TemptingSensor> GLARE_TEMPTATIONS = registerSensorType("glare_temptations", () -> new TemptingSensor(GlareBrain.getTemptations()));

    @NotNull
    private static SensorType<TemptingSensor> registerSensorType(String name, Supplier<TemptingSensor> supplier) {
        return SensorTypeAccessor.callRegister(UnvotedAndShelved.MODID + ":" + name, supplier);
    }

}
