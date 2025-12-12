package org.ricey_yam.skywiper.client.command.argument;


import org.ricey_yam.skywiper.client.utils.client.ClientUtils;

import java.util.regex.Pattern;

public class ClientBlockPosArgumentExt {
    public enum POSITION {
        X,
        Y,
        Z
    }
    private static final Pattern RELATIVE_SYMBOL_PATTERN = Pattern.compile("~(?<relativeValue>-?\\d+)");
    public static int getRelativeBlockPosValue(POSITION part,String valueStr){
        valueStr = valueStr.replaceAll("\"","");
        var baseValue = 0;
        switch (part){
            case X -> baseValue = ClientUtils.getPlayer().getBlockX();
            case Y -> baseValue = ClientUtils.getPlayer().getBlockY();
            case Z -> baseValue = ClientUtils.getPlayer().getBlockZ();
        }
        try{
            var matcher = RELATIVE_SYMBOL_PATTERN.matcher(valueStr);
            if(matcher.find()){
                var relativeValue = Integer.parseInt(matcher.group("relativeValue"));
                baseValue += relativeValue;
            }
            return baseValue;
        }
        catch(Exception e){
            e.printStackTrace();
            return baseValue;
        }
    }
}
