package org.ricey_yam.skywiper.client.event;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.task.ITaskType;
import org.ricey_yam.skywiper.client.task.Task;
import org.ricey_yam.skywiper.client.task.life.AutoRejoinSkyblockTask;
import org.ricey_yam.skywiper.client.task.life.LifeTask;
import org.ricey_yam.skywiper.client.task.life.LifeTaskType;
import org.ricey_yam.skywiper.client.task.skb.GettingEyesTask;
import org.ricey_yam.skywiper.client.task.TaskState;
import org.ricey_yam.skywiper.client.task.skb.SkbTask;
import org.ricey_yam.skywiper.client.task.skb.SkbTaskType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TaskEndTickTickEvent {
    private List<Task> tasks = new ArrayList<>();

    public void tick(){
        if(tasks == null || tasks.isEmpty()) return;
        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            if(task.getState() == TaskState.IDLE) {
                task.tick();
            }
        }
    }

    public void enable(SkbTaskType skbTaskType){
        var registeredTask = getRegisteredTask(skbTaskType);
        if(registeredTask == null) {
            registeredTask = new GettingEyesTask();
            registerTask(registeredTask);
        }
        registeredTask.start();
    }

    public void disable(SkbTaskType skbTaskType){
        var registeredTask = getRegisteredTask(skbTaskType);
        if(registeredTask == null) {
            registeredTask = new GettingEyesTask();
            registerTask(registeredTask);
        }
        registeredTask.stop();
    }

    public void registerTask(Task task){
        tasks.add(task);
    }

    private Task createNewTask(ITaskType taskType){
        if(taskType instanceof SkbTaskType skbTaskType){
            switch(skbTaskType){
                case GETTING_EYES ->{
                    return new GettingEyesTask();
                }
            }
        }
        else if(taskType instanceof LifeTaskType lifeTaskType){
            switch(lifeTaskType){
                case AUTO_REJOIN_SKYBLOCK ->{
                    return new AutoRejoinSkyblockTask();
                }
            }
        }
        return null;
    }

    public Task getRegisteredTask(ITaskType taskType){
        for(var task : tasks){
            if(task instanceof LifeTask lifeTask){
                var lifeTaskType = (LifeTaskType)taskType;
                if(lifeTask.getType() == lifeTaskType) return task;
            }
            else if(task instanceof SkbTask skbTask){
                var skbTaskType = (SkbTaskType) taskType;
                if(skbTask.getType() == skbTaskType) return task;
            }
        }
        return null;
    }
}
