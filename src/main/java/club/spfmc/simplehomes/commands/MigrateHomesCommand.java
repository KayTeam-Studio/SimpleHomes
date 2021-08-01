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
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MigrateHomesCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public MigrateHomesCommand(SimpleHomes simpleHomes) {
        super(simpleHomes, "MigrateHomes");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        if (player.hasPermission("simple.migrate.homes")) {
            if (arguments.length > 0) {
                switch (arguments[0].toLowerCase()) {
                    case "essentials":
                        simpleHomes.getHomesManager().migrateEssentialsHomes(player);
                        messages.sendMessage(player, "migrateHomes.migrateComplete", new String[][] {
                                {"%plugin%", arguments[0]}
                        });
                        break;
                    default:
                        messages.sendMessage(player, "migrateHomes.invalidPlugin", new String[][] {
                                {"%plugin%", arguments[0]}
                        });
                }
            } else {
                messages.sendMessage(player, "migrateHomes.emptyName");
            }
        } else {
            messages.sendMessage(player, "migrateHomes.noPermission");
        }
    }

    @Override
    public List<String> onPlayerTabComplete(Player player, Command command, String[] arguments) {
        List<String> result = new ArrayList<>();
        if (player.hasPermission("simple.migrate.homes")) {
            if (arguments.length == 1) {
                result.add("essentials");
            }
        }
        return result;
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        simpleHomes.getMessages().sendMessage(console, "migrateHomes.isConsole");
    }
}
