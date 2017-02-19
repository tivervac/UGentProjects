package org.aikodi.lang.funky.builder;

import org.aikodi.chameleon.core.factory.Factory;
import org.aikodi.chameleon.core.namespace.LazyRootNamespace;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.workspace.*;
import org.aikodi.lang.funky.language.Funky;


/**
 * A convenience builder for a project. It create a single view for
 * functional analysis.
 * <p>
 * Multiple scanners can be added by attaching them directly to
 * the view using {@link View#addSource(DocumentScanner)} or
 * {@link View#addBinary(DocumentScanner)}, depending on whether
 * to documents form an immutable library, or source documents for the
 * project.
 *
 * @author Marko van Dooren
 */
public class ProjectBuilder {

    /**
     * The view for functional analysis.
     */
    private View _view;

    /**
     * Create a builder for new project with the given name using the
     * language for functional analysis.
     * <p>
     * The view is connected to a project, which is connected to a workspace.
     * The language repository of the workspace contains only the language
     * for functional analysis.
     * <p>
     * A document scanner is created and this scanner is then attached to the {@link #view()}.
     *
     * @param name The name of the project.
     * @throws ProjectException
     */
    public ProjectBuilder(String name) throws ProjectException {
        createProject(name);
    }

    /**
     * Initialize the project with the given name.
     *
     * @param name The name of the project.
     * @throws ProjectException
     */
    protected void createProject(String name) throws ProjectException {
        LanguageRepository repository = new LanguageRepository();
        Funky uml = createLanguage();
        uml.setPlugin(Factory.class, createFactory());
        repository.add(uml);
        Workspace workspace = new Workspace(repository);
        _view = new View(createRootNamespace(), uml);
        Project project = new Project(name, null, _view);
        workspace.add(project);
    }

    /**
     * Create a new  root namespace.
     *
     * @return a new root namespace.
     */
    protected RootNamespace createRootNamespace() {
        return new LazyRootNamespace();
    }

    /**
     * Create a factory for creating generic language constructs.
     *
     * @return A factory for creating generic language constructs.
     */
    protected Factory createFactory() {
        return new Factory();
    }

    /**
     * @return a new language object for the project.
     */
    protected Funky createLanguage() {
        return new Funky();
    }

    /**
     * @return The project that was created.
     */
    public Project project() {
        return view().project();
    }

    /**
     * @return The view for the language for functional analysis.
     */
    public View view() {
        return _view;
    }

}
