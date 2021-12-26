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

package org.kayteam.simplehomes.tasks;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XSound;
import org.kayteam.kayteamapi.scheduler.Task;
import org.kayteam.kayteamapi.yaml.Yaml;
import org.kayteam.simplehomes.SimpleHomes;
import org.kayteam.simplehomes.home.Home;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
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
                countdown = 0;
                teleport();
                sendActions();
            } else {
                if (countdown == 0) {
                    teleport();
                    sendActions();
                } else {
                    sendActions();
                    countdown--;
                }
            }
        } else {
            TeleportTask.teleporting.remove(player.getName());
            stopScheduler();
        }
    }

    private void sendActions() {
        Yaml settings = simpleHomes.getSettings();
        if (settings.contains("teleport.messages." + countdown)) {
            if (settings.isString("teleport.messages." + countdown) || settings.isList("teleport.messages." + countdown)) {
                settings.sendMessage(player, "teleport.messages." + countdown, new String[][] {
                        {"%seconds%", countdown + ""},
                        {"%home_name%", home.getName()},
                        {"%home_world%", home.getWorld()},
                        {"%home_x%", Math.round(home.getX()) + ""},
                        {"%home_y%", Math.round(home.getY()) + ""},
                        {"%home_z%", Math.round(home.getZ()) + ""},
                        {"%home_yaw%", Math.round(home.getYaw()) + ""},
                        {"%home_pitch%", Math.round(home.getPitch()) + ""}
                });
            }
        }
        if (settings.contains("teleport.sounds." + countdown)) {
            if (settings.isString("teleport.sounds." + countdown)) {
                String soundName = settings.getString("teleport.sounds." + countdown);
                XSound xSound = XSound.matchXSound(soundName).orElse(null);
                assert xSound != null;
                Sound sound = xSound.parseSound();
                try {
                    assert sound != null;
                    player.playSound(player.getLocation(), sound, 1, 1);
                } catch (IllegalArgumentException e) {
                    simpleHomes.getLogger().info("The soundName: " + soundName + " no found in your server version.");
                }
            }
        }
        if (settings.contains("teleport.titles." + countdown)) {
            String title = "";
            String subTitle = "";
            if (settings.contains("teleport.titles." + countdown+ ".title")) {
                if (settings.isString("teleport.titles." + countdown+ ".title")) {
                    title = settings.getString("teleport.titles." + countdown + ".title");
                    title = title.replaceAll("%seconds%", countdown + "");
                    title = title.replaceAll("%home_name%", home.getName());
                    title = title.replaceAll("%home_world%", home.getWorld());
                    title = title.replaceAll("%home_x%", Math.round(home.getX()) + "");
                    title = title.replaceAll("%home_y%", Math.round(home.getY()) + "");
                    title = title.replaceAll("%home_z%", Math.round(home.getZ()) + "");
                    title = title.replaceAll("%home_yaw%", Math.round(home.getYaw()) + "");
                    title = title.replaceAll("%home_pitch%", Math.round(home.getPitch()) + "");
                }
            }
            if (settings.contains("teleport.titles." + countdown+ ".subTitle")) {
                if (settings.isString("teleport.titles." + countdown+ ".subTitle")) {
                    subTitle = settings.getString("teleport.titles." + countdown+ ".subTitle");
                    subTitle = subTitle.replaceAll("%home_name%", home.getName());
                    subTitle = subTitle.replaceAll("%home_world%", home.getWorld());
                    subTitle = subTitle.replaceAll("%home_x%", Math.round(home.getX()) + "");
                    subTitle = subTitle.replaceAll("%home_y%", Math.round(home.getY()) + "");
                    subTitle = subTitle.replaceAll("%home_z%", Math.round(home.getZ()) + "");
                    subTitle = subTitle.replaceAll("%home_yaw%", Math.round(home.getYaw()) + "");
                    subTitle = subTitle.replaceAll("%home_pitch%", Math.round(home.getPitch()) + "");
                    subTitle = subTitle.replaceAll("%seconds%", countdown + "");
                }
            }
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                title = PlaceholderAPI.setPlaceholders(player, title);
                subTitle = PlaceholderAPI.setPlaceholders(player, subTitle);
            }
            title = ChatColor.translateAlternateColorCodes('&', title);
            subTitle = ChatColor.translateAlternateColorCodes('&', subTitle);
            player.sendTitle(title, subTitle);
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
        } else {
            messages.sendMessage(player, "home.invalidWorld", new String[][] {{"%world%", home.getWorld()}});
        }
        TeleportTask.teleporting.remove(player.getName());
        stopScheduler();
    }

}