package rip.skyland.carly.util.command.annotation.adapter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public interface CommandTypeAdapter<T> {

    T transform(CommandSender sender, String source);

    default List<String> tabComplete(Player sender, String source) {
        return new ArrayList<>();
    }

}