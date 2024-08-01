package dev.microcontrollers.rendertweaks.mixin;

import dev.microcontrollers.rendertweaks.config.RenderTweaksConfig;
import dev.microcontrollers.rendertweaks.util.ColorUtil;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.LightningEntityRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.Color;

@Mixin(LightningEntityRenderer.class)
public class LightningEntityRendererMixin {
    @Unique
    private static final RenderTweaksConfig config = RenderTweaksConfig.CONFIG.instance();

    @Inject(method = "drawBranch", at = @At("HEAD"), cancellable = true)
    private static void cancelLightningRendering(Matrix4f matrix, VertexConsumer buffer, float x1, float z1, int y, float x2, float z2, float red, float green, float blue, float offset2, float offset1, boolean shiftEast1, boolean shiftSouth1, boolean shiftEast2, boolean shiftSouth2, CallbackInfo ci) {
        if (config.disableLightning || config.lightningAlpha == 0F || config.lightningColor.getAlpha() == 0) ci.cancel();
    }

    @ModifyArgs(method = "drawBranch", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"))
    private static void changeLightningColors(Args args) {
        Color color = ColorUtil.getColor(
                config.lightningChroma,
                config.lightningColor,
                config.lightningSpeed,
                config.lightningSaturation,
                config.lightningBrightness,
                config.lightningAlpha
        );
        args.set(0, color.getRed() / 255F);
        args.set(1, color.getGreen() / 255F);
        args.set(2, color.getBlue() / 255F);
        args.set(3, color.getAlpha() / 255F);
    }
}
