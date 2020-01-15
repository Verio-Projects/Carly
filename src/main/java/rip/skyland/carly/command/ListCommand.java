package rip.skyland.carly.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;

import java.util.Comparator;
import java.util.stream.Collectors;

public class ListCommand {

    @Command(names={"list", "who", "ewho"})
    public void execute(CommandSender sender) {
        String ranks = Core.INSTANCE.getHandlerManager().getRankHandler().getRanks().stream()
                // reversed sort by rank's weight
                .sorted(Comparator.comparingInt(Rank::getWeight).reversed())
                // map to rank's displayname
                .map(Rank::getDisplayName)
                .collect(Collectors.joining(CC.WHITE + ", "));

        String profiles = Bukkit.getOnlinePlayers().stream()
                // filter by profiles which arent null
                .filter(player -> CoreAPI.INSTANCE.getProfileByPlayer(player) != null)
                // reversed sort by player's rank weight
                .sorted(Comparator.comparingInt(player -> CoreAPI.INSTANCE.getProfileByPlayer((Player) player).getRank().getWeight()).reversed())
                // map to player's displayname
                .map(player -> CoreAPI.INSTANCE.getProfileByPlayer(player).getDisplayName())
                .collect(Collectors.joining(CC.WHITE + ", "));

        sender.sendMessage(CC.translate(ranks));
        sender.sendMessage(CC.translate("&f(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers()  + ") [" + profiles + "&f]"));
    }

}
