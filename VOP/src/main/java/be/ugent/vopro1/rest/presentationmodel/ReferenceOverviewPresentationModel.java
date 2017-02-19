package be.ugent.vopro1.rest.presentationmodel;

import com.google.common.collect.Lists;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * PresentationModel for the overview of references to an entity
 */
public class ReferenceOverviewPresentationModel extends PresentationModel {

    Map<String, List<Declaration>> references;
    List<ActorPresentationModel> actorReferences = new ArrayList<>();
    List<ConceptPresentationModel> conceptReferences = new ArrayList<>();
    List<UsecasePresentationModel> useCaseReferences = new ArrayList<>();
    List<ProcessPresentationModel> processReferences = new ArrayList<>();

    @Override
    public void setEntity(Object entity) {
        this.references = (Map<String, List<Declaration>>) entity;
        
        for (Declaration declaration : references.getOrDefault("actor", new ArrayList<>())) {
            ActorPresentationModel model = new ActorPresentationModel();
            model.setEntity(declaration);
            this.actorReferences.add(model);
        }

        for (Declaration declaration : references.getOrDefault("concept", new ArrayList<>())) {
            ConceptPresentationModel model = new ConceptPresentationModel();
            model.setEntity(declaration);
            this.conceptReferences.add(model);
        }

        for (Declaration declaration : references.getOrDefault("usecase", new ArrayList<>())) {
            UsecasePresentationModel model = new UsecasePresentationModel();
            model.setEntity(declaration);
            this.useCaseReferences.add(model);
        }

        for (Declaration declaration : references.getOrDefault("process", new ArrayList<>())) {
            ProcessPresentationModel model = new ProcessPresentationModel();
            model.setEntity(declaration);
            this.processReferences.add(model);
        }
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    /**
     * Returns the list of references to actors
     *
     * @return List of actors
     */
    public List<ActorPresentationModel> getActors() {
        return actorReferences;
    }

    /**
     * Returns the list of references to concepts
     *
     * @return List of concepts
     */
    public List<ConceptPresentationModel> getConcepts() {
        return conceptReferences;
    }

    /**
     * Returns the list of references to use cases
     *
     * @return List of use cases
     */
    public List<UsecasePresentationModel> getUseCases() {
        return useCaseReferences;
    }

    /**
     * Returns the list of references to processes
     *
     * @return List of processes
     */
    public List<ProcessPresentationModel> getProcesses() {
        return processReferences;
    }

    @Override
    public List<Supplier<List<? extends PresentationModel>>> subListPresentationModels() {
        return Lists.newArrayList(this::getActors, this::getConcepts, this::getUseCases, this::getProcesses);
    }
}
