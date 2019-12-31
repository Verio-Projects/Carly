package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BooleanTypeAdapter implements CommandTypeAdapter<Boolean> {

    private static final Map<String, Boolean> MAP = Map.of("true", true, "on", true, "yes", true, "false", false, "off", false, "no", false);

    public Boolean transform(CommandSender sender, String source) {
        if (!MAP.containsKey(source.toLowerCase())) {
            sender.sendMessage(ChatColor.RED + "Value " + source + " not found.");
            return null;
        }

        return MAP.get(source.toLowerCase());
    }

    public List<String> tabComplete(Player sender, String source) {
        return MAP.keySet()
                .stream()
                .filter(string -> StringUtils.startsWithIgnoreCase(string, source))
                .collect(Collectors.toList());
    }

}