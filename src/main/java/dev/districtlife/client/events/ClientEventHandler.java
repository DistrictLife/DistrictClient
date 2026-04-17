package dev.districtlife.client.events;

import dev.districtlife.client.skin.SkinCache;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * Handlers d'événements côté client uniquement.
 */
@Mod.EventBusSubscriber(modid = "dlclient", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    /**
     * Nettoyage GPU à la déconnexion.
     * Le skin sera réassigné au prochain AppearanceSyncPacket lors de la reconnexion.
     */
    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        if (event.getPlayer() == null) return;
        UUID uuid = event.getPlayer().getUUID();
        SkinCache.remove(uuid);
    }
}
