package rip.skyland.carly.command;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import rip.skyland.carly.Locale;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.JavaUtils;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;

import java.util.Map;

public class GamemodeCommand {

    private Map<String, GameMode> gamemodes = JavaUtils.mapOf(
            "creative", GameMode.CREATIVE,
            "c", GameMode.CREATIVE,
            "1", GameMode.CREATIVE,
            "survival", GameMode.SURVIVAL,
            "s", GameMode.SURVIVAL,
            "0", GameMode.SURVIVAL
    );

    @Command(names={"gamemode", "gm"}, permission="core.gamemode")
    public void execute(Player player, @Param(name="gamemode") String gamemode, @Param(name="player", value="sender") Player targetPlayer) {
        if(!this.gamemodes.containsKey(gamemode)) {
            player.sendMessage(CC.translate("&cUsage: /gamemode <gamemode> [player]"));
            return;
        }

        Locale locale = targetPlayer.equals(player) ? Locale.CHANGE_GAMEMODE : Locale.CHANGE_GAMEMODE_TARGET;
        GameMode gameMode = this.gamemodes.get(gamemode);
        targetPlayer.setGameMode(gameMode);

        player.sendMessage(CC.translate(locale.getAsString().replace("%gamemode%", gameMode.name()).replace("%player%", targetPlayer.getName())));
    }

}
