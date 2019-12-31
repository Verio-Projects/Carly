package rip.skyland.carly.rank.grants.impl;

import com.google.gson.JsonObject;
import lombok.Getter;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
public class TemporaryGrant implements IGrant {

    private Rank rank;
    private UUID targetUuid;
    private String granterName;
    private long grantTime, expirationTime;
    private boolean active;

    public TemporaryGrant(Rank rank, UUID targetUuid, String granterName, long grantTime, long expirationTime, boolean active) {
        this.rank = rank;
        this.targetUuid = targetUuid;
        this.granterName = granterName;
        this.grantTime = grantTime;
        this.expirationTime = expirationTime;
        this.active = active;
    }

    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("rank", rank.getName())
                .addProperty("targetUuid", targetUuid.toString())
                .addProperty("granterName", granterName)
                .addProperty("grantTime", grantTime)
                .addProperty("expirationTime", expirationTime)
                .addProperty("active", active)
                .getObject();
    }

}
