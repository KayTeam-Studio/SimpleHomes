/*
 *  Copyright (C) 2021 SirOswaldo
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package club.spfmc.simplehomes.util.inventory.inventories;

import club.spfmc.simplehomes.util.inventory.Item;
import club.spfmc.simplehomes.util.inventory.MenuInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class ConfirmInventory extends MenuInventory {

    private final Object object;

    public ConfirmInventory(Object object) {
        this.object = object;
        for (int i = 0; i < getRows() * 9; i++) {
            addMenuAction(i, new Item() {
                @Override
                public ItemStack getItem() {
                    return getPanel();
                }
            });
        }
        // Accept
        addMenuAction(11, new Item() {
            @Override
            public ItemStack getItem() {
                return getAccept();
            }

            @Override
            public void onLeftClick(Player player) {
                onAccept(player, object);
            }
        });

        // Information
        addMenuAction(13, new Item() {
            @Override
            public ItemStack getItem() {
                return getInformation();
            }
        });

        // Cancel
        addMenuAction(15, new Item() {
            @Override
            public ItemStack getItem() {
                return getCancel();
            }

            @Override
            public void onLeftClick(Player player) {
                onCancel(player, object);
            }
        });
    }

    public abstract String getTitle();

    public Object getObject() {
        return object;
    }

    @Override
    public int getRows() {
        return 3;
    }

    public abstract ItemStack getPanel();
    public abstract ItemStack getInformation();
    public abstract ItemStack getAccept();
    public abstract ItemStack getCancel();

    public abstract void onAccept(Player player, Object object);

    public abstract void onCancel(Player player, Object object);

}