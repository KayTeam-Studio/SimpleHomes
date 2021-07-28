package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

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
                        simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
                    } else {
                        if (player.hasPermission("simple.homes.other")) {
                            Homes homes = homesManager.getHomes(target.getName());
                            simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
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
                            simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
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
                simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
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