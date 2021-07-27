/*
 * Copyright (C) 2021 SirOswaldo
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

public abstract class SimpleInventory implements Listener {

    private String title;
    private int size;

    public SimpleInventory(String title, int size) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
        this.size = size;
    }

    public Inventory getInventory(String[] data) {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        setupItems(inventory, data);
        return inventory;
    }

    public abstract void setupItems(Inventory inventory, String[] data);

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(title)) {
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            onClick(player, slot, event);
        }
    }

    public abstract void onClick(Player player, int slot, InventoryClickEvent event);

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (onClose(player)) {
            event.getPlayer().openInventory(event.getInventory());
        }
    }

    public abstract boolean onClose(Player player);

}