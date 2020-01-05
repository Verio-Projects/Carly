package rip.skyland.carly.punishments.impl;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.punishments.IPunishment;
import rip.skyland.carly.punishments.PunishmentType;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TemporaryPunishment implements IPunishment {

    private String reason;
    private String punisher;
    private UUID uuid, targetUuid;
    private PunishmentType punishmentType;
    private boolean active;
    private long punishDate;
    private long expiration;

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("reason", reason)
                .addProperty("punisher", punisher)
                .addProperty("uuid", uuid.toString())
                .addProperty("targetUuid", targetUuid.toString())
                .addProperty("punishmentType", punishmentType.name())
                .addProperty("active", active)
                .addProperty("punishDate", punishDate)
                .addProperty("expiration", expiration)
                .getObject();
    }

    @Override
    public String getHistoryType() {
        return "Temporary Punishment";
    }

    @Override
    public JsonObject getHistoryDescription() {
        return new JsonBuilder()
                .addProperty("type", punishmentType.name())
                .addProperty("reason", reason)
                .addProperty("punisher", punisher)
                .getObject();
    }

}