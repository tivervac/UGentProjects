package be.itech.model;

import java.util.Date;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class Message {

    private String text;
    private Date created;
    private Person person;
    private String url;

    public Message(String text, Date created, Person person, String url) {
        this.text = text;
        this.created = created;
        this.person = person;
        this.url = url;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
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
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
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

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o instanceof Message) {
            equal = getUrl().equals(((Message) o).getUrl());
        }
        return equal;
    }
}
