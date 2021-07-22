package com.redspeaks.warpgui;

import com.earth2me.essentials.Essentials;
import com.redspeaks.warpgui.lib.Warp;
import com.redspeaks.warpgui.lib.WarpInv;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class WarpGUI extends JavaPlugin {
    @Getter private static WarpGUI instance = null;
    @Getter private Collection<String> warpNames = null;
    @Getter private WarpInv warpInv = null;
    @Getter private List<Warp> warpCollections = null;
    @Getter private Essentials essentials = null;
    @Getter private ItemStack placeHolder = null;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        if(essentials == null) {
            Bukkit.getPluginManager().disablePlugin(this);
            getLogger().severe("Disabled due to no dependency found");
            return;
        }
        load();

        getCommand("warpguiload").setExecutor(new WarpGUILoad());
    }

    public void load() {
        warpNames = essentials.getWarps().getList();
        placeHolder = new ItemStack(
                Material.getMaterial(getConfig().getString("fill-empty-slots.material")), getConfig().getInt("fill-empty-slots.amount"));
        ItemMeta fillerMeta = placeHolder.getItemMeta();
        fillerMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getConfig().getString("fill-empty-slots.displayname")));
        placeHolder.setItemMeta(fillerMeta);

        Bukkit.getPluginManager().registerEvents(new WarpCommand(getConfig().getStringList("aliases")), this);
        Bukkit.getPluginManager().registerEvents(new ClickListener(), this);
        warpCollections = new ArrayList<>();
        ConfigurationSection itemsSection = getConfig().getConfigurationSection("Items");
        for(String key : itemsSection.getKeys(false)) {
            Material material = Material.getMaterial(itemsSection.getString(key + ".material"));
            int slot = itemsSection.getInt(key + ".slot");
            String name = ChatColor.translateAlternateColorCodes('&', itemsSection.getString(key + ".display_name"));
            List<String> leftClick = itemsSection.getStringList(key + ".left_click_commands");
            List<String> rightClick = itemsSection.getStringList(key + ".right_click_commands");
            List<String> lore = itemsSection.get(key + ".lore") == null ? Collections.emptyList() : itemsSection.getStringList(key + ".lore");
            Map<Enchantment, Integer> enchants = new HashMap<>();
            if(itemsSection.get(key + ".enchantments") != null) {
                for(String en : itemsSection.getConfigurationSection(key + ".enchantments").getKeys(false)) {
                    enchants.put(EnchantmentWrapper.getByKey(NamespacedKey.minecraft(en)), itemsSection.getConfigurationSection(key + ".enchantments").getInt(en));
                }
            }
            warpCollections.add(new Warp(name, new ItemStack(material), leftClick, rightClick, lore, slot, enchants, itemsSection.getBoolean(key + ".hide_enchantments", false), itemsSection.getBoolean(key + ".hide_attributes", false)));
        }

        warpInv = new WarpInv(getConfig().getString("title"), placeHolder, getConfig().getInt("size"));
        warpInv.onLoad(warpCollections);
    }

    public String parsePlaceHolderIfPresent(String message, Player player) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListener here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            return PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }

    @Override
    public void onDisable() {
        instance = null;
        warpNames.clear();
        warpInv = null;
        warpCollections.clear();
        placeHolder = null;
    }
}
