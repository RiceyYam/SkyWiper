package org.ricey_yam.skywiper.client.event;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;

public class ModEndTickEventManager {
    private static final TaskEndTickTickEvent END_TICK_EVENT = new TaskEndTickTickEvent();
    private static final ReceiveMessageEvent RECEIVE_MESSAGE_EVENT = new ReceiveMessageEvent();

    public static void init(){
        registerTaskEndTickEvent();

        registerReceiveMessageEvent();
    }

    private static void registerTaskEndTickEvent(){
        MinecraftClient.getInstance().execute(END_TICK_EVENT::tick);
    }

    private static void registerReceiveMessageEvent(){
        ClientReceiveMessageEvents.ALLOW_GAME.register((RECEIVE_MESSAGE_EVENT::onMessage));
    }
}
