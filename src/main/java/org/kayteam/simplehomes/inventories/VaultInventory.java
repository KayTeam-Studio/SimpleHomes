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

import org.kayteam.api.inventory.InventoryBuilder;
import org.kayteam.api.yaml.Yaml;
import org.kayteam.simplehomes.SimpleHomes;
import org.kayteam.simplehomes.inputs.VaultDeleteHomeInput;
import org.kayteam.simplehomes.inputs.VaultOverwriteHomeInput;
import org.kayteam.simplehomes.inputs.VaultSetHomeInput;
import org.kayteam.simplehomes.inputs.VaultTeleportInput;

public class VaultInventory extends InventoryBuilder {

    public VaultInventory(SimpleHomes simpleHomes) {
        super(simpleHomes.getSettings().getString("inventory.vault.title"), 5);
        Yaml settings = simpleHomes.getSettings();
        Yaml messages = simpleHomes.getMessages();
        // Panel
        fillItem(() -> settings.getItemStack("inventory.vault.items.panel"));
        // SetHome
        addItem(10, () -> {
            double amount = settings.getDouble("vault.setHome", 0.0);
            return Yaml.replace(settings.getItemStack("inventory.vault.items.setHome"), new String[][] {{"%amount%", Math.round(amount) + ""}});
        });
        addLeftAction(10, (player, slot) -> {
            player.closeInventory();
            simpleHomes.getInputManager().addInput(player, new VaultSetHomeInput(simpleHomes));
            messages.sendMessage(player, "admin.vault.setHome.enterAmount");
        });
        // Overwrite
        addItem(12, () -> {
            double amount = settings.getDouble("vault.overwriteHome", 0.0);
            return Yaml.replace(settings.getItemStack("inventory.vault.items.overwriteHome"), new String[][] {{"%amount%", Math.round(amount) + ""}});
        });
        addLeftAction(12, (player, slot) -> {
            player.closeInventory();
            simpleHomes.getInputManager().addInput(player, new VaultOverwriteHomeInput(simpleHomes));
            messages.sendMessage(player, "admin.vault.overwriteHome.enterAmount");
        });
        // DeleteHome
        addItem(14, () -> {
            double amount = settings.getDouble("vault.deleteHome", 0.0);
            return Yaml.replace(settings.getItemStack("inventory.vault.items.deleteHome"), new String[][] {{"%amount%", Math.round(amount) + ""}});
        });
        addLeftAction(14, (player, slot) -> {
            player.closeInventory();
            simpleHomes.getInputManager().addInput(player, new VaultDeleteHomeInput(simpleHomes));
            messages.sendMessage(player, "admin.vault.deleteHome.enterAmount");
        });

        // Teleport
        addItem(16, () -> {
            double amount = settings.getDouble("vault.teleport", 0.0);
            return Yaml.replace(settings.getItemStack("inventory.vault.items.teleport"), new String[][] {{"%amount%", Math.round(amount) + ""}});
        });
        addLeftAction(16, (player, slot) -> {
            player.closeInventory();
            simpleHomes.getInputManager().addInput(player, new VaultTeleportInput(simpleHomes));
            messages.sendMessage(player, "admin.vault.teleport.enterAmount");
        });
        // Status
        setUpdatable(28, true);
        setUpdateInterval(28, 4);
        addItem(28, () -> {
            String status;
            if (settings.getBoolean("vault.enable")) {
                status = settings.getString("inventory.vault.status.enable");
            } else {
                status = settings.getString("inventory.vault.status.disable");
            }
            return Yaml.replace(settings.getItemStack("inventory.vault.items.status"), new String[][] {{"%status%", status}});
        });
        addLeftAction(28, (player, slot) -> {
            settings.set("vault.enable", !settings.getBoolean("vault.enable"));
            settings.saveFileConfiguration();
        });
        // Close
        addItem(31, () -> settings.getItemStack("inventory.vault.items.close"));
        addLeftAction(31, (player, slot) -> simpleHomes.getInventoryManager().openInventory(player, new SimpleHomesInventory(simpleHomes)));
    }

}
