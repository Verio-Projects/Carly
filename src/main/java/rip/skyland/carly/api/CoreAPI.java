package rip.skyland.carly.api;

import com.google.common.base.Preconditions;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.rank.Rank;

import java.util.UUID;

public enum CoreAPI {

    INSTANCE;

    @Getter
    public JsonParser PARSER = new JsonParser();

    public Rank getRankByName(String name) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");
        return Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(name);
    }

    public Profile getProfileByName(String name) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");
        return Core.INSTANCE.getHandlerManager().getProfileHandler().getProfileByName(name);
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
}
