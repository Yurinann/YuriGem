package me.yurinan.plugins.yurigem.managers;

import me.yurinan.plugins.yurigem.configurations.PluginConfig;

/**
 * @author Yurinan
 * @since 2022/2/27 17:04
 */

public class ConfigManager {
    
    public static boolean debugMode() {
        return FileManager.getConfig().getBoolean(PluginConfig.DEBUG);
    }

}
