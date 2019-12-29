package rip.skyland.carly;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.carly.handler.HandlerManager;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.util.database.IPacket;
import rip.skyland.carly.util.database.mongo.MongoHandler;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;
import rip.skyland.carly.util.database.redis.RedisHandler;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

@Getter
public enum Core {

    INSTANCE;

    private JavaPlugin plugin;
    private MongoHandler mongoHandler;
    private RedisHandler redisHandler;

    private HandlerManager handlerManager;

    public void start(JavaPlugin plugin) throws NoSuchFieldException, IllegalAccessException {
        this.plugin = plugin;

        this.handlerManager = new HandlerManager();
        this.mongoHandler = new MongoHandler("", "", "", "", 1).connect();
        this.redisHandler = new RedisHandler("", "", 1).connect("carly");
    }

    public void sendPacket(IPacket packet) {
        if(packet instanceof RedisPacket) {
            redisHandler.sendPacket((RedisPacket) packet);
        }

        if(packet instanceof MongoPacket) {
            mongoHandler.sendPacket((MongoPacket) packet);
        }
    }

    public void stop() {
        this.handlerManager.getHandlers().forEach(IHandler::unload);
    }

}
