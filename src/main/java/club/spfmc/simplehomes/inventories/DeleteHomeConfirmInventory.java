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
import club.spfmc.simplehomes.home.Homes;
import club.spfmc.simplehomes.home.HomesManager;
import club.spfmc.simplehomes.util.inventory.SimpleConfirmInventory;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DeleteHomeConfirmInventory extends SimpleConfirmInventory {

    private final SimpleHomes simpleHomes;

    public DeleteHomeConfirmInventory(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
    }

    @Override
    public String getTitle() {
        return simpleHomes.getSettings().getString("inventory.deleteConfirm.title");
    }

    @Override
    public ItemStack getPanel() {
        return simpleHomes.getSettings().getItemStack("inventory.deleteConfirm.items.panel");
    }

    @Override
    public ItemStack getInformation(Object object) {
        if (object != null) {
            Home home = (Home) object;
            return Yaml.replace(simpleHomes.getSettings().getItemStack("inventory.deleteConfirm.items.information"), new String[][] {
                    {"%name%", home.getName()},
                    {"%world%", home.getWorld()},
                    {"%x%", Math.round(home.getX()) + ""},
                    {"%y%", Math.round(home.getY()) + ""},
                    {"%z%", Math.round(home.getZ()) + ""},
                    {"%yaw%", Math.round(home.getYaw()) + ""},
                    {"%pitch%", Math.round(home.getPitch()) + ""}
            });
        } else {
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public ItemStack getCancelButton() {
        return simpleHomes.getSettings().getItemStack("inventory.deleteConfirm.items.cancel");
    }

    @Override
    public ItemStack getAcceptButton() {
        return simpleHomes.getSettings().getItemStack("inventory.deleteConfirm.items.accept");
    }

    @Override
    public void onAcceptClick(Player player, Object object) {
        if (object != null) {
            Home home = (Home) object;
            String name = home.getName();
            String owner = home.getOwner();
            HomesManager homesManager = simpleHomes.getHomesManager();
            Homes homes = homesManager.getHomes(home.getOwner());
            homes.removeHome(home.getName());
            Yaml messages = simpleHomes.getMessages();
            if (player.getName().equals(home.getOwner())) {
                messages.sendMessage(player,  "deleteHome.delete", new String[][]{{"%home%", name}});
            } else {
                messages.sendMessage(player, "deleteHome.deleteOther", new String[][] {
                        {"%home%", name},
                        {"%player%", owner}
                });
            }
            homesManager.saveHomes(owner);
            player.closeInventory();
            simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
        }
    }

    @Override
    public void onCancelClick(Player player, Object object) {
        if (object != null) {
            Home home = (Home) object;
            HomesManager homesManager = simpleHomes.getHomesManager();
            Homes homes = homesManager.getHomes(home.getOwner());
            player.closeInventory();
            simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
        }
    }

}