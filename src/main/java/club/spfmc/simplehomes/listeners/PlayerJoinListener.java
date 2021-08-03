/*
 * Copyright (C) 2021  SirOswaldo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        HomesManager homesManager = simpleHomes.getHomesManager();
        homesManager.loadHomes(player);
        if (player.hasPermission("simple.notify.update")) {
            if (simpleHomes.getUpdateChecker().getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED)) {
                simpleHomes.getUpdateChecker().sendOutDatedMessage(player);
            }
        }
    }

}