package dev.districtlife.client.network.packets.s2c;

import dev.districtlife.client.gui.creation.Screen4ProcessingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CharacterCreationFailedPacket {

    public static void handle(PacketBuffer buf, Supplier<NetworkEvent.Context> ctx) {
        int stepToReturnTo = buf.readInt();
        String errorMsg = buf.readUtf(256);

        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().screen instanceof Screen4ProcessingScreen) {
                ((Screen4ProcessingScreen) Minecraft.getInstance().screen).onCreationFailed(stepToReturnTo, errorMsg);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
