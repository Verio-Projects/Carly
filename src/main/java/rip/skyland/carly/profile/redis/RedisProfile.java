package rip.skyland.carly.profile.redis;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
@Setter
public class RedisProfile {

    private final UUID uuid;
    private final String name;

    private LastAction lastAction;
    private String lastSeenServer;

    public RedisProfile(JsonObject object) {
        this.uuid = UUID.fromString(object.get("uuid").getAsString());
        this.name = object.get("username").getAsString();
        this.lastAction = LastAction.valueOf(object.get("lastAction").getAsString());
        this.lastSeenServer = object.get("lastSeenServer").getAsString();
    }

    public RedisProfile(UUID uuid, String username) {
        this.uuid = uuid;
        this.name = username;
    }

    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("uuid", uuid.toString())
                .addProperty("username", name)
                .addProperty("lastAction", lastAction.name())
                .addProperty("lastSeenServer", lastSeenServer)
                .getObject();
    }

    // ngl looks way cleaner than making a separate class lol
    public enum LastAction {
        LEFT_SERVER,
        JOIN_SERVER,
        DISCONNECTED;
    }

}