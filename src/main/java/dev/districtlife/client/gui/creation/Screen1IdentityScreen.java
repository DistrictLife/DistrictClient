package dev.districtlife.client.gui.creation;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.districtlife.client.gui.base.DLButton;
import dev.districtlife.client.gui.base.DLScreen;
import dev.districtlife.client.gui.base.DLTextField;
import dev.districtlife.client.network.packets.c2s.CheckNameUniquePacket;
import dev.districtlife.client.validation.ClientValidator;

public class Screen1IdentityScreen extends DLScreen {

    private DLTextField firstNameField;
    private DLTextField lastNameField;
    private DLTextField dayField;
    private DLTextField monthField;
    private DLTextField yearField;
    private DLButton nextButton;
    private String globalError = "";

    private static final int PANEL_W = 400;
    private static final int PANEL_H = 300;

    public Screen1IdentityScreen() {
        super("Votre identit\u00e9");
    }

    @Override
    protected void init() {
        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        CharacterCreationFlow flow = CharacterCreationFlow.getInstance();

        // Champ prénom
        firstNameField = new DLTextField(this.font, px + 20, py + 60, 170, 16);
        firstNameField.setMaxLength(16);
        firstNameField.setValue(flow.getFirstName());
        firstNameField.setResponder(s -> onFieldChanged());
        this.addButton(firstNameField);

        // Champ nom
        lastNameField = new DLTextField(this.font, px + 210, py + 60, 170, 16);
        lastNameField.setMaxLength(16);
        lastNameField.setValue(flow.getLastName());
        lastNameField.setResponder(s -> onFieldChanged());
        this.addButton(lastNameField);

        // Champs date de naissance
        dayField = new DLTextField(this.font, px + 20, py + 120, 36, 16);
        dayField.setMaxLength(2);
        this.addButton(dayField);

        monthField = new DLTextField(this.font, px + 64, py + 120, 36, 16);
        monthField.setMaxLength(2);
        this.addButton(monthField);

        yearField = new DLTextField(this.font, px + 108, py + 120, 56, 16);
        yearField.setMaxLength(4);
        this.addButton(yearField);

        // Remplir les champs date depuis le flow
        if (!flow.getBirthDate().isEmpty()) {
            String[] parts = flow.getBirthDate().split("-");
            if (parts.length == 3) {
                yearField.setValue(parts[0]);
                monthField.setValue(parts[1]);
                dayField.setValue(parts[2]);
            }
        }

        // Focus auto sur le champ suivant lors du remplissage de la date
        dayField.setResponder(s -> { if (s.length() == 2) setFocused(monthField); onFieldChanged(); });
        monthField.setResponder(s -> { if (s.length() == 2) setFocused(yearField); onFieldChanged(); });
        yearField.setResponder(s -> onFieldChanged());

        // Bouton Suivant
        nextButton = new DLButton(px + PANEL_W - 110, py + PANEL_H - 30, 100, 20,
            "Suivant", btn -> onNextClicked());
        this.addButton(nextButton);

        // Afficher l'erreur serveur retournée depuis Screen4 (ex : race condition sur le nom)
        String serverError = CharacterCreationFlow.getInstance().getLastError();
        if (!serverError.isEmpty()) {
            globalError = serverError;
            CharacterCreationFlow.getInstance().setLastError("");
        }

        updateNextButton();
    }

    private void onFieldChanged() {
        // Auto-capitalisation
        String fn = ClientValidator.capitalizeWords(firstNameField.getValue());
        String ln = ClientValidator.capitalizeWords(lastNameField.getValue());
        if (!fn.equals(firstNameField.getValue())) firstNameField.setValue(fn);
        if (!ln.equals(lastNameField.getValue())) lastNameField.setValue(ln);

        globalError = "";
        firstNameField.clearError();
        lastNameField.clearError();
        updateNextButton();
    }

    private void updateNextButton() {
        boolean valid = isLocallyValid();
        nextButton.setEnabled(valid);
    }

    private boolean isLocallyValid() {
        if (!ClientValidator.validateFirstName(firstNameField.getValue())) return false;
        if (!ClientValidator.validateLastName(lastNameField.getValue())) return false;

        String day = dayField.getValue();
        String month = monthField.getValue();
        String year = yearField.getValue();

        if (day.length() != 2 || month.length() != 2 || year.length() != 4) return false;

        try {
            int d = Integer.parseInt(day);
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);
            return ClientValidator.validateBirthDate(d, m, y, dev.districtlife.client.state.AppearanceConfig.getInstance().getRpYear());
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void onNextClicked() {
        if (!isLocallyValid()) return;

        // Sauvegarder dans le flow
        CharacterCreationFlow flow = CharacterCreationFlow.getInstance();
        flow.setFirstName(firstNameField.getValue().trim());
        flow.setLastName(lastNameField.getValue().trim());
        flow.setBirthDate(String.format("%s-%s-%s", yearField.getValue(), monthField.getValue(), dayField.getValue()));

        // Désactiver le bouton pendant la vérification
        nextButton.setEnabled(false);
        globalError = "";

        CheckNameUniquePacket.send(flow.getFirstName(), flow.getLastName());
    }

    /**
     * Callback depuis NameCheckResponsePacket.
     */
    public void onNameCheckResponse(boolean available, String reason) {
        if (available) {
            this.minecraft.setScreen(new Screen2AppearanceScreen());
        } else {
            globalError = reason;
            nextButton.setEnabled(true);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);

        int px = (this.width - PANEL_W) / 2;
        int py = (this.height - PANEL_H) / 2;

        renderPanel(matrices, px, py, PANEL_W, PANEL_H);
        renderTitle(matrices, "Votre identit\u00e9", "\u00c9tape 1 / 3 \u2014 \u00c9tat civil");

        // Labels
        drawString(matrices, this.font, "Pr\u00e9nom", px + 20, py + 50, COLOR_TEXT_SECONDARY);
        drawString(matrices, this.font, "Nom de famille", px + 210, py + 50, COLOR_TEXT_SECONDARY);
        drawString(matrices, this.font, "Date de naissance  JJ  /  MM  /  AAAA", px + 20, py + 110, COLOR_TEXT_SECONDARY);

        // Erreur globale
        if (!globalError.isEmpty()) {
            drawCenteredString(matrices, this.font, "§c" + globalError, this.width / 2, py + PANEL_H - 50, COLOR_ERROR);
        }

        super.render(matrices, mouseX, mouseY, delta);
    }
}
