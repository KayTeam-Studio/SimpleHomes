/*
 * Copyright (C) 2021  SirOswaldo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.spfmc.simplehomes.util.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public abstract class SimplePagesInventory implements Listener {

    private static final HashMap<String, List<Object>> objects = new HashMap<>();
    private static final HashMap<String, Integer> page = new HashMap<>();

    /**
     *
     * @param player
     * @param objects
     * @param rows Amount of rows min: 1 max: 4
     */
    public void openInventory(Player player, List<Object> objects, int rows) {
        SimplePagesInventory.objects.put(player.getName(), objects);
        int page = 1;
        SimplePagesInventory.page.put(player.getName(), page);
        int slots = 0;
        if (!(rows > 0 && rows < 5)) {
            rows = 1;
        }
        slots = (rows + 2) * 9;;
        Inventory inventory = Bukkit.createInventory(null, slots, ChatColor.translateAlternateColorCodes('&', getTitle()));
        // Set Panels
        inventory.setItem(0, getPanel());
        inventory.setItem(1, getPanel());
        inventory.setItem(2, getPanel());
        inventory.setItem(3, getPanel());
        inventory.setItem(5, getPanel());
        inventory.setItem(6, getPanel());
        inventory.setItem(7, getPanel());
        inventory.setItem(8, getPanel());
        inventory.setItem(slots - 8, getPanel());
        inventory.setItem(slots - 7, getPanel());
        inventory.setItem(slots - 6, getPanel());
        inventory.setItem(slots - 4, getPanel());
        inventory.setItem(slots - 3, getPanel());
        inventory.setItem(slots - 2, getPanel());
        // Set Information
        inventory.setItem(4, getInformation(objects));
        // Set Prev Button
        inventory.setItem(slots - 9, getPrevPageButton());
        // Set Close Button
        inventory.setItem(slots - 5, getCloseButton());
        // Set Next Button
        inventory.setItem(slots - 1, getNextPageButton());
        // Set List
        for (int index = 9; index < slots - 9; index++) {
            int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
            int listIndex = realIndex - 1;
            if (objects.size() > listIndex) {
                inventory.setItem(index, getFormattedItem(objects.get(listIndex)));
            } else {
                inventory.setItem(index, getFormattedItem(null));
            }
        }
        // Open Inventory
        player.openInventory(inventory);
    }

    public abstract String getTitle();
    public abstract ItemStack getPanel();
    public abstract ItemStack getInformation(List<Object> objects);
    public abstract ItemStack getFormattedItem(Object objects);
    public abstract ItemStack getNextPageButton();
    public abstract ItemStack getPrevPageButton();
    public abstract ItemStack getCloseButton();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', getTitle()))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            int page = SimplePagesInventory.page.get(player.getName());
            int rows = (event.getInventory().getSize() - 18) / 9;
            if (slot > 8 && slot < (event.getInventory().getSize() - 9)) {
                int realIndex = ((page * (rows * 9)) -  (rows * 9)) + (slot - 8);
                int listIndex = realIndex - 1;
                List<Object> objects = SimplePagesInventory.objects.get(player.getName());
                Object object = null;
                if (objects.size() > listIndex) {
                    object = objects.get(listIndex);
                }
                switch (event.getClick()) {
                    case LEFT:
                        onLeftClick(player, object);
                        break;
                    case RIGHT:
                        onRightClick(player, object);
                        break;
                    case MIDDLE:
                        onMiddleClick(player, object);
                        break;
                    case SHIFT_LEFT:
                        onShiftLeftClick(player, object);
                        break;
                    case SHIFT_RIGHT:
                        onShiftRightClick(player, object);
                        break;
                }
            } else if (slot == (event.getInventory().getSize() - 9)) {
                if (page > 1) {
                    page = page - 1;
                    SimplePagesInventory.page.put(player.getName(), page);
                    List<Object> objects = SimplePagesInventory.objects.get(player.getName());
                    for (int index = 9; index < (event.getInventory().getSize() - 9); index++) {
                        int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
                        int listIndex = realIndex - 1;
                        if (objects.size() > listIndex) {
                            event.getInventory().setItem(index, getFormattedItem(objects.get(listIndex)));
                        } else {
                            event.getInventory().setItem(index, getFormattedItem(null));
                        }
                    }
                }
            } else if (slot == (event.getInventory().getSize() - 1)) {
                List<Object> objects = SimplePagesInventory.objects.get(player.getName());
                if (objects.size() > (page * (rows * 9))) {
                    page = page + 1;
                    SimplePagesInventory.page.put(player.getName(), page);
                    for (int index = 9; index < (event.getInventory().getSize() - 9); index++) {
                        int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
                        int listIndex = realIndex - 1;
                        if (objects.size() > listIndex) {
                            event.getInventory().setItem(index, getFormattedItem(objects.get(listIndex)));
                        } else {
                            event.getInventory().setItem(index, getFormattedItem(null));
                        }
                    }
                }
            } else if (slot == (event.getInventory().getSize() - 5)) {
                SimplePagesInventory.objects.remove(player.getName());
                SimplePagesInventory.page.remove(player.getName());
                player.closeInventory();
            }
        }
    }

    public  void onLeftClick(Player player, Object object) {};
    public  void onRightClick(Player player, Object object) {};
    public  void onMiddleClick(Player player, Object object) {};
    public  void onShiftRightClick(Player player, Object object) {};
    public  void onShiftLeftClick(Player player, Object object) {};


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', getTitle()))) {
            SimplePagesInventory.objects.remove(player.getName());
            SimplePagesInventory.page.remove(player.getName());
        }
    }

}