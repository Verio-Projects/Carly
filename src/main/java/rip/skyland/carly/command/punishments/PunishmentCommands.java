package rip.skyland.carly.command.punishments;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.punishments.IPunishment;
import rip.skyland.carly.punishments.PunishmentHandler;
import rip.skyland.carly.punishments.PunishmentType;
import rip.skyland.carly.punishments.impl.PermanentPunishment;
import rip.skyland.carly.punishments.impl.TemporaryPunishment;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.TimeUtil;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;

import java.util.UUID;

@AllArgsConstructor
public class PunishmentCommands {

    private PunishmentHandler handler;
    
    @Command(names="ban", permission="core.ban")
    public void executeBan(CommandSender sender, @Param(name="player") String targetName, @Param(name="reason", value="Not set -s") String reason, @Param(name="duration", value="perm") String duration) {
        this.execute(sender, targetName, reason, duration, PunishmentType.BAN, false);
    }

    @Command(names="unban", permission="core.unban")
    public void executeUnban(CommandSender sender, @Param(name="player") String targetName) {
        this.execute(sender, targetName, "", "", PunishmentType.BAN, true);
    }

    @Command(names="mute", permission="core.mute")
    public void executeMute(CommandSender sender, @Param(name="player") String targetName, @Param(name="reason", value="Not set -s") String reason, @Param(name="duration", value="perm") String duration) {
        this.execute(sender, targetName, reason, duration, PunishmentType.MUTE, false);
    }

    @Command(names="unmute", permission="core.unmute")
    public void executeUnmute(CommandSender sender, @Param(name="player") String targetName) {
        this.execute(sender, targetName, "", "", PunishmentType.MUTE, true);
    }


    private void execute(CommandSender sender, String targetName, String reason, String duration, PunishmentType punishmentType, boolean undo) {
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

        if(undo) {
            handler.unpunish(profile.getUuid());
        }

        if(!undo) {
            String punisher = sender instanceof Player ? CoreAPI.INSTANCE.getProfileByPlayer((Player) sender).getDisplayName() : "&4&l" + sender;
            IPunishment punishment;
            if (duration.equalsIgnoreCase("perm") || duration.equalsIgnoreCase("permanent")) {
                if (!(sender.hasPermission("core." + punishmentType.name().toLowerCase() + ".permanent"))) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                punishment = new PermanentPunishment(reason, punisher, UUID.randomUUID(), profile.getUuid(), punishmentType, true, System.currentTimeMillis());
            } else {
                long expiration = System.currentTimeMillis() + TimeUtil.parseTime(duration);
                punishment = new TemporaryPunishment(reason, punisher, UUID.randomUUID(), profile.getUuid(), punishmentType, true, System.currentTimeMillis(), expiration);
            }

            handler.punish(punishment);
        }
    }

}
