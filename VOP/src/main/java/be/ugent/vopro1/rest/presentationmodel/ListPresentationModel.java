package be.ugent.vopro1.rest.presentationmodel;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;

/**
 * Class used to present the data of a List to the user.
 *
 * @see PresentationModel
 */
public class ListPresentationModel extends PresentationModel {

    protected List<?> list;

    /**
     * A getter for the list this class is the presentationModel of.
     *
     * @return the list
     */
    @JsonValue
    public List<?> getList() {
        return list;
    }

    /**
     * Sets the reference of this ListPresentationModel to a List object, so
     * that all requested information for this ListPresentationModel can be
     * delegated to this List object.
     *
     * @param entity List object that must be represented by the
     * ListPresentationModel
     */
    @Override
    public void setEntity(Object entity) {
        list = (List<?>) entity;
    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
