package rip.skyland.carly.util.database.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;

import java.util.Collections;

@Getter
@Setter
public class MongoHandler {


    private String username, password, host, database;
    private int port;

    private MongoClient client;
    private MongoDatabase mongoDatabase;

    /**
     *
     * @param username username of the user for the database
     * @param password password for the database
     * @param host ip address of the database
     * @param database authentication database
     * @param port port of the database
     */

    public MongoHandler(String username, String password, String host, String database, int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.database = database;
        this.port = port;
    }

    public MongoHandler connect() {
        if(password.isEmpty()) {
            client = new MongoClient(host, port);
        } else {
            client = new MongoClient(new ServerAddress(host, port), Collections.singletonList(MongoCredential.createCredential(username, database, password.toCharArray())));
        }

        this.mongoDatabase = client.getDatabase(this.database);

        return this;
    }

    public void sendPacket(MongoPacket packet) {
        packet.savePacket(mongoDatabase);
    }

    public MongoCollection getCollection(String name) {
        if(mongoDatabase.getCollection(name) == null)
            mongoDatabase.createCollection(name);

        return mongoDatabase.getCollection(name);
    }

}
