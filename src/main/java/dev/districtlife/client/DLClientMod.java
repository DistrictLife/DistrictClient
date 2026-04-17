package dev.districtlife.client;

import dev.districtlife.client.network.PacketChannel;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("dlclient")
public class DLClientMod {

    public static final String MOD_ID = "dlclient";

    public DLClientMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        // Client-only setup (Mixin, SkinCache, packet handlers) — skipped on server
        DistExecutor.unsafeRunWhenOn(net.minecraftforge.api.distmarker.Dist.CLIENT,
            () -> () -> FMLJavaModLoadingContext.get().getModEventBus()
                .addListener(this::onClientSetup)
        );
    }

    /**
     * Common setup — runs on both client AND server.
     * Registers the SimpleChannel so Forge includes it in the handshake on both sides.
     * This prevents "mismatched mod channel list" when the server is Arclight.
     */
    private void onCommonSetup(FMLCommonSetupEvent event) {
        PacketChannel.init();
    }

    /**
     * Client-only setup — packet handlers, SkinCache placeholder.
     * Never called on a dedicated server.
     */
    private void onClientSetup(FMLClientSetupEvent event) {
        PacketChannel.registerClientHandlers();

        // Import done via DistExecutor, safe to reference client class here
        dev.districtlife.client.skin.SkinCache.initPlaceholder();
    }
}
