package rip.skyland.carly.command;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.menu.button.Button;
import rip.skyland.carly.util.menu.pagination.PaginatedMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MenuTestCommand {

    @Command(names="test")
    public void execute(Player player) {
        Core.INSTANCE.getMenuHandler().createMenu(new PaginatedMenu(player) {
            @Override
            public String getTitle() {
                return "haha";
            }

            @Override
            public List<Button> getButtons() {

                return new ArrayList<>(Arrays.asList(new Button(0, Material.STONE, "haha", Collections.emptyList(),0 , player -> {}),
                        new Button(11, Material.STONE, "haha2", Collections.emptyList(),0 , player -> { player.sendMessage(""); })));
            }

            @Override
            public void onClose() {

            }

            @Override
            public int size() {
                return 9;
            }
        });
    }

}
