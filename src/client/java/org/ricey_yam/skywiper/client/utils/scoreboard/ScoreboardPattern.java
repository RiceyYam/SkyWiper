package org.ricey_yam.skywiper.client.utils.scoreboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoreboardPattern {
    private static final Pattern LOCATION_PATTERN = Pattern.compile("\\s*[⏣ф✈]\\s+(?<location>.*)");
    private static final Pattern AREA_PATTERN = Pattern.compile("Area:\\s+(?<location>.*)");
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    public static String getGroup(Matcher matcher, String groupName) {
        if (matcher.matches() && matcher.group(groupName) != null) {
            return matcher.group(groupName);
        }
        return null;
    }

    /// 获取玩家所处Location
    public static String extractLocation(String input) {
        var cleanInput = STRIP_COLOR_PATTERN.matcher(input).replaceAll("");

        var m1 = LOCATION_PATTERN.matcher(cleanInput);
        if (m1.find()) return m1.group("location").trim();

        var m2 = AREA_PATTERN.matcher(cleanInput);
        if (m2.find()) return m2.group("location").trim();

        return null;
    }
}
