package org.ricey_yam.skywiper.client.task.skb;

import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;

@Getter
@Setter
public class GettingEyesTask extends SkbTask{
    public enum SubState{
        RUSHING_TO_ZEALOT_HIDEOUT,
        FINDING_ZEALOT,
        KILLING_ZEALOT
    }
    private SubState subState;

    public GettingEyesTask() {
        setType(SkbTaskType.GETTING_EYES);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        /// 玩家必须在Skyblock才能执行此Task
        if(!SkbPlayerHelper.isOnSkyblock()) return;

        switch (getSubState()) {
            case RUSHING_TO_ZEALOT_HIDEOUT -> rushingToZealotHideoutTick();

            case FINDING_ZEALOT -> findingZealotTick();

            case KILLING_ZEALOT -> killZealotTick();
        }
    }

    private void transitionToRushingToZealotHideout() {
        setSubState(SubState.RUSHING_TO_ZEALOT_HIDEOUT);
    }
    private void transitionToFindingZealot() {
        setSubState(SubState.FINDING_ZEALOT);
    }
    private void transitionToKillingZealot() {
        setSubState(SubState.KILLING_ZEALOT);
    }

    private void rushingToZealotHideoutTick() {

    }
    private void findingZealotTick() {

    }
    private void killZealotTick() {

    }

    @Override
    public void stop() {
        super.stop();
    }
}
