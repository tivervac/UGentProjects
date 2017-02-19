/**
 * HATEOAS service, keeping hardcoded URL's to a minimum.
 * Uses maps to be able to use HATEOAS for Projects, Usecases, Concepts
 * and Teams. These maps map the name on the object containing all the needed
 * links for the project/usecase/concept/team. In this way, these HATEOAS links
 * can be easily propagated and used across different controllers.
 */
var hateoasService = angular.module('hateoasService', []);

hateoasService.service('Hateoas', function() {

    var teamMap;
    var projectMap;
    var conceptMap = new Object();
    var usecaseMap = new Object();
    var actorMap = new Object();
    var processMap = new Object();

    /**
     * Removes elements from the project mapping.
     */
    var cleanProjectMap = function() {
        projectMap = new Object();
    };

    /**
     * Removes elements from the team mapping.
     */
    var cleanTeamMap = function() {
        teamMap = new Object();
    };

    /**
     * Removes elements from the concept mapping.
     */
    var cleanConceptMap = function(projectName) {
        conceptMap[projectName] = new Object();
    };

    /**
     * Removes elements from the use case mapping.
     */
    var cleanUsecaseMap = function(projectName) {
        usecaseMap[projectName] = new Object();
    };

    /**
     * Removes elements from the actor mapping.
     */
    var cleanActorMap = function(projectName) {
        actorMap[projectName] = new Object();
    };

    /**
     * Removes elements from the process mapping.
     */
    var cleanProcessMap = function(projectName){
        processMap[projectName] = new Object();
    }

    /**
     * Adds elements to the project mapping.
     */
    var setProjectInMap = function(key, value) {
        projectMap[key] = value;
    };

    /**
     * Gets element from the project mapping.
     */
    var getProjectInMap = function(key) {
        return projectMap[key];
    };

    /**
     * Adds elements to the team mapping.
     */
    var setTeamInMap = function(key, value) {
        teamMap[key] = value;
    };

    /**
     * Gets element from the team mapping.
     */
    var getTeamInMap = function(key) {
        return teamMap[key];
    };

    /**
     * Adds elements to the concept mapping.
     */
    var setConceptInMap = function(projectName, key, value) {
        conceptMap[projectName][key] = value;
    };

    /**
     * Gets element from the concept mapping.
     */
    var getConceptInMap = function(projectName, key) {
        return conceptMap[projectName][key];
    };

    /**
     * Adds elements to the use case mapping.
     */
    var setUsecaseInMap = function(projectName, key, value) {
        usecaseMap[projectName][key] = value;
    };

    /**
     * Gets element from the use case mapping.
     */
    var getUsecaseInMap = function(projectName, key) {
        return usecaseMap[projectName][key];
    };

    /**
     * Adds elements to the actor mapping.
     */
    var setActorInMap = function(projectName, key, value) {
        actorMap[projectName][key] = value;
    };

    /**
     * Gets element from the actor mapping.
     */
    var getActorInMap = function(projectName, key) {
        return actorMap[projectName][key];
    };

    /**
     * Adds elements to the concept mapping.
     */
    var setProcessInMap = function(projectName, key, value) {
        processMap[projectName][key] = value;
    };

    /**
     * Gets element from the concept mapping.
     */
    var getProcessInMap = function(projectName, key) {
        return processMap[projectName][key];
    };

    return {
        getTeamInMap: getTeamInMap,
        setTeamInMap: setTeamInMap,
        getProjectInMap: getProjectInMap,
        setProjectInMap: setProjectInMap,
        getConceptInMap: getConceptInMap,
        setConceptInMap: setConceptInMap,
        getUsecaseInMap: getUsecaseInMap,
        setUsecaseInMap: setUsecaseInMap,
        getActorInMap: getActorInMap,
        setActorInMap: setActorInMap,
        cleanProjectMap: cleanProjectMap,
        cleanTeamMap: cleanTeamMap,
        cleanConceptMap: cleanConceptMap,
        cleanUsecaseMap: cleanUsecaseMap,
        cleanActorMap: cleanActorMap,
        cleanProcessMap: cleanProcessMap,
        getProcessInMap: getProcessInMap,
        setProcessInMap: setProcessInMap,
    };

});

