package rip.skyland.carly.rank.packet;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import lombok.AllArgsConstructor;
import org.bson.Document;
import rip.skyland.carly.Core;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.DocumentBuilder;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class RankSavePacket implements MongoPacket, RedisPacket {

    private UUID uuid;
    private String name, prefix, suffix;
    private int weight;
    private CC color;
    private boolean bold, italic;
    private List<String> permissions;

    @Override
    public void onReceive() {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(name) != null, "tried updating non-existing rank");

        Rank rank = Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(name);

        rank.setPrefix(prefix);
        rank.setSuffix(suffix);
        rank.setWeight(weight);
        rank.setColor(color);
        rank.setPermissions(permissions);
        rank.setBold(bold);
        rank.setItalic(italic);
    }

    @Override
    public void onSend() {

    }

    @Override
    public void savePacket(MongoDatabase database) {
        MongoCollection collection = database.getCollection("ranks");
        Preconditions.checkArgument(collection.find(Filters.eq("uuid", uuid.toString())).first() != null, "Rank with that uuid does not exist");

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), new DocumentBuilder()
                .put("uuid", uuid.toString())
                .put("name", name)
                .put("prefix", prefix)
                .put("suffix", suffix)
                .put("weight", weight)
                .put("color", color.name())
                .put("bold", bold)
                .put("italic", italic)
                .put("permissions", permissions.toString()).getDocument(), new ReplaceOptions().upsert(true));
    }
}
