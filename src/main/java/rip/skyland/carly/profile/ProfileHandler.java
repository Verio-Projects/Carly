package rip.skyland.carly.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import rip.skyland.carly.Core;
import rip.skyland.carly.Locale;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.profile.packets.ProfileSavePacket;
import rip.skyland.carly.profile.redis.RedisProfile;
import rip.skyland.carly.profile.redis.packet.RedisProfileJoin;
import rip.skyland.carly.profile.redis.packet.RedisProfileLeave;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.rank.grants.impl.PermanentGrant;

import java.util.*;

@Getter
public class ProfileHandler implements IHandler {

    private List<Profile> profiles = new ArrayList<>();
    private List<RedisProfile> redisProfiles = new ArrayList<>();

    @Override
    public void load() {
    }

    @Override
    public void unload() {
        profiles.forEach(this::saveProfile);
    }

    public Profile createProfile(UUID uuid) {
        MongoCollection collection = Core.INSTANCE.getMongoHandler().getCollection("profiles");
        Document document = (Document) collection.find(Filters.eq("uuid", uuid.toString())).first();

        // make new profile
        Profile profile;

        if (this.getProfileByUuid(uuid) != null) {
            profile = this.getProfileByUuid(uuid);
        } else if (document == null) {
            profile = new Profile(uuid);
            // add default grant

            if (Bukkit.getOfflinePlayer(uuid) != null) {
                profile.setPlayerName(Bukkit.getOfflinePlayer(uuid).getName());
            }

            Core.INSTANCE.sendPacket(new ProfileSavePacket(profile));
        } else {
            profile = new Profile(uuid);

            // load profile stuff from mongo
            profile.setPlayerName(document.getString("name"));
            CoreAPI.INSTANCE.PARSER.parse(document.getString("grants")).getAsJsonArray().forEach(element -> this.addGrant(Core.INSTANCE.getHandlerManager().getRankHandler().getGrantByJson(element.getAsJsonObject()), profile));

            profiles.add(profile);
        }

        if (profile.getGrants().isEmpty()) {
            this.addGrant(new PermanentGrant(Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName("Default"), uuid, "first time join", "&4CONSOLE", System.currentTimeMillis(), true), profile);
        }

        Bukkit.getScheduler().runTaskLater(Core.INSTANCE.getPlugin(), () -> {
            if (Bukkit.getPlayer(uuid) != null) {
                RedisProfile redisProfile = new RedisProfile(uuid, profile.getDisplayName());
                redisProfile.setLastSeenServer(Locale.SERVER_NAME.getAsString());
                redisProfile.setLastAction(RedisProfile.LastAction.JOIN_SERVER);

                Core.INSTANCE.sendPacket(new RedisProfileJoin(redisProfile.toJson(), Locale.SERVER_NAME.getAsString()));

                if (Core.INSTANCE.getHandlerManager().getVaultHandler() != null) {
                    if (!profile.getGrants().isEmpty()) {
                        Core.INSTANCE.getHandlerManager().getVaultHandler().getPermission().playerAddGroup(null, Bukkit.getOfflinePlayer(uuid), profile.getRank().getName());
                    }
                    System.out.println(Core.INSTANCE.getHandlerManager().getVaultHandler().getPermission().getPrimaryGroup(Bukkit.getPlayer(uuid)));
                }
            }
        }, 1L);

        if(Bukkit.getPlayer(profile.getUuid()) != null) {
            profile.getGrants().forEach(grant -> grant.getRank().getAllPermissions().forEach(permission -> {
                Player player = Bukkit.getPlayer(profile.getUuid());
                PermissionAttachment attachment = player.addAttachment(Core.INSTANCE.getPlugin());
                attachment.setPermission(permission, true);

                // some spigots don't automatically recalculate permissions after setting a permission, dont ask me why.
                player.recalculatePermissions();
            }));
        }

        profiles.add(profile);
        return profile;
    }

    public void addGrant(IGrant grant, Profile profile) {
        profile.getGrants().add(grant);

        if(Bukkit.getPlayer(profile.getUuid()) != null) {
            grant.getRank().getAllPermissions().forEach(permission -> {
                Player player = Bukkit.getPlayer(profile.getUuid());
                PermissionAttachment attachment = player.addAttachment(Core.INSTANCE.getPlugin());
                attachment.setPermission(permission, true);

                // some spigots don't automatically recalculate permissions after setting a permission, dont ask me why.
                player.recalculatePermissions();
            });
        }
    }

    public void unloadProfile(Profile profile) {
        RedisProfile redisProfile = this.getRedisProfileByUuid(profile.getUuid());
        redisProfile.setLastAction(RedisProfile.LastAction.LEFT_SERVER);

        Core.INSTANCE.sendPacket(new RedisProfileLeave(redisProfile.toJson(), Locale.SERVER_NAME.getAsString()));

        profiles.remove(profile);
        this.saveProfile(profile);
    }

    public void saveProfile(Profile profile) {
        Core.INSTANCE.sendPacket(new ProfileSavePacket(profile));
    }

    public Profile getProfileByUuid(UUID uuid) {
        return profiles.stream().filter(profile -> profile.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public RedisProfile getRedisProfileByUuid(UUID uuid) {
        return redisProfiles.stream().filter(profile -> profile.getUuid().equals(uuid)).findFirst().orElse(null);
    }


}
