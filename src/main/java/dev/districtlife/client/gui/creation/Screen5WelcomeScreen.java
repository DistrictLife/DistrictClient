package dev.districtlife.client.gui.creation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.districtlife.client.gui.base.DLButton;
import dev.districtlife.client.gui.base.DLScreen;
import net.minecraft.util.text.StringTextComponent;

public class Screen5WelcomeScreen extends DLScreen {

    private static final int PANEL_W = 420;
    private static final int PANEL_H = 220;

    private final String firstName;
    private final String lastName;
    private final String serial;

    public Screen5WelcomeScreen(String firstName, String lastName, String serial) {
        super("Bienvenue \u00e0 District");
        this.firstName = firstName;
        this.lastName = lastName;
        this.serial = serial;
    }

    @Override
    protected void init() {
        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        addButton(new DLButton(
            px + PANEL_W / 2 - 75, py + PANEL_H - 35, 150, 20,
            "Entrer dans District",
            btn -> this.minecraft.setScreen(null)
        ));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        renderPanel(matrices, px, py, PANEL_W, PANEL_H);

        int cx = this.width / 2;

        drawCenteredString(matrices, this.font, "Bienvenue \u00e0 District", cx, py + 20, COLOR_TEXT_PRIMARY);
        drawCenteredString(matrices, this.font,
            "Bienvenue, " + firstName + " " + lastName + ".", cx, py + 50, COLOR_TEXT_PRIMARY);
        drawCenteredString(matrices, this.font,
            "Votre pi\u00e8ce d'identit\u00e9 a \u00e9t\u00e9 d\u00e9livr\u00e9e.", cx, py + 70, COLOR_TEXT_SECONDARY);

        // Numéro de série en accent, taille plus grande (simulé en majuscules ici)
        drawCenteredString(matrices, this.font, serial, cx, py + 100, COLOR_ACCENT);

        drawCenteredString(matrices, this.font,
            "Gardez-la pr\u00e9cieusement.", cx, py + 125, COLOR_TEXT_SECONDARY);
        drawCenteredString(matrices, this.font,
            "En cas de perte, rendez-vous \u00e0 la mairie.", cx, py + 138, COLOR_TEXT_SECONDARY);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
