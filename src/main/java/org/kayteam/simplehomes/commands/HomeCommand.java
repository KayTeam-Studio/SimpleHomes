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

import org.kayteam.api.command.SimpleCommand;
import org.kayteam.api.yaml.Yaml;
import org.kayteam.simplehomes.SimpleHomes;
import org.kayteam.simplehomes.home.Home;
import org.kayteam.simplehomes.home.Homes;
import org.kayteam.simplehomes.home.HomesManager;
import org.kayteam.simplehomes.tasks.TeleportTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public HomeCommand(SimpleHomes simpleHomes) {
        super("Home");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        Yaml settings = simpleHomes.getSettings();
        if (player.hasPermission("simple.home")) {
            if (arguments.length > 0) {
                HomesManager homesManager = simpleHomes.getHomesManager();
                Homes homes = homesManager.getHomes(player.getName());
                if (homes.containHome(arguments[0])) {
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
                        Home home = homes.getHome(arguments[0]);
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
                                    messages.sendMessage(player, "home.insufficientMoney", new String[][] {{"%amount%", Math.round(amount) + ""}});
                                    return;
                                }
                            }
                        }
                        TeleportTask teleportTask = new TeleportTask(simpleHomes, player, home);
                        teleportTask.startScheduler();
                    } else {
                        messages.sendMessage(player, "home.alreadyInTeleporting");
                    }
                } else {
                    messages.sendMessage(player, "home.invalidHome", new String[][] {{"%home%", arguments[0]}});
                }
            } else {
                messages.sendMessage(player, "home.emptyName");
            }
        } else {
            messages.sendMessage(player, "home.noPermission");
        }
    }

    @Override
    public List<String> onPlayerTabComplete(Player player, String[] arguments) {
        List<String> names = new ArrayList<>();
        if (arguments.length == 1) {
            if (player.hasPermission("simple.home")) {
                HomesManager homesManager = simpleHomes.getHomesManager();
                Homes homes = homesManager.getHomes(player.getName());
                for (Home home:homes.getHomes()) {
                    names.add(home.getName());
                }
            }
        }
        return names;
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, String[] arguments) {
        simpleHomes.getMessages().sendMessage(console, "home.isConsole");
    }

}