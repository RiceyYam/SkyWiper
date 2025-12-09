package org.ricey_yam.skywiper.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;
import org.ricey_yam.skywiper.client.command.ModCommand;
import org.ricey_yam.skywiper.client.event.ModEndTickEventManager;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

public class SkyWiperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModEndTickEventManager.init();

        ModCommand.registerCommand();
    }

    public static void sendMessageDirectly(String message){
        ClientUtils.getPlayer().sendMessage(Text.of(message),false);
    }

    public static void sendLogMessage(String message){
        sendMessageDirectly("§c§l[§6§lSkyWiper§c§l]§a " + message);
    }

    public static void sendWarningMessage(String message){
        sendMessageDirectly("§c§l[§6§lSkyWiper§c§l]§e " + message);
    }

    public static void sendErrorMessage(String message){
        sendMessageDirectly("§c§l[§6§lSkyWiper§c§l]§4 " + message);
    }
}
