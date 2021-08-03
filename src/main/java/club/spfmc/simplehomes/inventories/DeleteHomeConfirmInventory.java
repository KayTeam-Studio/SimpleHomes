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
import club.spfmc.simplehomes.util.inventory.inventories.ConfirmInventory;
import club.spfmc.simplehomes.util.yaml.Yaml;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeleteHomeConfirmInventory extends ConfirmInventory {

    private final SimpleHomes simpleHomes;
    private final String from;

    public DeleteHomeConfirmInventory(SimpleHomes simpleHomes, Home home, String from) {
        super(home);
        this.simpleHomes = simpleHomes;
        this.from = from;
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
    public ItemStack getInformation() {
        Home home = (Home) getObject();
        return Yaml.replace(simpleHomes.getSettings().getItemStack("inventory.deleteConfirm.items.information"), new String[][] {
                {"%name%", home.getName()},
                {"%world%", home.getWorld()},
                {"%x%", Math.round(home.getX()) + ""},
                {"%y%", Math.round(home.getY()) + ""},
                {"%z%", Math.round(home.getZ()) + ""},
                {"%yaw%", Math.round(home.getYaw()) + ""},
                {"%pitch%", Math.round(home.getPitch()) + ""}
        });
    }

    @Override
    public ItemStack getAccept() {
        return simpleHomes.getSettings().getItemStack("inventory.deleteConfirm.items.accept");
    }

    @Override
    public ItemStack getCancel() {
        return simpleHomes.getSettings().getItemStack("inventory.deleteConfirm.items.cancel");
    }

    @Override
    public void onAccept(Player player, Object object) {
        Yaml messages = simpleHomes.getMessages();
        Home home = (Home) object;
        String name = home.getName();
        String owner = home.getOwner();
        if (SimpleHomes.getEconomy() != null) {
            Economy economy = SimpleHomes.getEconomy();
            if (player.getName().equals(owner)) {
                economy.depositPlayer(player, simpleHomes.getSettings().getDouble("vault.deleteHome", 0.0));
            }
        }
        HomesManager homesManager = simpleHomes.getHomesManager();
        Homes homes = homesManager.getHomes(home.getOwner());
        homes.removeHome(home.getName());
        if (player.getName().equals(home.getOwner())) {
            if (SimpleHomes.getEconomy() != null) {

                messages.sendMessage(player,  "deleteHome.delete", new String[][]{{"%home%", name}, {"%cost%", simpleHomes.getSettings().getDouble("vault.deleteHome", 0.0) + ""}});
            } else {
                messages.sendMessage(player,  "deleteHome.delete", new String[][]{{"%home%", name}});
            }
        } else {
            messages.sendMessage(player, "deleteHome.deleteOther", new String[][] {
                    {"%home%", name},
                    {"%player%", owner}
            });
        }
        homesManager.saveHomes(owner);
        player.closeInventory();
        if (homes.getHomes().size() > 0) {
            if (from.equals("gui")) {
                simpleHomes.getMenuInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes.getHomes()));
            }
        } else {
            if (player.getName().equals(owner)) {
                messages.sendMessage(player, "homes.emptyHomes");
            } else {
                messages.sendMessage(player, "homes.emptyHomesOther", new String[][] {
                        {"%player%", owner}
                });
            }
        }

    }

    @Override
    public void onCancel(Player player, Object object) {
        player.closeInventory();
        if (from.equals("gui")) {
            Home home = (Home) object;
            HomesManager homesManager = simpleHomes.getHomesManager();
            Homes homes = homesManager.getHomes(home.getOwner());
            player.closeInventory();
            simpleHomes.getMenuInventoryManager().openInventory(player, new HomesInventory(simpleHomes, homes.getHomes()));
        }
    }
}
