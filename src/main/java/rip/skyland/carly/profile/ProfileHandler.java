package rip.skyland.carly.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.profile.packets.ProfileSavePacket;
import rip.skyland.carly.profile.redis.RedisProfile;
import rip.skyland.carly.profile.redis.packet.RedisProfileJoin;
import rip.skyland.carly.profile.redis.packet.RedisProfileLeave;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.rank.grants.impl.PermanentGrant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ProfileHandler implements IHandler {

    private List<Profile> profiles = new ArrayList<>();
    private List<RedisProfile> redisProfiles = new ArrayList<>();

    @Override
    public void load() {
    }

    @Override
    public void unload() {
        profiles.forEach(this::saveProfile);
    }

    public Profile createProfile(UUID uuid) {
        if (this.getProfileByUuid(uuid) != null) {
            return this.getProfileByUuid(uuid);
        }

        MongoCollection collection = Core.INSTANCE.getMongoHandler().getCollection("profiles");
        Document document = (Document) collection.find(Filters.eq("uuid", uuid.toString())).first();

        // make new profile
        Profile profile = new Profile(uuid);

        if (document == null) {
            // add default grant
            this.addGrant(new PermanentGrant(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName("Default"), uuid, "first time join", "&4CONSOLE", System.currentTimeMillis(), true), profile);

            // let the player join event set the player's name so i can save it inside of mongo
            // also instantly save the profile because it doesn't exist yet. (not needed)
            Bukkit.getScheduler().runTaskLater(Core.INSTANCE.getPlugin(), () -> Core.INSTANCE.sendPacket(new ProfileSavePacket(profile)), 5L);
        } else {
            // load profile stuff from mongo
            profile.setPlayerName(document.getString("name"));
            CoreAPI.INSTANCE.PARSER.parse(document.getString("grants")).getAsJsonArray().forEach(element -> this.addGrant(Core.INSTANCE.getHandlerManager().getRankHandler().getGrantByJson(element.getAsJsonObject()), profile));

            profiles.add(profile);
        }

        RedisProfile redisProfile = new RedisProfile(uuid, profile.getDisplayName());
        redisProfile.setLastSeenServer(Locale.SERVER_NAME.getAsString());
        redisProfile.setLastAction(RedisProfile.LastAction.JOIN_SERVER);

        Core.INSTANCE.sendPacket(new RedisProfileJoin(redisProfile.toJson(), Locale.SERVER_NAME.getAsString()));

        profiles.add(profile);
        return profile;
    }

    public void addGrant(IGrant grant, Profile profile) {
        profile.getGrants().add(grant);
    }

    public void unloadProfile(Profile profile) {
        RedisProfile redisProfile = this.getRedisProfileByUuid(profile.getUuid());
        redisProfile.setLastAction(RedisProfile.LastAction.LEFT_SERVER);

        Core.INSTANCE.sendPacket(new RedisProfileLeave(redisProfile.toJson(), Locale.SERVER_NAME.getAsString()));

        profiles.remove(profile);
        this.saveProfile(profile);
    }

    public void saveProfile(Profile profile) {
        Core.INSTANCE.sendPacket(new ProfileSavePacket(profile));
    }

    public Profile getProfileByName(String name) {
        return profiles.stream().filter(profile -> profile.getPlayerName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Profile getProfileByUuid(UUID uuid) {
        return profiles.stream().filter(profile -> profile.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public RedisProfile getRedisProfileByUuid(UUID uuid) {
        return redisProfiles.stream().filter(profile -> profile.getUuid().equals(uuid)).findFirst().orElse(null);
    }


}
