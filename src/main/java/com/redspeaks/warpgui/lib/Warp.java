package com.redspeaks.warpgui.lib;

import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Warp {

    @Getter private final String warpName;
    @Getter private final int slot;
    @Getter private final ItemStack logo;
    @Getter private final List<String> leftRawCommands, rightRawCommands, lore;
    @Getter private final Map<Enchantment, Integer> enchants;
    @Getter private final boolean hideEnchant, hideAttributes;
    public Warp(String warpName, ItemStack logo, List<String> leftRawCommands, List<String> rightRawCommands, List<String> lore, int slot, Map<Enchantment, Integer> enchants, boolean hideEnchant, boolean hideAttributes) {
        this.warpName = warpName;
        this.logo = logo;
        this.leftRawCommands = leftRawCommands;
        this.rightRawCommands = rightRawCommands;
        this.lore = lore;
        this.slot = slot;
        this.enchants = enchants;
        this.hideEnchant = hideEnchant;
        this.hideAttributes = hideAttributes;
    }
}
