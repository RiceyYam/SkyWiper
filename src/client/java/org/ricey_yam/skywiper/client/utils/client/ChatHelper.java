package org.ricey_yam.skywiper.client.utils.client;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class ChatHelper {
    /// 上次发送消息的时间戳
    @Getter
    @Setter
    private static long lastSendMessageTimestamp = 0;

    /// 上次执行指令的时间戳
    @Getter
    @Setter
    private static long lastSendCommandTimestamp = 0;

    /// 两次发送时间的间隔（时间戳）
    private static final long sendMessageTimestampDuration = 3000;

    /// 两次发送指令的间隔（时间戳）
    private static final long sendCommandTimestampDuration = 2000;

    /// 发送服务器聊天信息
    public static void sendMessage(String message){
        var currentTimestamp = System.currentTimeMillis();
        if(currentTimestamp - lastSendMessageTimestamp < sendMessageTimestampDuration) return;

        var client = ClientUtils.getClient();
        if(client == null) return;
        Objects.requireNonNull(client.getNetworkHandler()).sendChatMessage(message);
        lastSendMessageTimestamp = currentTimestamp;
    }

    /// 执行服务器指令
    public static void sendCommand(String command){
        var currentTimestamp = System.currentTimeMillis();
        if(currentTimestamp - lastSendCommandTimestamp < sendCommandTimestampDuration) return;

        var client = ClientUtils.getClient();
        if(client == null) return;
        Objects.requireNonNull(client.getNetworkHandler()).sendChatCommand(command);
        lastSendCommandTimestamp = currentTimestamp;
    }
}
