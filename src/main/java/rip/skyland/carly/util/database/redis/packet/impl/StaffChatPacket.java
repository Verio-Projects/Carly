package rip.skyland.carly.util.database.redis.packet.impl;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.profile.redis.RedisProfile;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.UUID;

@AllArgsConstructor
public class StaffChatPacket implements RedisPacket {

    private UUID uuid;
    private String message;
    private String server;

    @Override
    public void onReceive() {
        String toSend = Locale.STAFF_CHAT.getAsString().replace("%message%", message)
                .replace("%server%", server)
                .replace("%playerName%", CC.stripColor(Core.INSTANCE.getHandlerManager().getProfileHandler().getRedisProfileByUuid(uuid).getName()));

        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("core.staff")).forEach(player -> player.sendMessage(CC.translate(toSend)));
        Bukkit.getConsoleSender().sendMessage(CC.translate(toSend));
    }

    @Override
    public void onSend() {

    }
}
