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

package club.spfmc.simplehomes.util.inventory;

import java.util.HashMap;

public abstract class MenuInventory {

    private final HashMap<Integer, Item> items = new HashMap<>();

    public abstract String getTitle();

    public abstract int getRows();

    public HashMap<Integer, Item> getItems() {
        return items;
    }

    public void addMenuAction(int slot, Item item) {
        items.put(slot, item);
    }
    public void addMenuActions(int[] slots, Item item) {
        for (int slot:slots) {
            items.put(slot, item);
        }
    }

}