/**
 * General controller for navigation and to handle stuff across the entire
 * website. General controller gets called in multiple seperate html
 * files making it a unique controller in the project. The controller mostly
 * contains variables for html viewing options and navigation handling.
 */
var generalController = angular.module('generalController', []);

generalController.controller('generalCtrl', ['$scope', '$route',  'List', 'Auth', '$timeout', 'Dialog', 'Format', 'Path', 'Nav', function($scope, $route, List, Auth, $timeout, Dialog, Format, Path, Nav) {

    var self = this;

    self.currentProject = "";

    self.createTeam = false;
    self.createProject = false;
    self.createActor = false;

    self.query = "";

    self.loggedIn = false;

    /**
     * Sets the current working project.
     */
    self.setProject = function(project) {
        self.currentProject = project;
    };

    /**
     * Updates variables for effective query searches.
     */
    self.update = function() {
        self.createActor = false;
        self.createProject = false;
        self.loggedIn = true;
        self.query = "";
    };

    /**
     * Gets a list of all projects.
     */
    self.getProjectList = function(teamName) {
        return List.getProjectList()[teamName];
    };

    /**
     * Gets a list of all use cases in current project.
     */
    self.getUseCaseList = function() {
        return List.getUseCaseList();
    };

    /**
     * Gets a list of all concepts in current project.
     */
    self.getConceptList = function() {
        return List.getConceptList();
    };

    /**
     * Gets a list of all actors in current project.
     */
    self.getActorList = function() {
        return List.getActorList();
    };

    /**
     * Gets a list of all processes in current project.
     */
    self.getProcessList = function() {
        return List.getProcessList();
    };

    /**
     * Gets a list of all teams.
     */
    self.getTeamList = function() {
        return List.getTeamList();
    };

    /**
     * Gets a list of all users.
     */
    self.getUserList = function() {
        return List.getUserList();
    };

    /**
     * Help function to filter results. Calling function filters for actors,
     * concepts, projects, use cases and teams based on a search query.
     */
    var filterHelp = function(list, fields) {
        var tmpList = [];
        var ix = 0;
        angular.forEach(list, function(value, key) {
            for (var i = 0; i < fields.length; i++) {
                if ((self.formatToJs(value[fields[i]])).toLowerCase().indexOf(self.formatToJs(self.query).toLowerCase()) >= 0) {
                    tmpList[ix++] = value;
                    break;
                }
            }
        });
        return tmpList;
    };

    /**
     * Filters the content of every list based on a search query to show
     * matching results while user is typing.
     */
    self.filter = function() {
        var newList = new Object();
        for (var team in List.getWholeProjectList()) {
            newList[team] = filterHelp(List.getWholeProjectList()[team], ['name']);
        };

        List.setProjectList(newList);
        List.setTeamList(filterHelp(List.getWholeTeamList(), ['name']));
        List.setUseCaseList(filterHelp(List.getWholeUseCaseList(), ['name']));
        List.setActorList(filterHelp(List.getWholeActorList(), ['name']));
        List.setConceptList(filterHelp(List.getWholeConceptList(), ['name']));
        List.setUserList(filterHelp(List.getWholeUserList(), ['lastName', 'firstName']));
    };

    /**
     * Returns whether a user is an administrator.
     */
    self.isAdmin = function() {
        return Auth.isAdmin();
    };

    /**
     * Returns whether a user is an analyst for a project.
     */
    self.isAnalyst = function(projectName) {
        return Auth.isAnalyst(projectName);
    };

    /**
     * Logs out the current user.
     */
    self.logout = function() {
        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_PRIMARY,
            title: 'Logout ?',
            closable: false,
            cssClass: 'small-dialog',
            message: 'Are you sure you want to logout?',
            buttons: [{
                icon: 'glyphicon glyphicon-ok',
                label: 'Yes',
                cssClass: 'btn btn-success',
                action: function(dialogItself) {
                    Auth.logout();
                    dialogItself.close();
                }
            }, {
                icon: 'glyphicon glyphicon-remove',
                label: 'No',
                cssClass: 'btn btn-danger',
                action: function(dialogItself) {
                    dialogItself.close();
                }
            }]
        });
    };

    /**
     * Refreshes the current page
     */
    self.refresh = function() {
        $route.reload();
		Auth.updateAnalystProjects();
		Auth.updateUserList();
    };

    /**
     * Formatting function delegated to Format service.
     */
    self.formatToHtml = function(str) {
        return Format.formatToHtml(str);
    };

    /**
     * Formatting function delegated to Format service.
     */
    self.formatToJs = function(str) {
        return Format.formatToJs(str);
    };

    /**
     * Returns the manual pdf url.
     */
    self.getManualUrl = function() {
        return Path.getManualUrl();
    };

    /**
     * Clears alert for a given key.
     */
    self.clearAlert = function(key) {
        Dialog.clearAlert(key);
    };

    /**
     * Retrieves alert for a given key.
     */
    self.getAlert = function(key) {
        return Dialog.getAlert(key);
    };

    /**
     * Retrieves the name of the active user (who's logged in).
     */
    self.getName = function() {
        return Auth.getFirstname() + ' ' + Auth.getLastname();
    };

    /**
     * Navigates one page back.
     */
    self.back = function() {
        Nav.back();
    };

    /**
     * Returns whether we're on the create team page.
     */
    self.isCreateTeam = function() {
        return Nav.isCreateTeam();
    };

    /**
     * Sets current page to create team.
     */
    self.setCreateTeam = function(value) {
        Nav.setCreateTeam(value);
    };

    /**
     * Returns whether we're on the add existing projects page.
     */
    self.isAddExistingProjects = function() {
        return Nav.isAddExistingProjects();
    };

    /**
     * Sets current page to add existing projects page.
     */
    self.setAddExistingProjects = function(value) {
        Nav.setAddExistingProjects(value);
    };

    /**
     * Returns whether we're patching the leader.
     */
    self.isPatchTeamLeader = function() {
        return Nav.isPatchTeamLeader();
    };

    /**
     * Sets current page to patch team leader.
     */
    self.setPatchTeamLeader = function(value) {
        Nav.setPatchTeamLeader(value);
    };

    /**
     * Returns whether current page is use case details.
     */
    self.isUsecaseDetails = function() {
        return Nav.isUsecaseDetails();
    };

    /**
     * Sets current page to use cas details.
     */
    self.setUsecaseDetails = function(value) {
        Nav.setUsecaseDetails(value);
    };

    /**
     * Returns whether current page is create actor through the use case page.
     */
    self.isCreateActorOnUsecase = function() {
        return Nav.isCreateActorOnUsecase();
    };

    /**
     * Sets current page as create actor, accessed through the use case page.
     */
    self.setCreateActorOnUsecase = function(value) {
        Nav.setCreateActorOnUsecase(value);
    };

    /**
     * Returns whether current page is create concept, accessed through the use case page.
     */
    self.isCreateConceptOnUsecase = function() {
        return Nav.isCreateConceptOnUsecase();
    };

    /**
     * Sets current page as create concept, accessed through the use case page.
     */
    self.setCreateConceptOnUsecase = function(value) {
        Nav.setCreateConceptOnUsecase(value);
    };

    /**
     * Returns whether current page is update use case analyst.
     */
    self.isUpdateUsecaseAnalysts = function() {
        return Nav.isUpdateUsecaseAnalysts();
    };

    /**
     * Sets current page as update use case analyst.
     */
    self.setUpdateUsecaseAnalysts = function(value) {
        Nav.setUpdateUsecaseAnalysts(value);
    };

    /**
     * Returns whether current page is patch project leader.
     */
    self.isPatchProjectLeader = function() {
        return Nav.isPatchProjectLeader();
    };

    /**
     * Sets current page as patch project leader.
     */
    self.setPatchProjectLeader = function(value) {
        Nav.setPatchProjectLeader(value);
    };

    /**
     * Returns whether current page is import use case.
     */
    self.isImportUsecase = function() {
        return Nav.isImportUsecase();
    };

    /**
     * Sets current page as import use case.
     */
    self.setImportUsecase = function(value) {
        Nav.setImportUsecase(value);
    };

}]);

