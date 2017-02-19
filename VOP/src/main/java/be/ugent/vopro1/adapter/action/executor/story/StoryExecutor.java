package be.ugent.vopro1.adapter.action.executor.story;

import be.ugent.vopro1.adapter.action.ProjectActionAdapter;
import be.ugent.vopro1.adapter.action.executor.AbstractExecutor;
import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.interactor.entity.EntityPermission;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.model.UsecaseEntity;
import be.ugent.vopro1.story.StoryConfig;
import be.ugent.vopro1.story.StoryGenerator;
import org.aikodi.lang.funky.behavior.BranchingPoint;
import org.aikodi.lang.funky.behavior.RepeatLoop;
import org.aikodi.lang.funky.behavior.WhileLoop;

import java.util.Map;
import java.util.function.IntSupplier;

/**
 * Uses the executor interface to generate a story.
 *
 * @see Executor
 * @see ProjectActionAdapter
 */
public class StoryExecutor extends AbstractExecutor {

    private static final String ACTION_TYPE = "story";
    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String NAME_ARG = "name";
    private static final String AUTH_ARG = "auth";
    private static final String ACTION_ARG = "action";
    private EntityPermission entityPermission;
    private CustomWorkspace workspace;

    /**
     * Creates a new StoryExecutor
     * @param adapter {@inheritDoc}
     */
    public StoryExecutor(ProjectActionAdapter adapter) {
        super(adapter, ACTION_TYPE);
        this.entityPermission = PermissionProvider.get("entity");
        this.workspace = WorkspaceFactory.getInstance();
    }

    /**
     * Constructs the {@link StoryConfig} for the given usecase in the given
     * project.
     * <p>
     * Generates a story with the {@link StoryGenerator} using the StoryConfig.
     *
     * @param params {@inheritDoc}
     * @return {@inheritDoc}
     * @throws Exception thrown in {@link ProjectActionAdapter}
     * @see StoryAction
     * @see StoryConfig
     * @see UsecaseEntity
     */
    @Override
    public Result execute(Map<String, String> params) throws Exception {
        String origin = params.get(PROJECT_NAME_ARG);
        String usecaseName = params.get(NAME_ARG);
        StoryAction action = converter.convert(StoryAction.class, params.get(ACTION_ARG));

        // Set up the StoryGenerator based on the user's input
        StoryConfig storyConfig = new StoryConfig();
        storyConfig.addConfig(RepeatLoop.class, noHigherThan(5, action::getNumRepeat));
        storyConfig.addConfig(WhileLoop.class, noHigherThan(5, action::getNumWhile));
        storyConfig.addConfig(BranchingPoint.class, noHigherThan(10, action::getNumBranch));

        StoryGenerator storyGenerator = new StoryGenerator();

        UsecaseEntity usecaseEntity = workspace.getEntityFromProject(usecaseName, UsecaseEntity.class, origin);

        return supplier.get(storyGenerator::getStories, usecaseEntity, storyConfig);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the credentials allow the user to get a usecase from the
     * <code>origin</code> project, this method will return <code>true</code>.
     *
     * @param params {@inheritDoc}
     * @return {@inheritDoc}
     * @throws Exception thrown in {@link ProjectActionAdapter}
     * @see EntityPermission
     */
    @Override
    public boolean canExecute(Map<String, String> params) throws Exception {
        String origin = params.get(PROJECT_NAME_ARG);
        String auth = params.get(AUTH_ARG);

        return entityPermission.canGet(auth, origin);
    }

    /**
     * Ensures that the returned value, based on the supplied integer value is no higher than the maximum
     *
     * @param maximum Maximum value possible, will be returned if the supplied integer value is too high
     * @param intSupplier Function with no arguments of which the returned value will be returned if it
     *                    is acceptable (not higher than the maximum
     * @return supplied integer, or the maximum if it is too high
     */
    private int noHigherThan(int maximum, IntSupplier intSupplier) {
        int supplied = intSupplier.getAsInt();
        return (supplied <= maximum) ? supplied : maximum;
    }
}
