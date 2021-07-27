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

package club.spfmc.simplehomes.util.chatimput;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class ChatInputManager implements Listener {

    public ChatInputManager(JavaPlugin javaPlugin) {
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
    }

    private final HashMap<UUID, ChatInput> chatInputs = new HashMap<>();

    public void addChatInput(ChatInput chatInput) {
        chatInputs.put(chatInput.getUniqueId(), chatInput);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // Getting Player
        Player player = event.getPlayer();
        // Getting UUID
        UUID uuid = player.getUniqueId();
        // If exist Chat Input from player
        if (chatInputs.containsKey(uuid)) {
            // Cancel chat event
            event.setCancelled(true);
            // Getting the Chat Input
            ChatInput chatInput = chatInputs.get(uuid);
            // If onChatInput method return true
            if (chatInput.onChatInput(player, event.getMessage())) {
                // Removing Chat Input
                chatInputs.remove(uuid);
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        // Getting Player
        Player player = event.getPlayer();
        // Getting UUID
        UUID uuid = player.getUniqueId();
        // If exist Chat Input from player
        if (chatInputs.containsKey(uuid)) {
            // Getting the Chat Input
            ChatInput chatInput = chatInputs.get(uuid);
            // Call onPlayerSneak method
            chatInput.onPlayerSneak(player);
            // Removing Chat Input
            chatInputs.remove(uuid);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Getting Player
        Player player = event.getPlayer();
        // Getting UUID
        UUID uuid = player.getUniqueId();
        // Removing Chat Input from player, if exist
        chatInputs.remove(uuid);
    }

}