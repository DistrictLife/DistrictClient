package dev.districtlife.client.network.packets.s2c;

import dev.districtlife.client.gui.creation.Screen1IdentityScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenCharacterCreationPacket {

    public static void handle(PacketBuffer buf, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
            Minecraft.getInstance().setScreen(new Screen1IdentityScreen())
        );
        ctx.get().setPacketHandled(true);
    }
}
