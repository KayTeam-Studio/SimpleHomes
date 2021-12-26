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

package org.kayteam.simplehomes.inventories;

import org.kayteam.kayteamapi.inventory.InventoryBuilder;
import org.kayteam.kayteamapi.yaml.Yaml;
import org.kayteam.simplehomes.SimpleHomes;
import org.kayteam.simplehomes.home.Home;
import org.kayteam.simplehomes.home.Homes;
import org.kayteam.simplehomes.home.HomesManager;
import net.milkbowl.vault.economy.Economy;

public class DeleteHomeConfirmInventory extends InventoryBuilder {

    public DeleteHomeConfirmInventory(SimpleHomes simpleHomes, Home home, String from) {
        super(simpleHomes.getSettings().getString("inventory.deleteConfirm.title"), 3);
        Yaml settings = simpleHomes.getSettings();
        fillItem(() -> settings.getItemStack("inventory.deleteConfirm.items.panel"));
        // Information
        addItem(13, () -> Yaml.replace(settings.getItemStack("inventory.deleteConfirm.items.information"), new String[][] {
                {"%name%", home.getName()},
                {"%world%", home.getWorld()},
                {"%x%", Math.round(home.getX()) + ""},
                {"%y%", Math.round(home.getY()) + ""},
                {"%z%", Math.round(home.getZ()) + ""},
                {"%yaw%", Math.round(home.getYaw()) + ""},
                {"%pitch%", Math.round(home.getPitch()) + ""}
        }));
        // Cancel
        addItem(11, () -> settings.getItemStack("inventory.deleteConfirm.items.cancel"));
        addLeftAction(11, (player, slot) -> {
            player.closeInventory();
            if (from.equals("gui")) {
                HomesManager homesManager = simpleHomes.getHomesManager();
                Homes homes = homesManager.getHomes(home.getOwner());
                simpleHomes.getInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes, 1));
            }
        });
        // Accept
        addItem(15, () -> settings.getItemStack("inventory.deleteConfirm.items.accept"));
        addLeftAction(15, (player, slot) -> {
            Yaml messages = simpleHomes.getMessages();
            String name = home.getName();
            String owner = home.getOwner();
            if (SimpleHomes.getEconomy() != null) {
                Economy economy = SimpleHomes.getEconomy();
                if (player.getName().equals(owner)) {
                    if (!player.hasPermission("simple.bypass.home.cost")) {
                        double cost = simpleHomes.getSettings().getDouble("vault.deleteHome", 0.0);
                        economy.depositPlayer(player, cost);
                    }
                }
            }
            HomesManager homesManager = simpleHomes.getHomesManager();
            Homes homes = homesManager.getHomes(home.getOwner());
            homes.removeHome(home.getName());
            if (player.getName().equals(home.getOwner())) {
                if (SimpleHomes.getEconomy() != null) {

                    messages.sendMessage(player,  "deleteHome.delete", new String[][]{{"%home%", name}, {"%cost%", simpleHomes.getSettings().getDouble("vault.deleteHome", 0.0) + ""}});
                } else {
                    messages.sendMessage(player,  "deleteHome.delete", new String[][]{{"%home%", name}});
                }
            } else {
                messages.sendMessage(player, "deleteHome.deleteOther", new String[][] {
                        {"%home%", name},
                        {"%player%", owner}
                });
            }
            homesManager.saveHomes(owner);
            player.closeInventory();
            if (homes.getHomes().size() > 0) {
                if (from.equals("gui")) {
                    simpleHomes.getInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes, 1));
                }
            } else {
                if (player.getName().equals(owner)) {
                    messages.sendMessage(player, "homes.emptyHomes");
                } else {
                    messages.sendMessage(player, "homes.emptyHomesOther", new String[][] {
                            {"%player%", owner}
                    });
                }
            }
        });
    }

}
