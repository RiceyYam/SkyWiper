package org.ricey_yam.skywiper.client.utils.skyblock.entity.player;

import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import org.ricey_yam.skywiper.client.utils.mixin_accessor.SignBlockEntityAccessor;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;
import org.ricey_yam.skywiper.client.utils.game_ext.block.BlockUtils;
import org.ricey_yam.skywiper.client.utils.game_ext.block.SignBlockHelper;
import org.ricey_yam.skywiper.client.utils.format.pattern.ScoreboardPattern;
import org.ricey_yam.skywiper.client.utils.skyblock.ScoreboardUtils;
import org.ricey_yam.skywiper.client.utils.skyblock.message.StatusMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SkbPlayerHelper {
    @Getter
    private static final SkbPlayerStatus playerStatus = new SkbPlayerStatus();

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

    public static boolean isInHypixelServer(){
        return isOnSkyblock() || isOnHypixelLobby();
    }

    public static boolean isOnHypixelLobby(){
        var world = ClientUtils.getWorld();
        if(world == null) return false;
        for (var scoreboard : ClientUtils.getWorld().getScoreboard().getObjectives()) {
            if (scoreboard.getDisplayName().getString().contains("HYPIXEL")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOnSkyblock(){
        var world = ClientUtils.getWorld();
        if(world == null) return false;
        for (var scoreboard : ClientUtils.getWorld().getScoreboard().getObjectives()) {
            if (scoreboard.getDisplayName().getString().contains("SKYBLOCK")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInLimbo(){
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

    public static void refreshPlayerStatus(StatusMessage statusMessage){
        playerStatus.setHealth(StatusValue.getNewStatusValue(playerStatus.getHealth(),statusMessage.getHealth()));
        playerStatus.setMaxHealth(StatusValue.getNewStatusValue(playerStatus.getMaxHealth(),statusMessage.getMaxHealth()));

        playerStatus.setMana(StatusValue.getNewStatusValue(playerStatus.getMana(),statusMessage.getMana()));
        playerStatus.setMaxMana(StatusValue.getNewStatusValue(playerStatus.getMaxMana(),statusMessage.getMaxMana()));

        playerStatus.setDefense(StatusValue.getNewStatusValue(playerStatus.getDefense(),statusMessage.getDefense()));
    }

    /// 获取玩家背包全部物品
    public static List<String> getClientPlayerHotbarItemNames(){
        var result = new ArrayList<String>();
        var player = ClientUtils.getPlayer();
        if (player != null) {
            var inventory = player.getInventory();
            var start = 0;
            var end = 9;
            var sectionOfInventory = new ArrayList<>(inventory.getMainStacks());
            if(!sectionOfInventory.isEmpty()) {
                var j = 0;
                for (int i = 0; i < end - start; i++) {
                    var itemStack = sectionOfInventory.get(start + i);
                    result.add(itemStack != null && itemStack.getCustomName() != null? itemStack.getCustomName().getString() : "air");
                }
            }
        }
        return result;
    }
    public static List<String> getClientPlayerInventoryItemNames(){
        var player = MinecraftClient.getInstance().player;
        var result = new ArrayList<String>();
        if (player != null) {
            var stacks = player.getInventory().getMainStacks();
            for (int i = 0; i < stacks.size(); i++) {
                var itemStack = stacks.get(i);
                result.add(Objects.requireNonNull(itemStack.getCustomName()).getString());
            }
        }
        return result;
    }

    /// 玩家手持物品信息
    public static String getHoldingItemName(){
        var player = ClientUtils.getPlayer();
        if(player == null) return null;
        if(player.getMainHandStack().isEmpty()) return null;
        return player.getMainHandStack().getItemName().getString();
    }

    public static boolean hasItemInHotbar(String itemName){
        for(var hbItemName : getClientPlayerHotbarItemNames()){
            if(hbItemName.contains(itemName)) return true;
        }
        return false;
    }

    public static boolean  hasItemInInventory(String itemName){
        for(var invItemName : getClientPlayerInventoryItemNames()){
            if(invItemName.contains(itemName)) return true;
        }
        return false;
    }
}
