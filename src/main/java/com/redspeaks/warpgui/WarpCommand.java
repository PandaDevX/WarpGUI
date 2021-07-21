package com.redspeaks.warpgui;

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
        if(message.split(" ").length == 0) {
            return;
        }
        e.setCancelled(true);
        Player player = e.getPlayer();
        WarpGUI.getInstance().getWarpInv().open(player);
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
