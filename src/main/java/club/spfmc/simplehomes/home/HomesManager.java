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

package club.spfmc.simplehomes.home;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.util.yaml.Yaml;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

public class HomesManager {

    private final SimpleHomes simpleHomes;

    private TreeMap<Integer, String> maxHomes;
    private final HashMap<String, Homes> playerHomes = new HashMap<>();

    public HomesManager(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;

    }

    public void loadMaxHomes() {
        maxHomes = new TreeMap<>();
        Yaml settings = simpleHomes.getSettings();
        for (String name:settings.getFileConfiguration().getConfigurationSection("homes").getKeys(false)) {
            maxHomes.put(settings.getInt("homes." + name), name);
        }
    }

    public TreeMap<Integer, String> getMaxHomes() {
        return maxHomes;
    }

    public void migrateEssentialsHomes(Player player) {
        Plugin essentials = simpleHomes.getServer().getPluginManager().getPlugin("Essentials");
        if (essentials != null) {
            Yaml essentialsYaml = new Yaml( essentials.getDataFolder().getPath() + File.separator + "userdata", player.getUniqueId().toString());
            if (essentialsYaml.existFileConfiguration()) {
                essentialsYaml.registerFileConfiguration();
                if (essentialsYaml.contains("homes")) {
                    Homes homes = playerHomes.get(player.getName());
                    for (String name:essentialsYaml.getFileConfiguration().getConfigurationSection("homes").getKeys(false)) {
                        Home home = new Home(player.getName(), name);
                        World world = simpleHomes.getServer().getWorld(UUID.fromString(essentialsYaml.getString("homes." + name + ".world")));
                        if (world != null) {
                            home.setWorld(world.getName());
                        } else {
                            home.setWorld("invalid");
                        }
                        home.setX(essentialsYaml.getDouble("homes." + name + ".x"));
                        home.setY(essentialsYaml.getDouble("homes." + name + ".y"));
                        home.setZ(essentialsYaml.getDouble("homes." + name + ".z"));
                        home.setYaw((float) essentialsYaml.getDouble("homes." + name + ".yaw"));
                        home.setPitch((float) essentialsYaml.getDouble("homes." + name + ".pitch"));
                        if (homes.containHome(home.getName())) {
                            Home homeDuplicated = new Home(player.getName(),home.getName() + "-2");
                            homeDuplicated.setWorld(home.getWorld());
                            homeDuplicated.setX(home.getX());
                            homeDuplicated.setY(home.getY());
                            homeDuplicated.setZ(home.getZ());
                            homeDuplicated.setYaw(home.getYaw());
                            homeDuplicated.setPitch(home.getPitch());
                            homes.addHome(homeDuplicated);
                        } else {
                            homes.addHome(home);
                        }
                    }
                    saveHomes(player.getName());
                }
            }
        }
    }

    public void loadHomes(Player player) {
        String owner = player.getName();
        Yaml yaml = new Yaml(simpleHomes, "players", owner);
        Homes homes = new Homes(owner);
        playerHomes.put(owner, homes);
        if (yaml.existFileConfiguration()) {
            yaml.registerFileConfiguration();
            for (String name:yaml.getFileConfiguration().getKeys(false)) {
                Home home = new Home(owner, name);
                home.setWorld(yaml.getString(name + ".world"));
                home.setX(yaml.getDouble(name + ".x"));
                home.setY(yaml.getDouble(name + ".y"));
                home.setZ(yaml.getDouble(name + ".z"));
                home.setYaw((float) yaml.getDouble(name + ".yaw"));
                home.setPitch((float) yaml.getDouble(name + ".pitch"));
                homes.addHome(home);
            }
        } else {
            migrateEssentialsHomes(player);
        }
    }

    public void unloadHomes(String owner) {
        Yaml yaml = new Yaml(simpleHomes, "players", owner);
        yaml.registerFileConfiguration();
        Homes homes = playerHomes.get(owner);
        for (String home:yaml.getFileConfiguration().getKeys(false)) {
            if (!homes.containHome(home)) {
                yaml.set(home, null);
            }
        }
        for (Home home:homes.getHomes()) {
            yaml.set(home.getName() + ".world", home.getWorld());
            yaml.set(home.getName() + ".x", home.getX());
            yaml.set(home.getName() + ".y", home.getY());
            yaml.set(home.getName() + ".z", home.getZ());
            yaml.set(home.getName() + ".yaw", home.getYaw());
            yaml.set(home.getName() + ".pitch", home.getPitch());
        }
        yaml.saveFileConfiguration();
        playerHomes.remove(owner);
    }

    public void saveHomes(String owner) {
        Yaml yaml = new Yaml(simpleHomes, "players", owner);
        yaml.registerFileConfiguration();
        Homes homes = playerHomes.get(owner);
        for (String home:yaml.getFileConfiguration().getKeys(false)) {
            if (!homes.containHome(home)) {
                yaml.set(home, null);
            }
        }
        for (Home home:homes.getHomes()) {
            yaml.set(home.getName() + ".world", home.getWorld());
            yaml.set(home.getName() + ".x", home.getX());
            yaml.set(home.getName() + ".y", home.getY());
            yaml.set(home.getName() + ".z", home.getZ());
            yaml.set(home.getName() + ".yaw", home.getYaw());
            yaml.set(home.getName() + ".pitch", home.getPitch());
        }
        yaml.saveFileConfiguration();
    }

    public Homes getHomes(String name) {
        return playerHomes.get(name);
    }

}