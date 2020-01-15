package rip.skyland.carly.profile;

import lombok.Getter;
import lombok.Setter;
import rip.skyland.carly.Core;
import rip.skyland.carly.profile.punishments.IPunishment;
import rip.skyland.carly.profile.punishments.PunishmentType;
import rip.skyland.carly.profile.punishments.impl.TemporaryPunishment;
import rip.skyland.carly.rank.Rank;
import rip.skyland.carly.rank.grants.IGrant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Profile {

    private UUID uuid;
    private String playerName;
    private List<IGrant> grants;
    private long lastChat;

    @Deprecated
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.grants = new ArrayList<>();
    }

    public IPunishment getActivePunishment(PunishmentType type) {
        return Core.INSTANCE.getHandlerManager().getPunishmentHandler().getPunishments().stream()
                .filter(punishment -> {
                    if(punishment instanceof TemporaryPunishment) {
                        if(((TemporaryPunishment) punishment).getExpiration()-System.currentTimeMillis() <= 0)
                            punishment.setActive(false);
                            return false;
                    }

                    return punishment.getPunishmentType().equals(type) && punishment.getTargetUuid().equals(uuid) && punishment.isActive();
                })
                .findFirst()
                .orElse(null);
    }

    public Rank getRank() {
        return grants.stream().filter(IGrant::isActive).sorted(Comparator.comparingInt(grant -> grant.getRank().getWeight())).map(IGrant::getRank).reduce((first, second) -> second).orElse(null);
    }

    public String getDisplayName() {
        return this.getRank().getDisplayColor() + playerName;
    }
}
