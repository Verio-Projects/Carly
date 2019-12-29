package rip.skyland.carly.util.database.mongo.packet;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import rip.skyland.carly.util.database.IPacket;

public interface MongoPacket<T> extends IPacket {

    void savePacket(MongoDatabase database);

}
