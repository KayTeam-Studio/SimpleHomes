/*
 * Copyright (C) 2021 SirOswaldo
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

package org.kayteam.simplehomes;

import org.kayteam.api.BrandSender;
import org.kayteam.api.bStats.Metrics;
import org.kayteam.api.input.InputManager;
import org.kayteam.api.inventory.InventoryManager;
import org.kayteam.api.updatechecker.UpdateChecker;
import org.kayteam.api.yaml.Yaml;
import org.kayteam.simplehomes.commands.*;
import org.kayteam.simplehomes.home.HomesManager;
import org.kayteam.simplehomes.listeners.PlayerJoinListener;
import org.kayteam.simplehomes.listeners.PlayerMoveListener;
import org.kayteam.simplehomes.listeners.PlayerQuitListener;
import org.kayteam.simplehomes.placeholderapi.SimpleHomesExpansion;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class SimpleHomes extends JavaPlugin {

    private final Yaml settings = new Yaml(this, "settings");
    public Yaml getSettings() {
        return settings;
    }
    private final Yaml messages = new Yaml(this, "messages");
    public Yaml getMessages() {
        return messages;
    }

    private final HomesManager homesManager = new HomesManager(this);
    public HomesManager getHomesManager() {
        return homesManager;
    }

    private final InventoryManager inventoryManager = new InventoryManager(this);
    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    // Input Manager
    private final InputManager inputManager = new InputManager();
    public InputManager getInputManager() {
        return inputManager;
    }

    // Update Checker
    private UpdateChecker updateChecker;
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    // Vault
    private static Economy economy = null;
    public static Economy getEconomy() {
        return economy;
    }

    @Override
    public void onEnable() {
        bStats();
        registerFiles();
        registerCommands();
        registerListeners();
        loadHomes();
        updateChecker();
        placeholderApi();
        setupEconomy();
        BrandSender.sendBrandMessage(this, "&aEnabled");
    }

    @Override
    public void onDisable() {
        // Unload Player Homes
        for (Player player:getServer().getOnlinePlayers()) {
            player.closeInventory();
            homesManager.unloadHomes(player.getName());
        }
        BrandSender.sendBrandMessage(this, "&cDisabled");
    }

    public void onReload() {
        // Files
        settings.reloadFileConfiguration();
        messages.reloadFileConfiguration();
        // MaxHomes
        homesManager.loadMaxHomes();
        // Players
        for (Player player:getServer().getOnlinePlayers()) {
            player.closeInventory();
            homesManager.unloadHomes(player.getName());
        }
        for (Player player:getServer().getOnlinePlayers()) {
            homesManager.loadHomes(player);
        }
    }

    private void registerFiles() {
        settings.registerFileConfiguration();
        messages.registerFileConfiguration();
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerQuitListener(this), this);
        pluginManager.registerEvents(new PlayerMoveListener(this), this);
        pluginManager.registerEvents(inventoryManager, this);
        pluginManager.registerEvents(inputManager, this);
    }

    private void registerCommands() {
        new SetHomeCommand(this).registerCommand(this);
        new DeleteHomeCommand(this).registerCommand(this);
        new HomeCommand(this).registerCommand(this);
        new HomesCommand(this).registerCommand(this);
        new SimpleHomesCommand(this).registerCommand(this);
        new MigrateHomesCommand(this).registerCommand(this);
    }

    private void bStats() {
        int pluginId = 12209;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SingleLineChart("homes", () -> {
            int homes = 0;
            for (File file:Yaml.getFolderFiles(getDataFolder() + File.separator + "players")) {
                Yaml yaml = new Yaml(this, "players", file.getName().replaceAll(".yml", ""));
                yaml.registerFileConfiguration();
                homes = homes + yaml.getFileConfiguration().getKeys(false).size();
            }
            return homes;
        }));
    }

    private void placeholderApi() { if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) new SimpleHomesExpansion(this).register(); }

    private void updateChecker() {
        updateChecker = new UpdateChecker(this, 94734);
        if (updateChecker.getUpdateCheckResult().equals(UpdateChecker.UpdateCheckResult.OUT_DATED)) {
            updateChecker.sendOutDatedMessage(getServer().getConsoleSender());
        }
    }

    private void loadHomes() {
        for (Player player:getServer().getOnlinePlayers()) {
            homesManager.loadHomes(player);
        }
        homesManager.loadMaxHomes();
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        economy = rsp.getProvider();
    }

}