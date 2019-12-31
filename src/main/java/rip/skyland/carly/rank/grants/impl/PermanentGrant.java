package rip.skyland.carly.rank.grants.impl;

import com.google.gson.JsonObject;
import lombok.Getter;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
public class PermanentGrant implements IGrant {

    private Rank rank;
    private UUID targetUuid;
    private String granterName;
    private long grantTime;
    private boolean active;

    public PermanentGrant(Rank rank, UUID targetUuid, String granterName, long grantTime, boolean active) {
        this.rank = rank;
        this.targetUuid = targetUuid;
        this.granterName = granterName;
        this.grantTime = grantTime;
        this.active = active;
    }

    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("rank", rank.getName())
                .addProperty("targetUuid", targetUuid.toString())
                .addProperty("granterName", granterName)
                .addProperty("grantTime", grantTime)
                .addProperty("active", active)
                .getObject();
    }

}
