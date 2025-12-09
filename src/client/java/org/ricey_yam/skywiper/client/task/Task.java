package org.ricey_yam.skywiper.client.task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Task {
    protected TaskState state;

    public void start(){
        setState(TaskState.IDLE);
    }

    public abstract void tick();

    public void stop(){
        setState(TaskState.STOPPED);
    }
}
