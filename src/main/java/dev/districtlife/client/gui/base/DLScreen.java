package dev.districtlife.client.gui.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public abstract class DLScreen extends Screen {

    // Palette
    protected static final int COLOR_BG_OVERLAY   = 0xAA000000;
    protected static final int COLOR_PANEL         = 0xFF2A2D33;
    protected static final int COLOR_PANEL_BORDER  = 0xFF3D4148;
    protected static final int COLOR_ACCENT        = 0xFF4A90D9;
    protected static final int COLOR_TEXT_PRIMARY  = 0xFFEAEAEA;
    protected static final int COLOR_TEXT_SECONDARY= 0xFF9BA0A8;
    protected static final int COLOR_ERROR         = 0xFFE85D5D;
    protected static final int COLOR_SUCCESS       = 0xFF5DE87A;

    protected DLScreen(String title) {
        super(new StringTextComponent(title));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        // Fond assombri
        fill(matrices, 0, 0, this.width, this.height, COLOR_BG_OVERLAY);
    }

    protected void renderPanel(MatrixStack matrices, int x, int y, int w, int h) {
        // Bordure
        fill(matrices, x - 1, y - 1, x + w + 1, y + h + 1, COLOR_PANEL_BORDER);
        // Fond panneau
        fill(matrices, x, y, x + w, y + h, COLOR_PANEL);
    }

    protected void renderTitle(MatrixStack matrices, String title, String subtitle) {
        int cx = this.width / 2;
        int panelTop = (this.height - 300) / 2;

        // Titre
        drawCenteredString(matrices, this.font, title, cx, panelTop + 16, COLOR_TEXT_PRIMARY);
        // Sous-titre
        drawCenteredString(matrices, this.font, subtitle, cx, panelTop + 30, COLOR_TEXT_SECONDARY);
    }
}
