package org.ricey_yam.skywiper.client.task.skb;

import baritone.api.pathing.goals.GoalNear;
import lombok.Getter;
import lombok.Setter;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.utils.game_ext.EntityUtils;
import org.ricey_yam.skywiper.client.utils.game_ext.TransformUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.SkbEntity;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.SkbEntityUtils;

@Getter
@Setter
public class GettingEyesTask extends SkbTask{
    public enum SubState{
        RUSHING_TO_ZEALOT_HIDEOUT,
        FINDING_ZEALOT,
        KILLING_ZEALOT
    }
    private SubState subState;
    private SkbEntity currentZealotTarget;

    public GettingEyesTask() {
        setType(SkbTaskType.GETTING_EYES);
    }

    @Override
    public void start() {
        super.start();
        setSubState(SubState.RUSHING_TO_ZEALOT_HIDEOUT);
    }

    @Override
    public void tick() {
        super.tick();
        switch (getSubState()) {
            case RUSHING_TO_ZEALOT_HIDEOUT -> rushingToZealotHideoutTick();

            case FINDING_ZEALOT -> findingZealotTick();

            case KILLING_ZEALOT -> killZealotTick();
        }
    }

    private void transitionToRushingToZealotHideout() {
        getLookingSubTask().disable();
        getPathingFuncTask().disable();
        setSubState(SubState.RUSHING_TO_ZEALOT_HIDEOUT);
    }
    private void transitionToFindingZealot() {
        getLookingSubTask().disable();
        getPathingFuncTask().disable();
        setSubState(SubState.FINDING_ZEALOT);
    }
    private void transitionToKillingZealot() {
        getPathingFuncTask().disable();
        setSubState(SubState.KILLING_ZEALOT);
    }

    private void rushingToZealotHideoutTick() {
        if(isInZealotHideout()) {
            transitionToFindingZealot();
        }
    }
    private void findingZealotTick() {
        if(!isInZealotHideout()){
            transitionToRushingToZealotHideout();
            return;
        }

        updateZealotTarget();
        if(isTargetDied(currentZealotTarget)){
            SkbEntity.getInstancePool().returnInstance(currentZealotTarget);
            return;
        }
        if(currentZealotTarget != null) {
            transitionToKillingZealot();
        }
    }
    private void killZealotTick() {
        if(!isInZealotHideout()){
            transitionToRushingToZealotHideout();
            return;
        }

        if(isTargetDied(currentZealotTarget)){
            transitionToFindingZealot();
            return;
        }

        if(isNearbyZealotTarget()){
            var lookingPos = TransformUtils.getEntityCenterPos(currentZealotTarget.getBoundEntity());
            var randomLookingTickDuration = SkyWiperClient.getRandom().nextInt(6) + 6;
            if(getLookingSubTask().isLookingAt(lookingPos)){
                getLookingSubTask().disable();
                System.out.println("看向目标了！");
                //todo attack
            }
            else{
                System.out.println("尝试看向目标");
                getLookingSubTask().enable(lookingPos,randomLookingTickDuration);
            }
            getPathingFuncTask().disable();
        }
        else{
            System.out.println("正在走向目标！");
            var targetPos = currentZealotTarget.getBoundEntity().getBlockPos();
            var newGoal = new GoalNear(currentZealotTarget.getBoundEntity().getBlockPos(),2);
            getPathingFuncTask().enable(newGoal,targetPos);
        }
    }

    private void updateZealotTarget(){
        this.currentZealotTarget = SkbEntityUtils.findNearestSkbEntity(getPlayer(),100, e -> !isTargetDied(e) && e.getName().contains("Enderman"));
        //this.currentZealotTarget = SkbEntityUtils.findNearestSkbEntity(getPlayer(),100, e -> !isTargetDied(e) && e.getName().contains("Zealot"));
    }
    private boolean isInZealotHideout(){
        return true;
        //return SkbPlayerHelper.getPlayerLocation().contains("Zealot Bruiser Hideout");
    }
    private boolean isNearbyZealotTarget(){
        return getPlayer().distanceTo(currentZealotTarget.getBoundEntity()) <= 3;
    }
    private boolean isTargetDied(SkbEntity skbEntity){
        return skbEntity == null || skbEntity.getBoundEntity() == null || skbEntity.getHealth() <= 0;
    }

    @Override
    public void stop() {
        super.stop();
        getPathingFuncTask().disable();
    }
}
