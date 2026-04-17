package dev.districtlife.client.skin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SkinCache {

    private static final ConcurrentHashMap<UUID, ResourceLocation> cache = new ConcurrentHashMap<>();
    private static ResourceLocation placeholder;

    /**
     * Initialise le placeholder (skin_01 sans calques superposés).
     * Appelé au FMLClientSetupEvent — une seule fois.
     */
    public static void initPlaceholder() {
        try {
            NativeImage img = SkinLayerLoader.load("base/skin_01.png");
            DynamicTexture tex = new DynamicTexture(img);
            placeholder = Minecraft.getInstance().getTextureManager()
                .register("dlclient_skin_placeholder", tex);
        } catch (IOException e) {
            // Fallback : utiliser le skin Steve par défaut
            placeholder = new ResourceLocation("textures/entity/steve.png");
        }
    }

    /**
     * Enregistre un NativeImage dans le TextureManager et stocke la ResourceLocation.
     * Doit être appelé sur le thread de rendu.
     */
    public static void put(UUID uuid, NativeImage image) {
        // Nettoyer l'ancienne texture si elle existe
        remove(uuid);

        DynamicTexture tex = new DynamicTexture(image);
        ResourceLocation loc = Minecraft.getInstance().getTextureManager()
            .register("dlclient_skin_" + uuid.toString().replace("-", ""), tex);
        cache.put(uuid, loc);
    }

    public static ResourceLocation get(UUID uuid) {
        return cache.get(uuid);
    }

    public static boolean has(UUID uuid) {
        return cache.containsKey(uuid);
    }

    public static ResourceLocation getPlaceholder() {
        return placeholder != null ? placeholder : new ResourceLocation("textures/entity/steve.png");
    }

    /**
     * Supprime et libère la texture GPU associée à cet UUID.
     * Doit être appelé au PlayerLoggedOutEvent côté client.
     */
    public static void remove(UUID uuid) {
        ResourceLocation old = cache.remove(uuid);
        if (old != null) {
            Minecraft.getInstance().getTextureManager().release(old);
        }
    }
}
