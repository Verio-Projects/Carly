package rip.skyland.carly.handler;

import lombok.Getter;
import rip.skyland.carly.profile.ProfileHandler;
import rip.skyland.carly.rank.RankHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Getter
public class HandlerManager {

    private List<IHandler> handlers;

    private RankHandler rankHandler;
    private ProfileHandler profileHandler;

    public HandlerManager() throws NoSuchFieldException, IllegalAccessException {
        this.handlers = new ArrayList<>();
        this.registerHandler(this.getClass().getField("rankHandler"), new RankHandler());
        this.registerHandler(this.getClass().getField("profileHandler"), new ProfileHandler());
    }

    private void registerHandler(Field field, IHandler handler) throws IllegalAccessException {
        field.set(this, handler);
        handler.load();
        this.handlers.add(handler);
    }
}
