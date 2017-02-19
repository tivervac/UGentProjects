package be.ugent.vopro1.util.error;

/**
 * Provides some helpful error messages
 */
public class ErrorMessages {

    public static final String ALREADY_MEMBER = "User is already a member of the team";
    public static final String NOT_ALREADY_MEMBER = "User is not a member of the team";

    public static final String ALREADY_PROJECT_ANALYST = "User is already an analyst for this project";
    public static final String NOT_ALREADY_PROJECT_ANALYST = "User is not an analyst for this project";

    public static final String ALREADY_ENTITY_ANALYST = "User is already an analyst for this entity";
    public static final String NOT_ALREADY_ENTITY_ANALYST = "User is not an analyst for this entity";

    public static final String ALREADY_ASSIGNED = "Team is already assigned to the project";
    public static final String NOT_ALREADY_ASSIGNED = "Team is not assigned to the project";

    public static final String OVER_ASSIGNMENT_LIMIT = "Project has reached the maximum number of assigned teams";

    public static final String TEAM_MEMBER_REQUIRED = "User should be a member of a team assigned to the project";

    public static final String PROJECT_ANALYST_REQUIRED = "User should be a project analyst of the project containing this entity";

    public static final String ENTITY_ALREADY_EXISTS = "Entity with that name already exists within the project";
    public static final String ENTITY_DOES_NOT_EXIST = "Entity with that name does not exist within the project";

    public static final String PROJECT_ALREADY_EXISTS = "Project with that name already exists";
    public static final String PROJECT_DOES_NOT_EXIST = "Project with that name does not exist";

    public static final String USER_ALREADY_EXISTS = "User with that e-mail already exists";
    public static final String USER_DOES_NOT_EXIST = "User does not exist";
    
    public static final String TEAM_ALREADY_EXISTS = "Team with that name already exists";
    public static final String TEAM_DOES_NOT_EXIST = "Team does not exist";

    private ErrorMessages() {}
}
