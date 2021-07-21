package com.redspeaks.warpgui.lib;

import com.redspeaks.warpgui.WarpGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

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

    public void open(Player player) {
        for(int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if(stack == null) continue;
            ItemMeta meta = stack.getItemMeta();
            if(meta == null) continue;
            meta.setDisplayName(WarpGUI.getInstance().parsePlaceHolderIfPresent(meta.getDisplayName(), player));
            if(meta.hasLore()) {
                List<String> currentLore = meta.getLore();
                for(int j = 0; j < currentLore.size(); j++) {
                    currentLore.set(j, WarpGUI.getInstance().parsePlaceHolderIfPresent(currentLore.get(j), player));
                }
                meta.setLore(currentLore);
            }
            stack.setItemMeta(meta);
            inventory.setItem(i, stack);
        }
        player.openInventory(inventory);
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }
}
