package be.ugent.vopro1.rest.presentationmodel;

import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.concept.Concept;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to present the concept of a Concept to the user.
 *
 * @see PresentationModel
 * @see Concept
 */
public class ConceptPresentationModel extends PresentationModel {

    private Concept entity;

    /**
     * Default constructor of ConceptPresentationModel for Jackson.
     */
    public ConceptPresentationModel() {

    }

    /**
     * Sets the reference of this ConceptPresentationModel to a ConceptEntity
     * object, so that all requested information for this
     * ConceptPresentationModel can be delegated to this ConceptEntity object.
     *
     * @param entity a ConceptEntity to be represented by the
     * ConceptPresentationModel.
     * @see Concept
     */
    @Override
    public void setEntity(Object entity) {
        this.entity = (Concept) entity;
    }

    @Override
    public String getIdentifier() {
        return getName();
    }

    /**
     * This method calls {@link Concept#getName()} of the Concept
     * it's representing and returns the name of this Concept.
     *
     * @return the name of the Concept being represented.
     * @see Concept
     * @see Concept#getName()
     */
    public String getName() {
        return entity.getName();
    }

    /**
     * This method calls {@link Concept#getDefinition()} of the
     * Concept it's representing and returns the definition of this
     * Concept.
     *
     * @return the description of the Concept being represented.
     * @see Concept
     * @see Concept#getDefinition()
     */
    public Description getDefinition() {
        return entity.getDefinition();
    }

    /**
     * This method calls {@link Concept#getAttributes()} of the
     * Concept it's representing and returns the attributes of this
     * Concept.
     *
     * @return a List of attributes of the Concept being represented.
     * @see Concept
     * @see Concept#getAttributes()
     */
    public List<String> getAttributes() {
        List<String> result = new ArrayList<>();
        for (NameReference reference: entity.getAttributeReferences()){
            result.add(reference.name());
        }
        return result;
    }

}
