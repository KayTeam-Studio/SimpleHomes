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

package org.kayteam.simplehomes.commands;

import org.kayteam.kayteamapi.command.SimpleCommand;
import org.kayteam.kayteamapi.yaml.Yaml;
import org.kayteam.simplehomes.SimpleHomes;
import org.kayteam.simplehomes.home.Home;
import org.kayteam.simplehomes.home.Homes;
import org.kayteam.simplehomes.home.HomesManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class SetHomeCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public SetHomeCommand(SimpleHomes simpleHomes) {
        super("SetHome");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        Yaml settings = simpleHomes.getSettings();
        if (player.hasPermission("simple.set.home")) {
            if (arguments.length > 0) {
                HomesManager homesManager = simpleHomes.getHomesManager();
                int maxHomes = 0;
                for (Integer key:homesManager.getMaxHomes().keySet()) {
                    String name = homesManager.getMaxHomes().get(key);
                    if (name.equals("default") || player.hasPermission("simple.max.homes." + name)) maxHomes = key;
                }
                if (settings.getBoolean("world.disableSetHome.enable") && !player.hasPermission("simple.bypass.disabled.worlds")) {
                    List<String> worlds = settings.getStringList("world.disableSetHome.worlds");
                    Location location = player.getLocation();
                    if (worlds.contains(Objects.requireNonNull(location.getWorld()).getName())) {
                        messages.sendMessage(player, "setHome.disabledWorld", new String[][] {
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
                Homes homes = homesManager.getHomes(player.getName());
                if (homes.getHomes().size() < maxHomes || homes.containHome(arguments[0])) {
                    if (homes.containHome(arguments[0])) {
                        if (settings.getBoolean("vault.enable")) {
                            Economy economy = SimpleHomes.getEconomy();
                            if (economy != null) {
                                double cost = simpleHomes.getSettings().getDouble("vault.overwriteHome", 0.0);
                                if (player.hasPermission("simple.bypass.home.cost")) cost = 0;
                                if (economy.has(player, cost)) {
                                    economy.withdrawPlayer(player, cost);
                                    createHome(player, homes, arguments[0], cost, "overwrite");
                                } else {
                                    messages.sendMessage(player, "setHome.insufficientOverwrittenMoney", new String[][]{{"%cost%", cost + ""}});
                                }
                            } else {
                                createHome(player, homes, arguments[0], 0, "overwrite");
                            }
                        } else {
                            createHome(player, homes, arguments[0], 0, "overwrite");
                        }
                    } else {
                        if (settings.getBoolean("vault.enable")) {
                            Economy economy = SimpleHomes.getEconomy();
                            if (economy != null) {
                                double cost = simpleHomes.getSettings().getDouble("vault.setHome", 0.0);
                                if (player.hasPermission("simple.bypass.home.cost")) cost = 0;
                                if (economy.has(player, cost)) {
                                    economy.withdrawPlayer(player, cost);
                                    createHome(player, homes, arguments[0], cost, "new");
                                } else {
                                    messages.sendMessage(player, "setHome.insufficientSetMoney", new String[][]{{"%cost%", cost + ""}});
                                }
                            } else {
                                createHome(player, homes, arguments[0], 0, "new");
                            }
                        } else {
                            createHome(player, homes, arguments[0], 0, "new");
                        }
                    }
                } else {
                    messages.sendMessage(player, "setHome.maxHomesReached");
                }
            } else {
                messages.sendMessage(player, "setHome.emptyName");
            }
        } else {
            messages.sendMessage(player, "setHome.noPermission");
        }
    }

    private void createHome(Player player, Homes homes, String name, double cost, String type) {
        Location location = player.getLocation();
        Home home = new Home(player.getName(), name);
        home.setWorld(Objects.requireNonNull(location.getWorld()).getName());
        home.setX(location.getX());
        home.setY(location.getY());
        home.setZ(location.getZ());
        home.setYaw(location.getYaw());
        home.setPitch(location.getPitch());
        homes.addHome(home);
        if (type.equals("new")) {
            simpleHomes.getMessages().sendMessage(player, "setHome.set", new String[][]{{"%home%", name}, {"%cost%", cost + ""}});
        } else {
            simpleHomes.getMessages().sendMessage(player, "setHome.overwritten", new String[][]{{"%home%", name}, {"%cost%", cost + ""}});
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        messages.sendMessage(console, "setHome.isConsole");
    }

}