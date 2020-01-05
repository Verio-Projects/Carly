package rip.skyland.carly.punishments.packet;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bukkit.Bukkit;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.punishments.IPunishment;
import rip.skyland.carly.punishments.PunishmentType;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
public class UnpunishPlayerPacket implements RedisPacket {

    private UUID punishmentUuid;

    @Override
    public void onReceive() {
        System.out.println("lol unpunish");
        IPunishment punishment = Core.INSTANCE.getHandlerManager().getPunishmentHandler().loadPunishment((Document) Objects.requireNonNull(Core.INSTANCE.getMongoHandler().getCollection("punishments").find(Filters.eq("uuid", punishmentUuid.toString())).first()));

        boolean silent = punishment.getReason().contains("-s");

        Locale locale = punishment.getPunishmentType().equals(PunishmentType.MUTE) ? (silent ? Locale.PUNISHMENT_UNMUTE_SILENT : Locale.PUNISHMENT_UNMUTE) : (silent ? Locale.PUNISHMENT_UNBAN_SILENT : Locale.PUNISHMENT_UNBAN);
        String banMessage = locale.getAsString().replace("%punisher%", punishment.getPunisher()).replace("%player%", CoreAPI.INSTANCE.getOrCreateProfileByUuid(punishment.getTargetUuid()).getDisplayName());

        if(silent) {
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("core.staff")).forEach(player -> player.sendMessage(CC.translate(banMessage)));
            Bukkit.getConsoleSender().sendMessage(CC.translate(banMessage));
        } else {
            Bukkit.broadcastMessage(CC.translate(banMessage));
        }

        punishment.setActive(false);

        Core.INSTANCE.getHandlerManager().getPunishmentHandler().savePunishment(punishment);
    }

    @Override
    public void onSend() {

    }
}
