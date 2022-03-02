package me.yurinan.plugins.yurigem.commands;

import me.yurinan.plugins.yurigem.Main;
import me.yurinan.plugins.yurigem.configurations.PluginMessages;
import me.yurinan.plugins.yurigem.configurations.PluginPermissions;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Yurinan
 * @since 2022/2/27 16:45
 */

public class CommandHandler implements TabExecutor {

    public static Map<String, ItemStack> Items = Main.Items;
    public static List<String> GemList = Main.GemList;

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission(PluginPermissions.ADMIN)) {
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
                    MessageUtil.send(sender, PluginMessages.PREFIX + "&a当前可用的宝石列表: " + GemList.toString());
                } else if ("reload".equalsIgnoreCase(args[0])) {
                    FileManager.reloadAllConfig();
                    MessageUtil.send(sender, PluginMessages.PREFIX + "配置文件已成功重载!");
                } else {
                    MessageUtil.send(sender, PluginMessages.PREFIX + "&c错误的用法, 请使用 /yurigem help 查看插件帮助!");
                }
            } else if (args.length == 4) {
                if ("give".equalsIgnoreCase(args[0])) {
                    if (args[2].matches("^-?[1-9]\\d*$")) {
                        Player player = Bukkit.getPlayer(args[3]);
                        if (player == null) {
                            MessageUtil.send(sender, PluginMessages.PREFIX + "&c指定的玩家可能不存在或已离线!");
                            return true;
                        }
                        ItemStack item = Items.get(args[1]);
                        if (item != null) {
                            item.setAmount(Integer.parseInt(args[2]));
                            player.getInventory().addItem(item);
                            MessageUtil.send(player, PluginMessages.PREFIX + "&a您获得了 &e" + args[2] + "个 " + item.getItemMeta().getDisplayName() + "&a!");
                        } else {
                            MessageUtil.send(player, PluginMessages.PREFIX + "&c给予错误, 请检查!");
                        }
                        return true;
                    } else {
                        MessageUtil.send(sender, PluginMessages.PREFIX + "&c请指定一个宝石整数数量!");
                    }
                } else {
                    MessageUtil.send(sender, PluginMessages.PREFIX + "&c错误的用法, 请使用 /yurigem help 查看插件帮助!");
                }
            } else {
                MessageUtil.send(sender, PluginMessages.PREFIX + "&c错误的用法, 请使用 /yurigem help 查看插件帮助!");
            }
        } else {
            MessageUtil.send(sender, PluginMessages.PREFIX + "&c您没有权限使用此命令!");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (sender.hasPermission(PluginPermissions.ADMIN)) {
            switch (args.length) {
                case 1: {
                    List<String> param1Complete = new ArrayList<>();

                    param1Complete.add("help");
                    param1Complete.add("give");
                    param1Complete.add("list");
                    param1Complete.add("reload");

                    param1Complete.removeIf(s -> !s.startsWith(args[0].toLowerCase()));

                    return param1Complete;
                }
                case 2: {
                    if ("give".equalsIgnoreCase(args[0])) {
                        return GemList;
                    }
                }
                case 3: {
                    if ("give".equalsIgnoreCase(args[0])) {
                        return Collections.singletonList("<数量>");
                    }
                }
                case 4: {
                    if ("give".equalsIgnoreCase(args[0])) {
                        return null;
                    }
                }
                default: return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

}
