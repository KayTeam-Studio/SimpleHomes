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

package org.kayteam.simplehomes.home;

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