package rip.skyland.carly.profile.packets;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AllArgsConstructor;
import rip.skyland.carly.Core;
import rip.skyland.carly.util.DocumentBuilder;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;

import java.util.UUID;

@AllArgsConstructor
public class ProfileCreatePacket implements MongoPacket {

    private UUID uuid;
    private String name;

    @Override
    public void savePacket(MongoDatabase database) {
        MongoCollection collection = Core.INSTANCE.getMongoHandler().getCollection("profiles");
        Preconditions.checkArgument(collection.find(Filters.eq("uuid", uuid.toString())).first() == null, "profile with same uuid already exists");

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), new DocumentBuilder()
                .put("uuid", uuid.toString())
                .put("playerName", name).getDocument(), new ReplaceOptions().upsert(true));
    }
}
