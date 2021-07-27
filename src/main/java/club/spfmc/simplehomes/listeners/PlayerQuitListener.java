package club.spfmc.simplehomes.listeners;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.HomesManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final SimpleHomes simpleHomes;

    public PlayerQuitListener(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        simpleHomes.getServer().getPluginManager().registerEvents(this, simpleHomes);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        HomesManager homesManager = simpleHomes.getHomesManager();
        homesManager.unloadHomes(name);
    }

}