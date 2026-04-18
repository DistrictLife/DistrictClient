package dev.districtlife.client;

import dev.districtlife.client.item.IdCardItemForge;
import dev.districtlife.client.network.PacketChannel;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod("dlclient")
public class DLClientMod {

    public static final String MOD_ID = "dlclient";

    // ─── Item registry ────────────────────────────────────────────────────────

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    /**
     * Pièce d'identité RP — custom Forge item enregistré sous « dlclient:id_card ».
     * Apparence : texture vanilla paper (assets/dlclient/models/item/id_card.json).
     * Enregistré sur le client ET le serveur (Arclight charge ce mod des deux côtés).
     */
    public static final RegistryObject<IdCardItemForge> ID_CARD =
        ITEMS.register("id_card", IdCardItemForge::new);

    // ─── Constructor ──────────────────────────────────────────────────────────

    public DLClientMod() {
        // Item registry — runs on both client AND server (Arclight charges dlclient server-side too)
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);

        // Client-only setup (Mixin, SkinCache, packet handlers) — skipped on server
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
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
