package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.interactor.authentication.AuthenticationFactory;
import be.ugent.vopro1.interactor.authentication.HeaderAuthenticationHandler;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.user.UserInteractor;
import be.ugent.vopro1.interactor.user.UserInteractorFactory;
import be.ugent.vopro1.interactor.user.UserPermission;
import be.ugent.vopro1.model.User;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for User objects.
 * <p>
 * The class UserAdapter communicates with Interactors, it's main goal is to
 * accept complex String objects as defined in the REST API and pass the
 * appropriate objects to the correct Interactor. Through these interactors the
 * database will be manipulated for instance by storing, editing or removing
 * objects from it. The TeamAdapter class is responsible for directing these
 * objects to the right Interactor and calling the operation suited for the
 * required database operation. This class mainly handles Team objects.
 *
 * @see AdapterManager
 * @see UserInteractor
 * @see User
 * @see Result
 */
public class UserAdapter implements CommonAdapter {

    private static final String AUTH_ARG = "auth";
    private static final String ID_ARG = "id";
    private static final String BLOB_ARG = "blob";
    private UserPermission permissionHandler;
    private HeaderAuthenticationHandler authenticationHandler;
    private UserInteractor interactor;
    private JsonConverter converter;
    private ResultSupplier supplier;
    private ResultMapper resultMapper;

    /**
     * Constructs a UserAdapter
     *
     * @see UserInteractorFactory
     * @see JsonConverterFactory
     * @see ResultSupplierFactory
     */
    public UserAdapter() {
        interactor = UserInteractorFactory.getInstance();
        converter = JsonConverterFactory.getInstance();
        supplier = ResultSupplierFactory.getInstance();
        permissionHandler = PermissionProvider.get("user");
        authenticationHandler = AuthenticationFactory.getInstance("header");
        resultMapper = ResultMapperFactory.getInstance();
    }

    /**
     * Retrieves a User from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see UserInteractor
     * @see User
     * @see Result
     */
    @Override
    public Result get(Map<String, String> params) {
        Optional<String> idString = Optional.ofNullable(params.get(ID_ARG));
        Optional<String> authString = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> userEmailString = Optional.ofNullable(params.get("user_email"));

        try {
            String id = idString.get();
            String auth = authString.orElse("");
            String userEmail = userEmailString.orElse("");

            if (!userEmail.isEmpty()) {
                return new Result<>(new SuccessResult(),
                        interactor.getWorkhours(userEmail, params.get("project_name")));
            }
            if (id.equals("me")) {
                if (!permissionHandler.canGetSelf(auth)) {
                    return new Result<>(new NoPermissionResult());
                }

                return supplier.get(interactor::getUser, authenticationHandler.getEmail(auth));
            } else {
                int userId = Integer.valueOf(id);
                if (!permissionHandler.canGet(auth, userId)) {
                    return new Result<>(new NoPermissionResult());
                }

                return supplier.get(interactor::getUser, userId);
            }
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }

    }

    /**
     * Retrieves all Users from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see UserInteractor
     * @see User
     * @see Result
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            if (!permissionHandler.canGetAll(auth.orElse(""))) {
                return new Result<>(new NoPermissionResult());
            }

            return supplier.supply(interactor::getAllUsers);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Adds a User to the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see UserInteractor
     * @see User
     * @see Result
     */
    @Override
    public Result add(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> blob = Optional.ofNullable(params.get(BLOB_ARG));

        try {
            if (!permissionHandler.canAdd(auth.orElse(""))) {
                return new Result<>(new NoPermissionResult());
            }

            User entity = converter.convert(User.class, blob.get());
            return supplier.get(interactor::addUser, entity);
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Removes a User from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see UserInteractor
     * @see User
     * @see Result
     */
    @Override
    public Result remove(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> idString = Optional.ofNullable(params.get(ID_ARG));

        try {
            if (!permissionHandler.canRemove(auth.orElse(""))) {
                return new Result<>(new NoPermissionResult());
            }

            int id = Integer.valueOf(idString.get());
            return supplier.consume(interactor::removeUser, id);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Edits a User in the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     */
    @Override
    public Result edit(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> idString = Optional.ofNullable(params.get(ID_ARG));
        Optional<String> blob = Optional.ofNullable(params.get(BLOB_ARG));

        try {
            int id = Integer.valueOf(idString.get());

            if (!permissionHandler.canEdit(auth.orElse(""), id)) {
                return new Result<>(new NoPermissionResult());
            }

            User entity = converter.convert(User.class, blob.get());

            if (permissionHandler.canUpgrade(params.get(AUTH_ARG))) {
                // Authenticated user is an admin, he can change admin/analyst status
                return supplier.get(interactor::upgradeUser, id, entity);
            } else {
                // Authenticated user is not an admin, he cannot change admin/analyst status
                return supplier.get(interactor::editUser, id, entity);
            }
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The UserAdapter overrides this method for retrieval of the identifier based on the
     * unique user e-mail address.
     *
     * @param email E-mail address of the user to retrieve identifier for
     * @return The unique identifier, if an entity with the unique name is found
     */
    @Override
    public Optional<Integer> getId(String email) {
        if (email == null) {
            return Optional.empty();
        } else {
            return interactor.getId(email);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return Type of object that this adapter can be used for
     */
    @Override
    public Class getType() {
        return User.class;
    }
}
