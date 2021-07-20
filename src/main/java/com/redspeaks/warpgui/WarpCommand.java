package com.redspeaks.warpgui;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class WarpCommand implements Listener {

    private final List<String> aliases;
    public WarpCommand(List<String> aliases) {
        this.aliases = aliases;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String message = e.getMessage().trim();
        if(!findCommand(message)) return;
        e.setCancelled(true);
        if(message.split(" ").length == 0) {
            return;
        }
        Player player = e.getPlayer();
        player.openInventory(WarpGUI.getInstance().getWarpInv().getInventory());
    }

    public boolean findCommand(String message) {
        for(String alias : aliases) {
            if(message.toLowerCase().equals("/" + alias.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
