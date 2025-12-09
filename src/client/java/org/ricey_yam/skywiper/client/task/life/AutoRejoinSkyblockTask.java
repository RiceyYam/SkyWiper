package org.ricey_yam.skywiper.client.task.life;

import org.ricey_yam.skywiper.client.task.TaskState;

public class AutoRejoinSkyblockTask extends LifeTask {
    @Override
    public void start() {
        setState(TaskState.IDLE);
    }

    @Override
    public void tick() {
        /// 有SkbTask激活的时候才会自动重新加入Skyblock

    }

    @Override
    public void stop() {
        setState(TaskState.STOPPED);
    }
}
