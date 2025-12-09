package org.ricey_yam.skywiper.client.task.life;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.task.Task;

@Getter
@Setter
public abstract class LifeTask extends Task {
    protected LifeTaskType type;
}
