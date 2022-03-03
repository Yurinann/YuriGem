package me.yurinan.plugins.yurigem.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * @author CarmJos, Yurinan
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

    public static void sendToAllPlayers(List<String> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        for (String s : messages) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player == null) {
                    return;
                }
                send(player, s);
            }
        }
    }

    public static void sendToAllPlayers(String... messages) {
        sendToAllPlayers(Arrays.asList(messages));
    }
    
}
