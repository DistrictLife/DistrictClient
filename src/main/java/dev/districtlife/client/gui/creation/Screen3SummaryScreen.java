package dev.districtlife.client.gui.creation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.districtlife.client.gui.base.DLButton;
import dev.districtlife.client.gui.base.DLCheckbox;
import dev.districtlife.client.gui.base.DLScreen;
import dev.districtlife.client.network.packets.c2s.SubmitCharacterPacket;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;

public class Screen3SummaryScreen extends DLScreen {

    private static final int PANEL_W = 400;
    private static final int PANEL_H = 300;

    private DLButton validateButton;
    private DLCheckbox confirmCheckbox;
    private final CharacterCreationFlow flow = CharacterCreationFlow.getInstance();

    public Screen3SummaryScreen() {
        super("V\u00e9rification de vos informations");
    }

    @Override
    protected void init() {
        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        // Checkbox de confirmation
        confirmCheckbox = new DLCheckbox(px + 20, py + 200, "J'ai v\u00e9rifi\u00e9 mes informations et je les confirme");
        confirmCheckbox.setOnChange(checked -> validateButton.setEnabled(checked));
        this.addButton(confirmCheckbox);

        // Bouton Retour
        addButton(new DLButton(px + 10, py + PANEL_H - 30, 80, 20, "Retour",
            btn -> this.minecraft.setScreen(new Screen2AppearanceScreen())));

        // Bouton Valider (grisé tant que checkbox non cochée)
        validateButton = new DLButton(px + PANEL_W - 160, py + PANEL_H - 30, 150, 20,
            "Valider mon identit\u00e9", btn -> onValidate());
        validateButton.setEnabled(false);
        addButton(validateButton);
    }

    private void onValidate() {
        SubmitCharacterPacket.send(
            flow.getFirstName(), flow.getLastName(), flow.getBirthDate(),
            flow.getSkinTone(), flow.getEyeColor(), flow.getHairStyle(), flow.getHairColor()
        );
        this.minecraft.setScreen(new Screen4ProcessingScreen());
    }

    private int calculateAge() {
        if (flow.getBirthDate().isEmpty()) return 0;
        try {
            int birthYear = Integer.parseInt(flow.getBirthDate().substring(0, 4));
            return dev.districtlife.client.state.AppearanceConfig.getInstance().getRpYear() - birthYear;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        renderPanel(matrices, px, py, PANEL_W, PANEL_H);
        renderTitle(matrices, "V\u00e9rification de vos informations", "\u00c9tape 3 / 3 \u2014 Confirmation");

        // Bloc récap gauche
        int textX = px + 20;
        int textY = py + 55;
        drawString(matrices, this.font, flow.getFirstName() + " " + flow.getLastName(), textX, textY, COLOR_TEXT_PRIMARY);
        drawString(matrices, this.font, "N\u00e9(e) le : " + flow.getBirthDate() + " (" + calculateAge() + " ans)", textX, textY + 14, COLOR_TEXT_SECONDARY);

        // Avertissement
        String warning = "\u26a0 Ces informations seront d\u00e9finitives.";
        String warning2 = "Vous ne pourrez plus les modifier sans admin.";
        drawString(matrices, this.font, warning, textX, py + 155, COLOR_ERROR);
        drawString(matrices, this.font, warning2, textX, py + 167, COLOR_ERROR);

        // Aperçu 3D
        if (this.minecraft.player != null) {
            InventoryScreen.renderEntityInInventory(
                px + 310, py + 190, 40, (float)(px + 310 - mouseX), (float)(py + 80 - mouseY), this.minecraft.player
            );
        }

        super.render(matrices, mouseX, mouseY, delta);
    }
}
