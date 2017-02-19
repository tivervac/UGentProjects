/**
 * Service for authentication handling.
 * Gives the possibility to login and logout and contains information about
 * this login state of the application (whether someone is logged in or not).
 * If a user is logged in, the user information can also be retrieved.
 * In particular the information is stored about the permissions this user has
 * (whether the user has admin permissions and in which projects the user has
 * analyst permissions).
 * Also the deloitte email ending (used to login and register users) is stored in
 * this service.
 */
var authService = angular.module('authService', []);

authService.service('Auth', function($http, $location, $rootScope, Path, List) {

    var emailEnd = 'deloitte.com';

    var user = null;
    var loggedIn = false;
    var password = '';
    var analystProjects = [];

    var userList = [];

    /**
     * Logs a user in to the application and updates the service
     * variables for the newly logged in user.
     */
    var login = function(_user, _password) {
        if (_user) {
            loggedIn = true;
            user = _user.content;
            password = _password;
            updateAnalystProjects();
            updateUserList();
        }
    };

    /**
     * Logs out a user. Redirects to the login page and clears all the
     * information of the logged out user.
     */
    var logout = function() {
        loggedIn = false;
        user = null;
        password = '';
        analystProjects = [];
        $http.defaults.headers.common['Authorization'] = '';
        $location.path('/login');
        $rootScope.$apply();
    };

    /**
     * Checks in which projects the logged in user has analyst permissions.
     * Must be called at login and every time a project name changes.
     */
    var updateAnalystProjects = function() {
        analystProjects = [];
        var url = user.links['analyst_projects'];
        $http.get(url).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.length; i++) {
                analystProjects.push(data.content[i].name);
            }
        }).error(function(data, status, headers, config) {
            analystProjects = [];
        });
    };

    /**
     * Returns whether a user is currently logged in.
     */
    var isLoggedIn = function() {
        return loggedIn;
    };

    /**
     * Returns the email address of the currently logged in user.
     */
    var getEmail = function() {
        if (loggedIn) {
            return user.email;
        }
        else {
            return '';
        }
    };

    /**
     * Returns the first name of the currently logged in user.
     */
    var getFirstname = function() {
        if (loggedIn) {
            return user.firstName;
        }
        else {
            return '';
        }
    };

    /**
     * Returns the last name of the currently logged in user.
     */
    var getLastname = function() {
        if (loggedIn) {
            return user.lastName;
        }
        else {
            return '';
        }
    };

    /**
     * Returns the ID of the currently logged in user.
     */
    var getId = function() {
        if (loggedIn) {
            return user.id;
        }
        else {
            return -1;
        }
    };

    /**
     * Returns whether the currently logged in user is an admin.
     */
    var isAdmin = function() {
        if (loggedIn) {
            return user.admin;
        }
        else {
            return false;
        }
    };

    /**
     * Returns whether the currently logged in user is an analyst in the
     * project wit the given project name.
     */
    var isAnalyst = function(projectName) {
        if (loggedIn) {
            return $.inArray(projectName, analystProjects) > -1;
        }
        else {
            return false;
        }
    };

    /**
     * Returns Deloitte email ending.
     */
    var getEmailEnd = function() {
        return emailEnd;
    };

    var getUsersOnPage = function(url) {
        $http.get(url).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.page.length; i++) {
                userList.push(data.content.page[i]);
            }
            if (data.content.metadata.hasNextPage) {
                getUsersOnPage(data.content.links.next);
            }
            else {
                List.setWholeUserList(userList.sort(function(a, b) {
                    return a.lastName.toLowerCase() < b.lastName.toLowerCase() ? -1 : 1 ;
                }));
            }
        }).error(function(data, status, headers, config) {
            List.setWholeUserList([]);
        });
    };

    /**
     * Retrieves a list of all users, using http GET.
     */
    var updateUserList = function() {
        userList = [];
        self.error = false;
        var url = Path.getEndpoint('user');

        getUsersOnPage(url);
    };

    return {
        login: login,
        logout: logout,
        updateAnalystProjects: updateAnalystProjects,
        isLoggedIn: isLoggedIn,
        getEmail: getEmail,
        getFirstname: getFirstname,
        getLastname: getLastname,
        getId: getId,
        isAdmin: isAdmin,
        isAnalyst: isAnalyst,
        getEmailEnd: getEmailEnd,
        updateUserList: updateUserList
    };

});

