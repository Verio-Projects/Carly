package rip.skyland.carly;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.carly.command.RankCommand;
import rip.skyland.carly.handler.HandlerManager;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.listener.PlayerListener;
import rip.skyland.carly.util.command.CommandHandler;
import rip.skyland.carly.util.database.IPacket;
import rip.skyland.carly.util.database.mongo.MongoHandler;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;
import rip.skyland.carly.util.database.redis.RedisHandler;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;

import java.util.Collections;

@Getter
public enum Core {

    INSTANCE;

    private JavaPlugin plugin;
    private MongoHandler mongoHandler;
    private RedisHandler redisHandler;

    private HandlerManager handlerManager;

    public void start(JavaPlugin plugin) throws NoSuchFieldException, IllegalAccessException {
        this.plugin = plugin;

        // setup configuration
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();

        // register handlers
        this.mongoHandler = new MongoHandler(Locale.MONGO_USERNAME.getAsString(), Locale.MONGO_PASSWORD.getAsString(), Locale.MONGO_HOST.getAsString(), Locale.MONGO_DATABASE.getAsString(), Locale.MONGO_PORT.getAsInteger()).connect();
        this.redisHandler = new RedisHandler(Locale.REDIS_HOST.getAsString(), Locale.REDIS_PASSWORD.getAsString(), Locale.REDIS_PORT.getAsInteger()).connect("carly");
        this.handlerManager = new HandlerManager();

        // register commands
        new CommandHandler(plugin)
            .registerCommands(new RankCommand(handlerManager.getRankHandler()));

        // register listeners
        Collections.singletonList(new PlayerListener(handlerManager.getProfileHandler())).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
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
