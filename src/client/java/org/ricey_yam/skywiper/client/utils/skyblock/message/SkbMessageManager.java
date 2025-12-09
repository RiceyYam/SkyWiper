package org.ricey_yam.skywiper.client.utils.skyblock.message;

import lombok.Getter;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class SkbMessageManager {
    @Getter
    private static final StatusMessage STATUS_MESSAGE_INSTANCE = new StatusMessage();

    private static final Pattern STATUS_MESSAGE_PATTERN = Pattern.compile(
            "^(?=.*?(§c(?<health>\\d+)/(?<maxHealth>\\d+)❤))?" +
                    "(?=.*?(§a(?<defense>\\d+)§a❈\\s*Defense))?" +
                    "(?=.*?(§b(?<mana>\\d+)/(?<maxMana>\\d+)✎\\s*Mana))?" +
                    ".*$"
    );

    public static ISkbMessage getHandleReceivedMessage(Text message, boolean overlay){
        try{
            var messageStr = message.getString();

            /// 状态信息
            var statusMatcher = STATUS_MESSAGE_PATTERN.matcher(messageStr);
            if(statusMatcher.find()){
                STATUS_MESSAGE_INSTANCE.refreshStatusFromMatcher(message,statusMatcher);
                return STATUS_MESSAGE_INSTANCE;
            }
        }
        catch(Exception e){
            System.out.println("处理接收到的消息时发生错误：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
