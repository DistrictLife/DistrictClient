package dev.districtlife.client.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Item Forge custom « dlclient:id_card ».
 * Apparence : texture vanilla paper (définie dans id_card.json).
 * Comportement : ne se consomme jamais, l'ouverture de l'écran est gérée
 *   côté client par ClientEventHandler (Forge event annulé avant l'envoi du paquet).
 */
public class IdCardItemForge extends Item {

    public IdCardItemForge() {
        super(new Item.Properties().stacksTo(1));
    }

    /**
     * Retourne FAIL pour s'assurer que le serveur ne consomme pas l'item
     * même si le paquet use atteint le serveur (cas de désynchronisation).
     */
    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        return new ActionResult<>(ActionResultType.FAIL, player.getItemInHand(hand));
    }
}
