package com.redspeaks.warpgui;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class WarpGUILoad implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("warpgui.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
            return true;
        }
        WarpGUI.getInstance().reloadConfig();
        WarpGUI.getInstance().load();
        sender.sendMessage(ChatColor.GREEN + "Successfully reloaded the config.");
        return false;
    }
}
