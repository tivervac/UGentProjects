package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.model.UsecaseEntity;
import org.aikodi.lang.funky.behavior.Block;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.usecase.AlternateFlow;
import org.aikodi.lang.funky.usecase.ExceptionalFlow;

import java.util.List;

/**
 * Class used to present the data of a UsecaseEntity to the user.
 *
 * @see PresentationModel
 * @see UsecaseEntity
 */
public class UsecasePresentationModel extends PresentationModel {

    private UsecaseEntity entity;

    /**
     * Default constructor of UsecasePresentationModel for Jackson.
     */
    public UsecasePresentationModel() {

    }

    /**
     * Sets the reference of this UsecasePresentationModel to a UsecaseEntity
     * object, so that all requested information for this
     * UsecasePresentationModel can be delegated to this UsecaseEntity object.
     *
     * @param entity an UsecaseEntity to be represented by the
     * UsecasePresentationModel.
     * @see UsecaseEntity
     */
    @Override
    public void setEntity(Object entity) {
        this.entity = (UsecaseEntity) entity;
    }

    @Override
    public String getIdentifier() {
        return getName();
    }

    /**
     * This method calls {@link UsecaseEntity#name()} of the UsecaseEntity
     * it's representing and returns the name of this UsecaseEntity.
     *
     * @return the name of the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#name()
     */
    public String getName() {
        return entity.name();
    }

    /**
     * This method calls {@link UsecaseEntity#getObjective()} of the
     * UsecaseEntity it's representing and returns the objective of this
     * UsecaseEntity.
     *
     * @return the objective of the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getObjective()
     */
    public Description getObjective() {
        return entity.getObjective();
    }

    /**
     * This method calls {@link UsecaseEntity#getPreconditions()} of the
     * UsecaseEntity it's representing and returns the preconditions of this
     * UsecaseEntity.
     *
     * @return the List of preconditions of the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getPreconditions()
     */
    public List<Description> getPreconditions() {
        return entity.getPreconditions();
    }

    /**
     * This method calls {@link UsecaseEntity#getPostconditions()} of the
     * UsecaseEntity it's representing and returns the postconditions of this
     * UsecaseEntity.
     *
     * @return the List of postconditions of the UsecaseEntity being
     * represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getPostconditions()
     */
    public List<Description> getPostconditions() {
        return entity.getPostconditions();
    }

    /**
     * This method calls {@link UsecaseEntity#getNormalFlow()} of the
     * UsecaseEntity it's representing and returns the normal flow of this
     * UsecaseEntity.
     *
     * @return a List of FlowSteps, forming the normal flow of the UsecaseEntity
     * being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getNormalFlow()
     */
    public Block getNormalFlow() {
        return (Block) entity.normalFlow();
    }

    /**
     * This method calls {@link UsecaseEntity#getAlternativeFlows()} of the
     * UsecaseEntity it's representing and returns the alternative flows of this
     * UsecaseEntity.
     *
     * @return a List of Lists of FlowSteps, forming the alternative flows of
     * the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getAlternativeFlows()
     */
    public List<AlternateFlow> getAlternativeFlows() {
        return entity.getAlternativeFlows();
    }

    /**
     * This method calls {@link UsecaseEntity#getExceptionalFlows()} of the
     * UsecaseEntity it's representing and returns the exceptional flows of this
     * UsecaseEntity.
     *
     * @return a List of Lists of FlowSteps, forming the exceptional flows of
     * the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getExceptionalFlows()
     */
    public List<ExceptionalFlow> getExceptionalFlows() {
        return entity.getExceptionalFlows();
    }

    /**
     * This method calls {@link UsecaseEntity#getActors()} of the UsecaseEntity
     * it's representing and returns the actors of this UsecaseEntity.
     *
     * @return a List of Lists actors of the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getActors()
     */
    public List<String> getActors() {
        return entity.getActors();
    }

    /**
     * This method calls {@link UsecaseEntity#getConcepts()} of the
     * UsecaseEntity it's representing and returns the concepts of this
     * UsecaseEntity.
     *
     * @return a List of concepts of the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getConcepts()
     */
    public List<String> getConcepts() {
        return entity.getConcepts();
    }

    /**
     * This method calls {@link UsecaseEntity#getProcesses()} of the
     * UsecaseEntity it's representing and returns the processes of this
     * UsecaseEntity.
     *
     * @return a List of processes of the UsecaseEntity being represented.
     * @see UsecaseEntity
     * @see UsecaseEntity#getProcesses()
     */
    public List<String> getProcesses() {
        return entity.getProcesses();
    }
}
