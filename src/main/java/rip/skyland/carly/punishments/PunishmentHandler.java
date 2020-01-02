package rip.skyland.carly.punishments;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import rip.skyland.carly.Core;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.punishments.impl.PermanentPunishment;
import rip.skyland.carly.punishments.impl.TemporaryPunishment;
import rip.skyland.carly.punishments.packet.PunishPlayerPacket;
import rip.skyland.carly.util.DocumentBuilder;

import java.util.UUID;

public class PunishmentHandler implements IHandler {

    @Override
    public void load() {

    }

    @Override
    public void unload() {

    }

    public IPunishment loadPunishment(Document document) {
        if(document.containsKey("expiration")) {
            return new TemporaryPunishment(
                    document.getString("reason"),
                    document.getString("punisher"),
                    UUID.fromString(document.getString("uuid")),
                    UUID.fromString(document.getString("targetUuid")),
                    PunishmentType.valueOf(document.getString("punishmentType")),
                    document.getBoolean("active"),
                    document.getLong("punishDate"),
                    document.getLong("expiration"));
        } else {
            return new PermanentPunishment(
                    document.getString("reason"),
                    document.getString("punisher"),
                    UUID.fromString(document.getString("uuid")),
                    UUID.fromString(document.getString("targetUuid")),
                    PunishmentType.valueOf(document.getString("punishmentType")),
                    document.getBoolean("active"),
                    document.getLong("punishDate")
            );
        }
    }

    public void punish(IPunishment punishment) {
        this.savePunishment(punishment);
        Core.INSTANCE.sendPacket(new PunishPlayerPacket(punishment.getUuid()));
    }

    private void savePunishment(IPunishment punishment) {
        Document document = Document.parse(punishment.toJson().toString());

        Core.INSTANCE.getMongoHandler().getCollection("punishments").replaceOne(Filters.eq("uuid", punishment.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

}
