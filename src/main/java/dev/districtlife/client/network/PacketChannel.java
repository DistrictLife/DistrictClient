package dev.districtlife.client.network;

import dev.districtlife.client.network.packets.s2c.*;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Consumer;

/**
 * Canal réseau "districtlife:main" côté client (mod Forge dlclient).
 *
 * S2C — le client REÇOIT uniquement. Le dispatch s'opère par ID varint ;
 *   la collision de types PacketBuffer est sans conséquence ici.
 *
 * C2S — le client ENVOIE. Chaque direction utilise une classe wrapper distincte
 *   (C10, C11, C12) pour que Forge puisse résoudre l'encodeur+ID à l'envoi.
 */
public class PacketChannel {

    public static final String CHANNEL_ID = "districtlife:main";
    public static final String PROTOCOL_VERSION = "1.0.0";

    private static SimpleChannel channel;

    // ─── IDs S2C ────────────────────────────────────────────────────────────
    public static final int ID_OPEN_CHARACTER_CREATION  = 0;
    public static final int ID_NAME_CHECK_RESPONSE      = 1;
    public static final int ID_CHARACTER_CREATED        = 2;
    public static final int ID_CHARACTER_CREATION_FAILED = 3;
    public static final int ID_APPEARANCE_SYNC          = 4;
    public static final int ID_OPEN_ID_CARD             = 5;
    public static final int ID_APPEARANCE_CONFIG        = 6;

    // ─── IDs C2S ────────────────────────────────────────────────────────────
    public static final int ID_CHECK_NAME_UNIQUE  = 10;
    public static final int ID_SUBMIT_CHARACTER   = 11;
    public static final int ID_REQUEST_APPEARANCE = 12;

    // ─── Wrappers C2S ───────────────────────────────────────────────────────
    public static final class C10 { final PacketBuffer buf; public C10(PacketBuffer b) { buf = b; } }
    public static final class C11 { final PacketBuffer buf; public C11(PacketBuffer b) { buf = b; } }
    public static final class C12 { final PacketBuffer buf; public C12(PacketBuffer b) { buf = b; } }

    // ────────────────────────────────────────────────────────────────────────

    /**
     * Enregistre le canal — appelé sur les DEUX côtés pendant FMLCommonSetupEvent.
     * Nécessaire pour que Forge inclue le canal dans le handshake côté serveur Arclight.
     */
    public static void init() {
        channel = NetworkRegistry.newSimpleChannel(
            new net.minecraft.util.ResourceLocation(CHANNEL_ID),
            () -> PROTOCOL_VERSION,
            s -> s.equals(PROTOCOL_VERSION) || "ABSENT".equals(s),
            s -> s.equals(PROTOCOL_VERSION) || "ABSENT".equals(s)
        );
    }

    /**
     * Enregistre les handlers S2C + les encodeurs C2S.
     * Appelé uniquement côté CLIENT pendant FMLClientSetupEvent.
     */
    public static void registerClientHandlers() {
        // ── S2C (réception uniquement) ──────────────────────────────────────
        // Décodeur = identité (le buffer lui-même devient le "message").
        // La collision de type PacketBuffer est sans conséquence ici.
        channel.registerMessage(ID_OPEN_CHARACTER_CREATION, PacketBuffer.class,
            (msg, buf) -> {}, buf -> buf,
            (buf, ctx) -> OpenCharacterCreationPacket.handle(buf, ctx));

        channel.registerMessage(ID_NAME_CHECK_RESPONSE, PacketBuffer.class,
            (msg, buf) -> {}, buf -> buf,
            (buf, ctx) -> NameCheckResponsePacket.handle(buf, ctx));

        channel.registerMessage(ID_CHARACTER_CREATED, PacketBuffer.class,
            (msg, buf) -> {}, buf -> buf,
            (buf, ctx) -> CharacterCreatedPacket.handle(buf, ctx));

        channel.registerMessage(ID_CHARACTER_CREATION_FAILED, PacketBuffer.class,
            (msg, buf) -> {}, buf -> buf,
            (buf, ctx) -> CharacterCreationFailedPacket.handle(buf, ctx));

        channel.registerMessage(ID_APPEARANCE_SYNC, PacketBuffer.class,
            (msg, buf) -> {}, buf -> buf,
            (buf, ctx) -> AppearanceSyncPacket.handle(buf, ctx));

        channel.registerMessage(ID_OPEN_ID_CARD, PacketBuffer.class,
            (msg, buf) -> {}, buf -> buf,
            (buf, ctx) -> OpenIdCardPacket.handle(buf, ctx));

        channel.registerMessage(ID_APPEARANCE_CONFIG, PacketBuffer.class,
            (msg, buf) -> {}, buf -> buf,
            (buf, ctx) -> AppearanceConfigPacket.handle(buf, ctx));

        // ── C2S (envoi uniquement, classes distinctes) ──────────────────────
        channel.registerMessage(ID_CHECK_NAME_UNIQUE, C10.class,
            (msg, out) -> out.writeBytes(msg.buf), buf -> new C10(buf),
            (msg, ctx) -> ctx.get().setPacketHandled(true));

        channel.registerMessage(ID_SUBMIT_CHARACTER, C11.class,
            (msg, out) -> out.writeBytes(msg.buf), buf -> new C11(buf),
            (msg, ctx) -> ctx.get().setPacketHandled(true));

        channel.registerMessage(ID_REQUEST_APPEARANCE, C12.class,
            (msg, out) -> out.writeBytes(msg.buf), buf -> new C12(buf),
            (msg, ctx) -> ctx.get().setPacketHandled(true));
    }

    /**
     * Envoie un packet C2S au serveur.
     * Pas de writeInt(packetId) — Forge préfixe l'ID en varint automatiquement.
     */
    public static void sendToServer(int packetId, Consumer<PacketBuffer> writer) {
        PacketBuffer data = new PacketBuffer(io.netty.buffer.Unpooled.buffer());
        writer.accept(data);
        channel.sendToServer(buildClientWrapper(packetId, data));
    }

    private static Object buildClientWrapper(int packetId, PacketBuffer data) {
        switch (packetId) {
            case ID_CHECK_NAME_UNIQUE:  return new C10(data);
            case ID_SUBMIT_CHARACTER:   return new C11(data);
            case ID_REQUEST_APPEARANCE: return new C12(data);
            default:
                throw new IllegalArgumentException("Unknown C2S packet ID: " + packetId);
        }
    }

    public static SimpleChannel getChannel() {
        return channel;
    }
}
