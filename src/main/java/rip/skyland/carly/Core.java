package rip.skyland.carly;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.skyland.carly.command.*;
import rip.skyland.carly.command.essentials.HealthCommands;
import rip.skyland.carly.command.essentials.ItemCommands;
import rip.skyland.carly.command.grant.GrantCommand;
import rip.skyland.carly.command.punishments.HistoryCommand;
import rip.skyland.carly.command.punishments.PunishmentCommands;
import rip.skyland.carly.handler.HandlerManager;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.listener.ChatListener;
import rip.skyland.carly.listener.PlayerListener;
import rip.skyland.carly.util.command.CommandHandler;
import rip.skyland.carly.util.database.IPacket;
import rip.skyland.carly.util.database.mongo.MongoHandler;
import rip.skyland.carly.util.database.mongo.packet.MongoPacket;
import rip.skyland.carly.util.database.redis.RedisHandler;
import rip.skyland.carly.util.database.redis.packet.RedisPacket;
import rip.skyland.carly.util.menu.MenuHandler;

import java.util.Arrays;

@Getter
public enum Core {

    INSTANCE;

    private JavaPlugin plugin;
    private MongoHandler mongoHandler;
    private RedisHandler redisHandler;

    @Setter
    private MenuHandler menuHandler;

    private HandlerManager handlerManager;

    @Deprecated
    public void start(JavaPlugin plugin) throws NoSuchFieldException, IllegalAccessException {
        this.plugin = plugin;

        // setup configuration
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();

        // register handlers
        this.mongoHandler = new MongoHandler(Locale.MONGO_USERNAME.getAsString(), Locale.MONGO_PASSWORD.getAsString(), Locale.MONGO_HOST.getAsString(), Locale.MONGO_DATABASE.getAsString(), Locale.MONGO_PORT.getAsInteger()).connect();
        this.redisHandler = new RedisHandler(Locale.REDIS_HOST.getAsString(), Locale.REDIS_PASSWORD.getAsString(), Locale.REDIS_PORT.getAsInteger()).connect(Locale.REDIS_CHANNEL.getAsString());
        this.handlerManager = new HandlerManager();

        // register commands
        new CommandHandler(plugin, "carly").registerCommands(
                new RankCommand(handlerManager.getRankHandler()),
                new GrantCommand(),
                new ListCommand(),
                new PunishmentCommands(handlerManager.getPunishmentHandler()),
                new ChatCommands(handlerManager.getServerHandler()),
                new GamemodeCommand(),
                new PingCommand(),
                new HealthCommands(),
                new ItemCommands(),
                new HistoryCommand(handlerManager.getPunishmentHandler()));

        // register listeners
        Arrays.asList(
                new PlayerListener(handlerManager.getProfileHandler()),
                new ChatListener()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, plugin));
    }

    public void sendPacket(IPacket packet) {
        if (packet instanceof RedisPacket) {
            this.redisHandler.sendPacket((RedisPacket) packet);
        }

        if (packet instanceof MongoPacket) {
            this.mongoHandler.sendPacket((MongoPacket) packet);
        }
    }

    @Deprecated
    public void stop() {
        this.handlerManager.getHandlers().forEach(IHandler::unload);
    }
}
