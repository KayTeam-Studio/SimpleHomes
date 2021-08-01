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

package club.spfmc.simplehomes.placeholderapi;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Home;
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class SimpleHomesExpansion extends PlaceholderExpansion {

    private final SimpleHomes simpleHomes;

    public SimpleHomesExpansion(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "SimpleHomes";
    }

    @Override
    public @NotNull String getAuthor() {
        return "SirOswaldo";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    // List of Placeholders
    //
    // SimpleHomes_max_homes_amount
    // SimpleHomes_current_homes_amount
    // SimpleHomes_home_%home%_world
    // SimpleHomes_home_%home%_x
    // SimpleHomes_home_%home%_y
    // SimpleHomes_home_%home%_z
    // SimpleHomes_home_%home%_yaw
    // SimpleHomes_home_%home%_pitch
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        HomesManager homesManager = simpleHomes.getHomesManager();
        Homes homes = homesManager.getHomes(player.getName());
        if (params.equals("max_homes_amount")) {
            int maxHomes = 0;
            for (Integer key:homesManager.getMaxHomes().keySet()) {
                String name = homesManager.getMaxHomes().get(key);
                if (name.equals("default") || player.getPlayer().hasPermission("simple.max.homes." + name) ) {
                    maxHomes = key;
                }
            }
            return maxHomes + "";
        } else if (params.equals("current_homes_amount")){
            return homes.getHomes().size() + "";
        } else if (params.startsWith("home_") && params.endsWith("_world") && params.split("_").length == 3) {
            Home home = homes.getHome(params.split("_")[1]);
            if (home!=null) {
                return home.getWorld();
            }
        } else if (params.startsWith("home_") && params.endsWith("_x") && params.split("_").length == 3) {
            Home home = homes.getHome(params.split("_")[1]);
            if (home!=null) {
                return Math.round(home.getX()) + "";
            }
        } else if (params.startsWith("home_") && params.endsWith("_y") && params.split("_").length == 3) {
            Home home = homes.getHome(params.split("_")[1]);
            if (home!=null) {
                return Math.round(home.getY()) + "";
            }
        } else if (params.startsWith("home_") && params.endsWith("_z") && params.split("_").length == 3) {
            Home home = homes.getHome(params.split("_")[1]);
            if (home!=null) {
                return Math.round(home.getZ()) + "";
            }
        } else if (params.startsWith("home_") && params.endsWith("_yaw") && params.split("_").length == 3) {
            Home home = homes.getHome(params.split("_")[1]);
            if (home!=null) {
                return Math.round(home.getYaw()) + "";
            }
        } else if (params.startsWith("home_") && params.endsWith("_pitch") && params.split("_").length == 3) {
            Home home = homes.getHome(params.split("_")[1]);
            if (home!=null) {
                return Math.round(home.getPitch()) + "";
            }
        }
        return "";
    }

}