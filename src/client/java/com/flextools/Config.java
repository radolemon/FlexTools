package com.flextools;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.client.MinecraftClient;

public class Config {
    private static final String MOD_ID = "flextools";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Boolean chatLog = null;

    public static void configLoad() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null) {
            return;
        }

        Path runDir = minecraftClient.runDirectory.toPath();
        Path configDir = runDir.resolve("config/FlexTools/config.properties");

        Properties properties = new Properties();
        try (InputStream inputStream = Files.newInputStream(configDir)) {
            properties.load(inputStream);
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to load config file: %s", e.getMessage()));
            return;
        }

        chatLog = Boolean.parseBoolean(properties.getProperty("chat-log", "false"));
        
        LOGGER.info("- Config Load Complete.");
    }

    public static void configCheck() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null) {
            return;
        }

        Path runDir = minecraftClient.runDirectory.toPath();
        Path configDir = runDir.resolve("config");
        Path FlexToolsDir = configDir.resolve("FlexTools");
        Path ConfigFile = FlexToolsDir.resolve("config.properties");
    
        if (!Files.exists(FlexToolsDir)) {
            try {
                Files.createDirectories(FlexToolsDir);
                LOGGER.info("Created 'FlexTools' directory...");
            } catch (Exception e) {
                LOGGER.error(String.format("Directory to Create 'FlexTools' folder: %s", e.getMessage()));
            }
        } else {
            LOGGER.info("'FlexTools' directory found.");
        }

        if (!Files.exists(ConfigFile)) {
            try (InputStream InputStream = Config.class.getResourceAsStream("/default/config.properties")) {
                if (InputStream == null) {
                    LOGGER.error("Default config file not found in resources.");
                    return;
                }
                Files.copy(InputStream, ConfigFile, StandardCopyOption.REPLACE_EXISTING);
                LOGGER.info("Copied 'config.properties' file...");
            } catch (Exception e) {
                LOGGER.error(String.format("Failed to copy 'config.properties' file: %s", e.getMessage()));
            }
        } else {
            LOGGER.info("'config.properties' file found.");
        }

        LOGGER.info("- Config Check Complete.");
    }
}
