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

package club.spfmc.simplehomes.inventories;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.inputs.VaultDeleteHomeInput;
import club.spfmc.simplehomes.inputs.VaultOverwriteHomeInput;
import club.spfmc.simplehomes.inputs.VaultSetHomeInput;
import club.spfmc.simplehomes.inputs.VaultTeleportInput;
import club.spfmc.simplehomes.util.inventory.Item;
import club.spfmc.simplehomes.util.inventory.MenuInventory;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VaultInventory extends MenuInventory {

    private final SimpleHomes simpleHomes;

    public VaultInventory(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        Yaml settings = simpleHomes.getSettings();
        Yaml messages = simpleHomes.getMessages();
        // Panel
        for (int i = 0; i < (getRows() * 9); i++) {
            addMenuAction(i, new Item() {
                @Override
                public ItemStack getItem() {
                    return settings.getItemStack("inventory.vault.items.panel");
                }
            });
        }
        // SetHome
        addMenuAction(10, new Item() {
            @Override
            public ItemStack getItem() {
                double amount = settings.getDouble("vault.setHome", 0.0);
                return Yaml.replace(settings.getItemStack("inventory.vault.items.setHome"), new String[][] {
                        {"%amount%", Math.round(amount) + ""}
                });
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                simpleHomes.getInputManager().addInput(player, new VaultSetHomeInput(simpleHomes));
                messages.sendMessage(player, "admin.vault.setHome.enterAmount");
            }
        });
        // Overwrite
        addMenuAction(12, new Item() {
            @Override
            public ItemStack getItem() {
                double amount = settings.getDouble("vault.overwriteHome", 0.0);
                return Yaml.replace(settings.getItemStack("inventory.vault.items.overwriteHome"), new String[][] {
                        {"%amount%", Math.round(amount) + ""}
                });
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                simpleHomes.getInputManager().addInput(player, new VaultOverwriteHomeInput(simpleHomes));
                messages.sendMessage(player, "admin.vault.overwriteHome.enterAmount");
            }
        });
        // DeleteHome
        addMenuAction(14, new Item() {
            @Override
            public ItemStack getItem() {
                double amount = settings.getDouble("vault.deleteHome", 0.0);
                return Yaml.replace(settings.getItemStack("inventory.vault.items.deleteHome"), new String[][] {
                        {"%amount%", Math.round(amount) + ""}
                });
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                simpleHomes.getInputManager().addInput(player, new VaultDeleteHomeInput(simpleHomes));
                messages.sendMessage(player, "admin.vault.deleteHome.enterAmount");
            }
        });
        // Teleport
        addMenuAction(16, new Item() {
            @Override
            public ItemStack getItem() {
                double amount = settings.getDouble("vault.teleport", 0.0);
                return Yaml.replace(settings.getItemStack("inventory.vault.items.teleport"), new String[][] {
                        {"%amount%", Math.round(amount) + ""}
                });
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                simpleHomes.getInputManager().addInput(player, new VaultTeleportInput(simpleHomes));
                messages.sendMessage(player, "admin.vault.teleport.enterAmount");
            }
        });
        // Status
        addMenuAction(28, new Item() {
            @Override
            public ItemStack getItem() {
                String status;
                if (settings.getBoolean("vault.enable")) {
                    status = settings.getString("inventory.vault.status.enable");
                } else {
                    status = settings.getString("inventory.vault.status.disable");
                }
                return Yaml.replace(settings.getItemStack("inventory.vault.items.status"), new String[][] {
                        {"%status%", status}
                });
            }

            @Override
            public void onLeftClick(Player player) {
                if (settings.getBoolean("vault.enable")) {
                    settings.set("vault.enable", false);
                } else {
                    settings.set("vault.enable", true);
                }
                settings.saveFileConfiguration();
                player.getOpenInventory().setItem(28, getItem());
            }
        });
        // Close
        addMenuAction(31, new Item() {
            @Override
            public ItemStack getItem() {
                return settings.getItemStack("inventory.vault.items.close");
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                simpleHomes.getMenuInventoryManager().openInventory(player, new SimpleHomesInventory(simpleHomes));
            }
        });
    }

    @Override
    public String getTitle() {
        return simpleHomes.getSettings().getString("inventory.vault.title");
    }

    @Override
    public int getRows() {
        return 5;
    }
}
