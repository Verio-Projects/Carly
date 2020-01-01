package rip.skyland.carly.util.menu.button;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import rip.skyland.carly.util.menu.Menu;
import rip.skyland.carly.util.menu.MenuHandler;
import rip.skyland.carly.util.menu.pagination.PaginatedMenu;

import java.util.List;
import java.util.Objects;

public class ButtonListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (MenuHandler.getInstance().getByTitleAndPlayer((Player) event.getWhoClicked(), event.getInventory().getTitle()) != null) {

            Menu menu = MenuHandler.getInstance().getByTitleAndPlayer((Player) event.getWhoClicked(), event.getInventory().getTitle());

            if (!(event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null)) {

                event.setCancelled(true);
                List<Button> buttons = menu instanceof PaginatedMenu ? ((PaginatedMenu) menu).getPaginatedButtons() : menu.getButtons();
                Objects.requireNonNull(buttons.stream().filter(button -> button.getItem().equals(event.getCurrentItem())).findFirst().orElse(null)).getClick().accept((Player) event.getWhoClicked());
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (MenuHandler.getInstance().getByTitleAndPlayer((Player) event.getPlayer(), event.getInventory().getTitle()) != null) {
            MenuHandler.getInstance().getByTitleAndPlayer((Player) event.getPlayer(), event.getInventory().getTitle()).onClose();
            MenuHandler.getInstance().destroyMenu(MenuHandler.getInstance().getByTitleAndPlayer((Player) event.getPlayer(), event.getInventory().getTitle()));
        }
    }
}