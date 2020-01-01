package rip.skyland.carly.profile.packets;

    import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AllArgsConstructor;
import rip.skyland.carly.Core;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.util.DocumentBuilder;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;

import java.util.stream.Collectors;

@AllArgsConstructor
public class ProfileSavePacket implements MongoPacket {

    private Profile profile;

    @Override
    public void savePacket(MongoDatabase database) {
        MongoCollection collection = Core.INSTANCE.getMongoHandler().getCollection("profiles");

        collection.replaceOne(Filters.eq("uuid", profile.getUuid().toString()), new DocumentBuilder()
                        .put("uuid", profile.getUuid().toString())
                        .put("name", profile.getPlayerName())
                        .put("grants", profile.getGrants().stream().map(grant -> grant.toJson().toString()).collect(Collectors.toList()).toString()).getDocument(),
                new ReplaceOptions().upsert(true));
    }
}
