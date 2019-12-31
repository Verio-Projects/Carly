package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

import java.util.Arrays;

public class ChatColorTypeAdapter implements CommandTypeAdapter<CC> {

    @Override
    public CC transform(CommandSender sender, String source) {
        if (Arrays.stream(CC.values()).noneMatch(color -> color.name().equalsIgnoreCase(source))) {
            sender.sendMessage(ChatColor.RED + "Value " + source + " not found.");
            return null;
        }

        return CC.valueOf(source.toUpperCase());
    }
}
