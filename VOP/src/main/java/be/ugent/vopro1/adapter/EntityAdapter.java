package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.interactor.entity.*;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.model.EntityProject;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors.
 * <p>
 * The generic class Adapter communicates with Interactors, its main goal is to
 * accept complex String objects as defined in the REST API and pass the
 * appropriate objects (of generic type E) to the Interactor. Through these
 * interactors the database will be manipulated for instance by storing, editing
 * or removing objects from it. The Adapter class is responsible for directing
 * these objects to the right Interactor and calling the operation suited for
 * the required database operation.
 *
 * @param <E> the generic type, either Concept, Actor or a Usecase
 * @see EntityInteractor
 * @see Result
 */
public class EntityAdapter<E extends Declaration> implements CommonAdapter {

    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String ENTITY_NAME_ARG = "name";
    private static final String AUTH_ARG = "auth";
    private static final String BLOB_ARG = "blob";
    private static final String REFACTOR_ARG = "refactor";

    // A converter to convertToString json to objects
    private JsonConverter converter;
    // Class object storing the class of the generic type
    private Class<E> type;
    private String typeName;

    // Interactor to call database operations on
    private EntityInteractor<E> interactor;
    // Interactor to check for permission
    private EntityPermission permissionHandler;

    private ResultMapper resultMapper;
    private ResultSupplier supplier;

    /**
     * Constructor of a generic Adapter.
     *
     * @param type defines the class (Entity) for which the adapter will work
     * @param typeName the class name, as defined by type, in lowercase
     * @see Declaration
     */
    @SuppressWarnings("rawtypes")
    public EntityAdapter(Class<E> type, String typeName) {
        // save type for map access in method body
        this.type = type;
        this.typeName = typeName;

        interactor = InteractorFactory.getInstance();
        converter = JsonConverterFactory.getInstance();
        supplier = ResultSupplierFactory.getInstance();
        permissionHandler = PermissionProvider.get("entity");
        resultMapper = ResultMapperFactory.getInstance();
    }

    /**
     * Adds an object of type E to the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     * @see EntityProject
     */
    @Override
    public Result add(Map<String, String> args) {
        Optional<String> projectName = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));
        Optional<String> blob = Optional.ofNullable(args.get(BLOB_ARG));

        try {
            if (!permissionHandler.canAdd(auth.orElse(""), projectName.get())){
                return new Result<>(new NoPermissionResult());
            }
            
            E el = converter.convert(type, blob.get());
            return supplier.get(interactor::add, projectName.get(), el);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    /**
     * Removes an object of type E from the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     * @see EntityProject
     */
    @Override
    public Result remove(Map<String, String> args) {
        Optional<String> projectName = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));
        Optional<String> entityName = Optional.ofNullable(args.get(ENTITY_NAME_ARG));

        try {
            if (!permissionHandler.canRemove(auth.orElse(""), projectName.get())) {
                return new Result<>(new NoPermissionResult());
            }

            E el = type.newInstance();
            el.setName(entityName.get());

            return supplier.consume(interactor::remove, projectName.get(), el);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    /**
     * Retrieves an object of type E from the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     * @see EntityProject
     */
    @Override
    public Result get(Map<String, String> args) {
        Optional<String> projectName = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));
        Optional<String> entityName = Optional.ofNullable(args.get(ENTITY_NAME_ARG));

        try {
            if (!permissionHandler.canGet(auth.orElse(""), projectName.get())){
                return new Result<>(new NoPermissionResult());
            }

            E el = type.newInstance();
            el.setName(entityName.get());

            return supplier.get(interactor::get, projectName.get(), el);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    /**
     * Retrieves all objects of type E from the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     * @see EntityProject
     */
    @Override
    public Result getAll(Map<String, String> args) {
        Optional<String> projectName = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));
        Optional<String> name = Optional.ofNullable(args.get(ENTITY_NAME_ARG));

        try {
            if (!permissionHandler.canSearch(auth.orElse(""), projectName.orElse(""))){
                return new Result<>(new NoPermissionResult());
            }

            if (name.isPresent()) {
                // Search query, no project
                return supplier.get(interactor::find, null, name.get(), typeName);
            } else {
                return supplier.get(interactor::getAll, projectName.orElse(""), typeName);
            }
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Updates an object of type E in the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     * @see EntityProject
     */
    @Override
    public Result edit(Map<String, String> args) {
        Optional<String> projectName = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));
        Optional<String> entityName = Optional.ofNullable(args.get(ENTITY_NAME_ARG));
        Optional<String> blob = Optional.ofNullable(args.get(BLOB_ARG));
        Optional<String> refactor = Optional.ofNullable(args.get(REFACTOR_ARG));

        try {
            if (!permissionHandler.canEdit(auth.orElse(""), projectName.get())){
                return new Result<>(new NoPermissionResult());
            }

            E old = type.newInstance();
            old.setName(entityName.get());

            E el = converter.convert(type, blob.get());
            RefactorStrategy refactorStrategy = new NoRefactorStrategy();

            if (refactor.orElse("false").equals("true")) {
                refactorStrategy = new SimpleRefactorStrategy(old, el, projectName.get());
            }

            return supplier.get(interactor::edit, projectName.get(), old, el, refactorStrategy);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    /**
     * Retrieves the type of entity that this adapter can be used for.
     *
     * @return Type of entity that this adapter can be used for
     */
    @Override
    public Class getType() {
        return this.type;
    }
}
