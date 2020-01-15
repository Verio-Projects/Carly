package rip.skyland.carly.command.essentials;

import org.bukkit.entity.Player;
import rip.skyland.carly.Locale;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;

public class HealthCommands {

    @Command(names="feed", permission="core.feed")
    public void executeFeed(Player player, @Param(name="player", value="sender") Player target) {
        target.setFoodLevel(20);
        Locale locale = target.equals(player) ? Locale.FED_YOURSELF : Locale.FED_PLAYER;
        player.sendMessage(CC.translate(locale.getAsString().replace("%player%", target.getName())));
    }

    @Command(names="heal", permission="core.heal")
    public void executeHeal(Player player, @Param(name="player", value="sender") Player target) {
        target.setHealth(target.getMaxHealth());
        Locale locale = target.equals(player) ? Locale.HEALED_PLAYER : Locale.HEALED_YOURSELF;
        player.sendMessage(CC.translate(locale.getAsString().replace("%player%", target.getName())));
    }

}
