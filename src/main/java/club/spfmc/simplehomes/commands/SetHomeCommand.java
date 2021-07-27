package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Home;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SetHomeCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public SetHomeCommand(SimpleHomes simpleHomes) {
        super(simpleHomes, "SetHome");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        if (player.hasPermission("simple.set.home")) {
            if (arguments.length > 0) {
                Location location = player.getLocation();
                Home home = new Home(player.getName(), arguments[0]);
                home.setWorld(location.getWorld().getName());
                home.setX(location.getX());
                home.setY(location.getY());
                home.setZ(location.getZ());
                home.setYaw(location.getYaw());
                home.setPitch(location.getPitch());
                HomesManager homesManager = simpleHomes.getHomesManager();
                Homes homes = homesManager.getHomes(player.getName());
                homes.addHome(home);
                messages.sendMessage(player, "setHome.set", new String[][]{{"%home%", arguments[0]}});
            } else {
                messages.sendMessage(player, "setHome.emptyName");
            }
        } else {
            messages.sendMessage(player, "setHome.noPermission");
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        messages.sendMessage(console, "setHome.isConsole");
    }

}