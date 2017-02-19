'use strict';

/**
 * Define vopro app module and all used dependencies, including our own
 * written controllers, services and directives.
 * The code for every (own written) dependency is included in a seperate
 * file in this js folder.
 */
var voproApp = angular.module('voproApp', [
    'ngRoute',
    'ngSanitize',

    // Controllers
    'generalController',
    'projectExplorerController',
    'projectDetailsController',
    'conceptDetailsController',
    'usecaseDetailsController',
    'actorDetailsController',
    'createOrUpdateConceptController',
    'createOrUpdateUsecaseController',
    'allTeamsController',
    'teamDetailsController',
    'allUsersController',
    'loginController',
    'registerController',
    'updateAnalystsController',
    'usecaseStoriesController',
    'scheduleController',
    'processDetailsController',
    'createOrUpdateProcessController',

    // Services
    'pathService',
    'listService',
    'authService',
    'dialogService',
    'hateoasService',
    'formatService',
    'navService',

    // Directives
    'voproDirectives'
]);

/**
 * Called every time the route in the app changes.
 * Except for accessing login and register page, it checks whether the
 * user is logged in, and if not, it redirects the app to the login page.
 * In this way, it is impossible to visit a page other than login/register
 * when the user of the app is not logged in.
 */
voproApp.run(['$rootScope', '$location', 'Auth', function ($rootScope, $location, Auth) {
    $rootScope.$on('$routeChangeStart', function (event) {
        if($location.path() != '/login' && $location.path() != '/register') {
            if (!Auth.isLoggedIn()) {
                event.preventDefault();
                $location.path('/login');
            }
        }
    });
}]);

/**
 * Define all the routes used in this app.
 * For every route, an html template is defined as well as the
 * controller used for this route. An optional css file can also be
 * provided.
 */
voproApp.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/projects', {
            name: 'project-explorer',
            templateUrl: 'partials/project-explorer.html',
            controller: 'ProjectExplorerCtrl as ctrl'
        }).
        when('/projects/:projectName', {
            name: 'project-details',
            templateUrl: 'partials/project-details.html',
            controller: 'ProjectDetailsCtrl as ctrl',
            css: 'css/alert.css'
        }).
        when('/projects/:projectName/concepts/new', {
            name: 'create-or-update-concept',
            templateUrl: 'partials/create-or-update-concept.html',
            controller: 'CreateOrUpdateConceptCtrl as ctrl'
        }).
        when('/projects/:projectName/concepts/:conceptName', {
            name: 'concept-details',
            templateUrl: 'partials/concept-details.html',
            controller: 'ConceptDetailsCtrl as ctrl'
        }).
        when('/projects/:projectName/concepts/:conceptName/edit', {
            name: 'create-or-update-concept',
            templateUrl: 'partials/create-or-update-concept.html',
            controller: 'CreateOrUpdateConceptCtrl as ctrl'
        }).
        when('/projects/:projectName/usecases/new', {
            name: 'create-or-update-usecase',
            templateUrl: 'partials/create-or-update-usecase.html',
            controller: 'CreateOrUpdateUsecaseCtrl as ctrl'
        }).
        when('/projects/:projectName/usecases/:usecaseName', {
            name: 'usecase-details',
            templateUrl: 'partials/usecase-details.html',
            controller: 'UsecaseDetailsCtrl as ctrl'
        }).
        when('/projects/:projectName/usecases/:usecaseName/edit', {
            name: 'create-or-update-usecase',
            templateUrl: 'partials/create-or-update-usecase.html',
            controller: 'CreateOrUpdateUsecaseCtrl as ctrl'
        }).
        when('/projects/:projectName/actors/:actorName', {
            name: 'actor-details',
            templateUrl: 'partials/actor-details.html',
            controller: 'ActorDetailsCtrl as ctrl'
        }).
        when('/team/all', {
            name: 'all-teams',
            templateUrl: 'partials/all-teams.html',
            controller: 'AllTeamsCtrl as ctrl'
        }).
        when('/projects/:projectName/usecases/:usecaseName/stories', {
            name: 'usecase-stories',
            templateUrl: 'partials/usecase-stories.html',
            controller: 'UsecaseStoriesCtrl as ctrl'
        }).
        when('/team/:teamId', {
            name: 'team-details',
            templateUrl: 'partials/team-details.html',
            controller: 'TeamDetailsCtrl as ctrl'
        }).
        when('/projects/:projectName/analysts/update', {
            name: 'update-analysts',
            templateUrl: 'partials/update-analysts.html',
            controller: 'UpdateAnalystsCtrl as ctrl'
        }).
        when('/user/all', {
            name: 'all-users',
            templateUrl: 'partials/all-users.html',
            controller: 'AllUsersCtrl as ctrl'
        }).
        when('/login', {
            name: 'login',
            templateUrl: 'partials/login.html',
            controller: 'LoginCtrl as ctrl',
            css: 'css/validation.css'
        }).
        when('/register', {
            name: 'register',
            templateUrl: 'partials/register.html',
            controller: 'RegisterCtrl as ctrl',
            css: 'css/validation.css'
        }).
        when('/projects/:projectName/schedule', {
            templateUrl: 'partials/schedule.html',
            controller: 'ScheduleCtrl as ctrl'
        }).
        when('/projects/:projectName/processes/new', {
            templateUrl: 'partials/create-or-update-process.html',
            controller: 'CreateOrUpdateProcessCtrl as ctrl'
        }).
        when('/projects/:projectName/processes/:processName', {
            templateUrl: 'partials/process-details.html',
            controller: 'ProcessDetailsCtrl as ctrl'
        }).
        when('/projects/:projectName/processes/:processName/edit', {
            templateUrl: 'partials/create-or-update-process.html',
            controller: 'CreateOrUpdateProcessCtrl as ctrl'
        }).
        otherwise({
            redirectTo: '/projects'
        });
}]);
