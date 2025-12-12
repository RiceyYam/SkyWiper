package org.ricey_yam.skywiper.client.task;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.task.life.FunctionHubTask;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

import java.util.Random;

@Getter
@Setter
public abstract class Task {
    protected TaskState state;
    protected int tickTimer;

    public void start(){
        setState(TaskState.IDLE);
    }

    public void tick(){
        tickTimer++;
    }

    public void stop(){
        setState(TaskState.STOPPED);
    }

    protected PlayerEntity getPlayer(){
        return ClientUtils.getPlayer();
    }

    protected FunctionHubTask getFunctionHubTask(){
        return FunctionHubTask.getInstance();
    }

    protected FunctionHubTask.LookingSubTask getLookingSubTask(){
        return getFunctionHubTask().getLookingSubTask();
    }

    protected FunctionHubTask.PathingSubTask getPathingFuncTask(){
        return getFunctionHubTask().getPathingSubTask();
    }
}
