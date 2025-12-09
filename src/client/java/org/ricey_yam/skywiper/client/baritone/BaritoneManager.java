package org.ricey_yam.skywiper.client.baritone;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.behavior.ILookBehavior;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.process.ICustomGoalProcess;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

@Getter
@Setter
public class BaritoneManager {
    public static ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static IBaritone getClientBaritone() {
        if (getPlayer() == null) {
            return BaritoneAPI.getProvider().getPrimaryBaritone();
        }
        return BaritoneAPI.getProvider().getBaritoneForPlayer(getPlayer());
    }

    public static ICustomGoalProcess getCustomGoalProcess() {
        return getClientBaritone().getCustomGoalProcess();
    }

    public static IPathingBehavior getPathingBehavior() {
        return getClientBaritone().getPathingBehavior();
    }

    public static ILookBehavior getLookBehavior() {
        return getClientBaritone().getLookBehavior();
    }
}
