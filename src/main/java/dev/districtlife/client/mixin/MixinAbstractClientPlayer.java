package dev.districtlife.client.mixin;

import dev.districtlife.client.skin.SkinCache;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayer {

    @Inject(method = "getSkinTextureLocation", at = @At("HEAD"), cancellable = true)
    private void dlclient$overrideSkin(CallbackInfoReturnable<ResourceLocation> ci) {
        UUID uuid = ((AbstractClientPlayerEntity) (Object) this).getUUID();
        if (SkinCache.has(uuid)) {
            ci.setReturnValue(SkinCache.get(uuid));
        } else {
            ci.setReturnValue(SkinCache.getPlaceholder());
        }
    }
}
