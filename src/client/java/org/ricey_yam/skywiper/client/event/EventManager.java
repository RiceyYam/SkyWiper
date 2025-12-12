package org.ricey_yam.skywiper.client.event;

import lombok.Getter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class EventManager {
    public static final TaskEndTickTickEvent END_TICK_EVENT = new TaskEndTickTickEvent();
    public static final ReceiveMessageEvent RECEIVE_MESSAGE_EVENT = new ReceiveMessageEvent();

    public static void init(){
        registerTaskEndTickEvent();
        registerReceiveMessageEvent();
    }

    private static void registerTaskEndTickEvent(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> END_TICK_EVENT.tick());
    }

    private static void registerReceiveMessageEvent(){
        ClientReceiveMessageEvents.ALLOW_GAME.register((RECEIVE_MESSAGE_EVENT::onMessage));
    }
}
