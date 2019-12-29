package rip.skyland.carly;

import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin {

    public void onEnable() {
        try {
            Core.INSTANCE.start(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void onDisable() {
        Core.INSTANCE.stop();
    }

}
