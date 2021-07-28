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

package club.spfmc.simplehomes.tasks;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Home;
import club.spfmc.simplehomes.util.task.Task;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TeleportTask extends Task {

    private final static List<String> teleporting = new ArrayList<>();
    public static List<String> getTeleporting() {
        return teleporting;
    }

    private final SimpleHomes simpleHomes;
    private int countdown;
    private final Player player;
    private final Home home;

    public TeleportTask(SimpleHomes simpleHomes, Player player, Home home) {
        super(simpleHomes, 20L);
        this.simpleHomes = simpleHomes;
        countdown = simpleHomes.getSettings().getInt("teleport.countdown");
        this.player = player;
        this.home = home;
        getTeleporting().add(player.getName());
    }

    public Player getPlayer() {
        return player;
    }
    public Home getHome() {
        return home;
    }

    @Override
    public void actions() {
        if (player.isOnline() && TeleportTask.getTeleporting().contains(player.getName())) {
            if (player.hasPermission("simple.bypass.home.countdown")) {
                teleport();
            } else {
                Yaml settings = simpleHomes.getSettings();
                if (countdown == 0) {
                    teleport();
                    if (settings.contains("teleport.messages." + countdown)) {
                        settings.sendMessage(player, "teleport.messages." + countdown, new String[][] {{"%seconds%", countdown + ""}});
                    }
                    if (settings.contains("teleport.sounds." + countdown)) {
                        String sound = settings.getString("teleport.sounds." + countdown);
                        try {
                            player.playSound(player.getLocation(), Sound.valueOf(sound), 1, 1);
                        } catch (IllegalArgumentException e) {
                            simpleHomes.getLogger().info("The sound: " + sound + " no found in your server version.");
                        }
                    }
                } else {
                    if (settings.contains("teleport.messages." + countdown)) {
                        settings.sendMessage(player, "teleport.messages." + countdown, new String[][] {{"%seconds%", countdown + ""}});
                    }
                    if (settings.contains("teleport.sounds." + countdown)) {
                        String sound = settings.getString("teleport.sounds." + countdown);
                        try {
                            player.playSound(player.getLocation(), Sound.valueOf(sound), 1, 1);
                        } catch (IllegalArgumentException e) {
                            simpleHomes.getLogger().info("The sound: " + sound + " no found in your server version.");
                        }
                    }
                    countdown--;
                }
            }
        } else {
            TeleportTask.teleporting.remove(player.getName());
            stopScheduler();
        }
    }

    private void teleport() {
        Yaml messages = simpleHomes.getMessages();
        World world = simpleHomes.getServer().getWorld(home.getWorld());
        if (world != null) {
            double x = home.getX();
            double y = home.getY();
            double z = home.getZ();
            float yaw = home.getYaw();
            float pitch = home.getPitch();
            Location location = new Location(world, x, y, z, yaw, pitch);
            player.teleport(location);
            messages.sendMessage(player, "home.teleported", new String[][] {{"%home%", home.getName()}});
        } else {
            messages.sendMessage(player, "home.invalidWorld", new String[][] {{"%world%", home.getWorld()}});
        }
        TeleportTask.teleporting.remove(player.getName());
        stopScheduler();
    }

}