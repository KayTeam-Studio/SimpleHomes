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

package club.spfmc.simplehomes.inventories;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.home.Home;
import club.spfmc.simplehomes.tasks.TeleportTask;
import club.spfmc.simplehomes.util.inventory.inventories.PagesInventory;
import club.spfmc.simplehomes.util.yaml.Yaml;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HomesInventory extends PagesInventory {

    private final SimpleHomes simpleHomes;
    private final List<Home> homes;

    public HomesInventory(SimpleHomes simpleHomes, List<Home> homes) {
        super(simpleHomes.getSettings().getString("inventory.homes.title"), simpleHomes.getSettings().getInt("inventory.homes.rows", 1), new ArrayList<>(homes));
        this.simpleHomes = simpleHomes;
        this.homes = homes;
    }

    @Override
    public ItemStack getListedItem(Object object) {
        if (object != null) {
            Home home = (Home) object;
            ItemStack item = simpleHomes.getSettings().getItemStack("inventory.homes.items.home");
            item = Yaml.replace(item, new String[][] {
                    {"%name%", home.getName()},
                    {"%world%", home.getWorld()},
                    {"%x%", Math.round(home.getX()) + ""},
                    {"%y%", Math.round(home.getY()) + ""},
                    {"%z%", Math.round(home.getZ()) + ""},
                    {"%yaw%", Math.round(home.getYaw()) + ""},
                    {"%pitch%", Math.round(home.getPitch()) + ""}
            });
            return item;
        }
        return new ItemStack(Material.AIR);
    }

    @Override
    public ItemStack getPanel() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.panel");
    }

    @Override
    public ItemStack getInformation() {
        Home home = homes.get(0);
        return Yaml.replace(simpleHomes.getSettings().getItemStack("inventory.homes.items.information"), new String[][] {
                {"%owner%", home.getOwner()},
                {"%amount%", homes.size() + ""}
        });
    }

    @Override
    public ItemStack getPrevious() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.prevPage");
    }

    @Override
    public ItemStack getClose() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.close");
    }

    @Override
    public ItemStack getNext() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.nextPage");
    }

    @Override
    public void onLeftClick(Player player, Object object) {
        player.closeInventory();
        Yaml messages = simpleHomes.getMessages();
        Yaml settings = simpleHomes.getSettings();
        if (settings.getBoolean("world.disableTeleportFrom.enable") && !player.hasPermission("simple.bypass.disabled.worlds")) {
            List<String> worlds = settings.getStringList("world.disableTeleportFrom.worlds", new ArrayList<>());
            Location location = player.getLocation();
            if (worlds.contains(location.getWorld().getName())) {
                messages.sendMessage(player, "home.disabledWorldFrom", new String[][] {
                        {"%world_name%", location.getWorld().getName()},
                        {"%world_x%", Math.round(location.getX()) + ""},
                        {"%world_y%", Math.round(location.getY()) + ""},
                        {"%world_z%", Math.round(location.getZ()) + ""},
                        {"%world_yaw%", Math.round(location.getYaw()) + ""},
                        {"%world_pitch%", Math.round(location.getPitch()) + ""}
                });
                return;
            }
        }
        if (!TeleportTask.getTeleporting().contains(player.getName())) {
            Home home = (Home) object;
            if (settings.getBoolean("world.disableTeleportTo.enable") && !player.hasPermission("simple.bypass.disabled.worlds")) {
                List<String> worlds = settings.getStringList("world.disableTeleportTo.worlds", new ArrayList<>());
                if (worlds.contains(home.getWorld())) {
                    messages.sendMessage(player, "home.disabledWorldTo", new String[][] {
                            {"%home_name%", home.getName()},
                            {"%world_name%", home.getWorld()},
                            {"%world_x%", Math.round(home.getX()) + ""},
                            {"%world_y%", Math.round(home.getY()) + ""},
                            {"%world_z%", Math.round(home.getZ()) + ""},
                            {"%world_yaw%", Math.round(home.getYaw()) + ""},
                            {"%world_pitch%", Math.round(home.getPitch()) + ""}
                    });
                    return;
                }
            }
            if (settings.getBoolean("vault.enable")) {
                Economy economy = SimpleHomes.getEconomy();
                if (economy != null) {
                    double amount = settings.getDouble("vault.teleport");
                    if (economy.has(player, amount) || player.hasPermission("simple.bypass.home.cost")) {
                        if (!player.hasPermission("simple.bypass.home.cost")) {
                            economy.withdrawPlayer(player, amount);
                        }
                    } else {
                        messages.sendMessage(player, "home.insufficientMoney", new String[][] {
                                {"%amount%", Math.round(amount) + ""}
                        });
                        return;
                    }
                }
            }
            TeleportTask teleportTask = new TeleportTask(simpleHomes, player, home);
            teleportTask.startScheduler();
        } else {
            messages.sendMessage(player, "home.alreadyInTeleporting");
        }
    }

    @Override
    public void onRightClick(Player player, Object object) {
        player.closeInventory();
        simpleHomes.getMenuInventoryManager().openInventory(player, new DeleteHomeConfirmInventory(simpleHomes, (Home) object, "gui"));
    }

}