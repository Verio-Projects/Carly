package rip.skyland.carly.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.carly.Locale;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;

import java.lang.reflect.InvocationTargetException;

public class PingCommand {

    @Command(names="ping")
    public void execute(Player player, @Param(name="player", value="sender") Profile target) {
        try {
            player.sendMessage(CC.translate(Locale.PING.getAsString().replace("%player%", target.getDisplayName()).replace("%ping%", "" + CoreAPI.INSTANCE.getPing(Bukkit.getPlayer(target.getUuid())))));
        } catch (NoSuchMethodException | IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
