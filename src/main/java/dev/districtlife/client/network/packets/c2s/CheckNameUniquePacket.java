package dev.districtlife.client.network.packets.c2s;

import dev.districtlife.client.network.PacketChannel;

public class CheckNameUniquePacket {

    public static void send(String firstName, String lastName) {
        PacketChannel.sendToServer(PacketChannel.ID_CHECK_NAME_UNIQUE, buf -> {
            buf.writeUtf(firstName, 32);
            buf.writeUtf(lastName, 32);
        });
    }
}
