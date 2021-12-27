package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.client.entity.render.CopperGolemRenderer;
import com.cursedcauldron.unvotedandshelved.core.registries.USGeoEntities;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import software.bernie.geckolib3.GeckoLib;

import java.util.UUID;

//<>

@Environment(EnvType.CLIENT)
public class ClientUnvotedAndShelved implements ClientModInitializer {
    static {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(UnvotedAndShelved.GLOWBERRY_DUST_PARTICLES, FlameParticle.Factory::new);
    }

    @Override
    public void onInitializeClient() {
        USEntityRenderer.registerRenderers();
            EntityRendererRegistry.INSTANCE.register(USGeoEntities.COPPER_GOLEM, CopperGolemRenderer::new);


            ClientSidePacketRegistry.INSTANCE.register(ClientUnvotedAndShelved.EntityPacket.ID, (ctx, buf) -> {
                ClientUnvotedAndShelved.EntityPacketOnClient.onPacket(ctx, buf);
            });
    }

    public class EntityPacketOnClient {
        @Environment(EnvType.CLIENT)
        public static void onPacket(PacketContext context, PacketByteBuf byteBuf) {
            EntityType<?> type = Registry.ENTITY_TYPE.get(byteBuf.readVarInt());
            UUID entityUUID = byteBuf.readUuid();
            int entityID = byteBuf.readVarInt();
            double x = byteBuf.readDouble();
            double y = byteBuf.readDouble();
            double z = byteBuf.readDouble();
            float pitch = (byteBuf.readByte() * 360) / 256.0F;
            float yaw = (byteBuf.readByte() * 360) / 256.0F;
            context.getTaskQueue().execute(() -> {
                @SuppressWarnings("resource")
                ClientWorld world = MinecraftClient.getInstance().world;
                Entity entity = type.create(world);
                if (entity != null) {
                    entity.updatePosition(x, y, z);
                    entity.updateTrackedPosition(x, y, z);
                    entity.setPitch(pitch);
                    entity.setYaw(yaw);
                    entity.setId(entityID);
                    entity.setUuid(entityUUID);
                    world.addEntity(entityID, entity);
                }
            });
        }
    }

    public class EntityPacket {
        public static final Identifier ID = new Identifier(UnvotedAndShelved.MODID, "spawn_entity");

        public static Packet<?> createPacket(Entity entity) {
            PacketByteBuf buf = createBuffer();
            buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
            buf.writeUuid(entity.getUuid());
            buf.writeVarInt(entity.getId());
            buf.writeDouble(entity.getX());
            buf.writeDouble(entity.getY());
            buf.writeDouble(entity.getZ());
            buf.writeByte(MathHelper.floor(entity.getPitch() * 256.0F / 360.0F));
            buf.writeByte(MathHelper.floor(entity.getYaw() * 256.0F / 360.0F));
            buf.writeFloat(entity.getPitch());
            buf.writeFloat(entity.getYaw());
            return ServerPlayNetworking.createS2CPacket(ID, buf);
        }

        private static PacketByteBuf createBuffer() {
            return new PacketByteBuf(Unpooled.buffer());
        }
    }
}