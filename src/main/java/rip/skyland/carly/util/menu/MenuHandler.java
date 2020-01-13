package rip.skyland.carly.util.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.menu.button.ButtonListener;

import java.util.ArrayList;
import java.util.List;

public class MenuHandler {

    @Getter
    private static MenuHandler instance;

    @Getter
    private List<Menu> menus;

    public MenuHandler() {
        instance = this;
        this.menus = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(new ButtonListener(), Core.INSTANCE.getPlugin());
    }

    public void createMenu(Menu menu) {
        menu.openMenu();
        menus.add(menu);
    }

    public void destroyMenu(Menu menu) {
        menus.remove(menu);
    }

    public Menu getByTitleAndPlayer(Player player, String title) { return this.menus.stream().filter(menu -> CC.translate(menu.getTitle()).equalsIgnoreCase(CC.translate(title)) && menu.getPlayer().equals(player)).findFirst().orElse(null); }
    public Menu getMenuByPlayer(Player player) { return menus.stream().filter(menu -> menu.getPlayer().equals(player)).findFirst().orElse(null); }
    public Menu getByTitle(String title) { return menus.stream().filter(menu -> menu.getTitle().equals(title)).findFirst().orElse(null); }

}
