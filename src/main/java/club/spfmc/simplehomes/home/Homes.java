package club.spfmc.simplehomes.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Homes {

    private final String owner;

    private final HashMap<String, Home> homes = new HashMap<>();

    public Homes(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public Home getHome(String name) {
        return homes.get(name);
    }

    public void addHome(Home home) {
        homes.put(home.getName(), home);
    }
    public void setHome(Home home) {
        homes.put(home.getName(), home);
    }

    public void removeHome(String name) {
        homes.remove(name);
    }

    public boolean containHome(String name) {
        return homes.containsKey(name);
    }

    public List<Home> getHomes() {
        List<Home> homeList = new ArrayList<>();
        for (String name:homes.keySet()) {
            homeList.add(homes.get(name));
        }
        return homeList;
    }

}