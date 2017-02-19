package be.ugent.vopro1.util;

import be.ugent.vopro1.rest.controller.RestController;
import be.ugent.vopro1.rest.mapper.WebMapperProvider;

/**
 * Contains all local constants used in the application.
 */
public class LocalConstants {

    /**
     * Maximum number of results on a page
     */
    public static final int PAGE = 15;

    /**
     * Homepage for the API endpoints
     */
    public static final String HOME = "http://vopro1.ugent.be/api";

    /**
     * Special URL for checking user credentials
     */
    public static final String LOGIN = "/user/me";

    /**
     * Special URL for user registration
     */
    public static final String REGISTER = "/user";

    /**
     * Special URL for some specific endpoints
     *
     * @see be.ugent.vopro1.rest.route.ActorRoute#SELF
     * @see be.ugent.vopro1.rest.route.ConceptRoute#SELF
     * @see be.ugent.vopro1.rest.route.UsecaseRoute#SELF
     */
    public static final String URL = "/{url}";

    /**
     * Maximum number of teams that can be assigned to a single project
     */
    public static final int MAX_TEAMS_PER_PROJECT = 1;

    /**
     * Mapping mode for WebMapper. Can be <code>hateoas</code> for HATEOAS-enabled results, or
     * <code>normal</code> for normal mapping (no hateoas or other extra features)
     *
     * @see RestController#RestController()
     */
    public static final String MAPPING_MODE = WebMapperProvider.HATEOAS_MAPPING;

    /**
     * The amount of time to wait when trying to schedule a new event when all analysts are busy.
     * This is the time in minutes.
     */
    public static final long SCHEDULE_RETRY_TIME = 60;

    /**
     * The time signaling the start of the day for an analyst, in minutes.
     */
    public static final long START_WORK_DAY = 9 * 60;

    /**
     * The time signaling the end of the day for an analyst, in minutes.
     */
    public static final long STOP_WORK_DAY = 17 * 60;

}
