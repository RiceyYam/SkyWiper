package org.ricey_yam.skywiper.client.utils.format;

public class LongUtils {
    public static long parseHumanReadableNumber(String humanReadableNumber){
        var removedComma = humanReadableNumber.replaceAll(",", "");
        var toLower = removedComma.toLowerCase();
        var result = Long.parseLong(toLower);
        if(humanReadableNumber.contains("k")) {
            result = Long.parseLong(toLower.replaceAll("k", "")) * 1000;
        }
        else if(humanReadableNumber.contains("m")) {
            result = Long.parseLong(toLower.replaceAll("m", "")) * 1000000;
        }
        else if(humanReadableNumber.contains("b")) {
            result = Long.parseLong(toLower.replaceAll("b", "")) * 1000000000;
        }
        return result;
    }
}
