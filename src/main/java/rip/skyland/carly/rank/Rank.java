package rip.skyland.carly.rank;

import com.google.common.base.Preconditions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import rip.skyland.carly.Core;
import rip.skyland.carly.util.CC;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Rank {

    private UUID uuid;
    private String name, prefix, suffix;
    private int weight;
    private CC color;
    private boolean bold, italic;

    private List<UUID> inheritances;
    private List<String> permissions;

    public void delete() {
        MongoCollection collection = Core.INSTANCE.getMongoHandler().getCollection("ranks");
        Document document = (Document) collection.find(Filters.eq("uuid", uuid.toString())).first();
        Preconditions.checkArgument(document != null, "Rank with uuid does not exist");

        collection.deleteOne(document);
    }

    public String getFormattedPrefix() { return this.prefix.replace("_", " "); }
    public String getDisplayColor() {
        return (bold ? "&l" : "") + (italic ? "&o" : "") + color;
    }

    public String getDisplayName() {
        return this.getDisplayColor() + name;
    }
}
