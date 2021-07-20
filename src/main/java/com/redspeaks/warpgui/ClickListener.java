package com.redspeaks.warpgui;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.redspeaks.warpgui.lib.Warp;
import com.redspeaks.warpgui.lib.WarpInv;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.concurrent.TimeUnit;

public class ClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() == null)return;
        if(e.getClickedInventory().getHolder() instanceof WarpInv) {
            if (e.getCurrentItem() == null || e.getCurrentItem().isSimilar(WarpGUI.getInstance().getPlaceHolder())) {
                e.setCancelled(true);
                return;
            }
            Warp warp = getWarp(e.getCurrentItem(), e.getSlot());
            if (warp == null) return;
            Warp previousWarp = clickedWarpCache.getIfPresent((Player)e.getWhoClicked());
            if(previousWarp != null && previousWarp.getWarpName().equals(warp.getWarpName())) return;
            clickedWarpCache.put((Player)e.getWhoClicked(), warp);
            e.setCancelled(true);
            if (e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
                for (String left : warp.getLeftRawCommands()) {
                    if (left.startsWith("[player]")) {
                        String[] cmd = left.split(" ");
                        StringBuilder builder = new StringBuilder();
                        for (int i = 1; i < cmd.length; i++) {
                            builder.append(cmd[i]).append(" ");
                        }
                        ((Player) e.getWhoClicked()).performCommand(builder.toString().trim());
                        continue;
                    }
                    if (left.startsWith("[close]")) {
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        continue;
                    }
                    if (left.startsWith("[warp]")) {
                        String[] cmd = left.split(" ");
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        ((Player) e.getWhoClicked()).performCommand("warp " + cmd[1]);
                        continue;
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), left.replace("{player}", e.getWhoClicked().getName()));
                }
            }

            if (e.getClick().isRightClick()) {
                for (String right : warp.getRightRawCommands()) {
                    if (right.startsWith("[player]")) {
                        String[] cmd = right.split(" ");
                        StringBuilder builder = new StringBuilder();
                        for (int i = 1; i < cmd.length; i++) {
                            builder.append(cmd[i]).append(" ");
                        }
                        ((Player) e.getWhoClicked()).performCommand(builder.toString().trim());
                        continue;
                    }
                    if (right.startsWith("[close]")) {
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        continue;
                    }
                    if (right.startsWith("[warp]")) {
                        String[] cmd = right.split(" ");
                        e.setCancelled(true);
                        e.getWhoClicked().closeInventory();
                        ((Player) e.getWhoClicked()).performCommand("warp " + cmd[1]);
                        continue;
                    }
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), right.replace("{player}", e.getWhoClicked().getName()));
                }
            }
        }
    }

    private final Cache<Player, Warp> clickedWarpCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1L, TimeUnit.SECONDS)
            .build();

    private Warp getWarp(ItemStack clicked, int slot) {
        for(Warp warp : WarpGUI.getInstance().getWarpCollections()) {
            if(warp.getLogo().isSimilar(clicked) && warp.getSlot() == slot) {
                return warp;
            }
        }
        return null;
    }
}
