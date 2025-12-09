package org.ricey_yam.skywiper.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.ricey_yam.skywiper.client.SkyWiperClient;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;
import org.ricey_yam.skywiper.client.utils.game_ext.EntityUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.SkbEntityUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.entity.player.SkbPlayerHelper;

public class SkyWiperCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("sw")
                .then(ClientCommandManager.literal("debug")
                        .then(ClientCommandManager.literal("check_in_limbo")
                                .executes(SkyWiperCommand::checkInLimbo)
                        )
                        .then(ClientCommandManager.literal("get_area")
                                .executes(SkyWiperCommand::getAreaName)
                        )
                        .then(ClientCommandManager.literal("nearest_entity")
                                .executes(SkyWiperCommand::getNearestEntityName)
                        )
                )
        );
    }

    private static int checkInLimbo(CommandContext<FabricClientCommandSource> context) {
        SkyWiperClient.sendLogMessage("当前是否处于Limbo：" + SkbPlayerHelper.isOnLimbo());
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
}
