/**
 * Controller for create or update process page, this page can contain
 * both a form for process creation as one for updating it.
 */
var createOrUpdateProcessController = angular.module('createOrUpdateProcessController', []);

createOrUpdateProcessController.controller('CreateOrUpdateProcessCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', 'Path', 'Nav', 'Dialog', 'Hateoas', 'Format', 'Auth', function($scope, $rootScope, $routeParams, $http, $location, Path, Nav, Dialog, Hateoas, Format, Auth) {

    var self = this;

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);
    self.projectName = $routeParams.projectName;

    self.processName = "";
    self.processPriority = 0;
    self.assignableUsecases = [];

    self.create = true;
    self.action = 'Create';

    /**
     * Gets all use cases in a project.
     */
    self.getUsecasesInProject = function() {
        var getUsecasesUrl = Hateoas.getProjectInMap(self.projectName)['usecases'];

        $http.get(getUsecasesUrl).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.length; i++) {
                var usecase = {
                    name: data.content[i].name,
                    toAssign: false
                };
                self.assignableUsecases.push(usecase);
            }

            self.assignableUsecases = self.assignableUsecases.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });

            if ($routeParams.processName) {
                self.create = false;
                self.action = 'Update';

                self.processName = Format.formatToHtml($routeParams.processName);
                self.processOldName = $routeParams.processName;
                if (!Hateoas.getProcessInMap(self.projectName, self.processOldName)) {
                    Dialog.showErrorDialog('Something went wrong when loading the process details. The given process name is not known. You\'re redirected back to the project details.');
                    $location.path('/projects/' + self.projectName);
                    return;
                }

                var getProcessUrl = Hateoas.getProcessInMap(self.projectName, self.processOldName)['self'];
                $http.get(getProcessUrl).success(function(data, status, headers, config) {
                    self.processPriority = data.content.priority;
                    for (var i = 0; i < data.content.useCases.length; i++){
                        for (var t = 0; t < self.assignableUsecases.length; t++){
                            if (data.content.useCases[i] == self.assignableUsecases[t].name){
                                self.assignableUsecases[t].toAssign = true;
                            }
                        }
                    }
                }).error(function(data, status, headers, config) {
                    $location.path('/projects/' + self.projectName);
                    Dialog.showErrorDialog('Something went wrong when loading the process details. You\'re redirected back to the project details.');
                });
            }
        }).error(function(data, status, headers, config) {
            Dialog.showErrorDialog('Something went wrong when loading the process details. You\'re redirected back to the project details.');
            $location.path('/projects/' + self.projectName);
        });
    };

    if (Hateoas.getProjectInMap(self.projectName)) {
        self.getUsecasesInProject();
    }
    else {
        Dialog.showErrorDialog('Something went wrong when loading the process details. The given project name is not known. You\'re redirected back to the project page.');
        $location.path('/projects');
        return;
    }

    /**
     * Cancels a create or update process request.
     */
    self.cancelCreateOrUpdateProcess = function() {
        Nav.back();
    };

    /**
     * Checks if a string is empty.
     */
    self.isEmpty = function(str) {
        return str == "";
    };

    /**
     * Create or update process for a certain use case.
     */
    self.createOrUpdateProcess = function() {
        self.valid = true;
        self.errorMessages = [];

        if (self.isEmpty(self.processName)) {
            self.valid = false;
            self.errorMessages.push('Process name cannot be empty');
        }
        if (self.processName == 'new') {
            self.valid = false;
            self.errorMessages.push('Process name \'new\' is not allowed');
        }

        if (self.valid) {
            var usecaseList = []
            for (var i = 0; i < self.assignableUsecases.length; i++) {
                if (self.assignableUsecases[i].toAssign) {
                    usecaseList.push(self.assignableUsecases[i].name);
                }
            }

            var newProcess = {
                name: Format.formatToJs(self.processName),
                priority: self.processPriority,
                useCases: usecaseList
            };

            if (self.create) {
                self.createProcess(newProcess);
            }
            else {
                self.updateProcess(newProcess);
            }
        }
        else {
            var message = '<strong>Some fields are not valid</strong>.\n\nMake sure these rules are satisfied:<ul>';
            for (var i = 0; i < self.errorMessages.length; i++) {
                message += '<li>' + self.errorMessages[i] + '</li>';
            }
            message += '</ul>';
            Dialog.showErrorDialog(message);
        }
    };

    /**
     * Creates a new process in a use case.
     */
    self.createProcess = function(newProcess) {
        var createProcessUrl = Hateoas.getProjectInMap(self.projectName)['process_add'];

        $http.post(createProcessUrl, newProcess).success(function(data, status, headers, config) {
            Dialog.clearAlert();
			Dialog.setInitNeeded(false);
			Dialog.setAlert('createProcessAlert');			
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Creating process failed!', data);
        });
    };

    /**
     * Edits a process for use case.
     */
    self.updateProcess = function(newProcess) {
        var updateProcessUrl = Hateoas.getProcessInMap(self.projectName, self.processOldName)['patch'];

        $http.patch(updateProcessUrl, newProcess).success(function(data, status, headers, config) {
			Dialog.clearAlert();
			Dialog.setInitNeeded(false);
            Dialog.setAlert('patchProcessAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Updating process failed!', data);
        });
    };

}]);
