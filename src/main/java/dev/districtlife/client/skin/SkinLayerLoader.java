package dev.districtlife.client.skin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraft.resources.IResource;

import java.io.IOException;
import java.io.InputStream;

public class SkinLayerLoader {

    private static final String BASE_PATH = "textures/appearance/";

    /**
     * Charge un PNG depuis assets/dlclient/textures/appearance/ via le ResourceManager.
     * Lève une exception explicite si le fichier est absent.
     */
    public static NativeImage load(String relativePath) throws IOException {
        ResourceLocation loc = new ResourceLocation("dlclient", BASE_PATH + relativePath);
        // getResource() throws FileNotFoundException (extends IOException) si le fichier est absent.
        // Ne retourne jamais null dans Forge 1.16.5.
        try (IResource resource = Minecraft.getInstance().getResourceManager().getResource(loc);
             InputStream is = resource.getInputStream()) {
            return NativeImage.read(is);
        } catch (IOException e) {
            throw new IOException("Asset introuvable : dlclient:" + BASE_PATH + relativePath, e);
        }
    }
}
