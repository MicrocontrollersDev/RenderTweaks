package dev.microcontrollers.rendertweaks.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Cancellable;
import com.llamalad7.mixinextras.sugar.Local;
import dev.microcontrollers.rendertweaks.config.RenderTweaksConfig;
import dev.microcontrollers.rendertweaks.util.ColorUtil;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.awt.Color;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Unique
    private static final RenderTweaksConfig config = RenderTweaksConfig.CONFIG.instance();

    @ModifyExpressionValue(method = "renderWorldBorder", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/border/WorldBorderStage;getColor()I"))
    private int changeWorldBorderColor(int original, @Local(ordinal = 0) WorldBorder worldBorder, @Cancellable CallbackInfo ci) {
        return switch (worldBorder.getStage().name()) {
            case "STATIONARY" -> {
                if (config.disableWorldBorderStationary || config.worldBorderStationaryColor.getAlpha() == 0 || config.worldBorderStationaryAlpha == 0F) ci.cancel();
                Color color = ColorUtil.getColor(
                        config.worldBorderStationaryChroma,
                        config.worldBorderStationaryColor,
                        config.worldBorderStationarySpeed,
                        config.worldBorderStationarySaturation,
                        config.worldBorderStationaryBrightness,
                        config.worldBorderStationaryAlpha
                );
                yield color.getRGB();
            }
            case "SHRINKING" -> {
                if (config.disableWorldBorderShrinking || config.worldBorderShrinkingColor.getAlpha() == 0 || config.worldBorderShrinkingAlpha == 0F) ci.cancel();
                Color color = ColorUtil.getColor(
                        config.worldBorderShrinkingChroma,
                        config.worldBorderShrinkingColor,
                        config.worldBorderShrinkingSpeed,
                        config.worldBorderShrinkingSaturation,
                        config.worldBorderShrinkingBrightness,
                        config.worldBorderShrinkingAlpha
                );
                yield color.getRGB();
            }
            case "GROWING" -> {
                if (config.disableWorldBorderGrowing || config.worldBorderGrowingColor.getAlpha() == 0 || config.worldBorderGrowingAlpha == 0F) ci.cancel();
                Color color = ColorUtil.getColor(
                        config.worldBorderGrowingChroma,
                        config.worldBorderGrowingColor,
                        config.worldBorderGrowingSpeed,
                        config.worldBorderGrowingSaturation,
                        config.worldBorderGrowingBrightness,
                        config.worldBorderGrowingAlpha
                );
                yield color.getRGB();
            }
            default -> original;
        };
    }

    @ModifyArgs(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 0),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 3, shift = At.Shift.AFTER)))
    private void changeRainColor(Args args, @Cancellable CallbackInfo ci) {
        if (config.disableRain) ci.cancel();
        Color color = ColorUtil.getColor(
                config.rainChroma,
                config.rainColor,
                config.rainSpeed,
                config.rainSaturation,
                config.rainBrightness,
                config.rainAlpha
        );
        args.set(0, color.getRed() / 255F);
        args.set(1, color.getGreen() / 255F);
        args.set(2, color.getBlue() / 255F);
        args.set(3, (float) args.get(3) * color.getAlpha());
    }

    @ModifyArgs(method = "renderWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;color(FFFF)Lnet/minecraft/client/render/VertexConsumer;"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 4),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferBuilder;vertex(FFF)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 7, shift = At.Shift.AFTER)))
    private void changeSnowColor(Args args, @Cancellable CallbackInfo ci) {
        if (config.disableSnow) ci.cancel();
        Color color = ColorUtil.getColor(
                config.snowChroma,
                config.snowColor,
                config.snowSpeed,
                config.snowSaturation,
                config.snowBrightness,
                config.snowAlpha
        );
        args.set(0, color.getRed() / 255F);
        args.set(1, color.getGreen() / 255F);
        args.set(2, color.getBlue() / 255F);
        args.set(3, (float) args.get(3) * color.getAlpha());
    }
}
