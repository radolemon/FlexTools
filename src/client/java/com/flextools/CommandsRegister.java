package com.flextools;


import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;


public class CommandsRegister {
    private static MinecraftClient client = MinecraftClient.getInstance();

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, CommandsRegisterAccess) -> {
            dispatcher.register(
                LiteralArgumentBuilder.<FabricClientCommandSource>literal("flextools")
                    .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("reload")
                        .executes(context -> {
                            Config.configLoad();
                            context.getSource().sendFeedback(Text.literal("Config reloaded!"));
                            return 1;
                        })
                    )
            );

            dispatcher.register(
                LiteralArgumentBuilder.<FabricClientCommandSource>literal("ft")
                    /* .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("help")
                        .executes(context -> {
                        })
                    )*/
                    .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("reload")
                        .executes(context -> {
                            Config.configLoad();
                            context.getSource().sendFeedback(Text.literal("Config reloaded!"));
                            return 1;
                        })
                    )
            );
        });
    }
}
