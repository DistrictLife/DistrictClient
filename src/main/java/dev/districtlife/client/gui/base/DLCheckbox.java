package dev.districtlife.client.gui.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

import java.util.function.Consumer;

public class DLCheckbox extends Widget {

    private static final int COLOR_ACCENT  = 0xFF4A90D9;
    private static final int COLOR_BORDER  = 0xFF3D4148;
    private static final int COLOR_BG      = 0xFF15171B;
    private static final int COLOR_TEXT    = 0xFFEAEAEA;

    private boolean checked = false;
    private Consumer<Boolean> onChange;
    private final String label;

    public DLCheckbox(int x, int y, String label) {
        super(x, y, 12, 12, new StringTextComponent(label));
        this.label = label;
    }

    public void setOnChange(Consumer<Boolean> onChange) {
        this.onChange = onChange;
    }

    public boolean isChecked() {
        return checked;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        checked = !checked;
        if (onChange != null) onChange.accept(checked);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        // Bordure
        fill(matrices, this.x, this.y, this.x + 12, this.y + 12, COLOR_BORDER);
        // Fond
        fill(matrices, this.x + 1, this.y + 1, this.x + 11, this.y + 11, COLOR_BG);

        if (checked) {
            fill(matrices, this.x + 2, this.y + 2, this.x + 10, this.y + 10, COLOR_ACCENT);
        }

        // Label
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        mc.font.draw(matrices, label, this.x + 16, this.y + 2, COLOR_TEXT);
    }
}
