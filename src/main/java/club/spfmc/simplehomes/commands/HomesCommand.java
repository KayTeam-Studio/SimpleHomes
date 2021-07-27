package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
            Homes homes = homesManager.getHomes(player.getName());
            List<Object> objectList = new ArrayList<>(homes.getHomes());
            simpleHomes.getHomesInventory().openInventory(player, objectList);
        } else {
            messages.sendMessage(player, "homes.noPermission");
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        messages.sendMessage(console, "homes.isConsole");
    }

}