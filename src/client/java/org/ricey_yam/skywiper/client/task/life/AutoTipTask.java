package org.ricey_yam.skywiper.client.task.life;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.utils.client.ChatHelper;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;

@Getter
@Setter
public class AutoTipTask extends LifeTask {
    private int tipTickDuration;

    public AutoTipTask() {
        setType(LifeTaskType.AUTO_TIP);
    }

    @Override
    public void start() {
        super.start();
        tipTickDuration = getRandomTipTickDuration();
        tickTimer = tipTickDuration - 100;
    }

    @Override
    public void tick() {
        super.tick();
        if(tickTimer >= tipTickDuration) {
            tickTimer = 0;
            if(!SkbPlayerHelper.isInHypixelServer()) return;
            ChatHelper.sendCommand("tipall");
            tipTickDuration = getRandomTipTickDuration();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    private int getRandomTipTickDuration(){
        return SkyWiperClient.getRandom().nextInt(20 * 60 * 10) + 20 * 60 * 5;
    }
}
