package be.ugent.vopro1.interactor.entity;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.RuntimeLookupException;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.util.exception.Handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple implementation of the refactoring
 * Finds all references to an entity
 * Changes the references
 * Makes changes persistent immediately
 */
public class SimpleRefactorStrategy implements RefactorStrategy{
    private Declaration oldVersion;
    private Declaration newVersion;
    private String projectName;
    private List<CrossReference<?>> references;
    private EntityDAO dao;
    private ConverterFacade converter;

    /**
     * @param oldVersion old entity, must at least contain name
     * @param newVersion new entity
     * @param projectName project name of this entity
     */
    public SimpleRefactorStrategy(Declaration oldVersion, Declaration newVersion,
                                  String projectName){
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
        this.projectName = projectName;
        references = new ArrayList<>();
        dao = DAOProvider.get("entity");
        converter = ConverterFactory.getInstance();
    }

    @Override
    public void prepare() {
        CustomWorkspace workspace = WorkspaceFactory.getInstance();
        try {
            oldVersion = workspace.getEntityFromProject(oldVersion.name(), oldVersion.getClass(), projectName);
            references = oldVersion.findAllReferences(Handler.resume());
        } catch (LookupException e) {
           throw new RuntimeLookupException(e);
        }

    }

    @Override
    public void execute() {
        Set<Declaration> entities = new HashSet<>();

        // Iterate over all references
        for (CrossReference ref : references){

            // Should be a safe cast, only NameReferences should be used
            NameReference nameReference = (NameReference) ref;

            // Update the value
            nameReference.setName(newVersion.name());

            // Find all usecase parents
            List<Declaration> dec = nameReference.ancestors(Declaration.class);
            entities.addAll(dec);
        }

        // Save the changes
        for (Declaration dec : entities){
            PersistentObject oldObj = dao.getByName(dec.name(), projectName);
            PersistentObject obj = converter.convert(dec);
            obj.setId(oldObj.getId());
            dao.update(obj);
        }
    }
}
