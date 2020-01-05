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

    @Command(names = "history", permission = "core.history")
    public void execute(CommandSender sender, @Param(name = "player") String targetName, @Param(name = "page", value = "1") int page) {
        Profile profile = CoreAPI.INSTANCE.getProfileByName(targetName);

        if(profile == null) {
            sender.sendMessage(CC.translate("&cThat player does not exist"));
            return;
        }

        int maxOnPage = 8;

        IntStream.range(maxOnPage * page - maxOnPage, Math.min(maxOnPage * page, profile.getHistory().size())).forEach(i -> {
            IHistoryIndex historyIndex = profile.getHistory().get(i);

            sender.sendMessage(CC.translate("&3" + historyIndex.getHistoryType() + " &7- " + historyIndex.getHistoryDescription().entrySet().stream()
                    .map(entry -> Character.toUpperCase(entry.getKey().charAt(0)) + entry.getKey().substring(1) + ": " + entry.getValue().getAsString())));
        });
    }
}
