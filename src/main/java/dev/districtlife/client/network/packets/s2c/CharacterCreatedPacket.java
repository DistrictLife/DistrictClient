package dev.districtlife.client.network.packets.s2c;

import dev.districtlife.client.gui.creation.Screen4ProcessingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CharacterCreatedPacket {

    public static void handle(PacketBuffer buf, Supplier<NetworkEvent.Context> ctx) {
        String serial = buf.readUtf(32);
        String firstName = buf.readUtf(32);
        String lastName = buf.readUtf(32);

        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof Screen4ProcessingScreen) {
                ((Screen4ProcessingScreen) Minecraft.getInstance().screen).onCharacterCreated(serial, firstName, lastName);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
