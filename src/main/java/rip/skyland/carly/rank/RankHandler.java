package rip.skyland.carly.rank;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.rank.grants.GrantProcedure;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.rank.grants.impl.PermanentGrant;
import rip.skyland.carly.rank.grants.impl.TemporaryGrant;
import rip.skyland.carly.rank.packet.RankCreatePacket;
import rip.skyland.carly.rank.packet.RankDeletePacket;
import rip.skyland.carly.rank.packet.RankSavePacket;
import rip.skyland.carly.util.CC;

import java.util.*;
import java.util.function.Consumer;

@Getter
public class RankHandler implements IHandler {

    private List<Rank> ranks;
    private List<GrantProcedure> grantProcedures;

    @Override
    public void load() {
        this.ranks = new ArrayList<>();
        this.grantProcedures = new ArrayList<>();

        Core.INSTANCE.getMongoHandler().getCollection("ranks").find().forEach((Consumer<? super Document>) this::loadRank);

        if (this.getRankByName("Default") == null) {
            this.createRank("Default", UUID.randomUUID(), true);
        }

        // run grant task (pretty ugly, but idc)
        Bukkit.getScheduler().runTaskTimerAsynchronously(Core.INSTANCE.getPlugin(), () -> Core.INSTANCE.getHandlerManager().getProfileHandler().getProfiles().forEach(profile -> profile.getGrants().stream().filter(grant -> grant instanceof TemporaryGrant).forEach(grant -> {
            if(((TemporaryGrant) grant).getExpirationTime()-System.currentTimeMillis() <= 0) {
                profile.getGrants().remove(grant);
            }
        })), 20L, 20L);

    }

    @Override
    public void unload() {
        ranks.forEach(this::saveRank);
    }

    private void loadRank(Document document) {
        Rank rank = this.createRank(document.getString("name"), UUID.fromString(document.getString("uuid")), false);

        rank.setPrefix(document.getString("prefix"));
        rank.setSuffix(document.getString("suffix"));

        rank.setWeight(document.getInteger("weight"));
        rank.setColor(CC.valueOf(document.getString("color")));
        rank.setBold(document.getBoolean("bold"));
        rank.setItalic(document.getBoolean("italic"));

        List<UUID> inherits = new ArrayList<>();
        CoreAPI.INSTANCE.PARSER.parse(document.getString("inheritances")).getAsJsonArray().forEach(element -> inherits.add(UUID.fromString(element.getAsString())));
        rank.setInheritances(inherits);

        List<String> permissions = new ArrayList<>();
        CoreAPI.INSTANCE.PARSER.parse(document.getString("permissions")).getAsJsonArray().forEach(element -> permissions.add(element.getAsString()));
        rank.setPermissions(permissions);
    }

    public Rank createRank(String name, UUID uuid, boolean sendPacket) {
        Rank rank = new Rank(uuid, name, "", "", 0, CC.WHITE, false, false, Collections.emptyList(), Collections.emptyList());
        ranks.add(rank);

        if (sendPacket) {
            Core.INSTANCE.sendPacket(new RankCreatePacket(uuid, name));
            this.saveRank(rank);
        }
        ranks.sort(Comparator.comparingInt(Rank::getWeight));
        Collections.reverse(ranks);


        if(Core.INSTANCE.getHandlerManager().getVaultHandler() != null) {
            Core.INSTANCE.getHandlerManager().getVaultHandler().getChat().setGroupPrefix((String) null, rank.getName(), rank.getDisplayName());
        }

        return rank;
    }

    public void deleteRank(Rank rank) {
        Core.INSTANCE.sendPacket(new RankDeletePacket(rank.getUuid()));
    }

    public void saveRank(Rank rank) {
        if (!ranks.contains(rank)) {
            ranks.add(rank);
        }

        Core.INSTANCE.sendPacket(new RankSavePacket(rank.getUuid(), rank.getName(), rank.getPrefix(), rank.getSuffix(), rank.getWeight(), rank.getColor(), rank.isBold(), rank.isItalic(), rank.getInheritances(), rank.getPermissions()));
    }

    public void addPermission(Rank rank, String permission) {
        this.setPermission(rank, permission, true);
        rank.getPermissions().add(permission);
    }

    public void removePermission(Rank rank, String permission) {
        this.setPermission(rank, permission, false);
        rank.getPermissions().remove(permission);
    }

    private void setPermission(Rank rank, String permission, boolean active) {
        Core.INSTANCE.getHandlerManager().getProfileHandler().getProfiles().stream()
                .filter(profile -> profile.getGrants().stream().anyMatch(grant -> grant.getRank().equals(rank)))
                .filter(profile -> Bukkit.getPlayer(profile.getUuid()) != null)
                .forEach(profile -> {
                    Player player = Bukkit.getPlayer(profile.getUuid());
                    PermissionAttachment attachment = player.addAttachment(Core.INSTANCE.getPlugin());
                    attachment.setPermission(permission, active);

                    // some spigots don't automatically recalculate permissions after setting a permission, dont ask me why.
                    player.recalculatePermissions();
                });
    }

    public void addInheritance(Rank rank, UUID inheritance) {
        if (!rank.getInheritances().contains(inheritance)) {
            rank.getInheritances().add(inheritance);
            Core.INSTANCE.getHandlerManager().getProfileHandler().getProfiles().stream()
                    .filter(profile -> profile.getGrants().stream().anyMatch(grant -> grant.getRank().equals(rank)) && Bukkit.getPlayer(profile.getUuid()) != null)
                    .forEach(profile -> {
                        Player player = Bukkit.getPlayer(profile.getUuid());
                        PermissionAttachment attachment = player.addAttachment(Core.INSTANCE.getPlugin());

                        getRankByUuid(inheritance).getPermissions().forEach(permission -> attachment.setPermission(permission, true));

                        // some spigots don't automatically recalculate permissions after setting a permission, dont ask me why.
                        player.recalculatePermissions();
                    });
        }
    }

    public void removeInheritance(Rank rank, UUID inheritance) {
        if (rank.getInheritances().contains(inheritance)) {
            rank.getInheritances().remove(inheritance);
            Core.INSTANCE.getHandlerManager().getProfileHandler().getProfiles().stream()
                    .filter(profile -> profile.getGrants().stream().anyMatch(grant -> grant.getRank().equals(rank)) && Bukkit.getPlayer(profile.getUuid()) != null)
                    .forEach(profile -> {
                        Player player = Bukkit.getPlayer(profile.getUuid());
                        PermissionAttachment attachment = player.addAttachment(Core.INSTANCE.getPlugin());

                        getRankByUuid(inheritance).getPermissions().stream()
                                .filter(permission -> profile.getGrants().stream().anyMatch(grant -> grant.getRank().getPermissions().stream().noneMatch(permission2 -> permission2.equalsIgnoreCase(permission))))
                                .forEach(permission -> attachment.setPermission(permission, true));

                        // some spigots don't automatically recalculate permissions after setting a permission, dont ask me why.
                        player.recalculatePermissions();
                    });
        }
    }

    public IGrant getGrantByJson(JsonObject object) {
        if (object.get("expirationTime") != null) {
            return new TemporaryGrant(
                    this.getRankByUuid(UUID.fromString(object.get("rank").getAsString())),
                    UUID.fromString(object.get("targetUuid").getAsString()),
                    object.get("reason").getAsString(),
                    object.get("granterName").getAsString(),
                    object.get("grantTime").getAsLong(),
                    object.get("expirationTime").getAsLong(),
                    object.get("active").getAsBoolean());
        } else {
            return new PermanentGrant(
                    this.getRankByUuid(UUID.fromString(object.get("rank").getAsString())),
                    UUID.fromString(object.get("targetUuid").getAsString()),
                    object.get("reason").getAsString(),
                    object.get("granterName").getAsString(),
                    object.get("grantTime").getAsLong(),
                    object.get("active").getAsBoolean());
        }
    }

    public Rank getRankByName(String name) {
        return ranks.stream().filter(rank -> rank.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Rank getRankByUuid(UUID uuid) {
        return ranks.stream().filter(rank -> rank.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public GrantProcedure getProcedureByUuid(UUID uuid) {
        return Core.INSTANCE.getHandlerManager().getRankHandler().getGrantProcedures().stream().filter(grantProcedure -> grantProcedure.getGranterUuid().equals(uuid)).findFirst().orElse(null);
    }
}
