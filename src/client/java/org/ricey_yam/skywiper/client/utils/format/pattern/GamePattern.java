package org.ricey_yam.skywiper.client.utils.format.pattern;

import java.util.regex.Pattern;

public class GamePattern {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[a-z0-9A-Z]");
    public static String cleanColorSymbol(String string) {
        return STRIP_COLOR_PATTERN.matcher(string).replaceAll("").replaceAll("§","");
    }
}
