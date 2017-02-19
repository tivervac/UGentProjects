package be.ugent.vopro1.model;

import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.conditions.Condition;
import org.aikodi.lang.funky.conditions.TextualCondition;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.process.Process;
import org.aikodi.lang.funky.usecase.AlternateFlow;
import org.aikodi.lang.funky.usecase.ExceptionalFlow;
import org.aikodi.lang.funky.usecase.RegularUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper around the more general UseCase object
 * Has a simpler, but more restricting, get-set interface
 * @see RegularUseCase
 */
public class UsecaseEntity extends RegularUseCase {

    /**
     * Creates a temporary UsecaseEntity.
     */
    public UsecaseEntity() {
        this("temp");
    }

    /**
     * Constructor to create UsecaseEntities.
     * <p>
     * The UsecaseEntity has an empty ArrayList of attributes.
     *
     * @param name of the UsecaseEntity.
     * @see RegularUseCase
     */
    public UsecaseEntity(String name) {
        super(name);
    }


    @Override
    public void setName(String name) {
        if (name == null){
            throw new IllegalArgumentException("Name in UsecaseEntity cannot be null");
        }
        super.setName(name);
    }

    /**
     * Returns the preconditions to be fulfilled before the start of the normal
     * flow of the UsecaseEntity by calling
     * {@link org.aikodi.lang.funky.usecase.UseCase#precondition()}.
     *
     * @return List of preconditions of the UsecaseEntity.
     * @see org.aikodi.lang.funky.usecase.UseCase
     * @see org.aikodi.lang.funky.conditions.Condition
     */
    public List<Description> getPreconditions() {
        List<Description> result = new ArrayList<>();

        for (Condition c : super.precondition()){
            result.add(c.description());
        }
        return result;
    }

    /**
     * Sets the preconditions to be fulfilled before the start of the normal
     * flow of the UsecaseEntity by calling
     * {@link org.aikodi.lang.funky.usecase.UseCase#addPrecondition(org.aikodi.lang.funky.conditions.Condition)}.
     *
     * @param precondition List of preconditions of the UsecaseEntity.
     * @see org.aikodi.lang.funky.usecase.UseCase
     * @see org.aikodi.lang.funky.conditions.Condition
     */
    public void setPreconditions(List<Description> precondition) {
        if (precondition != null && precondition.size() > 0) {
            for (Description desc : precondition){
                super.addPrecondition(new TextualCondition(desc));
            }
        }
    }

    /**
     * Returns the postconditions to be fulfilled after the end of the normal
     * flow of the UsecaseEntity by calling
     * {@link org.aikodi.lang.funky.usecase.UseCase#addPostcondition(org.aikodi.lang.funky.conditions.Condition)}.
     *
     * @return List of postconditions of the UsecaseEntity.
     * @see org.aikodi.lang.funky.usecase.UseCase
     * @see org.aikodi.lang.funky.conditions.Condition
     */
    public List<Description> getPostconditions() {
        List<Description> result = new ArrayList<>();

        for (Condition c : super.postcondition()){
            result.add(c.description());
        }
        return result;
    }

    /**
     * Sets the postconditions to be fulfilled after the end of the normal flow
     * of the UsecaseEntity by calling
     * {@link org.aikodi.lang.funky.usecase.UseCase#addPostcondition(org.aikodi.lang.funky.conditions.Condition)}.
     *
     * @param postcondition List of postconditions of the UsecaseEntity.
     * @see org.aikodi.lang.funky.usecase.UseCase
     * @see org.aikodi.lang.funky.conditions.Condition
     */
    public void setPostconditions(List<Description> postcondition) {
        if (postcondition != null && postcondition.size() > 0) {
            for (Description desc : postcondition){
                super.addPostcondition(new TextualCondition(desc));
            }
        }
    }

    /**
     * Sets the actors that are a part of the execution of the UseCase.
     *
     * @param list List of names of actors of the UseCase.
     */
    public void setActors(List<String> list) {
        if (list == null || list.contains(null)){
            throw new IllegalArgumentException("List of actors cannot be, or contain, null");
        }

        for (String actor : list){
            this.addPrimaryActor(new NameReference<>(actor, Actor.class));
        }
    }

    /**
     * Sets the concepts that are being used in the UsecaseEntity.
     *
     * @param concepts List of concepts of the UsecaseEntity.
     */
    public void setConcepts(List<String> concepts) {
        if (concepts == null || concepts.contains(null)) {
            throw new IllegalArgumentException("List of concepts cannot be, or contain, null");
        }

        for (String concept : concepts){
            this.addConcept(new NameReference<>(concept, Concept.class));
        }
    }

    /**
     * Sets the processes that this UseCaseEntity is part of.
     *
     * @param processes List of processes
     */
    public void setProcesses(List<String> processes) {
        if (processes == null || processes.contains(null)) {
            throw new IllegalArgumentException("List of processes cannot be, or contain, null");
        }

        for (String process : processes) {
            this.addProcess(new NameReference<>(process, org.aikodi.lang.funky.process.Process.class));
        }
    }

    /**
     * Returns the concepts that are being used in the UsecaseEntity.
     *
     * @return List of concepts of the UsecaseEntity.
     */
    public List<String> getConcepts() {
        List<CrossReference<? extends Concept>> references = super.conceptReferences();
        List<String> result = new ArrayList<>();
        for (CrossReference ref : references){
            NameReference nameReference = (NameReference) ref;
            result.add(nameReference.name());
        }
        return result;
    }

    /**
     * Returns the actors that are a part of the execution of the UsecaseEntity.
     *
     * @return List of names of actors of the UsecaseEntity.
     */
    public List<String> getActors() {
        List<CrossReference<? extends Actor>> references = super.primaryActorReferences();
        List<String> result = new ArrayList<>();
        for (CrossReference ref : references){
            NameReference nameReference = (NameReference) ref;
            result.add(nameReference.name());
        }
        return result;
    }

    /**
     * Returns the processes that this UseCaseEntity is part of
     *
     * @return List of names of processes
     */
    public List<String> getProcesses() {
        List<CrossReference<? extends Process>> references = super.processReferences();
        List<String> result = new ArrayList<>();
        for (CrossReference ref : references){
            NameReference nameReference = (NameReference) ref;
            result.add(nameReference.name());
        }
        return result;
    }

    /**
     * Returns the normal flow of the UsecaseEntity.
     *
     * @return the normal flow of the UsecaseEntity.
     * @see org.aikodi.lang.funky.usecase.UseCase
     */
    public Behavior getNormalFlow() {
        return super.normalFlow();
    }

    /**
     * Returns the List of alternative flows, each formed by a List of
     * FlowSteps, of the UsecaseEntity.
     *
     * @return the list of alternative flows
     * @see org.aikodi.lang.funky.usecase.UseCase
     */
    public List<AlternateFlow> getAlternativeFlows() {
        return super.alternateFlows();
    }


    /**
     * Returns the List of exceptional flows, each formed by a List of
     * FlowSteps, of the UsecaseEntity.
     *
     * @return List of exceptional flows each formed by a List of FlowSteps.
     * @see org.aikodi.lang.funky.usecase.UseCase
     */
    public List<ExceptionalFlow> getExceptionalFlows() {
        return super.exceptionalFlows();
    }
}