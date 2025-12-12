package org.ricey_yam.skywiper.client.utils.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientUtils {
    public static GameOptions getOptions(){
        return MinecraftClient.getInstance().options;
    }
    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    public static PlayerEntity getPlayer() {
        return getClient().player;
    }

    public static World getWorld(){
        return getClient().world;
    }
}
