package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class WorldTypeAdapter implements CommandTypeAdapter<World> {

    public World transform(CommandSender sender, String source) {
        World world = Bukkit.getServer().getWorld(source);

        if (world == null) {
            sender.sendMessage(ChatColor.RED + "World " + source + " not found.");
            return null;
        }

        return world;
    }

    public List<String> tabComplete(Player sender, String source) {
        List<String> completions = new ArrayList<>();
        for (World world : Bukkit.getServer().getWorlds()) {
            if (StringUtils.startsWithIgnoreCase(world.getName(), source)) {
                completions.add(world.getName());
            }
        }
        return completions;
    }

}