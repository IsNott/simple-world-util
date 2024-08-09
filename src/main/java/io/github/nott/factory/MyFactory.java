package io.github.nott.factory;

import io.github.nott.manager.Manager;
import okhttp3.OkHttpClient;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.slf4j.Logger;

/**
 * @author Nott
 * @date 2024-8-1
 */
public class MyFactory {

    public static OkHttpClient okHttpClient;

    public static Logger sfl4jLogger;

    public static Configuration myConfiguration;

    public static Manager myDbManager;

    public static YamlConfiguration bugConfig;
}
