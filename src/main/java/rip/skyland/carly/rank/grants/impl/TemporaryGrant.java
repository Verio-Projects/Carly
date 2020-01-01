package rip.skyland.carly.rank.grants.impl;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.grants.IGrant;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TemporaryGrant implements IGrant {

    private Rank rank;
    private UUID targetUuid;
    private String reason;
    private String granterName;
    private long grantTime, expirationTime;
    private boolean active;

    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("rank", rank.getUuid().toString())
                .addProperty("targetUuid", targetUuid.toString())
                .addProperty("reason", reason)
                .addProperty("granterName", granterName)
                .addProperty("grantTime", grantTime)
                .addProperty("expirationTime", expirationTime)
                .addProperty("active", active)
                .getObject();
    }

}
