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