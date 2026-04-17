package dev.districtlife.client.gui.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class DLButton extends Button {

    private static final int COLOR_ACCENT       = 0xFF4A90D9;
    private static final int COLOR_HOVER        = 0xFF5BA3E8;
    private static final int COLOR_DISABLED_BG  = 0xFF2A2D33;
    private static final int COLOR_DISABLED_TEXT= 0xFF9BA0A8;
    private static final int COLOR_TEXT         = 0xFFEAEAEA;

    private boolean enabled = true;

    public DLButton(int x, int y, int w, int h, String label, IPressable onPress) {
        super(x, y, w, h, new StringTextComponent(label), onPress);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.active = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int bgColor = enabled
            ? (isHovered() ? COLOR_HOVER : COLOR_ACCENT)
            : COLOR_DISABLED_BG;
        int textColor = enabled ? COLOR_TEXT : COLOR_DISABLED_TEXT;

        // Fond bouton
        fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, bgColor);

        // Texte centré
        int cx = this.x + this.width / 2;
        int cy = this.y + (this.height - 8) / 2;
        drawCenteredString(matrices, net.minecraft.client.Minecraft.getInstance().font,
            this.getMessage().getString(), cx, cy, textColor);
    }
}
