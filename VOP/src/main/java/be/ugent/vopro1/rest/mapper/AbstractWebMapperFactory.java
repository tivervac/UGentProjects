package be.ugent.vopro1.rest.mapper;

import be.ugent.vopro1.adapter.result.types.*;
import be.ugent.vopro1.model.*;
import be.ugent.vopro1.rest.presentationmodel.*;
import be.ugent.vopro1.scheduling.Schedule;
import be.ugent.vopro1.util.pagination.PaginatedArrayList;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.virtualmachine.Story;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract factory for {@link WebMapper}s. Has an initialization method
 * that sets some default values for some maps.
 */
public abstract class AbstractWebMapperFactory implements WebMapperFactory {

    @Override
    public abstract WebMapper getInstance();

    @Override
    public abstract void setInstance(WebMapper mapper);

    /**
     * Initializes the WebMapper by setting up the {@link AbstractWebMapper#presentationModelMap},
     * {@link AbstractWebMapper#successStatusMap} and {@link AbstractWebMapper#errorStatusMap}.
     * <p>
     * Other maps will have to initialized by the classes overriding this abstract class.
     * Overriding classes should call <code>super.initialize()</code> so that their required maps
     * can be filled by this method.
     *
     * @see PresentationModel
     * @see HttpStatus
     * @see RequestMethod
     */
    protected void initialize() {
        Map<Class<?>, Class<?>> presentationModelMap  = new HashMap<>();
        presentationModelMap.put(ApiPresentationModel.class, ApiPresentationModel.class);
        presentationModelMap.put(Actor.class, ActorPresentationModel.class);
        presentationModelMap.put(EntityProject.class, ProjectPresentationModel.class);
        presentationModelMap.put(UsecaseEntity.class, UsecasePresentationModel.class);
        presentationModelMap.put(Concept.class, ConceptPresentationModel.class);
        presentationModelMap.put(User.class, UserPresentationModel.class);
        presentationModelMap.put(Team.class, TeamPresentationModel.class);
        presentationModelMap.put(Story.class, StoryPresentationModel.class);
        presentationModelMap.put(ProcessEntity.class, ProcessPresentationModel.class);
        presentationModelMap.put(Task.class, TaskPresentationModel.class);
        presentationModelMap.put(AvailableUser.class, AvailableUserPresentationModel.class);
        presentationModelMap.put(Schedule.class, SchedulePresentationModel.class);
        presentationModelMap.put(Assignment.class, AssignmentPresentationModel.class);
        presentationModelMap.put(PaginatedArrayList.class, PaginatedListPresentationModel.class);
        presentationModelMap.put(ArrayList.class, ListPresentationModel.class);
        presentationModelMap.put(HashMap.class, ReferenceOverviewPresentationModel.class);
        presentationModelMap.put(String.class, StringPresentationModel.class);
        getInstance().setPresentationModelMap(presentationModelMap);

        Map<RequestMethod, HttpStatus> successStatusMap = new HashMap<>();
        successStatusMap.put(RequestMethod.GET, HttpStatus.OK);
        successStatusMap.put(RequestMethod.POST, HttpStatus.CREATED);
        successStatusMap.put(RequestMethod.PATCH, HttpStatus.OK);
        successStatusMap.put(RequestMethod.PUT, HttpStatus.OK);
        successStatusMap.put(RequestMethod.DELETE, HttpStatus.NO_CONTENT);
        getInstance().setSuccessStatusMap(successStatusMap);

        Map<Class<? extends ResultType>, HttpStatus> errorStatusMap = new HashMap<>();
        errorStatusMap.put(NoPermissionResult.class, HttpStatus.FORBIDDEN);
        errorStatusMap.put(WrongCredentialsResult.class, HttpStatus.UNAUTHORIZED);
        errorStatusMap.put(JSONParseErrorResult.class, HttpStatus.BAD_REQUEST);
        errorStatusMap.put(JSONMappingErrorResult.class, HttpStatus.BAD_REQUEST);
        errorStatusMap.put(JSONUnknownErrorResult.class, HttpStatus.BAD_REQUEST);
        errorStatusMap.put(ConditionFailedResult.class, HttpStatus.CONFLICT);
        errorStatusMap.put(DBInvalidQueryResult.class, HttpStatus.BAD_REQUEST);
        errorStatusMap.put(DBAccessFailedResult.class, HttpStatus.INTERNAL_SERVER_ERROR);
        errorStatusMap.put(DBTempFailedResult.class, HttpStatus.INTERNAL_SERVER_ERROR);
        errorStatusMap.put(DBUnknownErrorResult.class, HttpStatus.INTERNAL_SERVER_ERROR);
        errorStatusMap.put(UnknownErrorResult.class, HttpStatus.INTERNAL_SERVER_ERROR);
        getInstance().setErrorStatusMap(errorStatusMap);
    }

}
