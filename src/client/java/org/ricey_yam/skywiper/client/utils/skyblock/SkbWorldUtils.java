package org.ricey_yam.skywiper.client.utils.skyblock;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.world.GameMode;
import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

import java.util.Comparator;

public class SkbWorldUtils {
    public static final Ordering<PlayerListEntry> playerOrdering = Ordering.from(new PlayerComparator());

    public static String copyContainsLine(String str) {
        var client = ClientUtils.getClient();

        if (client.getNetworkHandler() == null) return null;

        var players = playerOrdering.sortedCopy(client.getNetworkHandler().getPlayerList());
        for (PlayerListEntry info : players) {
            var name = client.inGameHud.getPlayerListHud().getPlayerName(info).getString();
            if (name.contains(str)) return name;
        }

        return null;
    }

    public static String getAreaName() {
        var temp = copyContainsLine("Area: ");
        if (temp != null) {
            return temp.replace("Area: ", "");
        }
        return "";
    }

    static class PlayerComparator implements Comparator<PlayerListEntry> {
        public int compare(PlayerListEntry p1, PlayerListEntry p2) {
            var team1 = p1.getScoreboardTeam();
            var team2 = p2.getScoreboardTeam();
            return ComparisonChain.start()
                    .compareTrueFirst(p1.getGameMode() != GameMode.SPECTATOR,
                            p2.getGameMode() != GameMode.SPECTATOR)
                    .compare(team1 != null ? team1.getName() : "", team2 != null ? team2.getName() : "")
                    .compare(p1.getProfile().getName(), p2.getProfile().getName()).result();
        }
    }
}
