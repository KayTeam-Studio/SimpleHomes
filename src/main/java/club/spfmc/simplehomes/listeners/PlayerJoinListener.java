package club.spfmc.simplehomes.listeners;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.util.updatechecker.UpdateChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SimpleHomes simpleHomes;

    public PlayerJoinListener(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
        simpleHomes.getServer().getPluginManager().registerEvents(this, simpleHomes);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        HomesManager homesManager = simpleHomes.getHomesManager();
        homesManager.loadHomes(name);
        if (player.isOp()) {
            if (simpleHomes.getUpdateChecker().getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED)) {
                simpleHomes.getUpdateChecker().sendOutDatedMessage(player);
            }
        }
    }

}