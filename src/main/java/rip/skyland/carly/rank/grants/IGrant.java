package rip.skyland.carly.rank.grants;

import com.google.gson.JsonObject;
import rip.skyland.carly.rank.Rank;

import java.util.UUID;

public interface IGrant {

    Rank getRank();
    UUID getTargetUuid();
    String getGranterName();
    long getGrantTime();
    boolean isActive();
    JsonObject toJson();

}
