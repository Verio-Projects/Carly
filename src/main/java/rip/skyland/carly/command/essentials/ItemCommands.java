package rip.skyland.carly.command.essentials;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.skyland.carly.Locale;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.Command;
import rip.skyland.carly.util.command.annotation.Param;

public class ItemCommands {

    @Command(names="more", permission="core.more")
    public void execute(Player player) {
        if(player.getItemInHand().getType().equals(Material.AIR)) {
            player.sendMessage(CC.translate(Locale.ERROR_MORE_AIR.getAsString()));
            return;
        }

        player.getItemInHand().setAmount(player.getItemInHand().getMaxStackSize());
        player.sendMessage(CC.translate(Locale.MORE_GAVE_MORE.getAsString().replace("%item%", player.getItemInHand().getType().name())));
    }

    @Command(names={"give", "i", "item"}, permission="core.give")
    public void executeGive(Player player, Material itemType, @Param(name="amount", value="1") int amount, @Param(name="player", value="sender") Player target) {
        target.getInventory().addItem(new ItemStack(itemType, amount));
        Locale locale = target.equals(player) ? Locale.GAVE_YOURSELF : Locale.GAVE_PLAYER;
        player.sendMessage(CC.translate(locale.getAsString().replace("%player%", target.getName()).replace("%amount%", amount + "").replace("%item%", itemType.name())));
    }

}
