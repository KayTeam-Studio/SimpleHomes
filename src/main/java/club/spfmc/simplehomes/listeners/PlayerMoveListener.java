package club.spfmc.simplehomes.listeners;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.tasks.TeleportTask;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final SimpleHomes simpleHomes;

    public PlayerMoveListener(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        simpleHomes.getServer().getPluginManager().registerEvents(this, simpleHomes);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (TeleportTask.getTeleporting().contains(name)) {
            Location from  = event.getFrom();
            Location to = event.getTo();
            if (!player.hasPermission("simple.bypass.home.movement")) {
                if (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
                    TeleportTask.getTeleporting().remove(name);
                    Yaml messages = simpleHomes.getMessages();
                    messages.sendMessage(player, "home.cancelByMovement");
                }
            }
        }
    }

}