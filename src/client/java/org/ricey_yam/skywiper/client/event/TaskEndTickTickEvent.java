package org.ricey_yam.skywiper.client.event;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.task.ITaskType;
import org.ricey_yam.skywiper.client.task.Task;
import org.ricey_yam.skywiper.client.task.life.*;
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
    public TaskEndTickTickEvent(){
        registerAllSkbTasks();
        registerAndEnableAllLifeTasks();
    }

    public void tick(){
        if(tasks == null || tasks.isEmpty()) return;
        for (int i = 0; i < tasks.size(); i++) {
            var task = tasks.get(i);
            if(task.getState() == TaskState.IDLE) {
                task.tick();
            }
        }
    }

    public void enable(ITaskType taskType){
        var registeredTask = getRegisteredTask(taskType);
        registeredTask.start();
    }

    public void disable(ITaskType taskType){
        var registeredTask = getRegisteredTask(taskType);
        registeredTask.stop();
    }

    public boolean isTaskEnabled(ITaskType taskType){
        var registeredTask = getRegisteredTask(taskType);
        if(registeredTask == null) return false;
        return registeredTask.getState() == TaskState.IDLE;
    }

    public void registerTask(Task task){
        tasks.add(task);
    }

    public Task getRegisteredTask(ITaskType taskType){
        for(var task : tasks){
            if(task instanceof LifeTask lifeTask && taskType instanceof LifeTaskType lifeTaskType){
                if(lifeTask.getType() == lifeTaskType) return task;
            }
            else if(task instanceof SkbTask skbTask && taskType instanceof SkbTaskType skbTaskType){
                if(skbTask.getType() == skbTaskType) return task;
            }
        }
        return null;
    }

    private void registerAllSkbTasks(){
        registerTask(new GettingEyesTask());
    }

    private void registerAndEnableAllLifeTasks(){
        registerTask(new FunctionHubTask());
        enable(LifeTaskType.FUNCTION_HUB);

        registerTask(new AutoRejoinSkyblockTask());
        enable(LifeTaskType.AUTO_REJOIN_SKYBLOCK);

        registerTask(new AutoTipTask());
        enable(LifeTaskType.AUTO_TIP);
    }
}
