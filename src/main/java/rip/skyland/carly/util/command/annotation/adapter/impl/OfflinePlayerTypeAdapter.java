package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class OfflinePlayerTypeAdapter implements CommandTypeAdapter<OfflinePlayer> {

    public OfflinePlayer transform(CommandSender sender, String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("self") || source.equals(""))) {
            return (Player) sender;
        }

        return Bukkit.getServer().getOfflinePlayer(source);
    }

    public List<String> tabComplete(Player sender, String source) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> StringUtils.startsWithIgnoreCase(player.getName(), source) && sender.canSee(player))
                .map(Player::getName)
                .collect(Collectors.toList());
    }

}