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
    
    @Command(names={"ban", "tempban", "tban"}, permission="core.ban")
    public void executeBan(CommandSender sender, @Param(name="player") String targetName, @Param(name="reason", value="Not set -s") String reason) {
        this.execute(sender, targetName, reason.substring(reason.split(" ")[0].length()), reason.substring(0, reason.split(" ")[0].length()), PunishmentType.BAN, false);
    }

    @Command(names="unban", permission="core.unban")
    public void executeUnban(CommandSender sender, @Param(name="player") String targetName, @Param(name="reason", value="Not set -s") String reason) {
        this.execute(sender, targetName, reason, "", PunishmentType.BAN, true);
    }

    @Command(names={"mute", "tempmute", "tmute"}, permission="core.mute")
    public void executeMute(CommandSender sender, @Param(name="player") String targetName, @Param(name="reason", value="Not set -s") String reason) {
        this.execute(sender, targetName, reason.substring(reason.split(" ")[0].length()), reason.substring(0, reason.split(" ")[0].length()), PunishmentType.MUTE, false);
    }

    @Command(names="unmute", permission="core.unmute")
    public void executeUnmute(CommandSender sender, @Param(name="player") String targetName, @Param(name="reason", value="Not set -s") String reason) {
        this.execute(sender, targetName, reason, "", PunishmentType.MUTE, true);
    }


    private void execute(CommandSender sender, String targetName, String reason, String duration, PunishmentType punishmentType, boolean undo) {
        Profile profile = CoreAPI.INSTANCE.getProfileByNameAndCreate(targetName);

        if(profile == null) {
            sender.sendMessage(CC.translate("&cThat player does not exist"));
            return;
        }

        if(undo && profile.getActivePunishment(punishmentType) != null) {
            profile.getActivePunishment(punishmentType).setUnpunishReason(reason);
            handler.unpunish(punishmentType, profile.getUuid());
        }

        if(!undo) {
            String punisher = sender instanceof Player ? CoreAPI.INSTANCE.getProfileByPlayer((Player) sender).getDisplayName() : "&4&lCONSOLE";
            IPunishment punishment;
            if(!duration.equalsIgnoreCase("perm")) {
                boolean containsDigit = false;
                for(char character : duration.toCharArray()) {
                    if(Character.isDigit(character))
                        containsDigit = true;
                }

                if(!containsDigit) {
                    reason = duration + reason;
                }
            }

            if (duration.equalsIgnoreCase("perm") || duration.equalsIgnoreCase("permanent")) {
                if (!(sender.hasPermission("core." + punishmentType.name().toLowerCase() + ".permanent"))) {
                    sender.sendMessage(CC.translate("&cNo permission."));
                    return;
                }
                punishment = new PermanentPunishment(reason, punisher, "", UUID.randomUUID(), profile.getUuid(), punishmentType, true, System.currentTimeMillis());
            } else {
                long expiration = System.currentTimeMillis() + TimeUtil.parseTime(duration);
                punishment = new TemporaryPunishment(reason, punisher, "", UUID.randomUUID(), profile.getUuid(), punishmentType, true, System.currentTimeMillis(), expiration);
            }

            handler.punish(punishment);
        }
    }

}
