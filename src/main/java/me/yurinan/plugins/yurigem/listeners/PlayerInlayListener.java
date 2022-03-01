package me.yurinan.plugins.yurigem.listeners;

import me.yurinan.plugins.yurigem.Main;
import me.yurinan.plugins.yurigem.configurations.PluginMessages;
import me.yurinan.plugins.yurigem.managers.FileManager;
import me.yurinan.plugins.yurigem.managers.GemManager;
import me.yurinan.plugins.yurigem.utils.ColorParser;
import me.yurinan.plugins.yurigem.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yurinan
 * @since 2022/2/27 22:34
 */

public class PlayerInlayListener implements Listener {

    public static Map<Player, String> ItemSelect = Main.ItemSelect;
    public static Map<String, String> ItemNameCheck = Main.ItemNameCheck;
    public static FileConfiguration gemConfig = FileManager.getGemConfig();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // 判断是否使用右键点击 Inventory (1)
        if (event.getClick() == ClickType.RIGHT) {
            // 获取右键点击 Inventory 触发事件的玩家实例 (2)
            Player player = (Player) event.getWhoClicked();
            // 判断右键点击的物品不为 null (3)
            if (event.getCurrentItem() != null) {
                // 判断 ItemSelect Map 中是否有 Player (4)
                if (ItemSelect.containsKey(player)) {
                    event.setCancelled(true);
                    Material currentMaterial = Material.valueOf(String.valueOf(event.getCurrentItem().getType()));
                    ItemStack item = event.getCurrentItem();
                    if (item != null) {
                        String allowInlayItem = gemConfig.getString(ItemSelect.get(player) + ".AllowInlayItemList");
                        String[] allowInlayItemList = Objects.requireNonNull(allowInlayItem).split(",");
                        int checkTime = 0;
                        for (String s : allowInlayItemList) {
                            if (Objects.equals(String.valueOf(currentMaterial).toUpperCase(), s)) {
                                ++checkTime;
                                break;
                            }
                        }
                        if (checkTime != 1) {
                            MessageUtil.send(player, PluginMessages.PREFIX + PluginMessages.INLAY_NOT_ALLOWED);
                            event.setCancelled(true);
                            player.closeInventory();
                            ItemSelect.remove(player);
                            return;
                        }

                        String selectGem = ItemSelect.get(player);
                        ItemMeta meta = item.getItemMeta();
                        if (!GemManager.selectGem(player, ColorParser.parse(gemConfig.getString(selectGem + ".DisplayName")))) {
                            MessageUtil.send(player, PluginMessages.PREFIX + PluginMessages.CLICK_GEM_IS_NULL);
                            ItemSelect.remove(player);
                            player.closeInventory();
                            return;
                        }

                        int successChance;
                        int loreNumber;
                        if (!meta.hasLore()) {
                            String gemLore = gemConfig.getString(selectGem + ".SuccessLore");
                            successChance = gemConfig.getInt(selectGem + ".SuccessChance");
                            Random random = new Random();
                            loreNumber = random.nextInt(100) + 1;
                            List<String> gemAllLore = new ArrayList<>();
                            if (loreNumber <= successChance) {
                                gemAllLore.add(PluginMessages.LORE_HEAD);
                                gemAllLore.add(gemLore);
                                gemAllLore.add(PluginMessages.LORE_END);
                                meta.setLore(gemAllLore.stream().map(ColorParser::parse).collect(Collectors.toList()));
                                item.setItemMeta(meta);
                                MessageUtil.send(player, PluginMessages.INLAY_SUCCEEDED);
                            } else {
                                MessageUtil.send(player, PluginMessages.INLAY_FAILED);
                            }
                            player.closeInventory();
                            player.updateInventory();
                            ItemSelect.remove(player);
                            return;
                        }

                        List<String> list = meta.getLore();
                        int lore2Number;
                        String successLore = gemConfig.getString(selectGem + ".SuccessLore");
                        int success2Chance = gemConfig.getInt(selectGem + ".SuccessChance");
                        Random random = new Random();
                        loreNumber = random.nextInt(100) + 1;
                        if (loreNumber <= success2Chance) {
                            int loreLoc = -1;
                            for (lore2Number = 1; lore2Number <= Objects.requireNonNull(list).size(); ++lore2Number) {
                                if (list.get(lore2Number - 1).equalsIgnoreCase(PluginMessages.LORE_HEAD)) {
                                    loreLoc = lore2Number;
                                    break;
                                }
                            }
                            if (loreLoc == -1) {
                                list.add(successLore);
                            } else {
                                list.add(loreLoc, successLore);
                            }
                            meta.setLore(list.stream().map(ColorParser::parse).collect(Collectors.toList()));
                            item.setItemMeta(meta);
                            MessageUtil.send(player, PluginMessages.INLAY_SUCCEEDED);
                        } else {
                            MessageUtil.send(player, PluginMessages.INLAY_FAILED);
                        }
                        player.closeInventory();
                        player.updateInventory();
                        ItemSelect.remove(player);
                        return;
                    }
                }
                String gem = this.checkItem(event.getCurrentItem());
                if (gem != null) {
                    ItemSelect.put(player, gem);
                    event.setCancelled(true);
                    player.closeInventory();
                    MessageUtil.send(player, PluginMessages.GEM_IS_SELECTED);
                }
            }
        }
    }

    public String checkItem(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                String name = meta.getDisplayName();
                if (ItemNameCheck.containsKey(name)) {
                    return ItemNameCheck.get(name);
                }
            }
        }
        return null;
    }

}
