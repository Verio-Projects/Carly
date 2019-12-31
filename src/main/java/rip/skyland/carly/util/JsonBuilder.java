package rip.skyland.carly.util;

import com.google.gson.JsonObject;
import lombok.Getter;

public class JsonBuilder {

    @Getter
    private JsonObject object;

    public JsonBuilder() {
        this.object = new JsonObject();
    }

    public JsonBuilder addProperty(String property, Number value) {
        object.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, boolean value) {
        object.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, String value) {
        object.addProperty(property, value);
        return this;
    }

    public JsonBuilder addProperty(String property, char value) {
        object.addProperty(property, value);
        return this;
    }
}
