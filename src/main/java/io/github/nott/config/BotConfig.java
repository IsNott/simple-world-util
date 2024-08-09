package io.github.nott.config;

import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nott
 * @date 2024-8-1
 */
@Data
public class BotConfig implements ConfigurationSerializable {

    private String privateKey;

    private String publicKey;

    private String url;

    private Long testQQ;

    private String testMsg;

    public BotConfig(String privateKey, String publicKey, String url, Long testQQ, String testMsg) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.url = url;
        this.testQQ = testQQ;
        this.testMsg = testMsg;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("privateKey", this.privateKey);
        map.put("publicKey", this.publicKey);
        map.put("testQQ", this.testQQ);
        map.put("testMsg", this.testMsg);
        map.put("url", this.url);
        return map;
    }

    public static BotConfig deserialize(Map<String, Object> args) {
        return new BotConfig(
                (String) args.get("privateKey"),
                (String) args.get("publicKey"),
                (String) args.get("url"),
                (Long) args.get("testQQ"),
                (String) args.get("testMsg"));
    }
}
