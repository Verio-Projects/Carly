package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerTypeAdapter implements CommandTypeAdapter<Player> {

    public Player transform(CommandSender sender, String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("sender") || source.equals(""))) {
            return (Player) sender;
        }

        Player player = Bukkit.getServer().getPlayer(source);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Player " + source + " not found.");
            return null;
        }

        return player;
    }

    public List<String> tabComplete(Player sender, String source) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> StringUtils.startsWithIgnoreCase(player.getName(), source) && sender.canSee(player))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

}