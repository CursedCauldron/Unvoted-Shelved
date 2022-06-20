package com.cursedcauldron.unvotedandshelved.init;

import com.cursedcauldron.unvotedandshelved.UnvotedAndShelved;
import com.cursedcauldron.unvotedandshelved.entities.ai.glare.GlareBrain;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = UnvotedAndShelved.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class USSensorTypes {

    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, UnvotedAndShelved.MODID);

    public static final RegistryObject<SensorType<TemptingSensor>> GLARE_TEMPTATIONS = SENSOR_TYPES.register("glare_temptations", () -> new SensorType<>(() -> new TemptingSensor(GlareBrain.getTemptations())));

}
