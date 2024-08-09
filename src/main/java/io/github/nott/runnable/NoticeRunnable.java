package io.github.nott.runnable;

import io.github.nott.bot.BotService;
import io.github.nott.enums.MessageEnum;
import io.github.nott.factory.MyFactory;
import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;

/**
 * @author Nott
 * @date 2024-8-2
 */
@Data
public class NoticeRunnable implements Runnable {

    private String noticeMessage;

    private Configuration configuration;

    public NoticeRunnable(String noticeMessage, Configuration configuration) {
        this.noticeMessage = noticeMessage;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        MyFactory.sfl4jLogger.info("Started to Notice");
        Bukkit.getServer().broadcast(Component.text(noticeMessage,
                Style.style().color(TextColor.color(TextColor.fromHexString(configuration.getString("notice.color")))).build()));
        try {
            BotService.sendMessage(noticeMessage.replaceAll("&6",""),configuration, MessageEnum.GROUP.ordinal());
        } catch (Exception e) {
            MyFactory.sfl4jLogger.error("Send Server BroadCast Error {}",e.getMessage(),e);
        }
        MyFactory.sfl4jLogger.info("Notice finished");
    }


}
