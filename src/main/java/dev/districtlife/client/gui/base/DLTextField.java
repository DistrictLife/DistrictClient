package dev.districtlife.client.gui.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

public class DLTextField extends TextFieldWidget {

    private static final int COLOR_BG           = 0xFF15171B;
    private static final int COLOR_BORDER        = 0xFF3D4148;
    private static final int COLOR_BORDER_FOCUS  = 0xFF4A90D9;
    private static final int COLOR_ERROR_TEXT    = 0xFFE85D5D;
    private static final int COLOR_TEXT          = 0xFFEAEAEA;

    private String errorMessage = null;

    public DLTextField(FontRenderer font, int x, int y, int w, int h) {
        super(font, x, y, w, h, new StringTextComponent(""));
        this.setTextColor(COLOR_TEXT);
        this.setBordered(false);
    }

    public void setError(String message) {
        this.errorMessage = message;
    }

    public void clearError() {
        this.errorMessage = null;
    }

    public boolean hasError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int borderColor = isFocused() ? COLOR_BORDER_FOCUS : COLOR_BORDER;

        // Bordure
        fill(matrices, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, borderColor);
        // Fond
        fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, COLOR_BG);

        super.renderButton(matrices, mouseX, mouseY, delta);

        // Message d'erreur sous le champ
        if (errorMessage != null && !errorMessage.isEmpty()) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            mc.font.draw(matrices, errorMessage, this.x, this.y + this.height + 3, COLOR_ERROR_TEXT);
        }
    }
}
