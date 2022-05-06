package com.cursedcauldron.unvotedandshelved.core;

import com.cursedcauldron.unvotedandshelved.client.entity.USEntityRenderer;
import com.cursedcauldron.unvotedandshelved.config.UnvotedConfigManager;
import com.cursedcauldron.unvotedandshelved.core.registries.USBlocks;
import com.cursedcauldron.unvotedandshelved.core.registries.USParticles;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.UUID;

//<>

@Environment(EnvType.CLIENT)
public class ClientUnvotedAndShelved implements ClientModInitializer {
    static {
        ParticleFactoryRegistry registry = ParticleFactoryRegistry.getInstance();
        registry.register(USParticles.GLOWBERRY_DUST_PARTICLES, FlameParticle.Provider::new);
    }

    @Override
    public void onInitializeClient() {
        USEntityRenderer.registerRenderers();
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.EXPOSED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WEATHERED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.OXIDIZED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_EXPOSED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_WEATHERED_COPPER_PILLAR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(USBlocks.WAXED_OXIDIZED_COPPER_PILLAR, RenderType.cutout());
        if (FabricLoader.getInstance().isModLoaded("modmenu")) {
            UnvotedConfigManager.initializeConfig();
        }
        ClientSidePacketRegistry.INSTANCE.register(ClientUnvotedAndShelved.EntityPacket.ID, EntityPacketOnClient::onPacket);
    }

    public static class EntityPacketOnClient {
        @Environment(EnvType.CLIENT)
        public static void onPacket(PacketContext context, FriendlyByteBuf byteBuf) {
            EntityType<?> type = Registry.ENTITY_TYPE.byId(byteBuf.readVarInt());
            UUID entityUUID = byteBuf.readUUID();
            int entityID = byteBuf.readVarInt();
            double x = byteBuf.readDouble();
            double y = byteBuf.readDouble();
            double z = byteBuf.readDouble();
            float pitch = (byteBuf.readByte() * 360) / 256.0F;
            float yaw = (byteBuf.readByte() * 360) / 256.0F;
            context.getTaskQueue().execute(() -> {
                @SuppressWarnings("resource")
                ClientLevel world = Minecraft.getInstance().level;
                Entity entity = type.create(world);
                if (entity != null) {
                    entity.absMoveTo(x, y, z);
                    entity.setPacketCoordinates(x, y, z);
                    entity.setXRot(pitch);
                    entity.setYRot(yaw);
                    entity.setId(entityID);
                    entity.setUUID(entityUUID);
                    world.putNonPlayerEntity(entityID, entity);
                }
            });
        }
    }

    public static class EntityPacket {
        public static final ResourceLocation ID = new ResourceLocation(UnvotedAndShelved.MODID, "spawn_entity");

        public static Packet<?> createPacket(Entity entity) {
            FriendlyByteBuf buf = createBuffer();
            buf.writeVarInt(Registry.ENTITY_TYPE.getId(entity.getType()));
            buf.writeUUID(entity.getUUID());
            buf.writeVarInt(entity.getId());
            buf.writeDouble(entity.getX());
            buf.writeDouble(entity.getY());
            buf.writeDouble(entity.getZ());
            buf.writeByte(Mth.floor(entity.getXRot() * 256.0F / 360.0F));
            buf.writeByte(Mth.floor(entity.getYRot() * 256.0F / 360.0F));
            buf.writeFloat(entity.getXRot());
            buf.writeFloat(entity.getYRot());
            return ServerPlayNetworking.createS2CPacket(ID, buf);
        }

        private static FriendlyByteBuf createBuffer() {
            return new FriendlyByteBuf(Unpooled.buffer());
        }

    }
}