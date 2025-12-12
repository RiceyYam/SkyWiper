package org.ricey_yam.skywiper.client.task.life;

import baritone.api.IBaritone;
import baritone.api.behavior.IPathingBehavior;
import baritone.api.pathing.goals.Goal;
import baritone.api.process.ICustomGoalProcess;
import baritone.api.utils.Rotation;
import baritone.api.utils.RotationUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.baritone.BaritoneManager;
import org.ricey_yam.skywiper.client.event.EventManager;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;
import org.ricey_yam.skywiper.client.utils.client.interaction.Button;
import org.ricey_yam.skywiper.client.utils.client.interaction.LeftMouseButton;
import org.ricey_yam.skywiper.client.utils.client.interaction.RightMouseButton;
import org.ricey_yam.skywiper.client.utils.game_ext.TransformUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.SkbItemUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class FunctionHubTask extends LifeTask{
    public static FunctionHubTask getInstance(){
        return (FunctionHubTask) EventManager.END_TICK_EVENT.getRegisteredTask(LifeTaskType.FUNCTION_HUB);
    }

    private final List<FunctionSubTask> functionSubTasks = new ArrayList<>();
    private final PathingSubTask pathingSubTask = new PathingSubTask();
    private final LookingSubTask lookingSubTask = new LookingSubTask();
    private final InvManagerSubTask invManagerSubTask = new InvManagerSubTask();
    private final InteractionSubTask interactionSubTask = new InteractionSubTask();

    public FunctionHubTask(){
        setType(LifeTaskType.FUNCTION_HUB);
        functionSubTasks.addAll(List.of(pathingSubTask,lookingSubTask,invManagerSubTask,interactionSubTask));
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void tick() {
        super.tick();
        functionSubTasks.sort(Comparator.comparing(FunctionSubTask::getWeight));
        FunctionSubTask lastTickedSubTask = null;
        for (int i = 0; i < functionSubTasks.size(); i++) {
            var subTask = functionSubTasks.get(i);
            if(subTask == null) continue;
            if(lastTickedSubTask != null && lastTickedSubTask.getWeight() > subTask.getWeight()){
                return;
            }
            subTask.tick();
            lastTickedSubTask = subTask;
        }
    }

    @Override
    public void stop() {
        super.stop();
        pathingSubTask.disable();
    }

    @Getter
    @Setter
    public static abstract class FunctionSubTask {
        protected int weight;
        protected boolean enabled;

        public void enable(Object... args){
            this.enabled = true;
        }

        public void disable(Object... args){
            this.enabled = false;
        }

        protected abstract void tick();

        protected PlayerEntity getPlayer(){
            return ClientUtils.getPlayer();
        }

        protected IBaritone getBaritone(){
            return BaritoneManager.getClientBaritone();
        }
    }

    @Getter
    @Setter
    public static class InteractionSubTask extends FunctionSubTask{
        private List<Button> buttons = new ArrayList<>();
        private LeftMouseButton leftMouseButton = new LeftMouseButton();
        private RightMouseButton rightMouseButton = new RightMouseButton();

        public InteractionSubTask(){
            this.weight = 1;
            buttons.addAll(List.of(leftMouseButton, rightMouseButton));
        }

        @Override
        protected void tick() {
            refreshButtonInfo();
            for(var button : buttons){
                if(button == null || button.isEmpty()) continue;
                button.update();
            }
        }

        public void refreshButtonInfo(){
            var options = ClientUtils.getOptions();
            if(leftMouseButton.isEmpty()) leftMouseButton = new LeftMouseButton(options.quickActionsKey);
            if(rightMouseButton.isEmpty()) rightMouseButton = new RightMouseButton(options.useKey);
        }

        public void leftClick(){
            leftMouseButton.click();
        }

        public void rightClick(){
            rightMouseButton.click();
        }
    }

    @Getter
    @Setter
    public static class InvManagerSubTask extends FunctionSubTask {
        public InvManagerSubTask(){
            this.weight = 1;
        }

        @Override
        protected void tick() {
        }

        public boolean switchToHotbarItem(String itemName) {
            var player = getPlayer();
            if (player == null) return false;
            var hotbarItemNames = SkbPlayerHelper.getClientPlayerHotbarItemNames();
            for (int slotIndex = 0; slotIndex < 9; slotIndex++) {
                var stackName = hotbarItemNames.get(slotIndex);
                if (stackName.contains(itemName)) {
                    player.getInventory().setSelectedSlot(slotIndex);
                    return true;
                }
            }
            return false;
        }
    }

    @Getter
    @Setter
    public static class PathingSubTask extends FunctionSubTask {
        private Goal goal;
        private BlockPos possiblePos;
        private ICustomGoalProcess customGoalProcess;
        private IPathingBehavior pathingBehavior;

        private String selectedTeleportToolItemName;
        private int teleportTickTimer;
        private int teleportTickDelay = 10;

        public PathingSubTask(){
            this.weight = 1;
            this.customGoalProcess = getBaritone().getCustomGoalProcess();
            this.pathingBehavior = getBaritone().getPathingBehavior();
        }

        @Override
        protected void tick() {
            if(!enabled || goal == null || customGoalProcess == null) return;
            if(goal.isInGoal(getPlayer().getBlockPos())) {
                disable();
                return;
            }
            teleportTickTimer++;
            var lookingTask = FunctionHubTask.getInstance().getLookingSubTask();
            if(teleportTickTimer >= teleportTickDelay && isTeleportablePos(possiblePos) && isTeleportInHotbar() && lookingTask.isLookingAt(Vec3d.of(possiblePos.down()))){
                updateSelectedTeleportToolItemName();

                if(selectedTeleportToolItemName == null || selectedTeleportToolItemName.isEmpty()) return;

                teleportTickTimer = 0;
                teleportTickDelay = SkyWiperClient.getRandom().nextInt(52) + 10;
                if(lookingTask.isLookingAt(Vec3d.of(possiblePos.down()))){
                    lookingTask.disable();
                    aspectTeleport();
                }
            }
            else {
                var randomLookingTickDuration = SkyWiperClient.getRandom().nextInt(2) + 1;
                lookingTask.enable(Vec3d.of(possiblePos.down()),randomLookingTickDuration);
            }
            customGoalProcess.setGoalAndPath(goal);
        }

        @Override
        public void enable(Object... args) {
            super.enable(args);
            this.goal = (Goal) args[0];
            this.possiblePos = (BlockPos) args[1];
            teleportTickDelay = SkyWiperClient.getRandom().nextInt(7) + 3;
            customGoalProcess.setGoalAndPath(goal);
        }

        @Override
        public void disable(Object... args) {
            if(!enabled) return;
            super.disable(args);
            this.goal = null;
            customGoalProcess.setGoal(null);
            pathingBehavior.cancelEverything();
        }

        public boolean isTeleportablePos(BlockPos pos) {
            if (pos == null) return false;
            if(TransformUtils.getDistance(getPlayer().getBlockPos(),pos) < 5) return false;
            return RotationUtils.reachable(getBaritone().getPlayerContext(), pos, 8, false).isEmpty();
        }

        public boolean isTeleportInHotbar(){
            return SkbPlayerHelper.hasItemInHotbar("Aspect of the End") || SkbPlayerHelper.hasItemInHotbar("Aspect of the Void");
        }

        public void updateSelectedTeleportToolItemName(){
            var hotbarItemNames = SkbPlayerHelper.getClientPlayerHotbarItemNames();
            for (int i = 0; i < hotbarItemNames.size(); i++) {
                var itemName = hotbarItemNames.get(i);
                if(SkbItemUtils.isTeleportTool(itemName)){
                    selectedTeleportToolItemName = itemName;
                    return;
                }
            }
            selectedTeleportToolItemName = "";
        }

        public void aspectTeleport(){
            System.out.println("check tool items");
            if(selectedTeleportToolItemName.isEmpty()) return;
            System.out.println("check Inv task");
            var invManager = FunctionHubTask.getInstance().getInvManagerSubTask();
            var interactionSubTask = FunctionHubTask.getInstance().getInteractionSubTask();
            if(invManager == null || interactionSubTask == null) return;
            System.out.println("switch!");
            var switchSuccess = invManager.switchToHotbarItem(selectedTeleportToolItemName);
            if(switchSuccess){
                interactionSubTask.rightClick();
            }
            else{
                System.out.println("切换为目标物品失败！");
            }
        }
    }

    @Getter
    @Setter
    public static class LookingSubTask extends FunctionSubTask {
        private Vec3d targetPos;
        private int tickTimer;
        private int tickDuration;
        private Rotation startRotation;
        private Rotation endRotation;

        public LookingSubTask(){
            this.weight = 1;
        }

        @Override
        protected void tick() {
            if (!enabled || endRotation == null) return;

            tickTimer++;
            float progress = Math.min(1f, (float) tickTimer / tickDuration);

            float y = TransformUtils.slerp(startRotation.getYaw(),  endRotation.getYaw(),  progress, true);
            float p = TransformUtils.slerp(startRotation.getPitch(), endRotation.getPitch(), progress, false);

            getPlayer().setYaw(y);
            getPlayer().setPitch(p);

            if (progress >= 1f || isLookingAt(targetPos)) disable();
        }

        @Override
        public void enable(Object... args) {
            super.enable(args);
            tickTimer = 0;
            targetPos = (Vec3d) args[0];
            tickDuration = args.length >= 2 ? (int) args[1] : 6;
            endRotation = TransformUtils.getRotation(getPlayer().getEyePos(), targetPos);
            startRotation = new Rotation(getPlayer().getYaw(), getPlayer().getPitch());
        }

        @Override
        public void disable(Object... args) {
            if(!enabled) return;
            super.disable(args);
            tickTimer = 0;
            tickDuration = 9999;
            startRotation = null;
            endRotation = null;
            targetPos = null;
        }

        public boolean isLookingAt(Vec3d pos){
            var baritone = BaritoneManager.getClientBaritone();
            if (baritone == null) return false;

            var ctx = baritone.getPlayerContext();

            var player = ctx.player();

            var eyePosition = player.getEyePos();

            var idealRotation = TransformUtils.getRotation(eyePosition, pos);

            var currentRotation = ctx.playerRotations();

            double yawDiff = Math.abs(TransformUtils.to180(idealRotation.getYaw() - currentRotation.getYaw()));
            double pitchDiff = Math.abs(idealRotation.getPitch() - currentRotation.getPitch());

            double totalDiff = Math.sqrt(yawDiff * yawDiff + pitchDiff * pitchDiff);

            return totalDiff < 2D;
        }
    }
}
