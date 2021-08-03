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

import java.util.List;

public abstract class PagesInventory extends MenuInventory {

    private final String title;
    private final int rows;
    private final List<Object> list;
    private int page;

    public PagesInventory(String title, int rows, List<Object> list) {
        this.title = title;
        this.rows = rows;
        page = 1;
        this.list = list;
        int slots = (getRows()) * 9;
        // Panels
        int[] panelSlot = new int[] {0, 1, 2, 3, 5, 6, 7, 8, slots - 8, slots - 7, slots - 6, slots - 4, slots - 3, slots - 2};
        addMenuActions(panelSlot, new Item() {
            @Override
            public ItemStack getItem() {
                return getPanel();
            }
        });
        // Items
        // Set List
        for (int index = 9; index < slots - 9; index++) {
            int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
            int listIndex = realIndex - 1;
            if (list.size() > listIndex) {
                addMenuAction(index, new Item() {
                    @Override
                    public ItemStack getItem() {
                        return getListedItem(list.get(listIndex));
                    }

                    @Override
                    public void onLeftClick(Player player) {
                        PagesInventory.this.onLeftClick(player, list.get(listIndex));
                    }

                    @Override
                    public void onShiftLeftClick(Player player) {
                        PagesInventory.this.onShiftLeftClick(player, list.get(listIndex));
                    }

                    @Override
                    public void onMiddleClick(Player player) {
                        PagesInventory.this.onMiddleClick(player, list.get(listIndex));
                    }

                    @Override
                    public void onRightClick(Player player) {
                        PagesInventory.this.onRightClick(player, list.get(listIndex));
                    }

                    @Override
                    public void onShiftRightClick(Player player) {
                        PagesInventory.this.onShiftRightClick(player, list.get(listIndex));
                    }

                });
            } else {
                addMenuAction(index, new Item() {
                    @Override
                    public ItemStack getItem() {
                        return getListedItem(null);
                    }
                });
            }
        }
        // Information
        addMenuAction(4, new Item() {
            @Override
            public ItemStack getItem() {
                return getInformation();
            }
        });
        // Previous
        addMenuAction(slots - 9, new Item() {
            @Override
            public ItemStack getItem() {
                return getPrevious();
            }

            @Override
            public void onLeftClick(Player player) {
                if (page > 1) {
                    page = page - 1;
                    for (int index = 9; index < slots - 9; index++) {
                        int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
                        int listIndex = realIndex - 1;
                        if (list.size() > listIndex) {
                            addMenuAction(index, new Item() {
                                @Override
                                public ItemStack getItem() {
                                    return getListedItem(list.get(listIndex));
                                }

                                @Override
                                public void onLeftClick(Player player) {
                                    PagesInventory.this.onLeftClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onShiftLeftClick(Player player) {
                                    PagesInventory.this.onShiftLeftClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onMiddleClick(Player player) {
                                    PagesInventory.this.onMiddleClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onRightClick(Player player) {
                                    PagesInventory.this.onRightClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onShiftRightClick(Player player) {
                                    PagesInventory.this.onShiftRightClick(player, list.get(listIndex));
                                }
                            });
                            player.getOpenInventory().setItem(index, getListedItem(list.get(listIndex)));
                        } else {
                            addMenuAction(index, new Item() {
                                @Override
                                public ItemStack getItem() {
                                    return getListedItem(null);
                                }
                            });
                            player.getOpenInventory().setItem(index, null);
                        }
                    }
                }
            }
        });
        // Close
        addMenuAction(slots - 5, new Item() {
            @Override
            public ItemStack getItem() {
                return getClose();
            }

            @Override
            public void onLeftClick(Player player) {
                player.closeInventory();
            }
        });
        // Next
        addMenuAction(slots - 1, new Item() {
            @Override
            public ItemStack getItem() {
                return getNext();
            }

            @Override
            public void onLeftClick(Player player) {
                if (list.size() > (page * (rows * 9))) {
                    page = page + 1;
                    for (int index = 9; index < slots - 9; index++) {
                        int realIndex = ((page * (rows * 9)) - (rows * 9)) + (index - 8);
                        int listIndex = realIndex - 1;
                        if (list.size() > listIndex) {
                            addMenuAction(index, new Item() {
                                @Override
                                public ItemStack getItem() {
                                    return getListedItem(list.get(listIndex));
                                }

                                @Override
                                public void onLeftClick(Player player) {
                                    PagesInventory.this.onLeftClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onShiftLeftClick(Player player) {
                                    PagesInventory.this.onShiftLeftClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onMiddleClick(Player player) {
                                    PagesInventory.this.onMiddleClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onRightClick(Player player) {
                                    PagesInventory.this.onRightClick(player, list.get(listIndex));
                                }

                                @Override
                                public void onShiftRightClick(Player player) {
                                    PagesInventory.this.onShiftRightClick(player, list.get(listIndex));
                                }
                            });
                            player.getOpenInventory().setItem(index, getListedItem(list.get(listIndex)));
                        } else {
                            addMenuAction(index, new Item() {
                                @Override
                                public ItemStack getItem() {
                                    return getListedItem(null);
                                }
                            });
                            player.getOpenInventory().setItem(index, null);
                        }
                    }
                }
            }
        });
    }

    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public int getRows() {
        return rows + 2;
    }
    public List<Object> getList() {
        return list;
    }


    public abstract ItemStack getListedItem(Object object);
    public abstract ItemStack getPanel();
    public abstract ItemStack getInformation();
    public abstract ItemStack getPrevious();
    public abstract ItemStack getClose();
    public abstract ItemStack getNext();

    public void onLeftClick(Player player, Object object) {}
    public void onRightClick(Player player, Object object) {}
    public void onMiddleClick(Player player, Object object) {}
    public void onShiftLeftClick(Player player, Object object) {}
    public void onShiftRightClick(Player player, Object object) {}

}