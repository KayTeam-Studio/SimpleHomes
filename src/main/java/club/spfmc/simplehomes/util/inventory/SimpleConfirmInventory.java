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

public abstract class SimpleConfirmInventory implements Listener {

    private static final HashMap<String, Object> objects = new HashMap<>();

    public abstract String getTitle();

    public abstract ItemStack getPanel();
    public abstract ItemStack getInformation(Object object);
    public abstract ItemStack getCancelButton();
    public abstract ItemStack getAcceptButton();

    public void openInventory(Player player, Object object) {
        SimpleConfirmInventory.objects.put(player.getName(), object);
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', getTitle()));
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, getPanel());
        }
        inventory.setItem(11, getAcceptButton());
        inventory.setItem(13, getInformation(object));
        inventory.setItem(15, getCancelButton());
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', getTitle()))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            if (slot == 11) {
                onAcceptClick(player, SimpleConfirmInventory.objects.get(player.getName()));
                SimpleConfirmInventory.objects.remove(player.getName());
            } else if (slot == 15) {
                onCancelClick(player, SimpleConfirmInventory.objects.get(player.getName()));
                SimpleConfirmInventory.objects.remove(player.getName());
            }
        }
    }

    public void onAcceptClick(Player player, Object object){}
    public void onCancelClick(Player player, Object object){}

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', getTitle()))) {
            SimpleConfirmInventory.objects.remove(player.getName());
        }
    }

}