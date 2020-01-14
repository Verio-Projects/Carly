package rip.skyland.carly.handler.impl.vault;

import lombok.Getter;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.rank.Rank;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.bukkit.craftbukkit.libs.jline.internal.Log.debug;

@Getter
public class PermissionImpl extends Permission {

    private boolean enabled;
    private String name;

    public PermissionImpl() {
        this.enabled = true;
        this.name = "Carly";

        this.register();
    }

    @Override
    public boolean hasSuperPermsCompat() {
        return false;
    }

    @Override
    public boolean playerHas(String s, String player, String permission) {
        if(Bukkit.getPlayer(player) == null)
            return false;

        return Bukkit.getPlayer(player).hasPermission(permission) || Bukkit.getPlayer(player).isOp();
    }

    @Override
    public boolean playerAdd(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean playerRemove(String s, String s1, String s2) {
        return false;
    }

    @Override
    public boolean groupHas(String world, String group, String permission) {
        return Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(permission).getPermissions().stream().anyMatch(permission::equalsIgnoreCase);
    }

    @Override
    public boolean groupAdd(String world, String group, String permission) {
        if(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(group) == null)
            return false;

        Core.INSTANCE.getHandlerManager().getRankHandler().addPermission(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(group), permission);
        return true;
    }

    @Override
    public boolean groupRemove(String world, String group, String permission) {
        if(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(group) == null)
            return false;

        Core.INSTANCE.getHandlerManager().getRankHandler().removePermission(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(group), permission);
        return false;
    }

    @Override
    public boolean playerInGroup(String world, String player, String group) {
        if(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(group) == null || CoreAPI.INSTANCE.getProfileByName(player) == null)
            return false;

        return CoreAPI.INSTANCE.getProfileByName(player).getGrants().stream().anyMatch(grant -> grant.getRank().getName().equalsIgnoreCase(group));
    }

    @Override
    public boolean playerAddGroup(String world, String player, String group) {
        return false;
    }

    @Override
    public boolean playerRemoveGroup(String world, String player, String group) {
        return false;
    }

    @Override
    public String[] getPlayerGroups(String world, String player) {
        Collection<String> result;
        result = CoreAPI.INSTANCE.getProfileByName(player).getGrants().stream().map(grant -> grant.getRank().getName()).collect(Collectors.toList());
        return (String[]) result.toArray();
    }

    @Override
    public String getPrimaryGroup(String world, String player) {
        if(CoreAPI.INSTANCE.getProfileByName(player) == null)
            return "no_profile";

        return CoreAPI.INSTANCE.getProfileByName(player).getRank().getName();
    }

    @Override
    public String[] getGroups() {
        return (String[]) Core.INSTANCE.getHandlerManager().getRankHandler().getRanks().stream().map(Rank::getName).toArray();
    }

    @Override
    public boolean hasGroupSupport() {
        return true;
    }

    public void register() {
        for (RegisteredServiceProvider<?> provider : Bukkit.getServicesManager().getRegistrations(Permission.class)) {
            if (Permission.class.equals(provider.getService()) && provider.getPlugin().getName().equals("Vault") && PermissionImpl.class.isAssignableFrom(provider.getProvider().getClass()) && provider.getPriority() == ServicePriority.Highest) {
                debug(plugin, "removing default vault permission hook");
                Bukkit.getServicesManager().unregister(Permission.class, provider.getProvider());
            }
        }

    }

}
