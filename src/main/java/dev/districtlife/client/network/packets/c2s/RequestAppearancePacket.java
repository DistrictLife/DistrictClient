package dev.districtlife.client.network.packets.c2s;

import dev.districtlife.client.network.PacketChannel;

import java.util.UUID;

public class RequestAppearancePacket {

    public static void send(UUID targetUuid) {
        PacketChannel.sendToServer(PacketChannel.ID_REQUEST_APPEARANCE, buf ->
            buf.writeUUID(targetUuid)
        );
    }
}
