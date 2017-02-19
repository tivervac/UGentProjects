package be.ugent.vopro1.rest.presentationmodel;

/**
 * Basic PresentationModel for empty results
 */
public class ApiPresentationModel extends PresentationModel {

    private static final String MESSAGE = "Welcome to VOPro Groep 1's API!";

    /**
     * Default constructor of ActorPresentationModel for Jackson.
     */
    public ApiPresentationModel() {
    }

    /**
     * No entity is needed for the API-representation
     */
    @Override
    public void setEntity(Object entity) {
    }

    /**
     * Shows a welcome message
     *
     * @return the welcome message
     */
    public String getWelcome() {
        return MESSAGE;
    }

    @Override
    public String getIdentifier() {
        return "";
    }
}
