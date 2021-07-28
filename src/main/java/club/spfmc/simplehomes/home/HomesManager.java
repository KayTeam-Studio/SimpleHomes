package club.spfmc.simplehomes.home;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.util.yaml.Yaml;

import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

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

    public void loadHomes(String owner) {
        Yaml yaml = new Yaml(simpleHomes, "players", owner);
        yaml.registerFileConfiguration();
        Homes homes = new Homes(owner);
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
        playerHomes.put(owner, homes);
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

    public Homes getHomes(String name) {
        return playerHomes.get(name);
    }

}