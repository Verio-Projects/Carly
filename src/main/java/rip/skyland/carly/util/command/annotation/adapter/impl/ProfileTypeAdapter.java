package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.profile.Profile;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

public class ProfileTypeAdapter implements CommandTypeAdapter<Profile> {

    @Override
    public Profile transform(CommandSender sender, String source) {
        if (sender instanceof Player && (source.equalsIgnoreCase("sender") || source.equals(""))) {
            return Core.INSTANCE.getHandlerManager().getProfileHandler().getProfileByUuid(((Player) sender).getUniqueId());
        }

        Profile player = CoreAPI.INSTANCE.getProfileByName2(source);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Profile " + source + " not found.");
            return null;
        }

        return player;    }
}
