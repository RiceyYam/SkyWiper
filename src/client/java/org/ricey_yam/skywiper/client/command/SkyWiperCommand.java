package org.ricey_yam.skywiper.client.command;

import baritone.api.pathing.goals.GoalBlock;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.util.math.BlockPos;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.command.argument.ClientBlockPosArgumentExt;
import org.ricey_yam.skywiper.client.event.EventManager;
import org.ricey_yam.skywiper.client.task.life.FunctionHubTask;
import org.ricey_yam.skywiper.client.task.skb.SkbTaskType;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;
import org.ricey_yam.skywiper.client.utils.game_ext.EntityUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.SkbEntityUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;

public class SkyWiperCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("sw")
                .then(ClientCommandManager.literal("debug")
                        .then(ClientCommandManager.literal("check_in_limbo")
                                .executes(DebugExecutor::checkInLimbo)
                        )
                        .then(ClientCommandManager.literal("get_area")
                                .executes(DebugExecutor::getAreaName)
                        )
                        .then(ClientCommandManager.literal("nearest_entity")
                                .executes(DebugExecutor::getNearestEntityName)
                        )
                        .then(ClientCommandManager.literal("hotbar_items")
                                .executes(DebugExecutor::listHotbarItems)
                        )
                        .then(ClientCommandManager.literal("right_click")
                                .executes(DebugExecutor::rightClick)
                        )
                        .then(ClientCommandManager.literal("left_click")
                                .executes(DebugExecutor::leftClick)
                        )
                )
                .then(ClientCommandManager.literal("module")
                        .then(ClientCommandManager.literal("get_eye")
                                .executes(ModuleExecutor::getEyeTask)
                        )
                        .then(ClientCommandManager.literal("path")
                                .then(ClientCommandManager.argument("x", StringArgumentType.string())
                                        .then(ClientCommandManager.argument("y", StringArgumentType.string())
                                                .then(ClientCommandManager.argument("z", StringArgumentType.string())
                                                        .executes(ModuleExecutor::pathTo)
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static class DebugExecutor{
        private static int checkInLimbo(CommandContext<FabricClientCommandSource> context) {
            SkyWiperClient.sendLogMessage("当前是否处于Limbo：" + SkbPlayerHelper.isInLimbo());
            return 1;
        }

        private static int getAreaName(CommandContext<FabricClientCommandSource> context) {
            var location = SkbPlayerHelper.getPlayerLocation();

            if(!SkbPlayerHelper.isOnSkyblock()) {
                SkyWiperClient.sendWarningMessage("玩家不在Skyblock！");
                return 0;
            }

            SkyWiperClient.sendLogMessage("当前玩家处于Skyblock的区域：" + SkbPlayerHelper.getPlayerLocation());
            return 1;
        }

        private static int getNearestEntityName(CommandContext<FabricClientCommandSource> context) {
            if(!SkbPlayerHelper.isOnSkyblock()) {
                SkyWiperClient.sendWarningMessage("玩家不在Skyblock！");
                return 0;
            }

            var skbEntity =  SkbEntityUtils.findNearestSkbEntity(ClientUtils.getPlayer(),5);
            if(skbEntity == null){
                SkyWiperClient.sendWarningMessage("附近没有实体！");
            }

            else{
                SkyWiperClient.sendLogMessage(
                        "\n****最近的实体信息****" +
                                "\n*昵称* " + skbEntity.getName() +
                                "\n*血量* " + skbEntity.getHealth() + " / " + skbEntity.getMaxHealth() +
                                "\n*绑定实体昵称* " + EntityUtils.getEntityDisplayName(skbEntity.getBoundEntity()));
                skbEntity.release();
            }
            return 1;
        }

        private static int listHotbarItems(CommandContext<FabricClientCommandSource> context) {
            if(!SkbPlayerHelper.isOnSkyblock()) return 0;
            var itemNames = SkbPlayerHelper.getClientPlayerHotbarItemNames();
            for(var itemName : itemNames){
                SkyWiperClient.sendLogMessage("快捷栏物品||" + itemNames.indexOf(itemName) + "||" + itemName);
            }
            return 1;
        }

        private static int rightClick(CommandContext<FabricClientCommandSource> context){
            var interactionSubTask = FunctionHubTask.getInstance().getInteractionSubTask();
            if(interactionSubTask == null) return 0;
            interactionSubTask.rightClick();
            SkyWiperClient.sendLogMessage("模拟了右键点击！");
            return 1;
        }

        private static int leftClick(CommandContext<FabricClientCommandSource> context){
            var interactionSubTask = FunctionHubTask.getInstance().getInteractionSubTask();
            if(interactionSubTask == null) return 0;
            interactionSubTask.leftClick();
            SkyWiperClient.sendLogMessage("模拟了左键点击！");
            return 1;
        }
    }

    private static class ModuleExecutor{
        private static int pathTo(CommandContext<FabricClientCommandSource> context) {
            var functionHub = FunctionHubTask.getInstance();
            var pathingSubTask = functionHub.getPathingSubTask();
            if(pathingSubTask.isEnabled()){
                pathingSubTask.disable();
                SkyWiperClient.sendLogMessage("取消了寻路任务！");
                return 1;
            }
            var x = StringArgumentType.getString(context, "x");
            var y = StringArgumentType.getString(context, "y");
            var z = StringArgumentType.getString(context, "z");
            var handledX = ClientBlockPosArgumentExt.getRelativeBlockPosValue(ClientBlockPosArgumentExt.POSITION.X,x);
            var handledY = ClientBlockPosArgumentExt.getRelativeBlockPosValue(ClientBlockPosArgumentExt.POSITION.Y,y);
            var handledZ = ClientBlockPosArgumentExt.getRelativeBlockPosValue(ClientBlockPosArgumentExt.POSITION.Z,z);
            var pos = new BlockPos(handledX, handledY, handledZ);
            SkyWiperClient.sendLogMessage("正在寻路到 X:" + handledX + " Y:" + handledY + " Z:" + handledZ);
            pathingSubTask.enable(new GoalBlock(pos),pos);
            return 1;
        }

        private static int getEyeTask(CommandContext<FabricClientCommandSource> context){
            var event = EventManager.END_TICK_EVENT;
            if(event.isTaskEnabled(SkbTaskType.GETTING_EYES)) {
                SkyWiperClient.sendLogMessage("自动刷眼已关闭！");
                event.disable(SkbTaskType.GETTING_EYES);
            }
            else {
                SkyWiperClient.sendLogMessage("自动刷眼已开启！");
                event.enable(SkbTaskType.GETTING_EYES);
            }
            return 1;
        }
    }
}
