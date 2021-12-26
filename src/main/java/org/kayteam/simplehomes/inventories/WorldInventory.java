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

import java.util.ArrayList;

public class WorldInventory extends InventoryBuilder {

    public WorldInventory(SimpleHomes simpleHomes) {
        super(simpleHomes.getSettings().getString("inventory.world.title"), 5);
        Yaml settings = simpleHomes.getSettings();
        // Panel
        fillItem(() -> settings.getItemStack("inventory.world.items.panel"));
        // SetHome
        setUpdatable(11, true);
        setUpdateInterval(11, 4);
        addItem(11, () -> {
            String status;
            if (settings.getBoolean("world.disableSetHome.enable")) {
                status = settings.getString("inventory.world.status.enable", "&aEnabled");
            } else {
                status = settings.getString("inventory.world.status.disable", "&cDisabled");
            }
            return Yaml.replace(settings.getItemStack("inventory.world.items.setHome"), new String[][]{{"%status%", status},});
        });
        addLeftAction(11, (player, slot) -> {
            settings.set("world.disableSetHome.enable", !settings.getBoolean("world.disableSetHome.enable", false));
            settings.saveFileConfiguration();
        });
        addRightAction(11, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new WorldSetHomeInventory(simpleHomes, 1)));
        // TeleportFrom
        setUpdatable(13, true);
        setUpdateInterval(13, 4);
        addItem(13, () -> {
            String status;
            if (settings.getBoolean("world.disableTeleportFrom.enable")) {
                status = settings.getString("inventory.world.status.enable", "&aEnabled");
            } else {
                status = settings.getString("inventory.world.status.disable", "&cDisabled");
            }
            return Yaml.replace(settings.getItemStack("inventory.world.items.teleportFrom"), new String[][]{{"%status%", status}});
        });
        addLeftAction(13, (player, slot) -> {
            settings.set("world.disableTeleportFrom.enable", !settings.getBoolean("world.disableTeleportFrom.enable", false));
            settings.saveFileConfiguration();
        });
        addRightAction(13, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new WorldTeleportFrom(simpleHomes, 1)));
        // TeleportTo
        setUpdatable(15, true);
        setUpdateInterval(15, 4);
        addItem(15, () -> {
            String status;
            if (settings.getBoolean("world.disableTeleportTo.enable")) {
                status = settings.getString("inventory.world.status.enable", "&aEnabled");
            } else {
                status = settings.getString("inventory.world.status.disable", "&cDisabled");
            }
            return Yaml.replace(settings.getItemStack("inventory.world.items.teleportTo"), new String[][]{{"%status%", status}});
        });
        addLeftAction(15, (player, slot) -> {
            settings.set("world.disableTeleportTo.enable", !settings.getBoolean("world.disableTeleportTo.enable", false));
            settings.saveFileConfiguration();
        });
        addRightAction(15, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new WorldTeleportTo(simpleHomes, 1)));
        // Close
        addItem(31, () -> settings.getItemStack("inventory.world.items.close"));
        addLeftAction(31, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new SimpleHomesInventory(simpleHomes)));
    }

}