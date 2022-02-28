package me.yurinan.plugins.yurigem;

import me.yurinan.plugins.yurigem.commands.CommandHandler;
import me.yurinan.plugins.yurigem.listeners.PlayerInlayListener;
import me.yurinan.plugins.yurigem.managers.ConfigManager;
import me.yurinan.plugins.yurigem.managers.FileManager;
import me.yurinan.plugins.yurigem.utils.ColorParser;
import me.yurinan.plugins.yurigem.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yurinan
 * @since 2022/2/26 16:44
 */

public final class Main extends JavaPlugin {

    public static Main instance;

    public static Map<Player, String> Click;
    public static Map<String, String> ItemNameCheck;
    public static List<String> gemList;
    public static Map<String, ItemStack> Items;

    @Override
    public void onEnable() {
        instance = this;
        log("&f插件开始加载...");
        long startTime = System.currentTimeMillis();
        log("&f加载内部数据...");
        Click = new HashMap<>();
        ItemNameCheck = new HashMap<>();
        gemList = new ArrayList<>();
        Items = new HashMap<>();
        log("&f加载配置文件...");
        FileManager.initConfig();
        log("&f注册监听器...");
        registerListener(new PlayerInlayListener());
        log("&f注册命令...");
        registerCommand("yurigem", new CommandHandler(), new CommandHandler());
        log("&3插件加载完成, 共耗时 &b" + (System.currentTimeMillis() - startTime) + " &3ms.");
    }

    @Override
    public void onDisable() {
        log("&f插件开始卸载...");
        long closeTime = System.currentTimeMillis();
        log("&f保存配置文件...");
        FileManager.saveAllConfig();
        log("&3插件卸载完成, 共耗时 &b" + (System.currentTimeMillis() - closeTime) + " &3ms.");
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("&8[&b" + instance.getName() + "&8] " + message));
    }

    public static void warn(String message) {
        log("&e警告! &r" + message);
    }

    public static void error(String message) {
        log("&c错误! &r" + message);
    }

    public static void debug(String message) {
        if (ConfigManager.debugMode()) {
            log("&7调试信息! &r" + message);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("YuriGem.Admin")) {
                    MessageUtil.send(player, "&7调试信息! &r" + message);
                }
            }
        }
    }

    public static void registerCommand(String commandName, @NotNull CommandExecutor executor) {
        registerCommand(commandName, executor, null);
    }

    public static void registerCommand(String commandName, @NotNull CommandExecutor executor, @Nullable TabCompleter tabCompleter) {
        PluginCommand command = Bukkit.getPluginCommand(commandName);
        if (command == null) {
            return;
        }
        command.setExecutor(executor);
        if (tabCompleter != null) {
            command.setTabCompleter(tabCompleter);
        }
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

}
