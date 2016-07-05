package be.itech.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class Event {

    private String title;
    private String description;
    private Date start;
    private String url;
    private Date end;
    private Date created;
    private Date updated;
    private String index;
    private String confirmationsUrl;
    private String messagesUrl;
    private List<Confirmation> confirmations;
    private List<Message> messages;

    public Event(String title, Date start, String url) {
        this.title = title;
        this.start = start;
        this.url = url;
    }

    public Event(String title, String desc, Date start, Date end, String index) {
        this.title = title;
        this.description = desc;
        this.start = start;
        this.end = end;
        this.index = index;
    }

    public Event(String title, String description, Date start, Date end, Date created, Date updated,
            String url, String index, String confirmationsUrl,
            String messagesUrl, List<Confirmation> confirmations, List<Message> messages) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.created = created;
        this.updated = updated;
        this.url = url;
        this.index = index;
        this.confirmationsUrl = confirmationsUrl;
        this.messagesUrl = messagesUrl;
        this.confirmations = confirmations;
        this.messages = messages;
    }

    public Event(String title, String url) {
        this.title = title;
        this.url = url;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the starttime
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start the starttime to set
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return the updated
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * @param updated the updated to set
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * @return the index
     */
    public String getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * @return the confirmationsUrl
     */
    public String getConfirmationsUrl() {
        return confirmationsUrl;
    }

    /**
     * @param confirmationsUrl the confirmationsUrl to set
     */
    public void setConfirmationsUrl(String confirmationsUrl) {
        this.confirmationsUrl = confirmationsUrl;
    }

    /**
     * @return the messagesUrl
     */
    public String getMessagesUrl() {
        return messagesUrl;
    }

    /**
     * @param messagesUrl the messagesUrl to set
     */
    public void setMessagesUrl(String messagesUrl) {
        this.messagesUrl = messagesUrl;
    }

    /**
     * @return the confirmations
     */
    public List<Confirmation> getConfirmations() {
        return confirmations;
    }

    /**
     * @param confirmations the confirmations to set
     */
    public void setConfirmations(List<Confirmation> confirmations) {
        this.confirmations = confirmations;
    }

    /**
     * @return the messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o instanceof Event) {
            equal = getUrl().equals(((Event) o).getUrl());
        }
        return equal;
    }
}
