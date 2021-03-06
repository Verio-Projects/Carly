package rip.skyland.carly.hooks.vault;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import rip.skyland.carly.Core;
import rip.skyland.carly.handler.IHandler;

@Getter
public class VaultHandler implements IHandler {

    private Permission permission;
    private Chat chat;

    @Override
    public void load() {
        Bukkit.getServer().getServicesManager().register(net.milkbowl.vault.permission.Permission.class, new PermissionImpl(), Core.INSTANCE.getPlugin(), ServicePriority.Lowest);
        RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);

        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }

        RegisteredServiceProvider<Chat> chatProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);

        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }
    }

    @Override
    public void unload() {

    }


}
