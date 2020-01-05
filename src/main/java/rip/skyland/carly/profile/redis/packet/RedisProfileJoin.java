package rip.skyland.carly.profile.redis.packet;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.profile.ProfileHandler;
import rip.skyland.carly.profile.redis.RedisProfile;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.UUID;

@AllArgsConstructor
public class RedisProfileJoin implements RedisPacket {

    private JsonObject object;
    private String server;

    @Override
    public void onReceive() {
        ProfileHandler profileHandler = Core.INSTANCE.getHandlerManager().getProfileHandler();

        RedisProfile profile;

        if(profileHandler.getRedisProfileByUuid(UUID.fromString(object.get("uuid").getAsString())) == null) {
            profile = new RedisProfile(object);
            profileHandler.getRedisProfiles().add(profile);
        } else {
            profile = profileHandler.getRedisProfileByUuid(UUID.fromString(object.get("uuid").getAsString()));
        }

        String message = null;
        if(profile.getLastAction().equals(RedisProfile.LastAction.LEFT_SERVER)) {
            message = Locale.PROFILE_SWITCH_SERVER.getAsString().replace("%playerName%", profile.getName()).replace("%newServer%", server).replace("%previousServer%", profile.getLastSeenServer());
        } else if(profile.getLastAction().equals(RedisProfile.LastAction.DISCONNECTED) || profile.getLastAction().equals(RedisProfile.LastAction.JOIN_SERVER)) {
            message = Locale.PROFILE_CONNECT.getAsString().replace("%playerName%", profile.getName()).replace("%server%", server);
        }

        String finalMessage = message;
        Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("core.staff")).forEach(player -> player.sendMessage(CC.translate(finalMessage)));
        Bukkit.getConsoleSender().sendMessage(CC.translate(message));
    }

    @Override
    public void onSend() {

    }
}
