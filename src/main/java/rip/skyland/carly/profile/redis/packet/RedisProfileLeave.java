package rip.skyland.carly.profile.redis.packet;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.profile.ProfileHandler;
import rip.skyland.carly.profile.redis.RedisProfile;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.UUID;

@AllArgsConstructor
public class RedisProfileLeave implements RedisPacket {

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

        Bukkit.getScheduler().runTaskLater(Core.INSTANCE.getPlugin(), () -> {
            if(profile.getLastAction().equals(RedisProfile.LastAction.JOIN_SERVER)) {
                return;
            }

            String message = Locale.PROFILE_DISCONNECT.getAsString().replace("%playerName%", profile.getName()).replace("%server%", server);
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("core.staff")).forEach(player -> player.sendMessage(CC.translate(message)));
            Bukkit.getConsoleSender().sendMessage(CC.translate(message));

            profile.setLastAction(RedisProfile.LastAction.DISCONNECTED);

            }, 10L);
    }

    @Override
    public void onSend() {

    }
}
