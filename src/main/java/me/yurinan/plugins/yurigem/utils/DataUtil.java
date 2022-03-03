package me.yurinan.plugins.yurigem.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yurinan
 * @since 2022/3/4 1:07
 */

public class DataUtil {

    private Map<Player, String> gemSelectMap;
    private Map<String, String> gemNameCheckMap;
    private Map<String, ItemStack> gemMap;
    private List<String> gemList;

    public void initPluginData() {
        this.gemSelectMap = new HashMap<>();
        this.gemNameCheckMap = new HashMap<>();
        this.gemMap = new HashMap<>();
        this.gemList = new ArrayList<>();
    }

    public Map<Player, String> getGemSelectMap() {
        return gemSelectMap;
    }

    public Map<String, String> getGemNameCheckMap() {
        return gemNameCheckMap;
    }

    public Map<String, ItemStack> getGemMap() {
        return gemMap;
    }

    public List<String> getGemList() {
        return gemList;
    }

}
