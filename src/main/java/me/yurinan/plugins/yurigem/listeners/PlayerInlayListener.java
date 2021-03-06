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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yurinan
 * @since 2022/2/27 22:34
 */

public class PlayerInlayListener implements Listener {

    /**
     * 处理玩家镶嵌。
     *
     * @param clickEvent InventoryClickEvent 点击事件, 判断宝石选择
     */
    @EventHandler
    public void onPlayerInlay(InventoryClickEvent clickEvent) {
        FileConfiguration gemConfig = FileManager.getGemConfig();
        Player player = (Player) clickEvent.getWhoClicked();
        if (clickEvent.getClickedInventory() != null) {
            if (clickEvent.getClick() == ClickType.RIGHT && clickEvent.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                if (clickEvent.getCurrentItem() != null && clickEvent.getCurrentItem().getType() != Material.AIR) {
                    if (Main.instance.getGemSelectMap().containsKey(player)) {
                        clickEvent.setCancelled(true);
                        Material currentMaterial = Material.valueOf(String.valueOf(clickEvent.getCurrentItem().getType()));
                        ItemStack item = clickEvent.getCurrentItem();
                        if (item != null) {
                            List<String> allowInlayItemList = new ArrayList<>(gemConfig.getStringList(Main.instance.getGemSelectMap().get(player) + ".AllowInlayItemList"));
                            int checkTime = 0;
                            for (String s : allowInlayItemList) {
                                if (Objects.equals(String.valueOf(currentMaterial).toUpperCase(), s)) {
                                    ++checkTime;
                                    break;
                                }
                            }
                            if (checkTime != 1) {
                                MessageUtil.send(player, PluginMessages.PREFIX + PluginMessages.INLAY_NOT_ALLOWED);
                                clickEvent.setCancelled(true);
                                Main.instance.getGemSelectMap().remove(player);
                                player.closeInventory();
                                return;
                            }

                            String selectGem = Main.instance.getGemSelectMap().get(player);
                            GemManager gemManager = new GemManager();
                            if (!gemManager.removeGem(player, ColorParser.parse(gemConfig.getString(selectGem + ".DisplayName")))) {
                                MessageUtil.send(player, PluginMessages.PREFIX + PluginMessages.CLICK_GEM_IS_NULL);
                                Main.instance.getGemSelectMap().remove(player);
                                player.closeInventory();
                                return;
                            }

                            int successChance;
                            int loreNumber;
                            ItemMeta meta = item.getItemMeta();
                            if (!meta.hasLore()) {
                                String gemLore = gemConfig.getString(selectGem + ".SuccessLore");
                                String gemDisplayName = gemConfig.getString(selectGem + ".DisplayName");
                                successChance = gemConfig.getInt(selectGem + ".SuccessChance");
                                Random random = new Random();
                                loreNumber = random.nextInt(100) + 1;
                                List<String> gemAllLore = new ArrayList<>();
                                if (loreNumber <= successChance) {
                                    gemAllLore.add("");
                                    gemAllLore.add(PluginMessages.LORE_HEAD);
                                    gemAllLore.add(gemDisplayName + " &a- " + gemLore);
                                    meta.setLore(gemAllLore.stream().map(ColorParser::parse).collect(Collectors.toList()));
                                    item.setItemMeta(meta);
                                    MessageUtil.send(player, PluginMessages.INLAY_SUCCEEDED);
                                    if (gemConfig.getBoolean(selectGem + ".SuccessBroadcast")) {
                                        MessageUtil.sendToAllPlayers(PluginMessages.PREFIX + Objects.requireNonNull(PluginMessages.INLAY_SUCCEEDED_BROADCAST).replace("{player_name}", player.getName()).replace("{gem}", gemDisplayName));
                                    }
                                } else {
                                    MessageUtil.send(player, PluginMessages.INLAY_FAILED);
                                }
                                player.closeInventory();
                                player.updateInventory();
                                Main.instance.getGemSelectMap().remove(player);
                                return;
                            }

                            List<String> list = meta.getLore();
                            int lore2Number;
                            String successLore = gemConfig.getString(selectGem + ".SuccessLore");
                            int success2Chance = gemConfig.getInt(selectGem + ".SuccessChance");
                            Random random = new Random();
                            loreNumber = random.nextInt(100) + 1;
                            if (loreNumber <= success2Chance) {
                                String gemDisplayName = gemConfig.getString(selectGem + ".DisplayName");
                                int loreLoc = -1;
                                for (lore2Number = 1; lore2Number <= Objects.requireNonNull(list).size(); ++lore2Number) {
                                    if (list.get(lore2Number - 1).equalsIgnoreCase(PluginMessages.LORE_HEAD)) {
                                        loreLoc = lore2Number;
                                        break;
                                    }
                                }
                                if (loreLoc == -1) {
                                    list.add(gemDisplayName + " &a- " + successLore);
                                } else {
                                    list.add(loreLoc, gemDisplayName + " &a- " + successLore);
                                }
                                meta.setLore(list.stream().map(ColorParser::parse).collect(Collectors.toList()));
                                item.setItemMeta(meta);
                                MessageUtil.send(player, PluginMessages.INLAY_SUCCEEDED);
                                if (gemConfig.getBoolean(selectGem + ".SuccessBroadcast")) {
                                    MessageUtil.sendToAllPlayers(PluginMessages.PREFIX + Objects.requireNonNull(PluginMessages.INLAY_SUCCEEDED_BROADCAST).replace("{player_name}", player.getName()).replace("{gem}", gemDisplayName));
                                }
                            } else {
                                MessageUtil.send(player, PluginMessages.INLAY_FAILED);
                            }
                            player.closeInventory();
                            player.updateInventory();
                            Main.instance.getGemSelectMap().remove(player);
                        }
                    }
                    String gem = this.checkItem(clickEvent.getCurrentItem());
                    if (gem != null) {
                        Main.instance.getGemSelectMap().put(player, gem);
                        clickEvent.setCancelled(true);
                        player.closeInventory();
                        MessageUtil.send(player, PluginMessages.GEM_IS_SELECTED);
                    }
                }
            }
        }
    }

    public String checkItem(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) {
                String name = meta.getDisplayName();
                if (Main.instance.getGemNameCheckMap().containsKey(name)) {
                    return Main.instance.getGemNameCheckMap().get(name);
                }
            }
        }
        return null;
    }

}
