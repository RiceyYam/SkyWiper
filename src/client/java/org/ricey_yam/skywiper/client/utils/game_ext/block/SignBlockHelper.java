package org.ricey_yam.skywiper.client.utils.game_ext.block;

import net.minecraft.block.entity.SignText;

public class SignBlockHelper {
    public static String getjointedString(SignText signText) {
        var result = new StringBuilder();
        if(signText == null) return null;
        var messages = signText.getMessages(false);
        for (int i = 0; i < messages.length; i++) {
            var text = messages[i];
            result.append(text.getString()).append(i == messages.length - 1 ? "" : "\n");
        }
        return result.toString();
    }
}
