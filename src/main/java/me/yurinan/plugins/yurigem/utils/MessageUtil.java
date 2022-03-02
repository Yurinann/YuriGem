package me.yurinan.plugins.yurigem.utils;

import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author CarmJos
 * @since 2022/2/27 16:45
 */

public class MessageUtil {

    public static void send(@Nullable CommandSender sender, List<String> messages) {
        if (messages == null || messages.isEmpty() || sender == null) {
            return;
        }
        for (String s : messages) {
            sender.sendMessage(ColorParser.parse(s));
        }
    }

    public static void send(@Nullable CommandSender sender, String... messages) {
        send(sender, Arrays.asList(messages));
    }
    
}
