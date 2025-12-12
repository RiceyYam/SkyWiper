package org.ricey_yam.skywiper.client.utils.skyblock;

import java.util.List;

public class SkbItemUtils {
    private static final List<String> TELEPORT_TOOLS = List.of("Aspect of the Void","Aspect of the End");

    public static boolean isTeleportTool(String itemName){
        for(var aot : TELEPORT_TOOLS){
            if(itemName.contains(aot)){
                return true;
            }
        }
        return false;
    }
}
