package rip.skyland.carly.rank.packet;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import rip.skyland.carly.Core;
import rip.skyland.carly.util.DocumentBuilder;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.UUID;

@AllArgsConstructor
public class RankCreatePacket implements MongoPacket, RedisPacket {

    private UUID uuid;
    private String name;

    @Override
    public void onReceive() {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByUuid(uuid) == null, "rank with same uuid already exists");
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(name) == null, "rank with same name already exists");

        Core.INSTANCE.getHandlerManager().getRankHandler().createRank(name, uuid, false);
    }

    @Override
    public void onSend() {}

    @Override
    public void savePacket(MongoDatabase database) {
        MongoCollection collection = database.getCollection("ranks");
        Preconditions.checkArgument(collection.find(Filters.eq("uuid", this.uuid.toString())).first() == null, "rank with same uuid already exists");

        collection.insertOne(new DocumentBuilder()
                .put("uuid", this.uuid.toString())
                .put("name", this.name).getDocument());
    }
}
