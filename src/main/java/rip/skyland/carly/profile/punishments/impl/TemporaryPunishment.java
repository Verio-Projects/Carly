package rip.skyland.carly.profile.punishments.impl;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.profile.punishments.IPunishment;
import rip.skyland.carly.profile.punishments.PunishmentType;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TemporaryPunishment implements IPunishment {

    private String reason;
    private String punisher;
    private String unpunishReason;
    private UUID uuid, targetUuid;
    private PunishmentType punishmentType;
    private boolean active;
    private long punishDate;
    private long expiration;

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("reason", reason)
                .addProperty("unpunishReason", unpunishReason)
                .addProperty("punisher", punisher)
                .addProperty("uuid", uuid.toString())
                .addProperty("targetUuid", targetUuid.toString())
                .addProperty("punishmentType", punishmentType.name())
                .addProperty("active", active)
                .addProperty("punishDate", punishDate)
                .addProperty("expiration", expiration)
                .getObject();
    }
}