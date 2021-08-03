/*
 *  Copyright (C) 2021 SirOswaldo
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.spfmc.simplehomes.util.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class MenuInventoryManager implements Listener {

    private final HashMap<String, MenuInventory> inventories = new HashMap<>();

    public void openInventory(Player player, MenuInventory menuInventory) {
        inventories.put(player.getName(), menuInventory);
        Inventory inventory = Bukkit.createInventory(null, menuInventory.getRows() * 9, ChatColor.translateAlternateColorCodes('&', menuInventory.getTitle()));
        for (int slot:menuInventory.getItems().keySet()) {
            Item action = menuInventory.getItems().get(slot);
            if (inventory.getSize() > slot) {
                inventory.setItem(slot, action.getItem());
            }
        }
        // Open Inventory
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (inventories.containsKey(player.getName())) {
            MenuInventory menuInventory = inventories.get(player.getName());
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', menuInventory.getTitle()))) {
                event.setCancelled(true);
                int slot = event.getRawSlot();
                HashMap<Integer, Item> items = menuInventory.getItems();
                if (items.containsKey(slot)) {
                    Item item = items.get(slot);
                    switch (event.getClick()) {
                        case LEFT:
                            item.onLeftClick(player);
                            break;
                        case RIGHT:
                            item.onRightClick(player);
                            break;
                        case MIDDLE:
                            item.onMiddleClick(player);
                            break;
                        case SHIFT_LEFT:
                            item.onShiftLeftClick(player);
                            break;
                        case SHIFT_RIGHT:
                            item.onShiftRightClick(player);
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (inventories.containsKey(player.getName())) {
            MenuInventory menuInventory = inventories.get(player.getName());
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', menuInventory.getTitle()))) {
                inventories.remove(player.getName());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        inventories.remove(event.getPlayer().getName());
    }

}