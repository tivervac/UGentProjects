/**
 * The navigation service serves to aid navigation throughout the application,
 * it's main purpose is supporting the 'back' feature.
 * <p>
 * Said feature is supported by storing booleans to keep track of the
 * history. Note that this is not necessarily equivalent to the browser
 * history. The back function returns to the previous point in the hierarchy,
 * rather than the last visited page (as browser history works).
 */

var navService = angular.module('navService', []);

navService.service('Nav', function($route) {

    var createTeam = false;

    var addExistingProjects = false;
    var patchTeamLeader = false;

    var usecaseDetails = false;
    var createActorOnUsecase = false;
    var createConceptOnUsecase = false;

    var updateUsecaseAnalysts = false;

    var patchProjectLeader = false;
    var importUsecase = false;

    /**
     * Returns to the previous page in the website hierarchy.
     */
    var back = function() {
        var route = $route.current.$$route.name;

        if (route == 'project-explorer' || route == 'login' || route == 'register') {
            return;
        }
        else if (route == 'all-teams') {
            if (createTeam) {
                createTeam = false;
            }
            else {
                window.history.back();
            }
        }
        else if (route == 'team-details') {
            if (addExistingProjects) {
                addExistingProjects = false;
            }
            else if (patchTeamLeader) {
                patchTeamLeader = false;
            }
            else {
                window.history.back();
            }
        }
        else if (route == 'create-or-update-usecase') {
            if (createActorOnUsecase) {
                createActorOnUsecase = false;
            }
            else if (createConceptOnUsecase) {
                createConceptOnUsecase = false;
            }
            else if (usecaseDetails) {
                usecaseDetails = false;
            }
            else {
                window.history.back();
            }
        }
        else if (route == 'usecase-details') {
            if (updateUsecaseAnalysts) {
                updateUsecaseAnalysts = false;
            }
            else {
                window.history.back();
            }
        }
        else if (route == 'project-details') {
            if (patchProjectLeader) {
                patchProjectLeader = false;
            }
            else if (importUsecase) {
                importUsecase = false
            }
            else {
                window.history.back();
            }
        }
        else {
            window.history.back();
        }
    };

    /**
     * Returns whether location is create team.
     */
    var isCreateTeam = function() {
        return createTeam;
    };

    /**
     * Set location to create team.
     */
    var setCreateTeam = function(value) {
        createTeam = value;
    };

    /**
     * Returns whether location is add existing projects.
     */
    var isAddExistingProjects = function() {
        return addExistingProjects;
    };

    /**
     * Sets location to add existing projects.
     */
    var setAddExistingProjects = function(value) {
        addExistingProjects = value;
    };

    /**
     * Returns whether location is patch team leader.
     */
    var isPatchTeamLeader = function() {
        return patchTeamLeader;
    };

    /**
     * Sets location to patch team leader.
     */
    var setPatchTeamLeader = function(value) {
        patchTeamLeader = value;
    };

    /**
     * Returns whether location is use case details.
     */
    var isUsecaseDetails = function() {
        return usecaseDetails;
    };

    /**
     * Sets location to use case details.
     */
    var setUsecaseDetails = function(value) {
        usecaseDetails = value;
    };

    /**
     * Returns whether location is create actor on use case.
     */
    var isCreateActorOnUsecase = function() {
        return createActorOnUsecase;
    };

    /**
     * Sets location to create actor on use case.
     */
    var setCreateActorOnUsecase = function(value) {
        createActorOnUsecase = value;
    };

    /**
     * Returns whether location is create concept on use case.
     */
    var isCreateConceptOnUsecase = function() {
        return createConceptOnUsecase;
    };

    /**
     * Sets location to create concept on use case.
     */
    var setCreateConceptOnUsecase = function(value) {
        createConceptOnUsecase = value;
    };

    /**
     * Returns whether location is update use case analyst.
     */
    var isUpdateUsecaseAnalysts = function() {
        return updateUsecaseAnalysts;
    };

    /**
     * Sets location to update use case analyst.
     */
    var setUpdateUsecaseAnalysts = function(value) {
        updateUsecaseAnalysts = value;
    };

    /**
     * Returns whether location is patch project leader.
     */
    var isPatchProjectLeader = function() {
        return patchProjectLeader;
    };

    /**
     * Sets current page as patch project leader.
     */
    var setPatchProjectLeader = function(value) {
        patchProjectLeader = value;
    };

    /**
     * Returns whether location is import use case.
     */
    var isImportUsecase = function() {
        return importUsecase;
    };

    /**
     * Sets location to import use case.
     */
    var setImportUsecase = function(value) {
        importUsecase = value;
    };

    return {
        back: back,
        isCreateTeam: isCreateTeam,
        setCreateTeam: setCreateTeam,
        isAddExistingProjects: isAddExistingProjects,
        setAddExistingProjects: setAddExistingProjects,
        isPatchTeamLeader: isPatchTeamLeader,
        setPatchTeamLeader: setPatchTeamLeader,
        isUsecaseDetails: isUsecaseDetails,
        setUsecaseDetails: setUsecaseDetails,
        isCreateActorOnUsecase: isCreateActorOnUsecase,
        setCreateActorOnUsecase: setCreateActorOnUsecase,
        isCreateConceptOnUsecase: isCreateConceptOnUsecase,
        setCreateConceptOnUsecase: setCreateConceptOnUsecase,
        isUpdateUsecaseAnalysts: isUpdateUsecaseAnalysts,
        setUpdateUsecaseAnalysts: setUpdateUsecaseAnalysts,
        isPatchProjectLeader: isPatchProjectLeader,
        setPatchProjectLeader: setPatchProjectLeader,
        isImportUsecase: isImportUsecase,
        setImportUsecase: setImportUsecase
    };

});
