package be.ugent.vopro1.interactor.entity;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.RuntimeLookupException;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.persistence.funky.CustomDocumentScanner;
import be.ugent.vopro1.util.RuntimeIOException;
import be.ugent.vopro1.util.error.ErrorMessages;
import be.ugent.vopro1.util.error.RequirementNotMetException;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.workspace.InputException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * An implementation of the {@link EntityInteractor} interface.
 *
 * @param <E> the type of the objects to interact with the database with
 * @see EntityInteractor
 * @see CustomWorkspace
 * @see EntityDAO
 * @see JsonConverter
 * @see EntityProject
 */
public class EntityInteractorImpl<E extends Declaration> implements EntityInteractor<E> {

    private CustomWorkspace workspace;
    private EntityDAO entityDAO;
    private ProjectDAO projectDAO;
    private ConverterFacade converter;

    /**
     * Constructs an Interactor.
     *
     * @see CustomWorkspace
     * @see EntityDAO
     * @see JsonConverter
     * @see EntityProject
     */
    public EntityInteractorImpl() {
        entityDAO = DAOProvider.get("entity");
        projectDAO = DAOProvider.get("project");

        converter = ConverterFactory.getInstance();
        workspace = WorkspaceFactory.getInstance();
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param t {@inheritDoc}
     * @return {@inheritDoc}
     * @see CustomWorkspace
     * @see EntityDAO
     * @see EntityProject
     * @see JsonConverter
     */
    @Override
    public E add(String projectName, E t) {
        try {
            checkNotExists(projectName, t.name());

            PersistentProject proj = projectDAO.getByName(projectName);
            PersistentObject obj = converter.convert(t);
            obj.setProjectId(proj.getId());

            PersistentObject result = entityDAO.save(obj);
            workspace.addEntityToProject(result.getId(), projectName);

            return (E) converter.convert(result);
        } catch (LookupException e) {
            throw new RuntimeLookupException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param t {@inheritDoc}
     * @return {@inheritDoc}
     * @see EntityDAO#exists(String, String)
     */
    @Override
    public boolean exists(String projectName, E t) {
        return entityDAO.exists(t.name(), projectName);
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param t {@inheritDoc}
     * @see CustomWorkspace
     * @see EntityDAO
     * @see EntityProject
     * @see JsonConverter
     */
    @Override
    public void remove(String projectName, E t) {
        checkExists(projectName, t.name());

        PersistentObject obj = entityDAO.getByName(t.name(), projectName);
        entityDAO.deleteById(obj.getId());
        try {
            workspace.removeEntityFromProject(obj.getId(), projectName);
        } catch (LookupException e) {
            throw new RuntimeLookupException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param t {@inheritDoc}
     * @return the requested object
     * @see EntityDAO
     * @see EntityProject
     * @see JsonConverter
     */
    @Override
    public E get(String projectName, E t) {
        checkExists(projectName, t.name());
        
        return (E) converter.convert(entityDAO.getByName(t.name(), projectName));
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param old {@inheritDoc}
     * @param t {@inheritDoc}
     * @return the updated object
     * @see CustomWorkspace
     * @see EntityDAO
     * @see EntityProject
     * @see JsonConverter
     */
    @Override
    public E edit(String projectName, E old, E t, RefactorStrategy refactorStrategy) {
        try {
            checkExists(projectName, old.name());

            refactorStrategy.prepare();

            PersistentObject persOld = entityDAO.getByName(old.name(), projectName);
            PersistentObject persNew = converter.convert(t);
            persNew.setId(persOld.getId());

            if (!old.name().equals(t.name())) {
                checkNotExists(projectName, t.name());
            }

            entityDAO.update(persNew);
            E result = get(projectName, t);

            CustomDocumentScanner scanner = workspace.getScanner(projectName);
            scanner.refreshLoader(persOld.getId());

            refactorStrategy.execute();
            workspace.getProject(projectName).flushSourceCache();

            return result;
        } catch (LookupException e) {
            throw new RuntimeLookupException(e);
        } catch (InputException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param type {@inheritDoc}
     * @return the List of objects
     * @see EntityDAO
     * @see EntityProject
     * @see JsonConverter
     */
    @Override
    public List<E> getAll(String projectName, String type) {
        List<PersistentObject> objects;
        if (projectName == null || projectName.length() <= 1) {
            objects = entityDAO.getAll();
        } else {
            objects = entityDAO.getAllForProject(projectName);
        }

        if (type != null && !type.isEmpty()) {
            objects = objects.stream()
                    .filter(obj -> obj.getType().equals(type))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        try {
            return convertObjects(objects);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param query {@inheritDoc}
     * @return the List of found objects
     * @see EntityDAO
     * @see EntityProject
     * @see JsonConverter
     * @see
     * <a href="http://www.postgresql.org/docs/8.3/static/functions-matching.html#FUNCTIONS-POSIX-REGEXP">Postgresqlregex
     * matching</a>
     */
    @Override
    public List<E> find(String projectName, String query, String type) {
        List<PersistentObject> objects;
        if (projectName == null) {
            objects = entityDAO.find(query);
        } else {
            objects = entityDAO.findInProject(projectName, query);
        }

        if (type != null && !type.isEmpty()) {
            objects = objects.stream()
                    .filter(obj -> obj.getType().equals(type))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        try {
            return convertObjects(objects);
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }
    }

    @Override
    public List<User> getAnalysts(String projectName, String name) {
        checkExists(projectName, name);

        return entityDAO.getAllAnalysts(name, projectName).stream().collect(mapping(converter::convert, toList()));
    }

    @Override
    public void addAnalyst(String projectName, String name, int userId) {
        checkExists(projectName, name);

        if (projectDAO.getAllAnalystsByName(projectName)
                .stream()
                .map(PersistentUser::getId)
                .noneMatch((projectAnalystId) -> projectAnalystId == userId)) {
            // User is not a project analyst, cannot become a entity analyst
            throw new RequirementNotMetException(ErrorMessages.PROJECT_ANALYST_REQUIRED);
        }

        if (entityDAO.getAllAnalysts(name, projectName)
                .stream()
                .map(PersistentUser::getId)
                .anyMatch((entityAnalystId) -> entityAnalystId == userId)) {
            // User is already an analyst for this entity
            throw new RequirementNotMetException(ErrorMessages.ALREADY_ENTITY_ANALYST);
        }

        entityDAO.addAnalyst(name, projectName, userId);
    }

    @Override
    public void removeAnalyst(String projectName, String name, int userId) {
        checkExists(projectName, name);

        if (entityDAO.getAllAnalysts(name, projectName)
                .stream()
                .map(PersistentUser::getId)
                .noneMatch((entityAnalystId) -> entityAnalystId == userId)) {
            // User is not an analyst for this entity
            throw new RequirementNotMetException(ErrorMessages.NOT_ALREADY_ENTITY_ANALYST);
        }

        entityDAO.deleteAnalyst(name, projectName, userId);
    }

    private void checkExists(String projectName, String name) {
        if (!entityDAO.exists(name, projectName)) {
            // Entity name does not exist within the project
            throw new RequirementNotMetException(ErrorMessages.ENTITY_DOES_NOT_EXIST);
        }
    }

    private void checkNotExists(String projectName, String name) {
        if (entityDAO.exists(name, projectName)) {
            // Entity name exists within the project
            throw new RequirementNotMetException(ErrorMessages.ENTITY_ALREADY_EXISTS);
        }
    }

    /**
     * Help method to convert a possibly big list of PersistentProjects into a
     * list of objects of the desired type.
     *
     * @param objects database formatted objects
     * @return a List of objects of the correct type
     * @see JsonConverter
     * @see PersistentObject
     */
    private List<E> convertObjects(List<PersistentObject> objects) throws IOException {
        List<E> entities = new ArrayList<>();

        for (PersistentObject object : objects) {
            entities.add(converter.convert(object));
        }

        return entities;
    }
}
