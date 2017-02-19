/**
 * A controller for the login page.
 */
var loginController = angular.module('loginController', []);

loginController.controller('LoginCtrl', ['$http', '$location', 'Auth', 'Path', 'Dialog', function($http, $location, Auth, Path, Dialog) {

    var self = this;

    if (Dialog.getInitNeeded()) {
        Dialog.initAlerts();
    }
    else {
        Dialog.setInitNeeded(true);
    }

    self.emailEnd = '@' + Auth.getEmailEnd();

    $http.get(Path.getBaseUrl()).success(function(data, status, headers, config) {
        Path.setUrls(data);
        self.valid = true;
    }).error(function(data, status, headers, config) {
        self.valid = false;
        Dialog.showHttpErrorDialog('An error occurred when connecting to the server. This can either be a server error or a connection error.\nYou won\'t be able to do anything until this error is solved. Please refresh the page when you want to try again.', data);
    });

    /**
     * Signs a user into the system.
     */
    self.signIn = function() {
        if (self.valid) {
            if (!self.username || !self.password) {
                Dialog.showErrorDialog('Email address and/or password can\'t be blank.');
                return;
            }

            var email = self.username + self.emailEnd;
            var auth = 'Basic ' + btoa(email + ':' + self.password);
            $http.defaults.headers.common['Authorization'] = auth;

            var url = Path.getEndpoint('login');

            $http.get(url).success(function(data, status, headers, config) {
                Auth.login(data, self.password);
                $location.path('/projects');
            }).error(function(data, status, headers, config) {
                $http.defaults.headers.common['Authorization'] = '';
                if (status == 403) {
                    Dialog.showErrorDialog('<strong>Login failed!</strong>\n\nPlease enter a valid combination of email address and password.');
                }
                else {
                    Dialog.showHttpErrorDialog('An unknown error occurred when logging in.', data);
                }
            });
        }
    };

    /**
     * Reroutes a user to the register page.
     */
    self.register = function() {
        if (self.valid) {
            $location.path('/register');
        }
    };

}]);

