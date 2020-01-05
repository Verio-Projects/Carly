package rip.skyland.carly.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.history.IHistoryIndex;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;

import java.util.stream.IntStream;

public class HistoryCommand {

    @Command(names="history", permission="core.history")
    public void execute(CommandSender sender, @Param(name="player") String targetName, @Param(name="page", value="1") int page) {
        if (Bukkit.getOfflinePlayer(targetName).getUniqueId() == null) {
            sender.sendMessage(CC.translate("&cThat player has never played before."));
            return;
        }

        Profile profile;
        if (Bukkit.getPlayer(targetName) == null) {
            profile = Core.INSTANCE.getHandlerManager().getProfileHandler().createProfile(Bukkit.getOfflinePlayer(targetName).getUniqueId());

            if(profile.getPlayerName() == null) {
                profile.setPlayerName(Bukkit.getOfflinePlayer(targetName).getName());
            }
        } else {
            profile = CoreAPI.INSTANCE.getProfileByUuid(Bukkit.getPlayer(targetName).getUniqueId());
        }

        int maxOnPage = 8;

        IntStream.range(maxOnPage*page-maxOnPage, Math.min(maxOnPage*page, profile.getHistory().size())).forEach(i -> {
            IHistoryIndex historyIndex = profile.getHistory().get(i);

            sender.sendMessage(CC.translate("&3" + historyIndex.getHistoryType() + " &7- " + historyIndex.getHistoryDescription().entrySet().stream()
                    .map(entry -> Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1) + ": " + entry.getValue().getAsString())));
        });

    }

}
