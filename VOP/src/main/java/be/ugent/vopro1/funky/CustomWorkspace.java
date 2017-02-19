package be.ugent.vopro1.funky;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.funky.CustomDocumentScanner;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.LazyRootNamespace;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.workspace.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 *
 * @see Workspace
 */
public class CustomWorkspace extends Workspace {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Creates a new CustomWorkspace with given {@link LanguageRepository}
     *
     * @param repository Repository to use
     */
    public CustomWorkspace(LanguageRepository repository) {
        super(repository);
        ProjectDAO projectDAO = DAOProvider.get("project");

        for (PersistentProject project : projectDAO.getAll()) {
            try {
                createProject(project.getName());
            } catch (ProjectException e) {
                logger.error("Failed to create a funky project for " + project.getName(), e);
            }
        }
    }

    /**
     * Creates a new Project.
     *
     * @param projectName Name of the project to create
     * @throws ProjectException if creation of the project fails
     * @see View
     * @see CustomDocumentScanner
     */
    public void createProject(String projectName) throws ProjectException {
        // New namespace, with 3 subnamespaces for the entities
        RootNamespace root = new LazyRootNamespace();
        root.getOrCreateNamespace("");

        // New view, existing language
        View view = new View(root, languageRepository().languages().get(0));

        // New scanner
        CustomDocumentScanner scanner = new CustomDocumentScanner(projectName);

        view.addSource(scanner);

        // Add this to the list of active projects
        Project project = new Project(projectName, null, view);
        this.add(project);
        project.flushSourceCache();
    }

    /**
     * Changes the name of an existing project loaded in funky
     * @param oldName the old name of a project
     * @param newName the new name of a project
     * @throws LookupException if the project does not exist
     */
    public void renameProject(String oldName, String newName) throws LookupException {
        Project p = getProject(oldName);
        p.setName(newName);
    }

    /**
     * Adds a new Entity to a Project, based on the entity's identifier and the
     * project name.
     *
     * @param entityID Identifier of the entity
     * @param projectName Name of the project
     * @throws LookupException if the project does not exist
     * @see CustomDocumentScanner
     */
    public void addEntityToProject(int entityID, String projectName) throws LookupException {
        CustomDocumentScanner scanner = getScanner(projectName);
        scanner.addToModel(entityID);
    }

    /**
     * Fetches the correct Project, removes a DocumentLoader for the given id.
     *
     * @param entityId The id of the object from which the DocumentLoader needs
     * to be removed from the scanner.
     * @param projectName The name of the project from which we need to retrieve the scanner.
     * @throws LookupException if the project does not exist
     * @see CustomDocumentScanner
     */
    public void removeEntityFromProject(int entityId, String projectName) throws LookupException {
        CustomDocumentScanner scanner = getScanner(projectName);
        // This call should also clear relevant caches
        scanner.removeFromModel(entityId);
    }

    /**
     * Retrieves an entity from a project
     *
     * @param entityName Name of the entity to retrieve
     * @param entityClass Class of the entity to retrieve
     * @param projectName Project that the entity resides in
     * @param <D> Class, extending {@link Declaration} of the entity to retrieve
     * @return the requested entity
     * @throws LookupException if the project or entity are not found
     */
    public <D extends Declaration> D getEntityFromProject(String entityName, Class<D> entityClass, String projectName) throws LookupException {
        return getProject(projectName).views().get(0).namespace().find(entityName, entityClass);
    }

    /**
     * Helper method to easily retrieve a scanner, given a projectName.
     * 
     * @param projectName The name of the project from which we need to retrieve the scanner.
     * @return the scanner linked to the given projectName.
     * @throws LookupException if the project is not found
     * @see CustomDocumentScanner
     */
    public CustomDocumentScanner getScanner(String projectName) throws LookupException {
        return (CustomDocumentScanner) getProject(projectName).views().get(0).sourceScanners().get(0);
    }
}
