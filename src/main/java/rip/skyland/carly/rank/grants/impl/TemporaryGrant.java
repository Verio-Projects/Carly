package rip.skyland.carly.rank.grants.impl;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class TemporaryGrant implements IGrant {

    private Rank rank;
    private UUID targetUuid;
    private String granterName;
    private long grantTime, expirationTime;
    private boolean active;

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
