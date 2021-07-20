package com.redspeaks.warpgui.lib;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class WarpInv implements InventoryHolder {

    private final Inventory inventory;

    public WarpInv(String title, ItemStack filler, int size) {
        this.inventory = Bukkit.createInventory(this, size, ChatColor.translateAlternateColorCodes('&', title));
        for(int i = 0; i < inventory.getSize(); i++) {
            inventory.setItem(i, filler);
        }
    }

    public void onLoad(List<Warp> warps) {
        for(Warp warp : warps) {
            ItemStack itemStack = warp.getLogo();
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(warp.getWarpName());
            if(!warp.getLore().isEmpty()) {
                for(int i = 0; i < warp.getLore().size(); i++) {
                    warp.getLore().set(i, ChatColor.translateAlternateColorCodes('&', warp.getLore().get(i)));
                }
                meta.setLore(warp.getLore());
            }
            if(!warp.getEnchants().isEmpty()) {
                warp.getEnchants().forEach((en, integer) -> meta.addEnchant(en, integer, true));
            }
            if(warp.isHideEnchant()) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if(warp.isHideAttributes()) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            }
            itemStack.setItemMeta(meta);
            inventory.setItem(warp.getSlot(), itemStack);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
