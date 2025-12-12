package org.ricey_yam.skywiper.client.utils.skyblock;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ScoreboardUtils {
    public static Scoreboard getScoreboard() {
        var world = ClientUtils.getWorld();
        if(world == null)  return null;
        return world.getScoreboard();
    }

    public static List<String> getScoreboardLineTextStrList(){
        var result = new ArrayList<String>();
        var scoreboard = getScoreboard();
        if (scoreboard != null) {
            var objectiveForSlot = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
            if(objectiveForSlot == null){
                System.out.println("无计分板！");
                return result;
            }
            var scores = new ArrayList<>(scoreboard.getScoreboardEntries(objectiveForSlot));
            scores.sort(Comparator.comparingInt(ScoreboardEntry::value).reversed().thenComparing(ScoreboardEntry::owner));
            for (var score : scores) {
                var owner = score.owner();
                var team = scoreboard.getScoreHolderTeam(owner);
                var lineTextStr = Team.decorateName(team, Text.literal(owner)).getString();
                result.add(lineTextStr);
            }
        }
        return result;
    }
}
