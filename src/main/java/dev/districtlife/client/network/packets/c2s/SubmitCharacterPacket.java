package dev.districtlife.client.network.packets.c2s;

import dev.districtlife.client.network.PacketChannel;

public class SubmitCharacterPacket {

    public static void send(String firstName, String lastName, String birthDate,
                            int skinTone, int eyeColor, int hairStyle, int hairColor) {
        PacketChannel.sendToServer(PacketChannel.ID_SUBMIT_CHARACTER, buf -> {
            buf.writeUtf(firstName, 32);
            buf.writeUtf(lastName, 32);
            buf.writeUtf(birthDate, 32);
            buf.writeInt(skinTone);
            buf.writeInt(eyeColor);
            buf.writeInt(hairStyle);
            buf.writeInt(hairColor);
        });
    }
}
