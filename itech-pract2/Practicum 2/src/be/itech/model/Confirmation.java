package be.itech.model;

/**
 *
 * @author Titouan Vervack & Caroline De Brouwer
 */
public class Confirmation {

    private Person person;
    private String url;
    private boolean going;

    public Confirmation(Person person) {
        this.person = person;
        this.going = true;
    }

    public Confirmation(boolean going, Person person, String url) {
        this.going = going;
        this.person = person;
        this.url = url;
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

    /**
     * @return the going
     */
    public boolean isGoing() {
        return going;
    }

    /**
     * @param going the going to set
     */
    public void setGoing(boolean going) {
        this.going = going;
    }

    @Override
    public boolean equals(Object o) {
        boolean equal = false;
        if (o instanceof Confirmation) {
            equal = getPerson().equals(((Confirmation) o).getPerson());
        }
        return equal;
    }
}
