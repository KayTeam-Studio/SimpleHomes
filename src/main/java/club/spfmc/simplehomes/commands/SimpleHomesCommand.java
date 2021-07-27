package club.spfmc.simplehomes.commands;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.util.command.SimpleCommand;
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

    }

    @Override
    public void onConsoleExecute(ConsoleCommandSender console, Command command, String[] arguments) {

    }

}