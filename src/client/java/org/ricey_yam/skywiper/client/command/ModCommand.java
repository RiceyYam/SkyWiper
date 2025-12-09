package org.ricey_yam.skywiper.client.command;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class ModCommand {
    public static void registerCommand(){
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> SkyWiperCommand.register(dispatcher));
    }
}
