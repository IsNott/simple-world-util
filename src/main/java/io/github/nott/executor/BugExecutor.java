package io.github.nott.executor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Nott
 * @date 2024-8-5
 */
public class BugExecutor implements CommandExecutor {

    private Plugin plugin;

    public BugExecutor(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int argLength = args.length;
        FileConfiguration config = plugin.getConfig();
        if (argLength > 1 || argLength == 0 || args[0].length() < config.getInt("bug.length",15)) {
            return false;
        }

        if (config.getBoolean("bug.ignoreConsole") && !(commandSender instanceof Player)) {
            commandSender.sendPlainMessage("You Are Console?Only Support Player Report Bug");
            return true;
        }

        this.asyncStoreBugInfo(commandSender, args, config);
        return true;
    }

    private void asyncStoreBugInfo(@NotNull CommandSender commandSender, @NotNull String @NotNull [] args, FileConfiguration config) {
        Bukkit.getServer().getScheduler().runTask(plugin, () -> {
            String msg;
            int limit = config.getInt("bug.limit", 5);
            try {
                String arg = args[0];
                String playerName = commandSender.getName();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = sdf.format(new Date());
                File file = new File(plugin.getDataFolder(), "bug.yml");
                if(!file.exists()){
                    file.getParentFile().mkdirs();
                    plugin.saveResource("bug.yml",false);
                }

                YamlConfiguration bugFile = YamlConfiguration.loadConfiguration(file);

                int reportCount = bugFile.getInt(dateStr + "." + playerName,0);
                if(reportCount >= limit){
                    commandSender.sendRichMessage(config.getString("bug.limitMsg",""));
                    return;
                }
                String uid = UUID.randomUUID().toString().replaceAll("-", "");
                bugFile.set(uid + ".username", playerName);
                bugFile.set(uid + ".context", arg);
                bugFile.set(uid + ".date", dateStr);
                bugFile.set(uid + ".status", 0);
                bugFile.set(dateStr + "." + playerName, reportCount + 1);
                bugFile.save(new File(plugin.getDataFolder() + File.separator + "bug.yml"));
                msg = config.getString("bug.success","&6提交成功");
            } catch (Exception e) {
                msg = config.getString("bug.failed","&6提交失败");
            }
            commandSender.sendRichMessage(msg);
        });
    }
}
