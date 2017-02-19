package be.ugent.vopro1.rest.presentationmodel;

import org.aikodi.lang.funky.executors.Actor;

/**
 * Class used to present the data of an Actor to the user.
 *
 * @see PresentationModel
 * @see Actor
 */
public class ActorPresentationModel extends PresentationModel {

    private Actor entity;

    /**
     * Default constructor of ActorPresentationModel for Jackson.
     */
    public ActorPresentationModel() {

    }

    /**
     * Sets the reference of this ActorPresentationModel to an Actor
     * object, so that all requested information for this ActorPresentationModel
     * can be delegated to this Actor object.
     *
     * @param entity an Actor to be represented by the
     * ActorPresentationModel.
     * @see Actor
     */
    @Override
    public void setEntity(Object entity) {
        this.entity = (Actor) entity;
    }

    /**
     * This method calls {@link Actor#name()} of the Actor it's
     * representing and returns the name of this Actor.
     *
     * @return the name of the Actor being represented.
     * @see Actor
     * @see Actor#name()
     */
    public String getName() {
        return entity.name();
    }

    @Override
    public String getIdentifier() {
        return getName();
    }
}
