package rip.skyland.carly.rank.packet;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.AllArgsConstructor;
import org.bson.Document;
import rip.skyland.carly.Core;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.util.CC;
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

        Document document = new Document();

        document.put("uuid", uuid.toString());
        document.put("name", name);
        document.put("prefix", prefix);
        document.put("suffix", suffix);
        document.put("weight", weight);
        document.put("color", color.name());
        document.put("bold", bold);
        document.put("italic", italic);
        document.put("permissions", permissions.toString());

        collection.updateOne(Filters.eq("uuid", uuid.toString()), document, new UpdateOptions().upsert(true));
    }
}
