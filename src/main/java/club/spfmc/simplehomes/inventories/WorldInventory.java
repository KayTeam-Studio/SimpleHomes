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

import java.util.ArrayList;

public class WorldInventory extends MenuInventory {

    private final SimpleHomes simpleHomes;

    public WorldInventory(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        Yaml settings = simpleHomes.getSettings();
        for (int i = 0; i < (getRows() * 9); i++) {
            addMenuAction(i, new Item() {
                @Override
                public ItemStack getItem() {
                    return settings.getItemStack("inventory.world.items.panel");
                }
            });
        }
        // SetHome
        addMenuAction(11, new Item() {
            @Override
            public ItemStack getItem() {
                String status;
                if (settings.getBoolean("world.disableSetHome.enable")) {
                    status = settings.getString("inventory.world.status.enable", "&aEnabled");
                } else {
                    status = settings.getString("inventory.world.status.disable", "&cDisabled");
                }
                return Yaml.replace(settings.getItemStack("inventory.world.items.setHome"), new String[][]{
                        {"%status%", status},
                });
            }

            @Override
            public void onLeftClick(Player player) {
                boolean status = settings.getBoolean("world.disableSetHome.enable", false);
                if (status) {
                    settings.set("world.disableSetHome.enable", false);
                } else {
                    settings.set("world.disableSetHome.enable", true);
                }
                settings.saveFileConfiguration();
                player.getOpenInventory().setItem(11, getItem());
            }

            @Override
            public void onRightClick(Player player) {
                player.closeInventory();
                simpleHomes.getMenuInventoryManager().openInventory(player, new WorldSetHomeInventory(simpleHomes, new ArrayList<>(simpleHomes.getServer().getWorlds())));
            }
        });
        // TeleportFrom
        addMenuAction(13, new Item() {
            @Override
            public ItemStack getItem() {
                String status;
                if (settings.getBoolean("world.disableTeleportFrom.enable")) {
                    status = settings.getString("inventory.world.status.enable", "&aEnabled");
                } else {
                    status = settings.getString("inventory.world.status.disable", "&cDisabled");
                }
                return Yaml.replace(settings.getItemStack("inventory.world.items.teleportFrom"), new String[][]{
                        {"%status%", status},
                });
            }

            @Override
            public void onLeftClick(Player player) {
                boolean status = settings.getBoolean("world.disableTeleportFrom.enable", false);
                if (status) {
                    settings.set("world.disableTeleportFrom.enable", false);
                } else {
                    settings.set("world.disableTeleportFrom.enable", true);
                }
                settings.saveFileConfiguration();
                player.getOpenInventory().setItem(13, getItem());
            }

            @Override
            public void onRightClick(Player player) {
                player.closeInventory();
                simpleHomes.getMenuInventoryManager().openInventory(player, new WorldTeleportFrom(simpleHomes, new ArrayList<>(simpleHomes.getServer().getWorlds())));
            }
        });
        // TeleportTo
        addMenuAction(15, new Item() {
            @Override
            public ItemStack getItem() {
                String status;
                if (settings.getBoolean("world.disableTeleportTo.enable")) {
                    status = settings.getString("inventory.world.status.enable", "&aEnabled");
                } else {
                    status = settings.getString("inventory.world.status.disable", "&cDisabled");
                }
                return Yaml.replace(settings.getItemStack("inventory.world.items.teleportTo"), new String[][]{
                        {"%status%", status},
                });
            }

            @Override
            public void onLeftClick(Player player) {
                boolean status = settings.getBoolean("world.disableTeleportTo.enable", false);
                if (status) {
                    settings.set("world.disableTeleportTo.enable", false);
                } else {
                    settings.set("world.disableTeleportTo.enable", true);
                }
                settings.saveFileConfiguration();
                player.getOpenInventory().setItem(15, getItem());
            }

            @Override
            public void onRightClick(Player player) {
                player.closeInventory();
                simpleHomes.getMenuInventoryManager().openInventory(player, new WorldTeleportTo(simpleHomes, new ArrayList<>(simpleHomes.getServer().getWorlds())));
            }
        });
        // Close
        addMenuAction(31, new Item() {
            @Override
            public ItemStack getItem() {
                return settings.getItemStack("inventory.world.items.close");
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
        return simpleHomes.getSettings().getString("inventory.world.title");
    }

    @Override
    public int getRows() {
        return 5;
    }
}