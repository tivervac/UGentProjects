package org.aikodi.lang.funky.usecase;

import com.google.java.contract.Ensures;
import com.google.java.contract.Requires;
import org.aikodi.chameleon.core.declaration.*;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.behavior.description.Description;
import org.aikodi.lang.funky.behavior.description.TextualDescription;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.conditions.Condition;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.executors.ExecutingEntity;
import org.aikodi.lang.funky.process.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * A abstract class for use cases.
 */
public abstract class UseCase extends CommonDeclaration implements TargetDeclaration,
        DeclarationContainer {

    private Multi<AlternateFlow> _alternateFlows = new Multi<>(this);
    private Multi<ExceptionalFlow> _exceptionalFlows = new Multi<>(this);
    private Multi<CrossReference<? extends Actor>> _primaryActors = new Multi<>(this, 1, -1);
    private Multi<CrossReference<? extends Concept>> _concepts = new Multi<>(this, 0, -1);
    private Multi<CrossReference<? extends Process>> _processes = new Multi<>(this, 0, -1);

    private Single<Description> objective = new Single<>(this,true);

    /**
     * The precondition is not mandatory because there will usually be a number
     * of use cases to start the interaction without requiring any conditions.
     */
    private Multi<Condition> _precondition = new Multi<>(this);
    /**
     * The postcondition is mandatory for now. If use case relations are added,
     * that may no longer be the case because it could be inherited through a
     * generalization.
     */
    private Multi<Condition> _postcondition = new Multi<>(this);

    /**
     * Create a new use case with the given name.
     *
     * @param name
     *           The name of the use case.
     */
    @Ensures({"name().equals(name)"})
    public UseCase(String name) {
        super(new SimpleNameSignature(name));
        set(objective, new TextualDescription(""));
    }

    /**
     * Create a new use case with the given name.
     *
     * @param name
     *           The name of the use case.
     */
    @Ensures({"name().equals(name)", "precondition() == precondition", "postcondition() == postcondition"})
    public UseCase(String name, Condition precondition, Condition postcondition) {
        super(new SimpleNameSignature(name));
        addPrecondition(precondition);
        addPostcondition(postcondition);

        set(objective, new TextualDescription(""));
    }

    /**
     * Just for cloning
     */
    protected UseCase() {
        super(null);
    }

    /**
     * {@inheritDoc}
     *
     * Returns all descendants of type {@link Declaration}. This means that for
     * now, no two descendants can have the same signature. If that must be
     * possible, a more advanced mechanism must be implemented.
     */
    @Override
    public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
        return descendants(Declaration.class);
    }


    /**
     * Returns the objective to be completed by the UsecaseEntity.
     *
     * @return the objective of this UsecaseEntity.
     */
    public Description getObjective() {
        return objective.getOtherEnd();
    }

    /**
     * Sets the objective to be completed by the UsecaseEntity.
     *
     * @param objective the objective of this UsecaseEntity.
     */
    public void setObjective(String objective) {
        if (objective == null) {
            throw new IllegalArgumentException("Objective in UsecaseEntity cannot be null");
        }
        set(this.objective, new TextualDescription(objective));
    }

    /**
     * Sets the objective to be completed by the UsecaseEntity.
     *
     * @param objective the objective of this UsecaseEntity.
     */
    public void setObjective(Description objective) {
        if (objective == null) {
            throw new IllegalArgumentException("Objective in UsecaseEntity cannot be null");
        }

        set(this.objective, objective);
    }

    /**
     * @return the list of cross-references to the processes of this use
     * case.
     */
    @Ensures({"result != null"})
    public List<CrossReference<? extends Process>> processReferences() {
        return _processes.getOtherEnds();
    }

    /**
     * Add a reference to a process.
     *
     * @param process A process of this use case.
     */
    public void addProcess(Process process) {
        add(_processes, new NameReference<>(process.name(), Process.class));
    }

    public void addProcess(NameReference<? extends Process> processReference) {
        add(_processes, processReference);
    }

    /**
     * @return The alternate flows of this use case.
     */
    @Ensures({"result != null"})
    public List<AlternateFlow> alternateFlows() {
        return _alternateFlows.getOtherEnds();
    }

    /**
     * Add the given alternate flow to this use case.
     *
     * @param flow
     *           The alternate flow to be added.
     */
    @Requires({"flow != null"})
    @Ensures({"alternateFlows().get(alternateFlows().size() - 1) == flow",
            "alternateFlows().subList(0,alternateFlows().size() - 1).equals(old(alternateFlows()))"})
    public void addAlternateFlow(AlternateFlow flow) {
        add(_alternateFlows, flow);
    }

    /**
     * @return The exceptional flows of this use case.
     */
    @Ensures({"result != null"})
    public List<ExceptionalFlow> exceptionalFlows() {
        return _exceptionalFlows.getOtherEnds();
    }

    /**
     * Add the given exceptional flow to this use case.
     *
     * @param flow
     *           The exceptional flow to be added.
     */
    @Requires({"flow != null"})
    @Ensures({"exceptionalFlows().get(exceptionalFlows().size() - 1) == flow",
            "exceptionalFlows().subList(0,exceptionalFlows().size() - 1).equals(old(exceptionalFlows()))"})
    public void addExceptionalFlow(ExceptionalFlow flow) {
        add(_exceptionalFlows, flow);
    }

    /**
     * @return The precondition of this use case.
     */
    public List<Condition> precondition() {
        return _precondition.getOtherEnds();
    }

    /**
     * Set the precondition of this use case.
     *
     * @param precondition
     *           The new precondition of this use case.
     */
    @Ensures({"precondition() == precondition", "postcondition() == postcondition"})
    public void addPrecondition(Condition precondition) {
        add(_precondition, precondition);
    }

    /**
     * @return The postcondition of this use case.
     */
    public List<Condition> postcondition() {
        return _postcondition.getOtherEnds();
    }

    /**
     * Set the postcondition of this use case.
     *
     * @param postcondition
     *           The new postcondition of this use case.
     */
    @Ensures({"postcondition() == postcondition"})
    public void addPostcondition(Condition postcondition) {
        add(_postcondition, postcondition);
    }

    /**
     * Add a reference to a primary actor.
     *
     * @param actorReference
     *           A cross-reference to one of the primary actors of this use case.
     */
    public void addPrimaryActor(CrossReference<? extends Actor> actorReference) {
        add(_primaryActors, actorReference);
    }

    /**
     * @return the list of cross-references to the primary actors of this use
     *         case.
     */
    @Ensures({"result != null"})
    public List<CrossReference<? extends Actor>> primaryActorReferences() {
        return _primaryActors.getOtherEnds();
    }

    /**
     * @return the list of cross-references to the concepts of this use
     *         case.
     */
    @Ensures({"result != null"})
    public List<CrossReference<? extends Concept>> conceptReferences() {
        return _concepts.getOtherEnds();
    }

    /**
     * Add a reference to a primary actor.
     *
     * @param conceptReference
     *           A cross-reference to one of the primary actors of this use case.
     */
    public void addConcept(CrossReference<? extends Concept> conceptReference) {
        add(_concepts, conceptReference);
    }

    /**
     * @return the list of concepts of this use case.
     * @throws LookupException
     *            A cross-reference to a concept could not be resolved.
     */
    public List<Concept> concepts() throws LookupException {
        List<Concept> result = new ArrayList<>(_concepts.size());
        for (CrossReference<? extends Concept> concept : conceptReferences()) {
            result.add(concept.getElement());
        }
        return result;
    }

    /**
     * @return the list of primary actors of this use case.
     * @throws LookupException
     *            A cross-reference to an actor could not be resolved.
     */
    @Ensures({"result != null", "primaryActorReferences().stream().allMatch(r -> result.contains(r.getElement())",
            "result.stream().allMatch(a -> primaryActorReferences().stream().anyMatch(r -> r.getElement() == a))",})
    public List<Actor> primaryActors() throws LookupException {
        List<Actor> result = new ArrayList<>(_primaryActors.size());
        for (CrossReference<? extends Actor> actor : primaryActorReferences()) {
            result.add(actor.getElement());
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * On top of the existing checks, the method checks
     * whether all actors mentioned in the use case are listed
     * as primary actors.
     */
    @Override
    protected Verification verifySelf() {
        Verification result = super.verifySelf();
        result = result.and(verifyActors());
        return result;
    }

    /**
     * Verify that all mentioned actors are part of the list of primary
     * actors.
     *
     * @return A valid verification result if all mentioned actors are part of the
     *         list of primary actors. Otherwise, a problem will be reported
     *         for each reference to an actor that is not a primary actor.
     */
    protected Verification verifyActors() {
        Verification result = Valid.create();
        List<Behavior> behaviors = descendants(Behavior.class);
        try {
            List<Actor> actors = primaryActors();
            for (Behavior behavior : behaviors) {
                try {
                    ExecutingEntity entity = behavior.executor();

                    if (entity instanceof Actor && ! actors.contains(entity)) {
                        result = result.and(new BasicProblem(behavior, "The actor of the step is not a primary actor of the use case."));
                    }
                } catch (LookupException e) {
                    // Nested try block in order to verify as much as possible.

                    // If the lookup of an actor or executing entity goes wrong,
                    // the verification of the cross-reference will report the error.
                    // We do not want the report it twice.
                }
            }
        } catch (LookupException e) {
            // If the lookup of an actor or executing entity goes wrong,
            // the verification of the cross-reference will report the error.
            // We do not want the report it twice.
        }
        return result;
    }
}