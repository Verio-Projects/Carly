package rip.skyland.carly.util.command.annotation.adapter.impl;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import rip.skyland.carly.util.CC;
import rip.skyland.carly.util.command.annotation.adapter.CommandTypeAdapter;

import java.util.Arrays;

public class MaterialTypeAdapter implements CommandTypeAdapter<Material> {

    @Override
    public Material transform(CommandSender sender, String source) {
        if(Arrays.stream(Material.values()).noneMatch(material -> material.name().equalsIgnoreCase(source))) {
            sender.sendMessage(CC.translate("&cValue " + source + " not found"));
            return null;
        }

        return Material.valueOf(source);
    }
}
