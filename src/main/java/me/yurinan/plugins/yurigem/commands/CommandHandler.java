package me.yurinan.plugins.yurigem.commands;

import me.yurinan.plugins.yurigem.Main;
import me.yurinan.plugins.yurigem.configurations.PluginMessages;
import me.yurinan.plugins.yurigem.managers.FileManager;
import me.yurinan.plugins.yurigem.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

/**
 * @author Yurinan
 * @since 2022/2/27 16:45
 */

public class CommandHandler implements TabExecutor {

    public static Map<String, ItemStack> Items = Main.Items;

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            MessageUtil.send(sender, PluginMessages.PREFIX + "&a请使用 /yurigem help 查看插件帮助!");
        } else if (args.length == 1) {
            if ("help".equalsIgnoreCase(args[0])) {
                MessageUtil.send(sender, PluginMessages.PREFIX + "&eYuriGem 插件帮助:");
                MessageUtil.send(sender, "&a▶ /yurigem help - 查看插件帮助");
                MessageUtil.send(sender, "&a▶ /yurigem give <宝石编号> <数量> <玩家> - 给予玩家宝石");
                MessageUtil.send(sender, "&a▶ /yurigem list - 获取宝石编号");
                MessageUtil.send(sender, "&a▶ /yurigem reload - 重载配置文件");
                return true;
            } else if ("list".equalsIgnoreCase(args[0])) {
                MessageUtil.send(sender, PluginMessages.PREFIX + "&a当前可用的宝石列表: " + Main.gemList.toString());
            } else if ("reload".equalsIgnoreCase(args[0])) {
                FileManager.reloadAllConfig();
                MessageUtil.send(sender, PluginMessages.PREFIX + "配置文件已成功重载!");
            } else {
                MessageUtil.send(sender, PluginMessages.PREFIX + "&c错误的用法, 请使用 /yurigem help 查看插件帮助!");
            }
        } else if (args.length == 4) {
            if ("give".equalsIgnoreCase(args[0])) {
                Player player = Bukkit.getPlayer(args[3]);
                if (player == null) {
                    MessageUtil.send(sender, PluginMessages.PREFIX + "&c指定的玩家可能不存在或已离线!");
                    return true;
                }
                ItemStack item = Items.get(args[1]);
                if (item != null) {
                    item.setAmount(Integer.parseInt(args[2]));
                    player.getInventory().addItem(item);
                    MessageUtil.send(player, PluginMessages.PREFIX + "&a您获得了 &e" + args[2] + item.getItemMeta().getDisplayName() + "!");
                }
                return true;
            } else {
                MessageUtil.send(sender, PluginMessages.PREFIX + "&c错误的用法, 请使用 /yurigem help 查看插件帮助!");
            }
        } else {
            MessageUtil.send(sender, PluginMessages.PREFIX + "&c错误的用法, 请使用 /yurigem help 查看插件帮助!");
        }
        return true;
    }

    // todo: command tabComplete
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

}
