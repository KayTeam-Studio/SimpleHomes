package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Home;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
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
                    simpleHomes.getDeleteHomeConfirmInventory().openInventory(player, homes.getHome(arguments[0]));
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