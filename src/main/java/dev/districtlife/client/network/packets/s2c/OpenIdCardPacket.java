package dev.districtlife.client.network.packets.s2c;

import dev.districtlife.client.gui.idcard.IdCardScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class OpenIdCardPacket {

    public static void handle(PacketBuffer buf, Supplier<NetworkEvent.Context> ctx) {
        String serial = buf.readUtf(32);
        UUID ownerUuid = buf.readUUID();
        String firstName = buf.readUtf(32);
        String lastName = buf.readUtf(32);
        String birthDate = buf.readUtf(32);

        ctx.get().enqueueWork(() ->
            Minecraft.getInstance().setScreen(
                new IdCardScreen(serial, ownerUuid, firstName, lastName, birthDate)
            )
        );
        ctx.get().setPacketHandled(true);
    }
}
