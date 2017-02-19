package be.ugent.vopro1.rest.presentationmodel;

import org.aikodi.lang.funky.virtualmachine.Story;

/**
 * Class used to present the data of a Story to the user.
 *
 * @see PresentationModel
 * @see Story
 */
public class StoryPresentationModel extends ListPresentationModel {

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
        list = ((Story) entity).getEntries();
    }
}
