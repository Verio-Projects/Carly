package rip.skyland.carly.api;

import com.google.common.base.Preconditions;
import com.google.gson.JsonParser;
import lombok.Getter;
import rip.skyland.carly.Core;
import rip.skyland.carly.rank.Rank;

public enum CoreAPI {

    INSTANCE;

    @Getter
    public JsonParser PARSER;

    public Rank getRankByName(String name) {
        Preconditions.checkArgument(Core.INSTANCE.getHandlerManager() != null, "handler manager is null");
        return Core.INSTANCE.getHandlerManager().getRankHandler().getRankByName(name);
    }

}
