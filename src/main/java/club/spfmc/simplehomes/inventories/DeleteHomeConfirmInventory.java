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
            HomesManager homesManager = simpleHomes.getHomesManager();
            Homes homes = homesManager.getHomes(player.getName());
            Home home = (Home) object;
            Yaml messages = simpleHomes.getMessages();
            messages.sendMessage(player,  "deleteHome.delete", new String[][]{{"%home%", home.getName()}});
            homes.removeHome(home.getName());
            player.closeInventory();
            simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
        }
    }

    @Override
    public void onCancelClick(Player player, Object object) {
        if (object != null) {
            HomesManager homesManager = simpleHomes.getHomesManager();
            Homes homes = homesManager.getHomes(player.getName());
            player.closeInventory();
            simpleHomes.getHomesInventory().openInventory(player, new ArrayList<>(homes.getHomes()));
        }
    }

}