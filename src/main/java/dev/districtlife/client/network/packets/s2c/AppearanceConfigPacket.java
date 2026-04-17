package dev.districtlife.client.network.packets.s2c;

import dev.districtlife.client.state.AppearanceConfig;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class AppearanceConfigPacket {

    public static void handle(PacketBuffer buf, Supplier<NetworkEvent.Context> ctx) {
        int skinToneCount = buf.readInt();
        int eyeColorCount = buf.readInt();
        int hairStyleCount = buf.readInt();
        int hairColorCount = buf.readInt();
        int rpYear = buf.readInt();

        ctx.get().enqueueWork(() ->
            AppearanceConfig.getInstance().update(skinToneCount, eyeColorCount, hairStyleCount, hairColorCount, rpYear)
        );
        ctx.get().setPacketHandled(true);
    }
}
