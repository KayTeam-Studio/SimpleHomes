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

public class SimpleHomesInventory extends InventoryBuilder {

    public SimpleHomesInventory(SimpleHomes simpleHomes) {
        super(simpleHomes.getSettings().getString("inventory.admin.title"), 5);
        Yaml settings = simpleHomes.getSettings();
        // Panel
        fillItem(() -> settings.getItemStack("inventory.admin.items.panel"));
        // World
        addItem(11, () -> settings.getItemStack("inventory.admin.items.world"));
        addLeftAction(11, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new WorldInventory(simpleHomes)));
        // Vault
        addItem(13, () -> settings.getItemStack("inventory.admin.items.vault"));
        addLeftAction(13, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new VaultInventory(simpleHomes)));
        // Reload
        addItem(15, () -> settings.getItemStack("inventory.admin.items.reload"));
        addLeftAction(15, (player, slot) -> {
            player.closeInventory();
            simpleHomes.onReload();
            simpleHomes.getMessages().sendMessage(player, "admin.reloaded", new String[][] {{"%plugin%", simpleHomes.getDescription().getName()}});
        });
        // Close
        addItem(31, () -> settings.getItemStack("inventory.admin.items.close"));
        addLeftAction(31, (player, slot) -> player.closeInventory());
    }

}