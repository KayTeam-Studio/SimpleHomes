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

package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.inventories.HomesInventory;
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;


public class HomesCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public HomesCommand(SimpleHomes simpleHomes) {
        super(simpleHomes, "Homes");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        if (player.hasPermission("simple.homes")) {
            HomesManager homesManager = simpleHomes.getHomesManager();
            if (arguments.length > 0) {
                String name = arguments[0];
                Player target = simpleHomes.getServer().getPlayer(name);
                if (target != null) {
                    if (player.equals(target)) {
                        Homes homes = homesManager.getHomes(player.getName());
                        if (homes.getHomes().size() > 0) {
                            simpleHomes.getMenuInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes.getHomes()));
                        } else {
                            messages.sendMessage(player, "homes.emptyHomes");
                        }
                    } else {
                        if (player.hasPermission("simple.homes.other")) {
                            Homes homes = homesManager.getHomes(target.getName());
                            if (homes.getHomes().size() > 0) {
                                simpleHomes.getMenuInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes.getHomes()));
                            } else {
                                messages.sendMessage(player, "homes.emptyHomesOther", new String[][] {
                                        {"%player%", arguments[0]}
                                });
                            }
                        } else {
                            messages.sendMessage(player, "homes.noPermissionOther");
                        }
                    }
                } else {
                    if (player.hasPermission("simple.homes.other")) {
                        Yaml yaml = new Yaml(simpleHomes, "players", arguments[0]);
                        if (yaml.existFileConfiguration()) {
                            homesManager.loadHomes(arguments[0]);
                            Homes homes = homesManager.getHomes(arguments[0]);
                            if (homes.getHomes().size() > 0) {
                                simpleHomes.getMenuInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes.getHomes()));
                            } else {
                                messages.sendMessage(player, "homes.emptyHomesOther", new String[][] {
                                        {"%player%", arguments[0]}
                                });
                            }
                        } else {
                            messages.sendMessage(player, "homes.invalidName", new String[][] {
                                    {"%player%", arguments[0]}
                            });
                        }
                    } else {
                        messages.sendMessage(player, "homes.noPermissionOther");
                    }
                }
            } else {
                Homes homes = homesManager.getHomes(player.getName());
                if (homes.getHomes().size() > 0) {
                    simpleHomes.getMenuInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes.getHomes()));
                } else {
                    messages.sendMessage(player, "homes.emptyHomes");
                }
            }
        } else {
            messages.sendMessage(player, "homes.noPermission");
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        simpleHomes.getMessages().sendMessage(console, "homes.isConsole");
    }

}