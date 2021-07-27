package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.util.command.SimpleCommand;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.command.Command;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class SimpleHomesCommand extends SimpleCommand {

    private final SimpleHomes simpleHomes;

    public SimpleHomesCommand(SimpleHomes simpleHomes) {
        super(simpleHomes, "SimpleHomes");
        this.simpleHomes = simpleHomes;
    }

    @Override
    public void onPlayerExecute(Player player, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        if (player.hasPermission("simple.admin.homes")) {
            if (arguments.length > 0) {
                switch (arguments[0].toLowerCase()) {
                    case "reload":
                        messages.sendMessage(player, "admin.reloaded", new String[][] {
                                {"%plugin%", simpleHomes.getDescription().getName()}
                        });
                        break;
                    case "version":
                        messages.sendMessage(player, "admin.version", new String[][] {
                                {"%version%", simpleHomes.getDescription().getVersion()}
                        });
                        break;
                    case "help":
                        messages.sendMessage(player, "admin.help");
                        break;
                    default:
                        messages.sendMessage(player, "admin.invalidOption", new String[][] {
                                {"%option%", arguments[0]}
                        });
                }
            } else {
                messages.sendMessage(player, "admin.emptyOption");
            }
        } else {
            messages.sendMessage(player, "admin.noPermission");
        }
    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {
        Yaml messages = simpleHomes.getMessages();
        if (arguments.length > 0) {
            switch (arguments[0].toLowerCase()) {
                case "reload":
                    messages.sendMessage(console, "admin.reloaded", new String[][] {
                            {"%plugin%", simpleHomes.getDescription().getName()}
                    });
                    break;
                case "version":
                    messages.sendMessage(console, "admin.version", new String[][] {
                            {"%version%", simpleHomes.getDescription().getVersion()}
                    });
                    break;
                case "help":
                    messages.sendMessage(console, "admin.help");
                    break;
                default:
                    messages.sendMessage(console, "admin.invalidOption", new String[][] {
                            {"%option%", arguments[0]}
                    });
            }
        } else {
            messages.sendMessage(console, "admin.emptyOption");
        }
    }

}