package org.ricey_yam.skywiper.client.task.skb;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.task.Task;

@Getter
@Setter
public abstract class SkbTask extends Task {
    protected SkbTaskType type;
}
