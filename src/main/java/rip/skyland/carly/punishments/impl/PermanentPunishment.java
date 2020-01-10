package rip.skyland.carly.punishments.impl;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.api.CoreAPI;
import rip.skyland.carly.punishments.IPunishment;
import rip.skyland.carly.punishments.PunishmentType;
import rip.skyland.carly.util.JsonBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class PermanentPunishment implements IPunishment {

    private String reason;
    private String punisher;
    private String unpunishReason;

    private UUID uuid, targetUuid;
    private PunishmentType punishmentType;
    private boolean active;
    private long punishDate;

    @Override
    public JsonObject toJson() {
        return new JsonBuilder()
                .addProperty("reason", reason)
                .addProperty("unpunishReason", unpunishReason)
                .addProperty("punisher", punisher)
                .addProperty("uuid", uuid.toString())
                .addProperty("targetUuid", targetUuid.toString())
                .addProperty("punishmentType", punishmentType.name())
                .addProperty("punishDate", punishDate)
                .addProperty("active", active)
                .getObject();
    }
}
