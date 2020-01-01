package rip.skyland.carly.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand {

    @Command(names="list")
    public void execute(CommandSender sender) {
        String ranks = Core.INSTANCE.getHandlerManager().getRankHandler().getRanks().stream()
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                .map(Rank::getDisplayName)
                .collect(Collectors.joining(CC.WHITE + ", "));
        String profiles = Bukkit.getOnlinePlayers().stream()
                .sorted(Comparator.comparingInt(player -> CoreAPI.INSTANCE.getProfileByPlayer((Player) player).getRank().getWeight()).reversed())
                .filter(player -> CoreAPI.INSTANCE.getProfileByPlayer(player) != null)
                .map(player -> CoreAPI.INSTANCE.getProfileByPlayer(player).getDisplayName())
                .collect(Collectors.joining(CC.WHITE + ", "));

        sender.sendMessage(CC.translate(ranks));
        sender.sendMessage(CC.translate("&f(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()  + ") [" + profiles + "&f]"));
    }

}
