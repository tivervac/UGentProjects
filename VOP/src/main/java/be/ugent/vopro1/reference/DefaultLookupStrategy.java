package be.ugent.vopro1.reference;

import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.model.UsecaseEntity;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.util.exception.Handler;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.executors.Actor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides an implementation of a {@link LookupStrategy}
 * <p>
 * Uses methods provided by the FunctionalAnalysis library to retrieve ancestors and descendants from the
 * {@link org.aikodi.chameleon.workspace.Workspace}
 */
public class DefaultLookupStrategy implements LookupStrategy {

    private static final String FAILED_TO_RETRIEVE_ENTITY = "Failed to retrieve entity \"";
    private static final String IN_PROJECT = "\" in project \"";
    private static BiMap<String, Class<? extends Declaration>> declarations = HashBiMap.create();
    static {
        declarations.put("actor", Actor.class);
        declarations.put("concept", Concept.class);
        declarations.put("usecase", UsecaseEntity.class);
        declarations.put("process", ProcessEntity.class);
    }
    private CustomWorkspace workspace;
    private Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creates a new DefaultLookupStrategy and retrieves the Funky workspace
     */
    public DefaultLookupStrategy() {
        this.workspace = WorkspaceFactory.getInstance();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves ancestors by calling {@link Declaration#findAllReferences(Handler)} and then looping over
     * the results and adding relevant results to the result Map.
     *
     * Will return the following ancestor references:
     * <ul>
     *     <li>"actor" -&gt; {@link Actor}</li>
     *     <li>"concept" -&gt; {@link Concept}</li>
     *     <li>"usecase" -&gt; {@link UsecaseEntity}</li>
     *     <li>"process" -&gt; {@link ProcessEntity}</li>
     * </ul>
     *
     * @param config Configuration for the ancestor lookup, containing at least the following items:
     *               <ul>
     *                  <li>{@link LookupConfig#entityName()}</li>
     *                  <li>{@link LookupConfig#projectName()}</li>
     *                  <li>{@link LookupConfig#clazz()}</li>
     *               </ul>
     * @return {@inheritDoc}
     */
    @Override
    public Map<String, List<? extends Declaration>> findAncestors(LookupConfig config) {
        Map<String, List<? extends Declaration>> referringDeclarations = new HashMap<>();

        try {
            Declaration entity = workspace.getEntityFromProject(config.entityName(), config.clazz(), config.projectName());

            List<CrossReference<?>> references = entity.findAllReferences(Handler.resume());

            for (Class<? extends Declaration> declarationClass : declarations.inverse().keySet()) {
                Set<Declaration> result = new HashSet<>();
                for (CrossReference ref : references){
                    result.addAll(ref.ancestors(declarationClass).stream().collect(Collectors.toList()));
                }

                referringDeclarations.put(declarations.inverse().get(declarationClass), new ArrayList<>(result));
            }
        } catch (LookupException e) {
            logger.debug(FAILED_TO_RETRIEVE_ENTITY + config.entityName() + IN_PROJECT + config.projectName() + "\"", e);
        }

        return referringDeclarations;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Retrieves descendants by calling {@link Element#descendants(Class)} with {@link CrossReference} as class parameter,
     * looping over the results, unwrapping the references and checking if the resulting element is of the right {@link Class}.
     * If it is, the relevant result is added to the result Map.
     *
     * Will return the following descendant references:
     * <ul>
     *     <li>"actor" -&gt; {@link Actor}</li>
     *     <li>"concept" -&gt; {@link Concept}</li>
     *     <li>"usecase" -&gt; {@link UsecaseEntity}</li>
     *     <li>"process" -&gt; {@link ProcessEntity}</li>
     * </ul>
     *
     * @param config Configuration for the descendant lookup, containing at least the following items:
     *               <ul>
     *                  <li>{@link LookupConfig#entityName()}</li>
     *                  <li>{@link LookupConfig#projectName()}</li>
     *                  <li>{@link LookupConfig#clazz()}</li>
     *               </ul>
     * @return {@inheritDoc}
     */
    @Override
    public Map<String, List<? extends Declaration>> findDescendants(LookupConfig config) {
        Map<String, List<? extends Declaration>> referringDeclarations = new HashMap<>();
        for (Class<? extends Declaration> declarationClass : declarations.inverse().keySet()) {
            referringDeclarations.put(declarations.inverse().get(declarationClass), new ArrayList<>());
        }

        try {
            Declaration entity = workspace.getEntityFromProject(config.entityName(), config.clazz(), config.projectName());

            for (Class<? extends Declaration> declarationClass : declarations.inverse().keySet()) {
                Set<Declaration> result = new HashSet<>();
                List<? extends CrossReference> childReferences = entity.descendants(CrossReference.class);

                for (CrossReference reference : childReferences) {
                    if (declarationClass.isInstance(reference.getElement())) {
                        result.add(reference.getElement());
                    }
                }

                referringDeclarations.put(declarations.inverse().get(declarationClass), new ArrayList<>(result));
            }
        } catch (LookupException e) {
            logger.debug(FAILED_TO_RETRIEVE_ENTITY + config.entityName() + IN_PROJECT + config.projectName() + "\"", e);
        }

        return referringDeclarations;
    }
}
