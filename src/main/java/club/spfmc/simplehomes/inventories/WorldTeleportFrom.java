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
import club.spfmc.simplehomes.util.inventory.inventories.PagesInventory;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WorldTeleportFrom extends PagesInventory {

    private final SimpleHomes simpleHomes;

    public WorldTeleportFrom(SimpleHomes simpleHomes, List<Object> list) {
        super(simpleHomes.getSettings().getString("inventory.worldTeleportFrom.title"), simpleHomes.getSettings().getInt("inventory.worldTeleportFrom.rows", 1), list);
        this.simpleHomes = simpleHomes;
    }

    @Override
    public ItemStack getListedItem(Object object) {
        if (object != null) {
            World world = (World) object;
            String status;
            if (simpleHomes.getSettings().getStringList("world.disableTeleportFrom.worlds", new ArrayList<>()).contains(world.getName())) {
                status = simpleHomes.getSettings().getString("inventory.worldTeleportFrom.status.enable", "&aEnable");
            } else {
                status = simpleHomes.getSettings().getString("inventory.worldTeleportFrom.status.disable", "&cDisable");
            }
            return Yaml.replace(simpleHomes.getSettings().getItemStack("inventory.worldTeleportFrom.items.item"), new String[][] {
                    {"%world_name%", world.getName()},
                    {"%status%", status}
            });
        } else {
            return new ItemStack(Material.AIR);
        }
    }

    @Override
    public ItemStack getPanel() {
        return simpleHomes.getSettings().getItemStack("inventory.worldTeleportFrom.items.panel");
    }

    @Override
    public ItemStack getInformation() {
        return simpleHomes.getSettings().getItemStack("inventory.worldTeleportFrom.items.information");
    }

    @Override
    public ItemStack getPrevious() {
        return simpleHomes.getSettings().getItemStack("inventory.worldTeleportFrom.items.previous");
    }

    @Override
    public ItemStack getClose() {
        return simpleHomes.getSettings().getItemStack("inventory.worldTeleportFrom.items.close");
    }

    @Override
    public ItemStack getNext() {
        return simpleHomes.getSettings().getItemStack("inventory.worldTeleportFrom.items.next");
    }

    @Override
    public void onLeftClick(Player player, Object object) {
        World world = (World) object;
        List<String> worlds = simpleHomes.getSettings().getStringList("world.disableTeleportFrom.worlds", new ArrayList<>());
        if (!worlds.contains(world.getName())) {
            worlds.add(world.getName());
        } else {
            while (worlds.contains(world.getName())) {
                worlds.remove(world.getName());
            }
        }
        simpleHomes.getSettings().set("world.disableTeleportFrom.worlds", worlds);
        simpleHomes.getSettings().saveFileConfiguration();
        update(player);
    }

    @Override
    public void onClose(Player player) {
        simpleHomes.getMenuInventoryManager().openInventory(player, new WorldInventory(simpleHomes));
    }

}