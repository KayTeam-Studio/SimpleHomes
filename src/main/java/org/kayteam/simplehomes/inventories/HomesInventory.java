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
import org.kayteam.simplehomes.tasks.TeleportTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;

import java.util.List;
import java.util.Objects;

public class HomesInventory extends InventoryBuilder {

    public HomesInventory(SimpleHomes simpleHomes, Homes homes, int page) {
        super(simpleHomes.getSettings().getString("inventory.homes.title"), simpleHomes.getSettings().getInt("inventory.homes.rows", 1) + 2);
        Yaml settings = simpleHomes.getSettings();
        int rows = settings.getInt("inventory.homes.rows", 1);
        // Panels
        fillItem(() -> settings.getItemStack("inventory.homes.items.panel"), new int[]{1, rows + 2});
        // Information
        addItem(4, () -> Yaml.replace(simpleHomes.getSettings().getItemStack("inventory.homes.items.information"), new String[][] {
                {"%owner%", homes.getOwner()},
                {"%amount%", homes.getHomes().size() + ""}
        }));
        // Homes
        for (int i = 9; i < (rows + 2) * 9; i++) {
            int index = ((page * (rows * 9)) - (rows * 9)) + (i - 9);
            if (index < homes.getHomes().size()) {
                Home home = homes.getHomes().get(index);
                addItem(i, () -> Yaml.replace(settings.getItemStack("inventory.homes.items.home"), new String[][]{
                        {"%name%", home.getName()},
                        {"%world%", home.getWorld()},
                        {"%x%", Math.round(home.getX()) + ""},
                        {"%y%", Math.round(home.getY()) + ""},
                        {"%z%", Math.round(home.getZ()) + ""},
                        {"%yaw%", Math.round(home.getYaw()) + ""},
                        {"%pitch%", Math.round(home.getPitch()) + ""}
                }));
                // Left Click
                addLeftAction(i, (player, slot) -> {
                    player.closeInventory();
                    Yaml messages = simpleHomes.getMessages();
                    if (settings.getBoolean("world.disableTeleportFrom.enable") && !player.hasPermission("simple.bypass.disabled.worlds")) {
                        List<String> worlds = settings.getStringList("world.disableTeleportFrom.worlds");
                        Location location = player.getLocation();
                        if (worlds.contains(Objects.requireNonNull(location.getWorld()).getName())) {
                            messages.sendMessage(player, "home.disabledWorldFrom", new String[][] {
                                    {"%world_name%", location.getWorld().getName()},
                                    {"%world_x%", Math.round(location.getX()) + ""},
                                    {"%world_y%", Math.round(location.getY()) + ""},
                                    {"%world_z%", Math.round(location.getZ()) + ""},
                                    {"%world_yaw%", Math.round(location.getYaw()) + ""},
                                    {"%world_pitch%", Math.round(location.getPitch()) + ""}
                            });
                            return;
                        }
                    }
                    if (!TeleportTask.getTeleporting().contains(player.getName())) {
                        if (settings.getBoolean("world.disableTeleportTo.enable") && !player.hasPermission("simple.bypass.disabled.worlds")) {
                            List<String> worlds = settings.getStringList("world.disableTeleportTo.worlds");
                            if (worlds.contains(home.getWorld())) {
                                messages.sendMessage(player, "home.disabledWorldTo", new String[][] {
                                        {"%home_name%", home.getName()},
                                        {"%world_name%", home.getWorld()},
                                        {"%world_x%", Math.round(home.getX()) + ""},
                                        {"%world_y%", Math.round(home.getY()) + ""},
                                        {"%world_z%", Math.round(home.getZ()) + ""},
                                        {"%world_yaw%", Math.round(home.getYaw()) + ""},
                                        {"%world_pitch%", Math.round(home.getPitch()) + ""}
                                });
                                return;
                            }
                        }
                        if (settings.getBoolean("vault.enable")) {
                            Economy economy = SimpleHomes.getEconomy();
                            if (economy != null) {
                                double amount = settings.getDouble("vault.teleport");
                                if (economy.has(player, amount) || player.hasPermission("simple.bypass.home.cost")) {
                                    if (!player.hasPermission("simple.bypass.home.cost")) {
                                        economy.withdrawPlayer(player, amount);
                                    }
                                } else {
                                    messages.sendMessage(player, "home.insufficientMoney", new String[][] {
                                            {"%amount%", Math.round(amount) + ""}
                                    });
                                    return;
                                }
                            }
                        }
                        TeleportTask teleportTask = new TeleportTask(simpleHomes, player, home);
                        teleportTask.startScheduler();
                    } else {
                        messages.sendMessage(player, "home.alreadyInTeleporting");
                    }
                });
                // Right Click
                addRightAction(i, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new DeleteHomeConfirmInventory(simpleHomes, home, "gui")));
            }
        }
        // Previous
        if (page > 1) {
            addItem(((rows + 2) * 9) - 9, () -> settings.getItemStack("inventory.homes.items.prevPage"));
            addLeftAction(((rows + 2) * 9) - 9, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes, page - 1)));
        }
        // Close
        addItem(((rows + 2) * 9) - 5, () -> settings.getItemStack("inventory.homes.items.close"));
        addLeftAction(((rows + 2) * 9) - 5, (player, slot) -> player.closeInventory());
        // Next
        if (homes.getHomes().size() > (page * (4 * 9))) {
            addItem(((rows + 2) * 9) - 1, () -> settings.getItemStack("inventory.homes.items.nextPage"));
            addLeftAction(((rows + 2) * 9) - 9, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes, page + 1)));
        }
    }

}