package rip.skyland.carly.util.menu;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.menu.button.Button;

import java.util.List;

@Getter
public abstract class Menu {

    private Player player;

    public Menu(Player player) {
        this.player = player;
    }

    public abstract String getTitle();
    public abstract List<Button> getButtons();
    public abstract void onClose();
    public abstract int size();

    public void openMenu() {
        Inventory inventory = Bukkit.createInventory(null, size(), CC.translate(getTitle()));

        getButtons().forEach(button -> inventory.setItem(button.getIndex(), button.getItem()));
        player.openInventory(inventory);
    }
}
