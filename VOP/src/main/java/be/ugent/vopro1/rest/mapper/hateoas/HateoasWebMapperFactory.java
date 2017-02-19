package be.ugent.vopro1.rest.mapper.hateoas;

import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.rest.mapper.AbstractWebMapperFactory;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.presentationmodel.*;
import be.ugent.vopro1.rest.route.*;
import org.apache.tomcat.jni.Proc;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static be.ugent.vopro1.util.LocalConstants.*;

/**
 * WebMapperFactory for the {@link HateoasWebMapper}
 */
public class HateoasWebMapperFactory extends AbstractWebMapperFactory {

    private WebMapper instance;

    public void setInstance(WebMapper mapper) {
        instance = mapper;
    }

    public WebMapper getInstance() {
        if (instance == null) {
            instance = new HateoasWebMapper();
            initialize();
        }

        return instance;
    }

    @Override
    protected void initialize() {
        super.initialize();

        String self = "self";
        String create = "create";
        String patch = "patch";
        String patchNoRefactor = "patch_no_refactor";
        String delete = "delete";
        String analysts = "analysts";
        String analystAdd = "analyst_add";
        String analystRemove = "analyst_remove";
        String all = "all";
        String refactor = "?refactor=true";

        Map<String, String> baseLinks = new HashMap<>();
        baseLinks.put("home", HOME);
        baseLinks.put("login", HOME + LOGIN);
        baseLinks.put("register", HOME + REGISTER);
        baseLinks.put("actor", HOME + ActorRoute.GET_ALL);
        baseLinks.put("project", HOME + ProjectRoute.GET_ALL);
        baseLinks.put("project_team_assignable", HOME + ProjectRoute.GET_ALL_TEAM_ASSIGNABLE);
        baseLinks.put("usecase", HOME + UsecaseRoute.GET_ALL);
        baseLinks.put("concept", HOME + ConceptRoute.GET_ALL);
        baseLinks.put("user", HOME + UserRoute.GET_ALL);
        baseLinks.put("team", HOME + TeamRoute.GET_ALL);

        Map<String, String> actorLinks = new HashMap<>();
        actorLinks.put(all, HOME + ActorRoute.GET_ALL);

        Map<String, String> projectLinks = new HashMap<>();
        projectLinks.put(create, HOME + ProjectRoute.POST);
        projectLinks.put(all, HOME + ProjectRoute.GET_ALL);
        projectLinks.put("team_assignable", HOME + ProjectRoute.GET_ALL_TEAM_ASSIGNABLE);

        Map<String, String> usecaseLinks = new HashMap<>();
        usecaseLinks.put(all, HOME + UsecaseRoute.GET_ALL);

        Map<String, String> conceptLinks = new HashMap<>();
        conceptLinks.put(all, HOME + ConceptRoute.GET_ALL);

        Map<String, String> processLinks = new HashMap<>();
        processLinks.put(all, HOME + ProcessRoute.GET_ALL);

        Map<String, String> userLinks = new HashMap<>();
        userLinks.put(create, HOME + UserRoute.POST);
        userLinks.put(all, HOME + UserRoute.GET_ALL);

        Map<String, String> teamLinks = new HashMap<>();
        teamLinks.put(create, HOME + TeamRoute.POST);
        teamLinks.put(all, HOME + TeamRoute.GET_ALL);

        Map<String, String> sBaseLinks = new HashMap<>();
        sBaseLinks.put(self, HOME);

        Map<String, String> sActorLinks = new HashMap<>();
        sActorLinks.put(patch, HOME + ActorRoute.PATCH + refactor);
        sActorLinks.put(patchNoRefactor, HOME + ActorRoute.PATCH);
        sActorLinks.put(delete, HOME + ActorRoute.DELETE);
        sActorLinks.put(self, HOME + ActorRoute.SELF);

        Map<String, String> sConceptLinks = new HashMap<>();
        sConceptLinks.put(patch, HOME + ConceptRoute.PATCH + refactor);
        sConceptLinks.put(patchNoRefactor, HOME + ConceptRoute.PATCH);
        sConceptLinks.put(delete, HOME + ConceptRoute.DELETE);
        sConceptLinks.put(self, HOME + ConceptRoute.SELF);

        Map<String, String> sProjectLinks = new HashMap<>();
        sProjectLinks.put(patch, HOME + ProjectRoute.PATCH);
        sProjectLinks.put(delete, HOME + ProjectRoute.DELETE);
        sProjectLinks.put("actors", HOME + ProjectRoute.GET_ACTOR_ALL);
        sProjectLinks.put("concepts", HOME + ProjectRoute.GET_CONCEPT_ALL);
        sProjectLinks.put("usecases", HOME + ProjectRoute.GET_USECASE_ALL);
        sProjectLinks.put("processes", HOME + ProjectRoute.GET_PROCESS_ALL);
        sProjectLinks.put("actor_add", HOME + ProjectRoute.POST_ACTOR);
        sProjectLinks.put("concept_add", HOME + ProjectRoute.POST_CONCEPT);
        sProjectLinks.put("usecase_add", HOME + ProjectRoute.POST_USECASE);
        sProjectLinks.put(analysts, HOME + ProjectRoute.GET_ANALYST_ALL);
        sProjectLinks.put("eligible_analyst", HOME + ProjectRoute.GET_ELIGIBLE_ANALYST_ALL);
        sProjectLinks.put(analystAdd, HOME + ProjectRoute.POST_ANALYST);
        sProjectLinks.put(analystRemove, HOME + ProjectRoute.DELETE_ANALYST);
        sProjectLinks.put(self, HOME + ProjectRoute.SELF);
        sProjectLinks.put("schedule", HOME + ProjectRoute.ACTION_SCHEDULE);
        sProjectLinks.put("process_add", HOME + ProjectRoute.POST_PROCESS);
        sProjectLinks.put("process_remove", HOME + ProjectRoute.DELETE_PROCESS);

        Map<String, String> sTeamLinks = new HashMap<>();
        sTeamLinks.put("projects", HOME + TeamRoute.PROJECT_GET_ALL);
        sTeamLinks.put(delete, HOME + TeamRoute.DELETE);
        sTeamLinks.put(patch, HOME + TeamRoute.PATCH);
        sTeamLinks.put("members", HOME + TeamRoute.MEMBER_GET_ALL);
        sTeamLinks.put("team_analysts", HOME + TeamRoute.ANALYST_GET_ALL);
        sTeamLinks.put("member_post", HOME + TeamRoute.MEMBER_POST);
        sTeamLinks.put("member_delete", HOME + TeamRoute.MEMBER_DELETE);
        sTeamLinks.put("project_post", HOME + TeamRoute.PROJECT_POST);
        sTeamLinks.put("project_delete", HOME + TeamRoute.PROJECT_DELETE);
        sTeamLinks.put(self, HOME + TeamRoute.SELF);

        Map<String, String> sUsecaseLinks = new HashMap<>();
        sUsecaseLinks.put(patch, HOME + UsecaseRoute.PATCH + refactor);
        sUsecaseLinks.put(patchNoRefactor, HOME + UsecaseRoute.PATCH);
        sUsecaseLinks.put(delete, HOME + UsecaseRoute.DELETE);
        sUsecaseLinks.put(self, HOME + UsecaseRoute.SELF);
        String analystUserId = "/analyst/{userId}";
        sUsecaseLinks.put(analystAdd, HOME + UsecaseRoute.SELF + analystUserId);
        sUsecaseLinks.put(analystRemove, HOME + UsecaseRoute.SELF + analystUserId);
        sUsecaseLinks.put(analysts, HOME +  UsecaseRoute.SELF + "/analyst");
        String task = "/task";
        sUsecaseLinks.put("task", HOME + UsecaseRoute.SELF + task);
        sUsecaseLinks.put("task_post", HOME + UsecaseRoute.SELF + task);
        sUsecaseLinks.put("task_delete", HOME + UsecaseRoute.SELF + task);
        sUsecaseLinks.put("task_patch", HOME + UsecaseRoute.SELF + task);

        Map<String, String> sProcessLinks = new HashMap<>();
        sProcessLinks.put(patch, HOME + ProcessRoute.PATCH + refactor);
        sProcessLinks.put(patchNoRefactor, HOME + ProcessRoute.PATCH);
        sProcessLinks.put(delete, HOME + ProcessRoute.DELETE);
        sProcessLinks.put(self, HOME + ProcessRoute.SELF);

        Map<String, String> sUserLinks = new HashMap<>();
        sUserLinks.put("analyst_projects", HOME + UserRoute.ANALYST_GET_ALL);
        sUserLinks.put("teams", HOME + UserRoute.TEAM_GET_ALL);
        sUserLinks.put(patch, HOME + UserRoute.PATCH);
        sUserLinks.put(delete, HOME + UserRoute.DELETE);
        sUserLinks.put(self, HOME + UserRoute.SELF);

        Map<Class<?>, Map<String, String>> generalLinksMap = new HashMap<>();
        generalLinksMap.put(ApiPresentationModel.class, baseLinks);
        generalLinksMap.put(ActorPresentationModel.class, actorLinks);
        generalLinksMap.put(ConceptPresentationModel.class, conceptLinks);
        generalLinksMap.put(ProjectPresentationModel.class, projectLinks);
        generalLinksMap.put(TeamPresentationModel.class, teamLinks);
        generalLinksMap.put(UsecasePresentationModel.class, usecaseLinks);
        generalLinksMap.put(UserPresentationModel.class, userLinks);
        generalLinksMap.put(ListPresentationModel.class, new HashMap<>());
        generalLinksMap.put(ReferenceOverviewPresentationModel.class, new HashMap<>());
        generalLinksMap.put(StringPresentationModel.class, new HashMap<>());
        generalLinksMap.put(StoryPresentationModel.class, new HashMap<>());
        generalLinksMap.put(ProcessPresentationModel.class, processLinks);
        generalLinksMap.put(TaskPresentationModel.class, new HashMap<>());  // TODO extra HATEOAS links?
        generalLinksMap.put(AvailableUserPresentationModel.class, userLinks);
        generalLinksMap.put(SchedulePresentationModel.class, new HashMap<>());
        generalLinksMap.put(AssignmentPresentationModel.class, new HashMap<>());
        generalLinksMap.put(PaginatedListPresentationModel.class, new HashMap<>());
        instance.setGeneralLinksMap(generalLinksMap);

        Map<Class<?>, Map<String, String>> specificLinksMap = new HashMap<>();
        specificLinksMap.put(ApiPresentationModel.class, sBaseLinks);
        specificLinksMap.put(ActorPresentationModel.class, sActorLinks);
        specificLinksMap.put(ConceptPresentationModel.class, sConceptLinks);
        specificLinksMap.put(ProjectPresentationModel.class, sProjectLinks);
        specificLinksMap.put(TeamPresentationModel.class, sTeamLinks);
        specificLinksMap.put(UsecasePresentationModel.class, sUsecaseLinks);
        specificLinksMap.put(UserPresentationModel.class, sUserLinks);
        specificLinksMap.put(ListPresentationModel.class, new HashMap<>());
        specificLinksMap.put(ReferenceOverviewPresentationModel.class, new HashMap<>());
        specificLinksMap.put(StringPresentationModel.class, new HashMap<>());
        specificLinksMap.put(StoryPresentationModel.class, new HashMap<>());
        specificLinksMap.put(ProcessPresentationModel.class, sProcessLinks);
        specificLinksMap.put(TaskPresentationModel.class, new HashMap<>()); // TODO extra HATEOAS links?
        specificLinksMap.put(AvailableUserPresentationModel.class, sUserLinks);
        specificLinksMap.put(SchedulePresentationModel.class, new HashMap<>());
        specificLinksMap.put(AssignmentPresentationModel.class, new HashMap<>());
        specificLinksMap.put(PaginatedListPresentationModel.class, new HashMap<>());
        instance.setSpecificLinksMap(specificLinksMap);
    }
}
