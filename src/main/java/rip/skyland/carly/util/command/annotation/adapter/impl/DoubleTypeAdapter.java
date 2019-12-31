package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

public class DoubleTypeAdapter implements CommandTypeAdapter<Double> {

    public Double transform(CommandSender sender, String source) {
        if (source.toLowerCase().contains("e")) {
            sender.sendMessage(ChatColor.RED + "Value " + source + " not found.");
            return null;
        }

        try {
            double parsed = Double.parseDouble(source);
            if (Double.isNaN(parsed) || !Double.isFinite(parsed)) {
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