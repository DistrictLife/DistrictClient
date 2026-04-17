package dev.districtlife.client.network.packets.s2c;

import dev.districtlife.client.gui.idcard.IdCardScreen;
import dev.districtlife.client.skin.SkinCache;
import dev.districtlife.client.skin.SkinComposer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import com.mojang.blaze3d.systems.RenderSystem;

import java.util.UUID;
import java.util.function.Supplier;

public class AppearanceSyncPacket {

    public static void handle(PacketBuffer buf, Supplier<NetworkEvent.Context> ctx) {
        UUID uuid = buf.readUUID();
        int skinTone = buf.readInt();
        int eyeColor = buf.readInt();
        int hairStyle = buf.readInt();
        int hairColor = buf.readInt();

        ctx.get().enqueueWork(() ->
            RenderSystem.recordRenderCall(() -> {
                try {
                    net.minecraft.client.renderer.texture.NativeImage image = SkinComposer.composeSync(skinTone, eyeColor, hairStyle, hairColor);
                    SkinCache.put(uuid, image);

                    // Si IdCardScreen est ouvert pour ce joueur, rafraîchir
                    if (Minecraft.getInstance().screen instanceof IdCardScreen) {
                        ((IdCardScreen) Minecraft.getInstance().screen).onAppearanceSynced(uuid);
                    }
                } catch (Exception e) {
                    // Log silencieux côté client
                }
            })
        );
        ctx.get().setPacketHandled(true);
    }
}
