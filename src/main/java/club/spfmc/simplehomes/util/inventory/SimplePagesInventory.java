package club.spfmc.simplehomes.util.inventory;

import javafx.beans.binding.ObjectExpression;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public abstract class SimplePagesInventory implements Listener {

    private static final HashMap<String, List<Object>> objects = new HashMap<>();
    private static final HashMap<String, Integer> page = new HashMap<>();

    public void openInventory(Player player, List<Object> objects) {
        SimplePagesInventory.objects.put(player.getName(), objects);
        int page = 1;
        SimplePagesInventory.page.put(player.getName(), page);
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', getTitle()));
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, getPanel());
        }
        inventory.setItem(9, getPrevPageButton());
        inventory.setItem(17, getNextPageButton());
        inventory.setItem(22, getCloseButton());
        for (int i = 10; i < 17; i++) {
            int realIndex = (page * 7 - 7) + (i - 9);
            int listIndex = realIndex - 1;
            if (objects.size() > listIndex) {
                inventory.setItem(i, getFormattedItem(objects.get(listIndex)));
            } else {
                inventory.setItem(i, getFormattedItem(null));
            }
        }
        player.openInventory(inventory);
    }

    public abstract String getTitle();

    public abstract ItemStack getPanel();
    public abstract ItemStack getFormattedItem(Object objects);
    public abstract ItemStack getNextPageButton();
    public abstract ItemStack getPrevPageButton();
    public abstract ItemStack getCloseButton();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', getTitle()))) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            int slot = event.getRawSlot();
            int page = SimplePagesInventory.page.get(player.getName());
            if (slot > 9 && slot < 17) {
                int realIndex = (page * 7 - 7) + (slot - 9);
                int listIndex = realIndex - 1;
                List<Object> objects = SimplePagesInventory.objects.get(player.getName());
                Object object = null;
                if (objects.size() > listIndex) {
                    object = objects.get(listIndex);
                }
                switch (event.getClick()) {
                    case LEFT:
                        onLeftClick(player, object);
                        break;
                    case RIGHT:
                        onRightClick(player, object);
                        break;
                    case MIDDLE:
                        onMiddleClick(player, object);
                        break;
                    case SHIFT_LEFT:
                        onShiftLeftClick(player, object);
                        break;
                    case SHIFT_RIGHT:
                        onShiftRightClick(player, object);
                        break;
                }
            } else if (slot == 9) {
                if (page > 1) {
                    page = page - 1;
                    SimplePagesInventory.page.put(player.getName(), page);
                    List<Object> objects = SimplePagesInventory.objects.get(player.getName());
                    for (int i = 10; i < 17; i++) {
                        int realIndex = (page * 7 - 7) + (i - 9);
                        int listIndex = realIndex - 1;
                        if (objects.size() > listIndex) {
                            event.getInventory().setItem(i, getFormattedItem(objects.get(listIndex)));
                        } else {
                            event.getInventory().setItem(i, getFormattedItem(null));
                        }
                    }
                }
            } else if (slot == 17) {
                List<Object> objects = SimplePagesInventory.objects.get(player.getName());
                if (objects.size() > (page * 7)) {
                    page = page + 1;
                    SimplePagesInventory.page.put(player.getName(), page);
                    for (int i = 10; i < 17; i++) {
                        int realIndex = (page * 7 - 7) + (i - 9);
                        int listIndex = realIndex - 1;
                        if (objects.size() > listIndex) {
                            event.getInventory().setItem(i, getFormattedItem(objects.get(listIndex)));
                        } else {
                            event.getInventory().setItem(i, getFormattedItem(null));
                        }
                    }
                }
            } else if (slot == 22) {
                SimplePagesInventory.objects.remove(player.getName());
                SimplePagesInventory.page.remove(player.getName());
                player.closeInventory();
            }
        }
    }

    public  void onLeftClick(Player player, Object object) {};
    public  void onRightClick(Player player, Object object) {};
    public  void onMiddleClick(Player player, Object object) {};
    public  void onShiftRightClick(Player player, Object object) {};
    public  void onShiftLeftClick(Player player, Object object) {};


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', getTitle()))) {
            SimplePagesInventory.objects.remove(player.getName());
            SimplePagesInventory.page.remove(player.getName());
        }
    }

}