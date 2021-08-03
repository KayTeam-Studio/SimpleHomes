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
import club.spfmc.simplehomes.home.Home;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.inventories.DeleteHomeConfirmInventory;
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DeleteHomeCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public DeleteHomeCommand(SimpleHomes simpleHomes) {
        super(simpleHomes, "DeleteHome");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        if (player.hasPermission("simple.delete.home")) {
            if (arguments.length > 0) {
                HomesManager homesManager = simpleHomes.getHomesManager();
                Homes homes = homesManager.getHomes(player.getName());
                if (homes.containHome(arguments[0])) {
                    Home home = homes.getHome(arguments[0]);
                    simpleHomes.getMenuInventoryManager().openInventory(player, new DeleteHomeConfirmInventory(simpleHomes, home, "cmd"));
                } else {
                    messages.sendMessage(player, "deleteHome.invalidHome", new String[][] {{"%home%", arguments[0]}});
                }
            } else {
                messages.sendMessage(player,  "deleteHome.emptyName");
            }
        } else {
            messages.sendMessage(player, "deleteHome.noPermission");
        }
    }

    @Override
    public List<String> onPlayerTabComplete(Player player, Command command, String[] arguments) {
        List<String> names = new ArrayList<>();
        if (arguments.length == 1) {
            if (player.hasPermission("simple.delete.home")) {
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
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        messages.sendMessage(console, "deleteHome.isConsole");
    }

}