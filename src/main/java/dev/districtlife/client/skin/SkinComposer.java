package dev.districtlife.client.skin;

import net.minecraft.client.renderer.texture.NativeImage;

import java.io.IOException;

public class SkinComposer {

    /**
     * Compose un skin à partir des 3 calques PNG.
     * Doit être appelé sur le thread de rendu (RenderSystem.recordRenderCall).
     *
     * @return NativeImage résultant (64x64)
     */
    public static NativeImage composeSync(int skinTone, int eyeColor, int hairStyle, int hairColor) throws IOException {
        String basePath = String.format("base/skin_%02d.png", skinTone);
        String eyesPath = String.format("eyes/eyes_%02d.png", eyeColor);
        String hairPath = String.format("hair/hair_%02d_%02d.png", hairStyle, hairColor);

        NativeImage base = SkinLayerLoader.load(basePath);
        NativeImage eyes = SkinLayerLoader.load(eyesPath);
        NativeImage hair = SkinLayerLoader.load(hairPath);

        try {
            for (int x = 0; x < 64; x++) {
                for (int y = 0; y < 64; y++) {
                    int eyesPixel = eyes.getPixelRGBA(x, y);
                    if (alpha(eyesPixel) > 0) {
                        base.setPixelRGBA(x, y, eyesPixel);
                    }
                    int hairPixel = hair.getPixelRGBA(x, y);
                    if (alpha(hairPixel) > 0) {
                        base.setPixelRGBA(x, y, hairPixel);
                    }
                }
            }
        } finally {
            eyes.close();
            hair.close();
        }

        return base;
    }

    private static int alpha(int rgba) {
        // NativeImage stocke en ABGR sur certaines versions, vérifier l'ordre des octets
        return (rgba >> 24) & 0xFF;
    }
}
