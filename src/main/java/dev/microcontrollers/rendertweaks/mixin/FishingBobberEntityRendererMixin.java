package dev.microcontrollers.rendertweaks.mixin;

import dev.microcontrollers.rendertweaks.config.RenderTweaksConfig;
import dev.microcontrollers.rendertweaks.util.ColorUtil;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//#if MC <= 1.20.6
//$$ import org.spongepowered.asm.mixin.injection.ModifyArgs;
//#endif
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#if MC <= 1.20.6
//$$ import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
//$$
//$$ import java.awt.Color;
//#endif

@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {
    @Unique
    private static final RenderTweaksConfig config = RenderTweaksConfig.CONFIG.instance();

    @Inject(method = "renderFishingLine", at = @At("HEAD"), cancellable = true)
    private static void cancelFishingLineRendering(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry matrices, float segmentStart, float segmentEnd, CallbackInfo ci) {
        if (config.disableFishingLine || config.fishingLineColor.getAlpha() == 0 || config.fishingLineAlpha == 0F) ci.cancel();
    }

    //#if MC >= 1.21
    @ModifyArg(method = "renderFishingLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(I)Lnet/minecraft/client/render/VertexConsumer;"))
    private static int changeFishingLineColor(int argb) {
        return ColorUtil.getColor(
                config.fishingLineChroma,
                config.fishingLineColor,
                config.fishingLineSpeed,
                config.fishingLineSaturation,
                config.fishingLineBrightness,
                config.fishingLineAlpha
        ).getRGB();
    }
    //#else
    //$$ @ModifyArgs(method = "renderFishingLine", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(IIII)Lnet/minecraft/client/render/VertexConsumer;"))
    //$$ private static void changeFishingLineAlpha(Args args) {
    //$$     Color color = ColorUtil.getColor(config.fishingLineChroma,
    //$$             config.fishingLineColor,
    //$$             config.fishingLineSpeed,
    //$$             config.fishingLineSaturation,
    //$$             config.fishingLineBrightness,
    //$$             config.fishingLineAlpha
    //$$     );
    //$$     args.set(0, color.getRed());
    //$$     args.set(1, color.getGreen());
    //$$     args.set(2, color.getBlue());
    //$$     args.set(3, color.getAlpha());
    //$$ }
    //#endif
}
