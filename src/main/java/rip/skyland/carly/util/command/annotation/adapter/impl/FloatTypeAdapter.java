package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

public class FloatTypeAdapter implements CommandTypeAdapter<Float> {

    public Float transform(CommandSender sender, String source) {
        if (source.toLowerCase().contains("e")) {
            sender.sendMessage(ChatColor.RED + "Value " + source + " not found.");
            return null;
        }

        try {
            float parsed = Float.parseFloat(source);
            if (Float.isNaN(parsed) || !Float.isFinite(parsed)) {
                sender.sendMessage(ChatColor.RED + "Value " + source + " not found.");
                return null;
            }
            return parsed;
        } catch (NumberFormatException exception) {
            sender.sendMessage(ChatColor.RED + "Value " + source + " not found.");
            return null;
        }
    }
    
}