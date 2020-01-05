package rip.skyland.carly.history;

import com.google.gson.JsonObject;

public interface IHistoryIndex {

    String getHistoryType();
    JsonObject getHistoryDescription();

}
