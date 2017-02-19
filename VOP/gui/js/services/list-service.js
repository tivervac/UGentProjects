/**
 * The list service serves as a datastructure containing many lists of concepts,
 * users, use cases and whatnot. Together they include all of the valid references
 * one can insert in any reference applicable input field, such as objective.
 * <p>
 * Lists are not manipulated here, only received and dispatched. The service
 * is best thought of as a datatype available to all controllers. Whereas specific
 * controllers can generally only access some of these lists without this service.
 */
var listService = angular.module('listService', []);

listService.service('List', function() {

    var projectList = [];
    var wholeProjectList = [];
    var actorList = [];
    var wholeActorList = [];
    var useCaseList = [];
    var wholeUseCaseList = [];
    var conceptList = [];
    var wholeConceptList = [];
    var wholeTeamList = [];
    var teamList = [];
    var wholeUserList = [];
    var userList = [];
    var wholeProcessList = [];
    var processList = [];

    /**
     * Sets list of all projects.
     */
     var setProjectList = function(list) {
         projectList = list;
     };

    /**
     * Gets list of all projects in team.
     */
     var getProjectList = function() {
         return projectList;
     };

    /**
     * Sets list of ALL projects.
     */
     var setWholeProjectList = function(list) {
         wholeProjectList = list;
         projectList = list;
     };

    /**
     * Gets all projects.
     */
    var getWholeProjectList = function() {
        return wholeProjectList;
    };

    /**
     * Sets list of all concepts.
     */
    var setConceptList = function(list) {
        conceptList = list;
    };

    /**
     * Gets list of all concepts.
     */
    var getConceptList = function() {
        return conceptList;
    };

    /**
     * Sets list of ALL concepts.
     */
    var setWholeConceptList = function(list) {
        wholeConceptList = list;
        conceptList = list;
    };

    /**
     * Get all concpets.
     */
    var getWholeConceptList = function() {
        return wholeConceptList;
    };

    /**
     * Sets list of all use cases.
     */
    var setUseCaseList = function(list) {
        useCaseList = list;
    };

    /**
     * Gets list of use cases.
     */
    var getUseCaseList = function() {
        return useCaseList;
    };

    /**
     * Sets list of ALL use cases.
     */
    var setWholeUseCaseList = function(list) {
        wholeUseCaseList = list;
        useCaseList = list;
    };

    /**
     * Gets list of ALL use cases.
     */
    var getWholeUseCaseList = function() {
        return wholeUseCaseList;
    };

    /**
     * Sets list of all actors.
     */
    var setActorList = function(list) {
        actorList = list;
    };

    /**
     * Gets list of all actors.
     */
    var getActorList = function() {
        return actorList;
    };

    /**
     * Sets list of ALL actors.
     */
    var setWholeActorList = function(list) {
        wholeActorList = list;
        actorList = list;
    };

    /**
     * Gets list of ALL actors.
     */
    var getWholeActorList = function() {
        return wholeActorList;
    };

    /**
     * Sets list of all teams.
     */
    var setTeamList = function(list) {
        teamList = list;
    };

    /**
     * gets list of all teams.
     */
    var getTeamList = function() {
        return teamList;
    };

    /**
     * Sets list of ALL teams.
     */
    var setWholeTeamList = function(list) {
        wholeTeamList = list;
        teamList = list;
    };

    /**
     * Gets list of ALL teams.
     */
    var getWholeTeamList = function() {
        return wholeTeamList;
    };

    /**
     * Sets list of all users.
     */
    var setUserList = function(list) {
        userList = list;
    };

    /**
     * gets list of all users.
     */
    var getUserList = function() {
        return userList;
    };

    /**
     * Sets list of ALL users.
     */
    var setWholeUserList = function(list) {
        wholeUserList = list;
        userList = list;
    };

    /**
     * Gets list of ALL users.
     */
    var getWholeUserList = function() {
        return wholeUserList;
    };

    /**
     * Sets list of all processes.
     */
    var setProcessList = function(list) {
        processList = list;
    };

    /**
     * Gets list of all processes in team.
     */
    var getProcessList = function() {
        return processList;
    };

    /**
     * Sets list of ALL processes.
     */
    var setWholeProcessList = function(list) {
        wholeProcessList = list;
        processList = list;
    };

    /**
     * Gets all processes.
     */
    var getWholeProcessList = function() {
        return wholeProcessList;
    };

    return {
        setActorList: setActorList,
        getActorList: getActorList,
        setWholeActorList: setWholeActorList,
        getWholeActorList: getWholeActorList,
        setUseCaseList: setUseCaseList,
        getUseCaseList: getUseCaseList,
        setWholeUseCaseList: setWholeUseCaseList,
        getWholeUseCaseList: getWholeUseCaseList,
        setConceptList: setConceptList,
        getConceptList: getConceptList,
        setWholeConceptList: setWholeConceptList,
        getWholeConceptList: getWholeConceptList,
        setProjectList: setProjectList,
        getProjectList: getProjectList,
        setWholeProjectList: setWholeProjectList,
        getWholeProjectList: getWholeProjectList,
        setWholeTeamList: setWholeTeamList,
        getWholeTeamList: getWholeTeamList,
        getTeamList: getTeamList,
        setTeamList: setTeamList,
        setWholeUserList: setWholeUserList,
        getWholeUserList: getWholeUserList,
        getUserList: getUserList,
        setUserList: setUserList,
        setWholeProcessList: setWholeProcessList,
        getWholeProcessList: getWholeProcessList,
        getProcessList: getProcessList,
        setProcessList: setProcessList,
    };

});
