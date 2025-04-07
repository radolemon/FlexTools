package com.flextools;

import net.minecraft.client.MinecraftClient;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatLog {
    private static final String MOD_ID = "flextools";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static String LogFile = "";

    public static void LogFileCheck() {
        if (Config.chatLog != true) {
            LOGGER.info("Chat logging is disabled.");
            return;
        }

        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient == null || minecraftClient.getCurrentServerEntry() == null) {
            LOGGER.warn("Minecraft client or server entry is null. Skipping log file check.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));

        Path runDir = minecraftClient.runDirectory.toPath();
        Path logDir = runDir.resolve("chatlog");
        Path serverDir = logDir.resolve(minecraftClient.getCurrentServerEntry().address);
        Path logFile = serverDir.resolve("ChatLog_" + date + ".txt");

        try {
            if (!Files.exists(logDir)) {
                Files.createDirectories(logDir);
                LOGGER.info("Created 'chatlog' directory...");
            } else {
                LOGGER.info("'chatlog' directory found.");
            }

            if (!Files.exists(serverDir)) {
                Files.createDirectories(serverDir);
                LOGGER.info("Created '" + minecraftClient.getCurrentServerEntry().address + "' directory...");
            } else {
                LOGGER.info("'" + minecraftClient.getCurrentServerEntry().address + "' directory found.");
            }

            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
                LOGGER.info("Created '" + logFile.getFileName() + "' file...");
                LogFile = logFile.toString();
            } else {
                LOGGER.info("'" + logFile.getFileName() + "' file found.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to create log file: " + e.getMessage(), e);
        }

        LOGGER.info("- LogFile Check Complete.");
    }

    public static void saveLogFile(String message) {
        if (Config.chatLog != true) {
            LOGGER.info("Chat Logging is disabled.");
            return;
        }

        if (LogFile.isEmpty()) {
            LOGGER.info("Log file not found.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logEntry = String.format("[%s]: %s%n", time, message);

        try {
            Files.writeString(Path.of(LogFile), logEntry, StandardOpenOption.APPEND);
            LOGGER.info("Log entry saved: " + logEntry);
        } catch (Exception e) {
            System.err.println("Failed to save log entry: " + e.getMessage());
        }
    }
}
