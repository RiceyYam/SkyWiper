package org.ricey_yam.skywiper.client.event;

import net.minecraft.text.Text;
import org.ricey_yam.skywiper.client.utils.skyblock.message.SkbMessageManager;

public class ReceiveMessageEvent {
    public boolean onMessage(Text message, boolean overlay){
        var skbMessage = SkbMessageManager.getHandleReceivedMessage(message, overlay);
        //System.out.println("新消息：" + message.getString());
        if (skbMessage != null) {
            skbMessage.onReceive();
        }
        return true;
    }
}
