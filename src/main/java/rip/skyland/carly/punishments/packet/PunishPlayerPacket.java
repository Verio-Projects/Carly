package rip.skyland.carly.punishments.packet;

import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
public class PunishPlayerPacket implements RedisPacket {

    private UUID punishmentUuid;

    @Override
    public void onReceive() {
        // load punishment
        IPunishment punishment = Core.INSTANCE.getHandlerManager().getPunishmentHandler().loadPunishment((Document) Objects.requireNonNull(Core.INSTANCE.getMongoHandler().getCollection("punishments").find(Filters.eq("uuid", punishmentUuid.toString())).first()));

        boolean silent = punishment.getReason().contains("-s");

        Locale locale = punishment.getPunishmentType().equals(PunishmentType.MUTE) ? (silent ? Locale.PUNISHMENT_MUTE_SILENT : Locale.PUNISHMENT_MUTE) : (silent ? Locale.PUNISHMENT_BAN_SILENT : Locale.PUNISHMENT_BAN);
        String banMessage = locale.getAsString().replace("%punisher%", punishment.getPunisher()).replace("%player%", CoreAPI.INSTANCE.getOrCreateProfileByUuid(punishment.getTargetUuid()).getDisplayName());

        if(silent) {
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("core.staff")).forEach(player -> player.sendMessage(CC.translate(banMessage)));
            Bukkit.getConsoleSender().sendMessage(CC.translate(banMessage));
        } else {
            Bukkit.broadcastMessage(CC.translate(banMessage));
        }

        if (punishment.getPunishmentType().equals(PunishmentType.BAN) && punishment.isActive()) {
            // async catcher bypass
            if (Bukkit.getPlayer(punishment.getTargetUuid()) != null) {
                Player player = Bukkit.getPlayer(punishment.getTargetUuid());
                Bukkit.getScheduler().runTaskLater(Core.INSTANCE.getPlugin(), () -> player.kickPlayer(CC.translate(Locale.PUNISHMENT_BAN_KICK.getAsString().replace("%reason%", punishment.getReason().replace("-s", "")))), 1L);
            }
        }
    }

    @Override
    public void onSend() {

    }
}
