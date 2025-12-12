package org.ricey_yam.skywiper.client.task.skb;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.task.Task;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;

@Getter
@Setter
public abstract class SkbTask extends Task {
    protected SkbTaskType type;

    @Override
    public void tick() {
        super.tick();
        /// 玩家必须在Skyblock才能执行此Task
        if(!SkbPlayerHelper.isOnSkyblock()) return;
    }
}
