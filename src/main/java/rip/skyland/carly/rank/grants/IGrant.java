package rip.skyland.carly.rank.grants;

import com.google.gson.JsonObject;
import rip.skyland.carly.history.IHistoryIndex;
import rip.skyland.carly.rank.Rank;

import java.util.UUID;

public interface IGrant extends IHistoryIndex {

    Rank getRank();
    UUID getTargetUuid();
    String getGranterName();
    String getReason();
    long getGrantTime();
    boolean isActive();
    JsonObject toJson();

    void setActive(boolean val);

}
