package be.ugent.vopro1.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * List that can contains links
 */
public class LinkableList {

    // Showing null when there are no links is useless
    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private Map<String, String> links;
    private Object content;

    /**
     * Sets the links to use
     * @param links Links to use
     */
    public void setLinks(Map<String, String> links) {
        this.links = new HashMap<>(links);
    }

    /**
     * Sets the content
     *
     * @param content Content to set
     */
    public void setContent(Object content) {
        this.content = content;
    }

    /**
     * Retrieves the content
     * @return content of the list
     */
    public Object getContent() {
        return content;
    }

    /**
     * Retrieves the links
     *
     * @return the links
     */
    public Map<String, String> getLinks() {
        return links;
    }

    /**
     * Adds a single link
     *
     * @param tag Tag to use for added link
     * @param link Link to add
     */
    public void addLink(String tag, String link) {
        links.put(tag, link);
    }

}
