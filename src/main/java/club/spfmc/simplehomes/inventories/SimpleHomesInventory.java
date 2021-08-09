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
import club.spfmc.simplehomes.util.inventory.Item;
import club.spfmc.simplehomes.util.inventory.MenuInventory;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SimpleHomesInventory extends MenuInventory {

    private final SimpleHomes simpleHomes;

    public SimpleHomesInventory(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        Yaml settings = simpleHomes.getSettings();
        // Panel
        for (int i = 0; i < (getRows() * 9); i++) {
            addMenuAction(i, new Item() {
                @Override
                public ItemStack getItem() {
                    return settings.getItemStack("inventory.admin.items.panel");
                }
            });
        }
        // World
        addMenuAction(11, new Item() {
            @Override
            public ItemStack getItem() {
                return settings.getItemStack("inventory.admin.items.world");
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                simpleHomes.getMenuInventoryManager().openInventory(player, new WorldInventory(simpleHomes));
            }
        });
        // Vault
        addMenuAction(13, new Item() {
            @Override
            public ItemStack getItem() {
                return settings.getItemStack("inventory.admin.items.vault");
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                simpleHomes.getMenuInventoryManager().openInventory(player, new VaultInventory(simpleHomes));
            }
        });
        // Reload
        addMenuAction(15, new Item() {
            @Override
            public ItemStack getItem() {
                return settings.getItemStack("inventory.admin.items.reload");
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
                // Files
                simpleHomes.getSettings().reloadFileConfiguration();
                simpleHomes.getMessages().reloadFileConfiguration();
                // MaxHomes
                simpleHomes.getHomesManager().loadMaxHomes();
                // Players
                for (Player p:simpleHomes.getServer().getOnlinePlayers()) {
                    p.closeInventory();
                    simpleHomes.getHomesManager().unloadHomes(p.getName());
                }
                for (Player p:simpleHomes.getServer().getOnlinePlayers()) {
                    simpleHomes.getHomesManager().loadHomes(player);
                }
                simpleHomes.getMessages().sendMessage(player, "admin.reloaded", new String[][] {
                        {"%plugin%", simpleHomes.getDescription().getName()}
                });
            }
        });
        // Close
        addMenuAction(31, new Item() {
            @Override
            public ItemStack getItem() {
                return settings.getItemStack("inventory.admin.items.close");
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
            }
        });
    }

    @Override
    public String getTitle() {
        return simpleHomes.getSettings().getString("inventory.admin.title");
    }

    @Override
    public int getRows() {
        return 5;
    }

}