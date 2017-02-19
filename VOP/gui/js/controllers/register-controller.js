/**
 * Controller for the register page, this page is meant for users to register
 * on the website and server.
 */
var registerController = angular.module('registerController', []);

registerController.controller('RegisterCtrl', ['$http', '$location', 'Auth', 'Path', 'Dialog', function($http, $location, Auth, Path, Dialog) {

    var self = this;

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);
    self.emailEnd = '@' + Auth.getEmailEnd();

    /**
     * Registers a user.
     */
    self.register = function() {
        var fields = [self.firstname, self.lastname, self.username, self.password, self.password2];
        for (var i = 0; i < fields.length; i++) {
            if (!fields[i]) {
                Dialog.showErrorDialog('Please make sure all fields are filled in.');
                return;
            }
        }

        if (self.password != self.password2) {
            Dialog.showErrorDialog('Password and password confirmation don\'t match. Please re-enter them.');
            self.password = '';
            self.password2 = '';
            return;
        }

        var user = {
            admin: false,
            analyst: false,
            email: self.username + self.emailEnd,
            firstName: self.firstname,
            lastName: self.lastname,
            password: self.password
        };

        $http.post(Path.getEndpoint('user'), user).success(function(data, status, headers, config) {
            Dialog.setAlert('registerUserAlert');
            $location.path('/login');
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Something went wrong when creating the new user account...', data);
        });
    };

}]);
