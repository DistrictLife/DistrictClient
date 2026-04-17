package dev.districtlife.client.network.packets.s2c;

import dev.districtlife.client.gui.creation.Screen1IdentityScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class NameCheckResponsePacket {

    public static void handle(PacketBuffer buf, Supplier<NetworkEvent.Context> ctx) {
        boolean available = buf.readBoolean();
        String reason = buf.readUtf(256);

        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof Screen1IdentityScreen) {
                ((Screen1IdentityScreen) Minecraft.getInstance().screen).onNameCheckResponse(available, reason);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
