package rip.skyland.carly.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public enum ReflectionCache {

    INSTANCE;

    private Object handleObject;
    private Field pingField;


    public int getPing(Player player) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        if(handleObject == null) {
            handleObject = player.getClass().getMethod("getHandle").invoke(player);
            pingField = handleObject.getClass().getField("ping");
        }

        return pingField.getInt(player);
    }
}
