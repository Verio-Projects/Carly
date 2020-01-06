package rip.skyland.carly.api;

import com.google.common.base.Preconditions;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.util.CC;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public enum CoreAPI {

    INSTANCE;

    @Getter
    public JsonParser PARSER = new JsonParser();

    public Rank getRankByName(String name) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");
        return Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(name);
    }

    public Profile getProfileByName2(String name) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");

        return Core.INSTANCE.getHandlerManager().getProfileHandler().getProfiles().stream().filter(profile -> profile.getPlayerName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Profile getProfileByName(String name) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");

        if (Bukkit.getOfflinePlayer(name).getUniqueId() == null) {
            return null;
        }

        Profile profile;
        if (Bukkit.getPlayer(name) == null) {
            profile = Core.INSTANCE.getHandlerManager().getProfileHandler().createProfile(Bukkit.getOfflinePlayer(name).getUniqueId());

            if(profile.getPlayerName() == null) {
                profile.setPlayerName(Bukkit.getOfflinePlayer(name).getName());
            }
        } else {
            profile = CoreAPI.INSTANCE.getProfileByUuid(Bukkit.getPlayer(name).getUniqueId());
        }

        return profile;
    }

    public Profile getProfileByUuid(UUID uuid) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");
        return Core.INSTANCE.getHandlerManager().getProfileHandler().getProfileByUuid(uuid);
    }

    public Profile getOrCreateProfileByUuid(UUID uuid) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");
        if(this.getProfileByUuid(uuid) != null)
            return this.getProfileByUuid(uuid);
        else
            return Core.INSTANCE.getHandlerManager().getProfileHandler().createProfile(uuid);
    }

    public Profile getProfileByPlayer(Player player) {
        return this.getProfileByUuid(player.getUniqueId());
    }

    public int getPing(Player player) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        return player.getClass().getMethod("getHandle").invoke(player).getClass().getField("ping").getInt(player.getClass().getMethod("getHandle").invoke(player));
    }
}
