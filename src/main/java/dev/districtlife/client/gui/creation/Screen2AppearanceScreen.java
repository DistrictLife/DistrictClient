package dev.districtlife.client.gui.creation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.districtlife.client.gui.base.DLButton;
import dev.districtlife.client.gui.base.DLScreen;
import dev.districtlife.client.skin.SkinCache;
import dev.districtlife.client.skin.SkinComposer;
import dev.districtlife.client.state.AppearanceConfig;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import com.mojang.blaze3d.systems.RenderSystem;

public class Screen2AppearanceScreen extends DLScreen {

    private static final int PANEL_W = 400;
    private static final int PANEL_H = 300;

    private final CharacterCreationFlow flow = CharacterCreationFlow.getInstance();
    private String serverError = "";

    public Screen2AppearanceScreen() {
        super("Votre apparence");
    }

    @Override
    protected void init() {
        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        AppearanceConfig cfg = AppearanceConfig.getInstance();

        // Sélecteurs : flèches gauche/droite pour chaque paramètre
        int selectorX = px + 20;
        int selectorStartY = py + 60;
        int selectorStep = 36;

        addSelectorButtons(selectorX, selectorStartY, "Teinte de peau", 0,
            flow.getSkinTone(), cfg.getSkinToneCount(), v -> flow.setSkinTone(v));
        addSelectorButtons(selectorX, selectorStartY + selectorStep, "Couleur des yeux", 1,
            flow.getEyeColor(), cfg.getEyeColorCount(), v -> flow.setEyeColor(v));
        addSelectorButtons(selectorX, selectorStartY + 2 * selectorStep, "Coupe de cheveux", 2,
            flow.getHairStyle(), cfg.getHairStyleCount(), v -> flow.setHairStyle(v));
        addSelectorButtons(selectorX, selectorStartY + 3 * selectorStep, "Couleur des cheveux", 3,
            flow.getHairColor(), cfg.getHairColorCount(), v -> flow.setHairColor(v));

        // Afficher l'erreur serveur si le flow nous a été renvoyé depuis Screen4
        String srvErr = flow.getLastError();
        if (!srvErr.isEmpty()) {
            serverError = srvErr;
            flow.setLastError("");
        }

        // Bouton Retour
        addButton(new DLButton(px + 10, py + PANEL_H - 30, 80, 20, "Retour",
            btn -> this.minecraft.setScreen(new Screen1IdentityScreen())));

        // Bouton Suivant
        addButton(new DLButton(px + PANEL_W - 90, py + PANEL_H - 30, 80, 20, "Suivant",
            btn -> this.minecraft.setScreen(new Screen3SummaryScreen())));
    }

    private void addSelectorButtons(int x, int y, String label, int paramIndex,
                                     int current, int max,
                                     java.util.function.IntConsumer setter) {
        // Bouton gauche
        addButton(new DLButton(x, y + 12, 16, 16, "<", btn -> {
            int val = current <= 1 ? max : current - 1;
            setter.accept(val);
            // Recomposer le skin preview
            recomposePreview();
            rebuildButtons();
        }));

        // Bouton droit
        addButton(new DLButton(x + 120, y + 12, 16, 16, ">", btn -> {
            int val = current >= max ? 1 : current + 1;
            setter.accept(val);
            recomposePreview();
            rebuildButtons();
        }));
    }

    private void recomposePreview() {
        RenderSystem.recordRenderCall(() -> {
            try {
                var image = SkinComposer.composeSync(
                    flow.getSkinTone(), flow.getEyeColor(),
                    flow.getHairStyle(), flow.getHairColor()
                );
                // Utiliser l'UUID local pour le preview
                java.util.UUID localUuid = this.minecraft.player != null
                    ? this.minecraft.player.getUUID()
                    : java.util.UUID.randomUUID();
                SkinCache.put(localUuid, image);
            } catch (Exception ignored) {}
        });
    }

    private void rebuildButtons() {
        this.buttons.clear();
        this.children.clear();
        init();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        renderPanel(matrices, px, py, PANEL_W, PANEL_H);
        renderTitle(matrices, "Votre apparence", "\u00c9tape 2 / 3 \u2014 Personnalisation");

        AppearanceConfig cfg = AppearanceConfig.getInstance();
        int selectorX = px + 20;
        int selectorStartY = py + 60;
        int selectorStep = 36;

        // Labels et valeurs des sélecteurs
        String[][] rows = {
            {"Teinte de peau", String.valueOf(flow.getSkinTone()) + " / " + cfg.getSkinToneCount()},
            {"Couleur des yeux", String.valueOf(flow.getEyeColor()) + " / " + cfg.getEyeColorCount()},
            {"Coupe de cheveux", String.valueOf(flow.getHairStyle()) + " / " + cfg.getHairStyleCount()},
            {"Couleur des cheveux", String.valueOf(flow.getHairColor()) + " / " + cfg.getHairColorCount()},
        };

        for (int i = 0; i < rows.length; i++) {
            int y = selectorStartY + i * selectorStep;
            drawString(matrices, this.font, rows[i][0], selectorX, y, COLOR_TEXT_SECONDARY);
            drawCenteredString(matrices, this.font, rows[i][1], selectorX + 68, y + 16, COLOR_TEXT_PRIMARY);
        }

        // Erreur serveur (ex : valeurs d'apparence hors limites renvoyées par le serveur)
        if (!serverError.isEmpty()) {
            drawCenteredString(matrices, this.font, "§c" + serverError,
                this.width / 2, py + PANEL_H - 50, COLOR_ERROR);
        }

        // Aperçu 3D du joueur
        if (this.minecraft.player != null) {
            InventoryScreen.renderEntityInInventory(
                px + 300, py + 220, 40, (float)(px + 300 - mouseX), (float)(py + 100 - mouseY), this.minecraft.player
            );
        }

        super.render(matrices, mouseX, mouseY, delta);
    }
}
