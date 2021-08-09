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

package club.spfmc.simplehomes.inputs;

import club.spfmc.simplehomes.SimpleHomes;
import club.spfmc.simplehomes.inventories.VaultInventory;
import club.spfmc.simplehomes.util.input.inputs.ChatInput;
import club.spfmc.simplehomes.util.yaml.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VaultDeleteHomeInput implements ChatInput {

    private final SimpleHomes simpleHomes;

    public VaultDeleteHomeInput(SimpleHomes simpleHomes) {
        this.simpleHomes = simpleHomes;
    }

    @Override
    public boolean onChatInput(Player player, String input) {
        Yaml messages = simpleHomes.getMessages();
        try {
            double amount = Double.parseDouble(input);
            if (amount > 0) {
                Yaml settings = simpleHomes.getSettings();
                settings.set("vault.deleteHome", amount);
                settings.saveFileConfiguration();
                Bukkit.getScheduler().runTaskLater(simpleHomes, () -> simpleHomes.getMenuInventoryManager().openInventory(player, new VaultInventory(simpleHomes)), 1);
                return true;
            } else {
                messages.sendMessage(player, "admin.vault.deleteHome.invalidAmount", new String[][] {
                        {"%amount%", input}
                });
                return false;
            }
        } catch (NumberFormatException e) {
            messages.sendMessage(player, "admin.vault.deleteHome.invalidAmount", new String[][] {
                    {"%amount%", input}
            });
            return false;
        }
    }

    @Override
    public void onPlayerSneak(Player player) {
        simpleHomes.getMenuInventoryManager().openInventory(player, new VaultInventory(simpleHomes));
    }

}