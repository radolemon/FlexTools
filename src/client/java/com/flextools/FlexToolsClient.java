package com.flextools;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;

public class FlexToolsClient implements ClientModInitializer {
	private static final String MOD_ID = "flextools";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitializeClient() {
		LOGGER.info("> Config Check...");
		Config.configCheck();

		LOGGER.info("> Config Load...");
		Config.configLoad();

		ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
			String serverId = client.getCurrentServerEntry().address;
			LOGGER.info(String.format("Join to Server: %s", serverId));
			LOGGER.info("> ChatLog check...");
			ChatLog.LogFileCheck();
		});

		ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, gameProfile, messageTypeParameters, timestamp) -> {
			String chatMessage = message.getString();
			ChatLog.saveLogFile(chatMessage);
			
			return true;
		});
	}
}