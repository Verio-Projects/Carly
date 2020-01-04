package rip.skyland.carly.punishments;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;
import rip.skyland.carly.Core;
import rip.skyland.carly.handler.IHandler;
import rip.skyland.carly.punishments.impl.PermanentPunishment;
import rip.skyland.carly.punishments.impl.TemporaryPunishment;
import rip.skyland.carly.punishments.packet.PunishPlayerPacket;
import rip.skyland.carly.punishments.packet.UnpunishPlayerPacket;
import rip.skyland.carly.util.DocumentBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
public class PunishmentHandler implements IHandler {

    private List<IPunishment> punishments = new ArrayList<>();

    @Override
    public void load() {
        Core.INSTANCE.getMongoHandler().getCollection("punishments").find().forEach((Consumer<Document>) this::loadPunishment);
    }

    @Override
    public void unload() {
        this.punishments.forEach(this::savePunishment);
    }

    public IPunishment loadPunishment(Document document) {
        if(document.containsKey("expiration")) {
            TemporaryPunishment punishment = new TemporaryPunishment(
                    document.getString("reason"),
                    document.getString("punisher"),
                    UUID.fromString(document.getString("uuid")),
                    UUID.fromString(document.getString("targetUuid")),
                    PunishmentType.valueOf(document.getString("punishmentType")),
                    document.getBoolean("active"),
                    document.getLong("punishDate"),
                    document.getLong("expiration"));

            punishments.add(punishment);
            return punishment;
        } else {
            PermanentPunishment punishment = new PermanentPunishment(
                    document.getString("reason"),
                    document.getString("punisher"),
                    UUID.fromString(document.getString("uuid")),
                    UUID.fromString(document.getString("targetUuid")),
                    PunishmentType.valueOf(document.getString("punishmentType")),
                    document.getBoolean("active"),
                    document.getLong("punishDate")
            );

            punishments.add(punishment);
            return punishment;
        }
    }

    public void punish(IPunishment punishment) {
        this.savePunishment(punishment);
        Core.INSTANCE.sendPacket(new PunishPlayerPacket(punishment.getUuid()));

        this.punishments.add(punishment);
    }

    public void unpunish(UUID uuid) {
        if(this.punishments.stream().anyMatch(punishment -> punishment.getTargetUuid().equals(uuid))) {
            Core.INSTANCE.sendPacket(new UnpunishPlayerPacket(this.punishments.stream().filter(punishment -> punishment.getTargetUuid().equals(uuid) && punishment.isActive()).findFirst().orElse(null).getUuid()));
            this.punishments.stream().filter(punishment -> punishment.getTargetUuid().equals(uuid)).forEach(punishment -> punishment.setActive(false));
        }
    }

    private void savePunishment(IPunishment punishment) {
        Document document = Document.parse(punishment.toJson().toString());

        Core.INSTANCE.getMongoHandler().getCollection("punishments").replaceOne(Filters.eq("uuid", punishment.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }
}