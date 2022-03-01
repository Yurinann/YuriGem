package me.yurinan.plugins.yurigem.configurations;

import me.yurinan.plugins.yurigem.managers.FileManager;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Yurinan
 * @since 2022/2/27 16:54
 */

public class PluginMessages {

    public static FileConfiguration messageConfig = FileManager.getMessageConfig();
    public static FileConfiguration gemConfig = FileManager.getGemConfig();

    public static final String LORE_HEAD = gemConfig.getString("Lore-Head");
    public static final String LORE_END = gemConfig.getString("Lore-End");

    public static final String PREFIX = messageConfig.getString("Prefix");
    public static final String CLICK_GEM_IS_NULL = messageConfig.getString("Click-Gem-Is-Null");
    public static final String INLAY_SUCCEEDED = messageConfig.getString("Inlay-Succeeded");
    public static final String INLAY_FAILED = messageConfig.getString("Inlay-Failed");
    public static final String INLAY_NOT_ALLOWED = messageConfig.getString("Inlay-Not-Allowed");
    public static final String GEM_IS_SELECTED = messageConfig.getString("Gem-Is-Selected");

}
