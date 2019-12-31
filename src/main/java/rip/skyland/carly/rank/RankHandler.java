package rip.skyland.carly.rank;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.bson.Document;
import rip.skyland.carly.Core;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.rank.grants.impl.PermanentGrant;
import rip.skyland.carly.rank.grants.impl.TemporaryGrant;
import rip.skyland.carly.rank.packet.RankCreatePacket;
import rip.skyland.carly.rank.packet.RankDeletePacket;
import rip.skyland.carly.rank.packet.RankSavePacket;
import rip.skyland.carly.util.CC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class RankHandler implements IHandler {

    private List<Rank> ranks;

    @Override
    public void load() {
        this.ranks = new ArrayList<>();

        Core.INSTANCE.getMongoHandler().getCollection("ranks").find().forEach((Consumer<? super Document>) this::loadRank);
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

        List<String> permissions = new ArrayList<>();
        CoreAPI.INSTANCE.PARSER.parse(document.getString("permissions")).getAsJsonArray().forEach(element -> permissions.add(element.getAsString()));
        rank.setPermissions(permissions);
    }

    public Rank createRank(String name, UUID uuid, boolean sendPacket) {
        Rank rank = new Rank(uuid, name, "", "", 0, CC.WHITE, false, false, Collections.emptyList());
        ranks.add(rank);

        Core.INSTANCE.sendPacket(new RankCreatePacket(uuid, name));

        return rank;
    }

    public void deleteRank(Rank rank) {
        ranks.remove(rank);
        Core.INSTANCE.sendPacket(new RankDeletePacket(rank.getUuid()));
    }

    public void saveRank(Rank rank) {
        if(!ranks.contains(rank)) {
            ranks.add(rank);
        }

        Core.INSTANCE.sendPacket(new RankSavePacket(rank.getUuid(), rank.getName(), rank.getPrefix(), rank.getSuffix(), rank.getWeight(), rank.getColor(), rank.isBold(), rank.isItalic(), rank.getPermissions()));
    }

    public IGrant getGrantByJson(JsonObject object) {
        if(object.get("expirationTime") != null) {
            return new TemporaryGrant(
                    this.getRankByUuid(UUID.fromString(object.get("rank").getAsString())),
                    UUID.fromString(object.get("targetUuid").getAsString()),
                    object.get("granterName").getAsString(),
                    object.get("grantTime").getAsLong(),
                    object.get("expirationTime").getAsLong(),
                    object.get("active").getAsBoolean());
        } else {
            return new PermanentGrant(
                    this.getRankByUuid(UUID.fromString(object.get("rank").getAsString())),
                    UUID.fromString(object.get("targetUuid").getAsString()),
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

}
