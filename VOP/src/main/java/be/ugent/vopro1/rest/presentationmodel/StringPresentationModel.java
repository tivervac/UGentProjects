package be.ugent.vopro1.rest.presentationmodel;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Class used to present the data of a String to the user.
 *
 * @see PresentationModel
 * @see String
 */
public class StringPresentationModel extends PresentationModel {

    private String string;

    @JsonValue
    public String getResult() {
        return string;
    }

    @Override
    public void setEntity(Object entity) {
        this.string = (String) entity;
    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
