package rip.skyland.carly.util.menu.pagination;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import rip.skyland.carly.Core;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.menu.Menu;
import rip.skyland.carly.util.menu.button.Button;

import java.util.*;

@Getter
public abstract class PaginatedMenu extends Menu {

    private Player player;

    @Setter
    private int currentPage = 1;

    private List<Button> navigationButtons;

    public PaginatedMenu(Player player) {
        super(player);
        this.player = player;

        this.navigationButtons = Arrays.asList(
                new PaginationButton("&7Previous Page", this, false),
                new PaginationButton("&7Next Page", this, true));
    }

    public int paginatedSize() {
        return size() + 9;
    }

    public List<Button> getPaginatedButtons() {
        List<Button> buttons = new ArrayList<>();
        this.getButtons().forEach(button -> buttons.add(new Button(button.getIndex() + 9, button.getType(), button.getDisplayName(), button.getLore(), button.getDurability(), button.getClick())));
        buttons.addAll(navigationButtons);
        return buttons;
    }

    public void openMenu() {
        this.updatePage();
    }

    public void updatePage() {
        Inventory inventory = Bukkit.createInventory(null, paginatedSize(), CC.translate(getTitle()));

        Core.INSTANCE.getMenuHandler().destroyMenu(this);
        player.closeInventory();

        navigationButtons.forEach(button -> inventory.setItem(button.getIndex(), button.getItem()));

        getPaginatedButtons().stream().filter(button -> (button.getIndex() < paginatedSize() * currentPage) && (button.getIndex() + 1 > paginatedSize() * (currentPage - 1)))
                .forEach(button -> inventory.setItem(button.getIndex() - (button instanceof PaginationButton ? 0 : paginatedSize() * (currentPage - 1)) + (currentPage == 1 ? 0 :
                                button instanceof PaginationButton ? 0 : 9),
                        button.getItem()));

        Core.INSTANCE.getMenuHandler().getMenus().add(this);
        player.openInventory(inventory);
    }

}
