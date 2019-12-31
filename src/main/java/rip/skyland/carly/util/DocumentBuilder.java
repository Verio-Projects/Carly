package rip.skyland.carly.util;

import lombok.Getter;
import org.bson.Document;

public class DocumentBuilder {

    @Getter
    private Document document;

    public DocumentBuilder() {
        this.document = new Document();
    }

    public DocumentBuilder put(String path, Object value) {
        document.put(path, value);
        return this;
    }

}
