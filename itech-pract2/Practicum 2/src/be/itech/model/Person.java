package be.itech.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class Person {

    private String name;
    private String url;
    private List<Event> eventsList;
    private Date updated;
    private Date created;
    private Date birth;
    private String email;
    private String peopleIndex;

    public Person(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Person(String name, String email, Date birth, String peopleIndex) {
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.peopleIndex = peopleIndex;
    }

    public Person(String name, String email, Date birth, Date created, Date updated, String url, String peopleIndex, List<Event> eventsList) {
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.created = created;
        this.updated = updated;
        this.url = url;
        this.peopleIndex = peopleIndex;
        this.eventsList = eventsList;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the eventsList
     */
    public List<Event> getEventsList() {
        return eventsList;
    }

    /**
     * @param eventsList the eventsList to set
     */
    public void setEventsList(List<Event> eventsList) {
        this.eventsList = eventsList;
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
     * @return the birth
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * @param birth the birth to set
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the peopleIndex
     */
    public String getPeopleIndex() {
        return peopleIndex;
    }

    /**
     * @param peopleIndex the peopleIndex to set
     */
    public void setPeopleIndex(String peopleIndex) {
        this.peopleIndex = peopleIndex;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o instanceof Person) {
            equal = getUrl().equals(((Person) o).getUrl());
        }
        return equal;
    }

    @Override
    public String toString() {
        return getName();
    }
}
