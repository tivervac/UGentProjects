/**
 * Controller for process details page, here processes are shown in detail.
 */
var processDetailsController = angular.module('processDetailsController', []);

processDetailsController.controller('ProcessDetailsCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', 'Path', 'Dialog', 'Hateoas', 'Format', 'Auth', function($scope, $rootScope, $routeParams, $http, $location, Path, Dialog, Hateoas, Format, Auth) {

    var self = this;

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);

    self.projectName = $routeParams.projectName;
    self.processName = $routeParams.processName;

    if (Hateoas.getProjectInMap(self.projectName)) {
        var url = Hateoas.getProjectInMap(self.projectName)['self'];

        $http.get(url).success(function(data, status, headers, config) {
            self.project = data.content;
            self.isProjectLeader = self.project.leader.id == Auth.getId();
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the project leader. This means it is possible that you won\'t have all the usual permissions when you are the leader of this project (and no admin or project analyst).', data);
        });
    }
    else {
        Dialog.showErrorDialog('Something went wrong when loading the process details. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    if (Hateoas.getProcessInMap(self.projectName, self.processName)) {
        var getProcessUrl =  Hateoas.getProcessInMap(self.projectName, self.processName)['self'];
        $http.get(getProcessUrl).success(function(data, status, headers, config) {
            self.processPriority = data.content.priority;
            self.usecases = data.content.useCases;
        }).error(function(data, status, headers, config) {
            $location.path('/projects/' + self.projectName);
            Dialog.showHttpErrorDialog('An error occured when loading the process details, you\'re redirected back to the project page...', data);
        });
    }
    else {
        $location.path('/projects/' + self.projectName);
        Dialog.showErrorDialog('An error occured when loading the process details. The given process is unknown. You\'re redirected back to the project page...');
    };

    self.deleteProcess = function() {
        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_DANGER,
            title: 'Delete process ?',
            closable: false,
            cssClass: 'small-dialog',
            message: 'Are you sure you want to delete this process?',
            buttons: [{
                icon: 'glyphicon glyphicon-ok',
                label: 'Delete',
                cssClass: 'btn btn-success',
                action: function(dialogItself) {
                    self.deleteProcessForSure();
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
     * Deletes a process.
     */
    self.deleteProcessForSure = function() {
        var deleteProcessUrl = Hateoas.getProcessInMap(self.projectName, self.processName)['delete'];

        $http.delete(deleteProcessUrl).success(function(data, status, headers, config) {
            Dialog.setAlert('deleteProcessAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Deleting process failed!', data);
        });
    };

}]);
