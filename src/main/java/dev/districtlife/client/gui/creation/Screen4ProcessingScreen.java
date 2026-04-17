package dev.districtlife.client.gui.creation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.districtlife.client.gui.base.DLScreen;

public class Screen4ProcessingScreen extends DLScreen {

    private static final int PANEL_W = 400;
    private static final int PANEL_H = 200;
    private static final long MIN_DISPLAY_MS = 3000L;

    private final long startTime;

    // Packets en attente
    private String pendingSerial;
    private String pendingFirstName;
    private String pendingLastName;
    private Integer pendingFailStep;
    private String pendingFailMsg;

    private int dotCount = 0;
    private long lastDotUpdate = 0;

    public Screen4ProcessingScreen() {
        super("Traitement en cours");
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        // Aucun bouton — écran non-interactif
    }

    public void onCharacterCreated(String serial, String firstName, String lastName) {
        this.pendingSerial = serial;
        this.pendingFirstName = firstName;
        this.pendingLastName = lastName;
    }

    public void onCreationFailed(int stepToReturnTo, String errorMsg) {
        this.pendingFailStep = stepToReturnTo;
        this.pendingFailMsg = errorMsg;
    }

    @Override
    public void tick() {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < MIN_DISPLAY_MS) return;

        if (pendingSerial != null) {
            String serial = pendingSerial;
            String fn = pendingFirstName;
            String ln = pendingLastName;
            pendingSerial = null;
            CharacterCreationFlow.getInstance().reset();
            this.minecraft.setScreen(new Screen5WelcomeScreen(fn, ln, serial));
            return;
        }

        if (pendingFailStep != null) {
            int step = pendingFailStep;
            String msg = pendingFailMsg;
            pendingFailStep = null;
            pendingFailMsg = null;
            CharacterCreationFlow.getInstance().setLastError(msg);
            switch (step) {
                case 1:
                    this.minecraft.setScreen(new Screen1IdentityScreen());
                    break;
                case 2:
                    this.minecraft.setScreen(new Screen2AppearanceScreen());
                    break;
                case 3:
                    this.minecraft.setScreen(new Screen3SummaryScreen());
                    break;
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        renderPanel(matrices, px, py, PANEL_W, PANEL_H);

        // Mise à jour animation dots toutes les 500ms
        long now = System.currentTimeMillis();
        if (now - lastDotUpdate > 500L) {
            dotCount = (dotCount + 1) % 4;
            lastDotUpdate = now;
        }
        String dots = ".".repeat(dotCount);

        String line1 = "Traitement de votre dossier par la Pr\u00e9fecture de District";
        String line2 = "Veuillez patienter" + dots;

        drawCenteredString(matrices, this.font, line1, this.width / 2, py + 80, COLOR_TEXT_PRIMARY);
        drawCenteredString(matrices, this.font, line2, this.width / 2, py + 100, COLOR_TEXT_SECONDARY);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
