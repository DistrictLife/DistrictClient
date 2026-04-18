package dev.districtlife.client.events;

import dev.districtlife.client.DLClientMod;
import dev.districtlife.client.gui.idcard.IdCardScreen;
import dev.districtlife.client.skin.SkinCache;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * Handlers d'événements côté client uniquement.
 */
@Mod.EventBusSubscriber(modid = "dlclient", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    // ─── Clic droit dans l'air ───────────────────────────────────────────────

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!isLocalPlayer(event.getPlayer())) return;
        if (!isIdCard(event.getItemStack())) return;

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.FAIL);

        openIdCardScreen(event.getItemStack());
    }

    // ─── Clic droit sur un bloc ──────────────────────────────────────────────

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!isLocalPlayer(event.getPlayer())) return;
        if (!isIdCard(event.getItemStack())) return;

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.FAIL);
        event.setUseItem(Event.Result.DENY);
        event.setUseBlock(Event.Result.DENY);

        openIdCardScreen(event.getItemStack());
    }

    // ─── Clic droit sur une entité (autre joueur, PNJ…) ────────────────────

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (!isLocalPlayer(event.getPlayer())) return;
        if (!isIdCard(event.getItemStack())) return;

        event.setCanceled(true);
        event.setCancellationResult(ActionResultType.FAIL);

        openIdCardScreen(event.getItemStack());
    }

    // ─── Clic gauche dans l'air (prévient suppression serveur) ──────────────

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (!isLocalPlayer(event.getPlayer())) return;
        if (!isIdCard(event.getItemStack())) return;

        event.setCanceled(true);
    }

    // ─── Clic gauche sur un bloc (prévient suppression serveur) ─────────────

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!isLocalPlayer(event.getPlayer())) return;
        if (!isIdCard(event.getItemStack())) return;

        event.setCanceled(true);
        event.setUseItem(Event.Result.DENY);
        event.setUseBlock(Event.Result.DENY);
    }

    // ─── Nettoyage GPU à la déconnexion ─────────────────────────────────────

    @SubscribeEvent
    public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        if (event.getPlayer() == null) return;
        SkinCache.remove(event.getPlayer().getUUID());
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    /**
     * Vérifie que l'event concerne le joueur local (pas les autres joueurs visibles).
     */
    private static boolean isLocalPlayer(PlayerEntity player) {
        Minecraft mc = Minecraft.getInstance();
        return mc.player != null && player.getUUID().equals(mc.player.getUUID());
    }

    /**
     * Détecte une pièce d'identité.
     *
     * Priorité 1 : item custom Forge « dlclient:id_card » (version serveur avec dlclient installé).
     * Priorité 2 : PAPER vanilla avec tag PDC Bukkit « dlcitizens:id_serial » (compatibilité ascendante).
     */
    public static boolean isIdCard(ItemStack item) {
        if (item.isEmpty()) return false;

        // Vérification via registryName (custom Forge item)
        ResourceLocation regName = item.getItem().getRegistryName();
        if (regName != null
                && "dlclient".equals(regName.getNamespace())
                && "id_card".equals(regName.getPath())) {
            // Custom item — vérifie quand même la présence du PDC pour éviter les /give sauvages
            CompoundNBT tag = item.getTag();
            if (tag == null || !tag.contains("PublicBukkitValues", 10)) return false;
            return tag.getCompound("PublicBukkitValues").contains("dlcitizens:id_serial", 8);
        }

        // Fallback : PAPER + PDC (cartes créées avant la mise à jour ou si dlclient absent du serveur)
        if (item.getItem() != Items.PAPER) return false;
        CompoundNBT tag = item.getTag();
        if (tag == null || !tag.contains("PublicBukkitValues", 10)) return false;
        return tag.getCompound("PublicBukkitValues").contains("dlcitizens:id_serial", 8);
    }

    /**
     * Lit les données PDC de la carte et ouvre l'IdCardScreen.
     * Planifié sur le thread principal Minecraft.
     */
    private static void openIdCardScreen(ItemStack item) {
        CompoundNBT pdc = item.getTag().getCompound("PublicBukkitValues");

        String serial    = pdc.getString("dlcitizens:id_serial");
        String ownerStr  = pdc.getString("dlcitizens:id_owner");
        String firstName = pdc.getString("dlcitizens:id_first_name");
        String lastName  = pdc.getString("dlcitizens:id_last_name");
        String birthDate = pdc.getString("dlcitizens:id_birth_date");

        if (serial.isEmpty() || ownerStr.isEmpty()) return;

        UUID ownerUuid;
        try { ownerUuid = UUID.fromString(ownerStr); }
        catch (IllegalArgumentException e) { return; }

        final UUID   ou = ownerUuid;
        final String sr = serial;
        final String fn = firstName;
        final String ln = lastName;
        final String bd = birthDate;

        Minecraft.getInstance().execute(() ->
            Minecraft.getInstance().setScreen(new IdCardScreen(sr, ou, fn, ln, bd))
        );
    }
}
