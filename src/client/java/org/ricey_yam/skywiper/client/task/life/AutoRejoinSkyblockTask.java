package org.ricey_yam.skywiper.client.task.life;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.utils.client.ChatHelper;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;

import java.util.List;

@Getter
@Setter
public class AutoRejoinSkyblockTask extends LifeTask {
    private final static List<String> lobbyCommands = List.of("l","lobby");
    private int checkDelay;

    public AutoRejoinSkyblockTask() {
        setType(LifeTaskType.AUTO_REJOIN_SKYBLOCK);
    }

    @Override
    public void start() {
        super.start();
        tickTimer = -40;
        checkDelay = getRandomCheckDelay();
    }

    @Override
    public void tick() {
        super.tick();
        if(tickTimer >= checkDelay){
            tickTimer = 0;
            if(SkbPlayerHelper.isOnSkyblock()) return;
            if(SkbPlayerHelper.isInLimbo()){
                var escapingCommand = lobbyCommands.get(SkyWiperClient.getRandom().nextInt(2) == 0 ? 0 : 1);
                SkyWiperClient.sendLogMessage("正在逃离Limbo！ Command: /" + escapingCommand);
                ChatHelper.sendCommand(escapingCommand);
            }
            else if(SkbPlayerHelper.isOnHypixelLobby()){
                SkyWiperClient.sendLogMessage("正在返回Skyblock！");
                ChatHelper.sendCommand("skyblock");
            }
            checkDelay = getRandomCheckDelay();
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    private int getRandomCheckDelay(){
        return SkyWiperClient.getRandom().nextInt(20) + 40;
    }
}
