package org.ricey_yam.skywiper.client.utils.skyblock.entity.player;

import lombok.Getter;
import org.ricey_yam.skywiper.client.utils.mixin_accessor.SignBlockEntityAccessor;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;
import org.ricey_yam.skywiper.client.utils.game_ext.block.BlockUtils;
import org.ricey_yam.skywiper.client.utils.game_ext.block.SignBlockHelper;
import org.ricey_yam.skywiper.client.utils.scoreboard.ScoreboardPattern;
import org.ricey_yam.skywiper.client.utils.scoreboard.ScoreboardUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.SkbWorldUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.message.StatusMessage;

public class SkbPlayerHelper {
    @Getter
    private static final SkbPlayerStatus PLAYER_INFO = new SkbPlayerStatus();

    public static String getPlayerLocation(){
        var scoreStrList = ScoreboardUtils.getScoreboardLineTextStrList();
        for (var str : scoreStrList) {
            var extracted = ScoreboardPattern.extractLocation(str);
            if(extracted != null && !extracted.isEmpty()){
                return extracted;
            }
        }
        return "";
    }

    public static boolean isOnHypixelLobby(){
        for (var scoreboard : ClientUtils.getWorld().getScoreboard().getObjectives()) {
            if (scoreboard.getDisplayName().getString().contains("www.hypixel.net")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnSkyblock(){
        for (var scoreboard : ClientUtils.getWorld().getScoreboard().getObjectives()) {
            if (scoreboard.getDisplayName().getString().contains("SKYBLOCK")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnLimbo(){
        var limboSuitableSignBlockEntity = BlockUtils.findNearestBlock(ClientUtils.getPlayer(), 20, pos -> {
            try{
                var world = ClientUtils.getWorld();
                var se = world.getBlockEntity(pos);
                var seMixin = (SignBlockEntityAccessor)se;
                if(seMixin == null) return false;

                var frontStr = SignBlockHelper.getjointedString(seMixin.getFrontText());
                var backStr = SignBlockHelper.getjointedString(seMixin.getBackText());

                if (frontStr == null || backStr == null) {
                    return false;
                }

                var limboSignKeyWord = "to continue\nplaying!";
                return frontStr.contains(limboSignKeyWord) || backStr.contains(limboSignKeyWord);
            }
            catch(Exception e){
                return false;
            }
        });

        return limboSuitableSignBlockEntity != null;
    }

    public static boolean isOnTheEnd(){
        return SkbWorldUtils.getAreaName().equals("The End");
    }

    public static void refreshPlayerStatus(StatusMessage statusMessage){
        PLAYER_INFO.setHealth(StatusValue.getNewStatusValue(PLAYER_INFO.getHealth(),statusMessage.getHealth()));
        PLAYER_INFO.setMaxHealth(StatusValue.getNewStatusValue(PLAYER_INFO.getMaxHealth(),statusMessage.getMaxHealth()));

        PLAYER_INFO.setMana(StatusValue.getNewStatusValue(PLAYER_INFO.getMana(),statusMessage.getMana()));
        PLAYER_INFO.setMaxMana(StatusValue.getNewStatusValue(PLAYER_INFO.getMaxMana(),statusMessage.getMaxMana()));

        PLAYER_INFO.setDefense(StatusValue.getNewStatusValue(PLAYER_INFO.getDefense(),statusMessage.getDefense()));
    }
}
