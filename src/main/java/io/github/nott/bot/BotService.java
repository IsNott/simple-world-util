package io.github.nott.bot;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.nott.SimpleWorldPlugins;
import io.github.nott.common.CommonUtils;
import io.github.nott.common.MyOkHttpClient;
import io.github.nott.enums.MessageType;
import io.github.nott.factory.MyFactory;
import io.github.nott.model.R;
import io.github.nott.model.TextMessage;
import okhttp3.*;
import okio.BufferedSink;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author Nott
 * @date 2024-8-1
 */
public class BotService {

    public static void sendTestMessage(Configuration config) throws Exception {
        sendMessage(null, config, 0);
    }

    public static void sendMessage(String msg, Configuration config, Integer type) throws Exception {
        if(SimpleWorldPlugins.BOT_ENABLE){
            String url = config.getString("bot.url");
            TextMessage message = new TextMessage();
            message.setCode(config.getLong("bot.testQQ"));
            message.setMessage(StringUtils.isBlank(msg) ? config.getString("bot.testMsg") : msg);

            switch (type) {
                default:
                    break;
                case 0:
                case 1: {
                    url += MessageType.FRIEND.getUrl();
                    break;
                }
                case 2: {
                    message.setCode(config.getLong("bot.groupQQ"));
                    url += MessageType.GROUP.getUrl();
                    break;
                }
            }
            message.setSign(config.getString("bot.privateKey"),
                    config.getString("bot.publicKey"));
            JSONObject respJson = MyOkHttpClient.sendJsonRequest(url, message);
            CommonUtils.requireTrue(respJson.getInt("code") == 200);
        }
    }
}
