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
import org.bukkit.World;

import java.util.List;

public class WorldTeleportFrom extends InventoryBuilder {

    public WorldTeleportFrom(SimpleHomes simpleHomes, int page) {
        super(simpleHomes.getSettings().getString("inventory.worldTeleportFrom.title"), simpleHomes.getSettings().getInt("inventory.worldTeleportFrom.rows", 1) + 2);
        Yaml settings = simpleHomes.getSettings();
        int rows = settings.getInt("inventory.worldTeleportFrom.rows", 1);
        // Panel
        fillItem(() -> settings.getItemStack("inventory.worldTeleportFrom.items.panel"), new int[] {1, rows + 2});
        // Information
        addItem(4, () -> settings.getItemStack("inventory.worldTeleportFrom.items.information"));
        // Worlds
        List<World> worlds = simpleHomes.getServer().getWorlds();
        for (int i = 0; i < 45; i++) {
            int index = ((page * (4 * 9)) - (4 * 9)) + (i - 9);
            if (index < worlds.size()) {
                World world = worlds.get(index);
                setUpdatable(i, true);
                setUpdateInterval(i, 4);
                addItem(i, () -> {
                    String status;
                    if (settings.getStringList("world.disableTeleportFrom.worlds").contains(world.getName())) {
                        status = settings.getString("inventory.worldTeleportFrom.status.enable", "&aEnable");
                    } else {
                        status = settings.getString("inventory.worldTeleportFrom.status.disable", "&cDisable");
                    }
                    return Yaml.replace(settings.getItemStack("inventory.worldTeleportFrom.items.item"), new String[][] {
                            {"%world_name%", world.getName()},
                            {"%status%", status}
                    });
                });
                // Left Click
                addLeftAction(i, (player, slot) -> {
                    List<String> disabledWorlds = simpleHomes.getSettings().getStringList("world.disableTeleportFrom.worlds");
                    if (!disabledWorlds.contains(world.getName())) {
                        disabledWorlds.add(world.getName());
                    } else {
                        while (disabledWorlds.contains(world.getName())) {
                            disabledWorlds.remove(world.getName());
                        }
                    }
                    simpleHomes.getSettings().set("world.disableTeleportFrom.worlds", disabledWorlds);
                    simpleHomes.getSettings().saveFileConfiguration();
                });
            }
        }
        // Previous
        if (page > 1) {
            addItem(((rows + 2) * 9) - 9, () -> settings.getItemStack("inventory.worldTeleportFrom.items.previous"));
            addLeftAction(((rows + 2) * 9) - 9, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new WorldTeleportFrom(simpleHomes, page - 1)));
        }
        // Close
        addItem(((rows + 2) * 9) - 5, () -> settings.getItemStack("inventory.worldTeleportFrom.items.close"));
        addLeftAction(((rows + 2) * 9) - 5, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new WorldInventory(simpleHomes)));
        // Next
        if (worlds.size() > (page * (4 * 9))) {
            addItem(((rows + 2) * 9) - 1, () -> settings.getItemStack("inventory.worldTeleportFrom.items.next"));
            addLeftAction(((rows + 2) * 9) - 9, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new WorldTeleportFrom(simpleHomes, page + 1)));
        }
    }

}