package models.document;


import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

/**
 * @author Titouan Vervack
 */
public class JsonDocument implements Document {

    private ObjectNode root;
    private String url;

    public JsonDocument(ObjectNode node) {
        this.root = node;
    }

    @Override
    public String getText() {
        return root.toString();
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
