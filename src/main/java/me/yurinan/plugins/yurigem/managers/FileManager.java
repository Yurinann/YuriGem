package me.yurinan.plugins.yurigem.managers;

import com.google.common.base.Charsets;
import me.yurinan.plugins.yurigem.Main;
import me.yurinan.plugins.yurigem.utils.ColorParser;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yurinan
 * @since 2022/2/27 17:01
 */

public class FileManager {

    public static List<String> GemList = Main.GemList;
    public static Map<String, String> ItemNameCheck = Main.ItemNameCheck;
    public static Map<String, ItemStack> Items = Main.Items;

    private static FileConfiguration newConfig = null;
    private static FileConfiguration newMessageConfig = null;
    private static FileConfiguration newGemConfig = null;

    public static File dataFolder = Main.instance.getDataFolder();
    public static File configFile = new File(dataFolder + "/config.yml");
    public static File messageFile = new File(dataFolder + "/messages.yml");
    public static File gemFile = new File(dataFolder + "/gems.yml");

    public static void initConfig() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        if (!configFile.exists()) {
            Main.instance.saveResource("config.yml", false);
        }
        if (!messageFile.exists()) {
            Main.instance.saveResource("messages.yml", false);
        }
        if (!gemFile.exists()) {
            Main.instance.saveResource("gems.yml", false);
        }

        GemList.clear();
        for (String str : getGemConfig().getKeys(false)) {
            if (!"Lore-Head".equalsIgnoreCase(str) && !"Lore-End".equalsIgnoreCase(str)) {
                GemList.add(ColorParser.parse(str));
            }
        }
        Main.log("&f读取到的宝石: &8" + GemList.toString());

        for (int i = 1; i <= getGemConfig().getKeys(false).size(); ++i) {
            if (getGemConfig().getConfigurationSection("Item" + i) != null) {
                ItemStack item = new ItemStack(Material.STONE);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ColorParser.parse(getGemConfig().getString("Item" + i + ".DisplayName")));
                meta.setLore(getGemConfig().getStringList("Item" + i + ".Lore").stream().map(ColorParser::parse).collect(Collectors.toList()));
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                item.setItemMeta(meta);
                item.setType(Material.valueOf(getGemConfig().getString("Item" + i + ".Id")));
                Items.put("Item" + i, item);
                ItemNameCheck.put(ColorParser.parse(getGemConfig().getString("Item" + i + ".DisplayName")), "Item" + i);
            }
        }
    }

    @NotNull
    public static FileConfiguration getConfig() {
        if (newConfig == null) {
            reloadConfig();
        }
        return newConfig;
    }

    @NotNull
    public static FileConfiguration getMessageConfig() {
        if (newMessageConfig == null) {
            reloadMessageConfig();
        }
        return newMessageConfig;
    }

    @NotNull
    public static FileConfiguration getGemConfig() {
        if (newGemConfig == null) {
            reloadGemConfig();
        }
        return newGemConfig;
    }

    public static void saveConfig() {
        try {
            getConfig().save("config.yml");
        } catch (IOException e) {
            Main.warn("Could not save config to" + configFile + e);
        }
    }

    public static void saveMessageConfig() {
        try {
            getMessageConfig().save("messages.yml");
        } catch (IOException e) {
            Main.warn("Could not save config to" + messageFile + e);
        }
    }

    public static void saveGemConfig() {
        try {
            getMessageConfig().save("gems.yml");
        } catch (IOException e) {
            Main.warn("Could not save config to" + gemFile + e);
        }
    }

    public static void saveAllConfig() {
        saveConfig();
        saveMessageConfig();
        saveGemConfig();
    }

    public static void reloadConfig() {
        newConfig = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = Main.instance.getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public static void reloadMessageConfig() {
        newMessageConfig = YamlConfiguration.loadConfiguration(messageFile);

        final InputStream defConfigStream = Main.instance.getResource("messages.yml");
        if (defConfigStream == null) {
            return;
        }

        newMessageConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public static void reloadGemConfig() {
        newGemConfig = YamlConfiguration.loadConfiguration(gemFile);

        final InputStream defConfigStream = Main.instance.getResource("gems.yml");
        if (defConfigStream == null) {
            return;
        }

        newGemConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    public static void reloadAllConfig() {
        initConfig();
        reloadConfig();
        reloadMessageConfig();
        reloadGemConfig();
    }

}
