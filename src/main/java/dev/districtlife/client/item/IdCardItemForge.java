package dev.districtlife.client.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Item Forge custom « dlclient:id_card ».
 * Apparence : texture vanilla paper (définie dans assets/dlclient/models/item/id_card.json).
 *
 * Toutes les méthodes d'utilisation retournent FAIL/PASS pour garantir qu'aucune
 * logique de consommation ne peut réduire le stack ou supprimer l'item.
 * La protection serveur est renforcée par ForgeProtectionListener (côté DLCitizens).
 */
public class IdCardItemForge extends Item {

    public IdCardItemForge() {
        super(new Item.Properties().stacksTo(1));
    }

    /** Clic droit dans l'air — ne consomme jamais l'item. */
    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        return new ActionResult<>(ActionResultType.FAIL, player.getItemInHand(hand));
    }

    /** Clic droit sur un bloc — ne consomme jamais l'item. */
    @Override
    public ActionResultType useOn(ItemUseContext context) {
        return ActionResultType.FAIL;
    }

    /** Clic droit sur une entité vivante — ne consomme jamais l'item. */
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity player,
                                                 LivingEntity target, Hand hand) {
        return ActionResultType.FAIL;
    }
}
