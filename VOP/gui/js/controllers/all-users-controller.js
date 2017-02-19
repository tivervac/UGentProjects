/**
 * Controller for the user page, where a list view of users is shown.
 */
var allUsersController = angular.module('allUsersController', []);

allUsersController.controller('AllUsersCtrl', ['$scope', '$routeParams', '$http', '$location', 'List', 'Auth', 'Path', 'Dialog', 'Nav', function($scope, $routeParams, $http, $location, List, Auth, Path, Dialog, Nav) {

    var self = this;

    Dialog.initAlerts();

    self.isAdmin = Auth.isAdmin();
    self.adminState = {};
    self.newAdminState = {};
    self.userid = Auth.getId();

    self.users = [];

    self.error = false;

    self.adminState = {};
    self.newAdminState = {};
    for (var i = 0; i < List.getWholeUserList().length; i++) {
        self.adminState[List.getWholeUserList()[i].email] = List.getWholeUserList()[i].admin;
        self.newAdminState[List.getWholeUserList()[i].email] = List.getWholeUserList()[i].admin;
    }

    /**
     * Updates admin states of users.
     */
    self.updateAdmins = function() {
        self.error = false;
        self.index = 0;
        var count = 0;

        for (var i = 0; i < List.getWholeUserList().length; i++) {
            if (self.newAdminState[List.getWholeUserList()[i].email] != self.adminState[List.getWholeUserList()[i].email]) {
                count++;
            }
        }

        for (var i = 0; i < List.getWholeUserList().length; i++) {
            if (self.newAdminState[List.getWholeUserList()[i].email] != self.adminState[List.getWholeUserList()[i].email]) {
                var url = List.getWholeUserList()[i].links['patch'];
                var content = { admin: self.newAdminState[List.getWholeUserList()[i].email] };

                $http.patch(url, content).success(function(data, status, headers, config) {
                    if (++self.index == count && !self.error) {
                        Dialog.setAlert('patchAdminAlert');
                        var node = document.getElementById('patchAdminAlert');
                        var list = document.getElementById('alerts');
                        list.insertBefore(node,list.childNodes[0]);

                        Auth.updateUserList();
                    }
                }).error(function(data, status, headers, config) {
                    Dialog.showHttpErrorDialog('Something went wrong when updating the admins. Some of your changes may not be saved...', data);
                });
            }
        }
    };

    /**
     * Cancels updating, sets path to /projects again.
     */
    self.cancelUpdating = function() {
        Nav.back();
    };

    /**
     * Prompts user for user removal.
     */
    self.removeUser = function(index) {
        self.error = false;

        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_DANGER,
            title: 'Remove user ?',
            closable: false,
            cssClass: 'small-dialog',
            message: 'Are you sure you want to remove this account ? <strong>This cannot be undone!</strong>',
            buttons: [{
                icon: 'glyphicon glyphicon-ok',
                label: 'Remove',
                cssClass: 'btn btn-success',
                action: function(dialogItself) {
                    self.removeUserForSure(index);
                    dialogItself.close();
                }
            }, {
                icon: 'glyphicon glyphicon-remove',
                label: 'Cancel',
                cssClass: 'btn btn-danger',
                action: function(dialogItself) {
                    dialogItself.close();
                }
            }]
        });
    };

    /**
     * Irreversibly removes a user.
     */
    self.removeUserForSure = function(index) {
        self.error = false;

        var url = List.getWholeUserList()[index].links['delete'];

        $http.delete(url).success(function(data, status, headers, config) {
            Auth.updateUserList();
            Dialog.setAlert('deleteUserAlert');
            var node = document.getElementById('deleteUserAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node,list.childNodes[0]);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Something went wrong when removing the user. Your changes may not be saved...', data);
            self.error = true;
        });
    };

}]);

