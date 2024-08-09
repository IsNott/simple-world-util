package io.github.nott;

import io.github.nott.bot.BotService;
import io.github.nott.common.MyOkHttpClient;
import io.github.nott.config.BotConfig;
import io.github.nott.enums.StartEndEnum;
import io.github.nott.executor.BugExecutor;
import io.github.nott.factory.MyFactory;
import io.github.nott.runnable.NoticeRunnable;
import io.papermc.paper.util.Tick;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

/**
 * @author Nott
 * @date 2024-7-31
 */

public class SimpleWorldPlugins extends JavaPlugin implements Listener {

    public static boolean BOT_ENABLE = true;

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        Logger slf4JLogger = getSLF4JLogger();
        MyFactory.sfl4jLogger = slf4JLogger;
        slf4JLogger.info("starting to Load OkHttp client");
        MyOkHttpClient.build();
        slf4JLogger.info(" OkHttp client Loaded");
        ConfigurationSerialization.registerClass(BotConfig.class);
        slf4JLogger.info("BotConfig class registered");
        FileConfiguration config = getConfig();
        boolean testOnLoad = config.getBoolean("bot.test-onload");
        MyFactory.myConfiguration = config;
        if (testOnLoad) {
            try {
                BotService.sendTestMessage(config);
                slf4JLogger.info("Bot Test Success!");
            } catch (Exception e) {
                BOT_ENABLE = false;
                slf4JLogger.error("Bot load failed {}", e.getMessage(), e);
            }
        }
        // 国战提示信息
        this.registerNoticeTask(config);

        // 注册指令
        this.registerCommand();
    }

    private void registerCommand() {
        Objects.requireNonNull(this.getCommand("bug")).setExecutor(new BugExecutor(this));
    }


    private void registerNoticeTask(FileConfiguration config) {
        MyFactory.sfl4jLogger.info("Started to Register Notice Task");
        BukkitScheduler scheduler = this.getServer().getScheduler();
        String startStr = config.getString("notice.startTime", "10:00");
        String endStr = config.getString("notice.endTime", "11:00");
        int textCount = config.getInt("notice.textCount", 1);
        int interval = config.getInt("notice.interval", 10);

        LocalTime startTime = LocalTime.parse(startStr);
        LocalTime endTime = LocalTime.parse(endStr);

        compareAndRegister(config, StartEndEnum.START, textCount, startTime, interval, scheduler);
        compareAndRegister(config, StartEndEnum.END, textCount, endTime, interval, scheduler);
        MyFactory.sfl4jLogger.info("Register Notice Task Completely");

    }

    private void compareAndRegister(FileConfiguration config, StartEndEnum mode, int textCount, LocalTime time, int interval, BukkitScheduler scheduler) {
        LocalTime now = LocalTime.now();
        // 当前启动时间晚于通告时间
        if (now.isAfter(time)) {
            MyFactory.sfl4jLogger.info("Staring time before now,Missing this notice");
            return;
        }
        String message = config.getString("notice.noticeMessage");
        if (StringUtils.isBlank(message)) {
            MyFactory.sfl4jLogger.info("Notice Feature Haven't Message Value");
            return;
        }
        int missingNoticeCount = 0;
        for (int i = 1; i < textCount; i++) {
            int totalInterval = i * interval;
            LocalTime beforeStartTime = time.minusMinutes(totalInterval);
            if (now.isAfter(beforeStartTime)) {
                missingNoticeCount++;
                continue;
            }
            Duration duration = Duration.between(now, beforeStartTime);

            String actuallyMessage = message.replaceAll("\\{MODE}", mode.getMode()).replaceAll("\\{MIN}", totalInterval + "");
            scheduler.runTaskLater(this, new NoticeRunnable(actuallyMessage, config), Tick.tick().fromDuration(duration));
        }
        MyFactory.sfl4jLogger.info("Missing {} Times {} Notice", missingNoticeCount, mode.getEng());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(PlayerLoginEvent loginEvent) {
        if (BOT_ENABLE) {
            Player player = loginEvent.getPlayer();
            int playerCount = getServer().getOnlinePlayers().size();
            String msg = String.format("[服务器通知]玩家:%s登录，服务器当前在线人数%s人", player.getName(), playerCount);
            try {
                BotService.sendMessage(msg, getConfig(), 2);
            } catch (Exception e) {
                getSLF4JLogger().error("PlayerLogin messages send failed {}", e.getMessage(), e);
            }
        } else {
            getSLF4JLogger().info("Bot current disabled please check");
        }
    }


}
