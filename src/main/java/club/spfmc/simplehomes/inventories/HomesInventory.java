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
import club.spfmc.simplehomes.util.inventory.SimplePagesInventory;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HomesInventory extends SimplePagesInventory {

    private final SimpleHomes simpleHomes;

    public HomesInventory(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
    }

    @Override
    public String getTitle() {
        return simpleHomes.getSettings().getString("inventory.homes.title");
    }

    @Override
    public ItemStack getPanel() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.panel");
    }

    @Override
    public ItemStack getFormattedItem(Object object) {
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
    public ItemStack getNextPageButton() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.nextPage");
    }

    @Override
    public ItemStack getPrevPageButton() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.prevPage");
    }


    @Override
    public ItemStack getCloseButton() {
        return simpleHomes.getSettings().getItemStack("inventory.homes.items.close");
    }


    @Override
    public void onLeftClick(Player player, Object object) {
        if (object != null) {
            player.closeInventory();
            if (!TeleportTask.getTeleporting().contains(player.getName())) {
                Home home = (Home) object;
                TeleportTask teleportTask = new TeleportTask(simpleHomes, player, home);
                teleportTask.startScheduler();
            } else {
                Yaml messages = simpleHomes.getMessages();
                messages.sendMessage(player, "home.alreadyInTeleporting");
            }
        }
    }

    @Override
    public void onRightClick(Player player, Object object) {
        if (object != null) {
            player.closeInventory();
            simpleHomes.getDeleteHomeConfirmInventory().openInventory(player, object);
        }
    }

}