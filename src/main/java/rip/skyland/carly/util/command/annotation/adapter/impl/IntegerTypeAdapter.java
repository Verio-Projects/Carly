package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

public class IntegerTypeAdapter implements CommandTypeAdapter<Integer> {

    public Integer transform(CommandSender sender, String source) {
        try {
            return (Integer.parseInt(source));
        } catch (NumberFormatException exception) {
            sender.sendMessage(ChatColor.RED + "Value " + source + " not found.");
            return null;
        }
    }

}