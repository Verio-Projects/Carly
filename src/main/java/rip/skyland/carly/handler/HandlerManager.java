package rip.skyland.carly.handler;

import lombok.Getter;
import org.bukkit.Bukkit;
import rip.skyland.carly.Core;
import rip.skyland.carly.handler.impl.ServerHandler;
import rip.skyland.carly.handler.impl.vault.VaultHandler;
import rip.skyland.carly.profile.ProfileHandler;
import rip.skyland.carly.punishments.PunishmentHandler;
import rip.skyland.carly.rank.RankHandler;
import rip.skyland.carly.util.menu.MenuHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Getter
public class HandlerManager {

    private List<IHandler> handlers;

    public RankHandler rankHandler;
    public ProfileHandler profileHandler;
    public PunishmentHandler punishmentHandler;
    public ServerHandler serverHandler;
    public VaultHandler vaultHandler;

    public HandlerManager() throws NoSuchFieldException, IllegalAccessException {
        this.handlers = new ArrayList<>();

        if(Bukkit.getPluginManager().getPlugin("Vault") != null) {
            this.registerHandler(this.getClass().getField("vaultHandler"), new VaultHandler());
        }

        this.registerHandler(this.getClass().getField("rankHandler"), new RankHandler());
        this.registerHandler(this.getClass().getField("profileHandler"), new ProfileHandler());
        this.registerHandler(this.getClass().getField("punishmentHandler"), new PunishmentHandler());
        this.registerHandler(this.getClass().getField("serverHandler"), new ServerHandler());

        Core.INSTANCE.setMenuHandler(new MenuHandler());
    }

    private void registerHandler(Field field, IHandler handler) throws IllegalAccessException {
        field.set(this, handler);

        Bukkit.getScheduler().runTaskLater(Core.INSTANCE.getPlugin(), handler::load, 1L);
        this.handlers.add(handler);
    }
}
