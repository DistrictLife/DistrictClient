package dev.districtlife.client.gui.idcard;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.districtlife.client.gui.base.DLButton;
import dev.districtlife.client.network.packets.c2s.RequestAppearancePacket;
import dev.districtlife.client.skin.SkinCache;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.util.UUID;

public class IdCardScreen extends Screen {

    private static final ResourceLocation BG_TEXTURE =
        new ResourceLocation("dlclient", "textures/gui/id_card_background.png");

    // Dimensions réelles de la texture id_card_background.png
    private static final int BG_W = 256;
    private static final int BG_H = 160;

    // Couleurs (ARGB)
    private static final int COLOR_TEXT_DARK = 0xFF222222;
    private static final int COLOR_ACCENT    = 0xFF4A90D9;

    private final String serial;
    private final UUID   ownerUuid;
    private final String firstName;
    private final String lastName;
    private final String birthDate;

    public IdCardScreen(String serial, UUID ownerUuid,
                        String firstName, String lastName, String birthDate) {
        super(new StringTextComponent("Pi\u00e8ce d'identit\u00e9"));
        this.serial    = serial;
        this.ownerUuid = ownerUuid;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.birthDate = birthDate;
    }

    /** Appelé depuis AppearanceSyncPacket quand l'apparence du propriétaire arrive. */
    public void onAppearanceSynced(UUID uuid) {
        // Le prochain render() utilisera automatiquement le nouveau SkinCache
    }

    @Override
    public boolean shouldCloseOnEsc() { return true; }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    protected void init() {
        int px = (this.width  - BG_W) / 2;
        int py = (this.height - BG_H) / 2;

        addButton(new DLButton(
            px + BG_W / 2 - 40, py + BG_H - 25, 80, 16,
            "Fermer", btn -> this.minecraft.setScreen(null)
        ));

        // Demander l'apparence si absente du cache
        if (!SkinCache.has(ownerUuid)) {
            RequestAppearancePacket.send(ownerUuid);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int px = (this.width  - BG_W) / 2;
        int py = (this.height - BG_H) / 2;

        // ── Fond de la carte ──────────────────────────────────────────────────
        // Signature statique AbstractGui.blit :
        //   blit(ms, x, y, u, v, width, height, textureHeight, textureWidth)
        // NB : textureHeight vient AVANT textureWidth dans la signature Forge.
        this.minecraft.getTextureManager().bind(BG_TEXTURE);
        blit(matrices, px, py, 0.0f, 0.0f, BG_W, BG_H, BG_H, BG_W);

        // ── Visage du propriétaire (zoom ×8 : 8×8 px → 64×64 écran) ─────────
        // La zone "face" dans un skin 64×64 est à UV pixel (8, 8), taille 8×8.
        // On utilise le matrix stack pour scaler ×8 plutôt que de tordre les UV.
        ResourceLocation skinLoc = SkinCache.has(ownerUuid)
            ? SkinCache.get(ownerUuid)
            : SkinCache.getPlaceholder();

        if (skinLoc != null) {
            this.minecraft.getTextureManager().bind(skinLoc);
            matrices.pushPose();
            // Déplacer l'origine à la position de destination
            matrices.translate(px + 12, py + 20, 0);
            // Scaler 8× : dessiner 8×8 px texture = 64×64 écran
            matrices.scale(8.0f, 8.0f, 1.0f);
            // blit(ms, 0, 0, u=8, v=8, w=8, h=8, texH=64, texW=64)
            blit(matrices, 0, 0, 8.0f, 8.0f, 8, 8, 64, 64);
            matrices.popPose();
        }

        // ── Texte ─────────────────────────────────────────────────────────────
        int textX = px + 90;
        int textY = py + 28;
        int lineH = 14;

        this.font.draw(matrices, firstName + " " + lastName,
            textX, textY, COLOR_TEXT_DARK);
        this.font.draw(matrices, "N\u00e9(e) le : " + birthDate,
            textX, textY + lineH, COLOR_TEXT_DARK);
        this.font.draw(matrices, serial,
            textX, textY + lineH * 2, COLOR_ACCENT);

        // ── Pied de carte ─────────────────────────────────────────────────────
        this.font.draw(matrices,
            "Pr\u00e9fecture de District \u2014 R\u00e9publique Roleplay",
            px + 10, py + BG_H - 40, COLOR_TEXT_DARK);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
