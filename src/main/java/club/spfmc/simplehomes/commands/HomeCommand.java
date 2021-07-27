package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Home;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.tasks.TeleportTask;
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public HomeCommand(SimpleHomes simpleHomes) {
        super(simpleHomes, "Home");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        if (player.hasPermission("simple.home")) {
            if (arguments.length > 0) {
                HomesManager homesManager = simpleHomes.getHomesManager();
                Homes homes = homesManager.getHomes(player.getName());
                if (homes.containHome(arguments[0])) {
                    if (!TeleportTask.getTeleporting().contains(player.getName())) {
                        Home home = homes.getHome(arguments[0]);
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
    public List<String> onPlayerTabComplete(Player player, Command command, String[] arguments) {
        return super.onPlayerTabComplete(player, command, arguments);
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        messages.sendMessage(console, "home.isConsole");
    }

}