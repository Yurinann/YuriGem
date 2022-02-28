package me.yurinan.plugins.yurigem.managers;

import com.google.common.base.Charsets;
import me.yurinan.plugins.yurigem.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author Yurinan
 * @since 2022/2/27 17:01
 */

public class FileManager {

    public static List<String> gemList = Main.gemList;
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

        for (String str : getGemConfig().getKeys(false)) {
            if (!"Lore-Head".equalsIgnoreCase(str) && !"Lore-End".equalsIgnoreCase(str)) {
                gemList.add(str);
            }
        }
        Main.log("" + gemList.toString());

        for (int i = 1; i <= getMessageConfig().getKeys(false).size(); ++i) {
            if (getMessageConfig().getConfigurationSection("Item" + i) != null) {
                ItemStack is = new ItemStack(Material.STONE);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(getMessageConfig().getString("Item" + i + ".DisplayName"));
                im.setLore(getMessageConfig().getStringList("Item" + i + ".Lore"));
                im.addEnchant(Enchantment.DAMAGE_UNDEAD, 1, true);
                is.setItemMeta(im);
                is.setType(Material.valueOf(getMessageConfig().getString("Item" + i + ".Id")));
                Items.put("Item" + i, is);
                ItemNameCheck.put(getMessageConfig().getString("Item" + i + ".DisplayName"), "Item" + i);
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
        reloadConfig();
        reloadMessageConfig();
        reloadGemConfig();
    }

}
