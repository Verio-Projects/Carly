package rip.skyland.carly.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.profile.packets.ProfileCreatePacket;
import rip.skyland.carly.rank.grants.IGrant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ProfileHandler implements IHandler {

    private List<Profile> profiles = new ArrayList<>();

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    public Profile createProfile(UUID uuid) {
        if (this.getProfileByUuid(uuid) != null) {
            return this.getProfileByUuid(uuid);
        }

        MongoCollection collection = Core.INSTANCE.getMongoHandler().getCollection("profiles");
        Document document = (Document) collection.find(Filters.eq("uuid", uuid)).first();

        if (document == null) {
            Profile profile = new Profile(uuid);
            profiles.add(profile);
            return profile;
        } else {
            Profile profile = new Profile(uuid);
            profile.setPlayerName(document.getString("playerName"));

            CoreAPI.INSTANCE.PARSER.parse(document.getString("grants")).getAsJsonArray()
                    .forEach(element -> this.addGrant(Core.INSTANCE.getHandlerManager().getRankHandler().getGrantByJson(element.getAsJsonObject()), profile));

            profiles.add(profile);
            return profile;
        }
    }

    public void addGrant(IGrant grant, Profile profile) {
        profile.getGrants().add(grant);
    }

    public void unloadProfile(Profile profile) {
        profiles.remove(profile);
        this.saveProfile(profile);
    }

    public void saveProfile(Profile profile) {
        Core.INSTANCE.sendPacket(new ProfileCreatePacket(profile.getUuid(), profile.getPlayerName()));
    }

    public Profile getProfileByName(String name) {
        return profiles.stream().filter(profile -> profile.getPlayerName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Profile getProfileByUuid(UUID uuid) {
        return profiles.stream().filter(profile -> profile.getUuid().equals(uuid)).findFirst().orElse(null);
    }


}
