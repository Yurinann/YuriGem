package me.yurinan.plugins.yurigem.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

/**
 * @author Yurinan
 * @since 2022/2/27 22:28
 */

public class GemManager {

    /**
     * 移除选定宝石
     *
     * @param player 获取指定玩家的物品栏
     * @param gem 获取选定的宝石
     * @return true: 已移除 / false: 未移除
     */
    public boolean removeGem(Player player, String gem) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 0; i < inventory.getSize(); ++i) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    String name = meta.getDisplayName();
                    if (name.equalsIgnoreCase(gem)) {
                        if (Objects.requireNonNull(inventory.getItem(i)).getAmount() == 1) {
                            inventory.setItem(i, null);
                            return true;
                        }
                        item.setAmount(item.getAmount() - 1);
                        inventory.setItem(i, item);
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
