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

    public static boolean selectGem(Player player, String gem) {
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
