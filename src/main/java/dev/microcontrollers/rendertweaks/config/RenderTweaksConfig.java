package dev.microcontrollers.rendertweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.ColorController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class RenderTweaksConfig {
    public static final ConfigClassHandler<RenderTweaksConfig> CONFIG = ConfigClassHandler.createBuilder(RenderTweaksConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("rendertweaks.json"))
                    .build())
            .build();

    @SerialEntry public Color lightningColor = new Color(0.45F, 0.45F, 0.5F, 0.3F);
    @SerialEntry public Color worldBorderColor = new Color(1F, 1F, 1F, 1F);

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("Render Tweaks"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Render Tweaks"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Lightning Bolt Color"))
                                        .description(OptionDescription.of(Text.of("Change the color of lightning bolts.")))
                                        .binding(defaults.lightningColor, () -> config.lightningColor, value -> config.lightningColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("World Border Color"))
                                        .description(OptionDescription.of(Text.of("Allows setting a custom color for the world border.")))
                                        .binding(defaults.worldBorderColor, () -> config.worldBorderColor, value -> config.worldBorderColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .build())
        )).generateScreen(parent);
    }
}
