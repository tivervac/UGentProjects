package be.ugent.vopro1.funky;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a CustomWorkspace.
 *
 * @see CustomWorkspace
 * @see org.aikodi.chameleon.workspace.Workspace
 */
public class WorkspaceFactory {

    private static CustomWorkspace instance;

    /**
     * Sets a custom Workspace, for dependency injection and tests
     *
     * @param workspace Workspace to set and return in future
     * {@link #getInstance()} calls
     * @see CustomWorkspace
     */
    public static void setInstance(CustomWorkspace workspace) {
        instance = workspace;
    }

    /**
     * Retrieves the CustomWorkspace instance.
     * <p>
     * The default workspace is based on the "workspace" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return Converter instance
     * @see ApplicationContextProvider
     */
    public static CustomWorkspace getInstance() {
        if (instance == null) {
            setDefault();
        }

        return instance;
    }

    /**
     * Returns the Workspace to it's default instance
     */
    public static void setDefault() {
        instance = ApplicationContextProvider.getApplicationContext().getBean("workspace", CustomWorkspace.class);
    }

}
