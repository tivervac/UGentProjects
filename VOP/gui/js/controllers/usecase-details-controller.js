/**
 * Controller for use case details page. This page displays a use case,
 * all it's attributes and functionality.
 */
var usecaseDetailsController = angular.module('usecaseDetailsController', []);

usecaseDetailsController.controller('UsecaseDetailsCtrl', ['$scope', '$routeParams', '$http', '$location', 'Dialog', 'Path','Hateoas','Auth', 'Nav', function($scope, $routeParams, $http, $location, Dialog, Path, Hateoas, Auth, Nav) {

    var self = this;

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);
    self.projectName = $routeParams.projectName;
    self.usecaseName = $routeParams.usecaseName;
    Nav.setUpdateUsecaseAnalysts(false);

    self.workload = 0;
    self.priority = 0;

    self.scheduleUsecase = false;
    self.rescheduleUsecase = false;

    if (Hateoas.getProjectInMap(self.projectName)) {
        var url = Hateoas.getProjectInMap(self.projectName)['self'];

        $http.get(url).success(function(data, status, headers, config) {
            self.project = data.content;
            self.isProjectLeader = self.project.leader.id == Auth.getId();
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the project leader. This means it is possible that you won\t have all usual permissions when you are the leader of this project (and no admin or project analyst).', data);
        });
    }
    else {
        Dialog.showErrorDialog('Something went wrong when loading the use case details. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    self.usecaseObjective = "";
    self.usecaseProcess = "";
    self.usecasePreconditions = [];
    self.usecasePostconditions = [];
    self.usecaseActors = [];
    self.usecaseConcepts = [];
    self.usecaseNormalFlow = new Object();
    self.usecaseAlternativeFlows = [];
    self.usecaseExceptionalFlows = [];
    self.BranchesAlternativeFlow = [];
    self.BranchesExceptionalFlow = [];
    self.normalFlowSteps = [];
    self.returnsMap = new Object();

    if (Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)) {
        var getTaskUrl = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['task'];
        $http.get(getTaskUrl).success(function(data, status, headers, config) {
            self.usecaseIsScheduled = true;
            self.workload = data.content.workload / 3600;
            self.priority = data.content.priority;
        }).error(function(data, status, headers, config) {
            self.usecaseIsScheduled = false;
        });

        var getUsecaseUrl = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['self'];

        $http.get(getUsecaseUrl).success(function(data, status, headers, config) {
            self.usecase = data.content
            self.usecaseObjective = data.content.objective;
            self.usecasePreconditions = data.content.preconditions;
            self.usecasePostconditions = data.content.postconditions;
            self.usecaseActors = data.content.actors;
            self.usecaseConcepts = data.content.concepts;
            self.usecaseNormalFlow = data.content.normalFlow;
            self.usecaseAlternativeFlows = data.content.alternativeFlows;
            self.usecaseExceptionalFlows = data.content.exceptionalFlows;

            for (var i = 0; i < self.usecaseNormalFlow.behaviors.length; i++) {
                if (self.usecaseNormalFlow.behaviors[i].type == 'branchingPoint') {
                    var done = false;
                    var currentBranch = self.usecaseNormalFlow.behaviors[i];
                    while (!done) {
                        for (var t = 0; t < self.usecaseAlternativeFlows.length;t++) {
                            if (currentBranch.behavior['targetReference'] == self.usecaseAlternativeFlows[t].name) {
                                self.BranchesAlternativeFlow[t] = i+1;
                            }
                        }
                        for (var t = 0; t < self.usecaseExceptionalFlows.length;t++) {
                            if (currentBranch.behavior['targetReference'] == self.usecaseExceptionalFlows[t].name) {
                                self.BranchesExceptionalFlow[t] = i+1;
                            }
                        }
                        if (currentBranch.behavior.normalBehavior.type == 'textualStep') {
                            done = true;
                        }
                        else {
                            currentBranch = currentBranch.behavior.normalBehavior;
                        }
                    }
                }
            }
            for (var i = 0; i < self.usecaseAlternativeFlows.length; i++) {
                for (var t = 0; t < self.usecaseAlternativeFlows[i].behavior.behaviors.length; t++) {
                    if (self.usecaseAlternativeFlows[i].behavior.behaviors[t].type == 'return') {
                        for (var k = 0; k < self.usecaseNormalFlow.behaviors.length; k++) {
                            if (self.usecaseNormalFlow.behaviors[k].type == 'branchingPoint') {
                                var currentBranch = self.usecaseNormalFlow.behaviors[k];
                                var done = false;
                                while (!done) {
                                    if (currentBranch.behavior.normalBehavior.type == 'textualStep') {
                                        if (currentBranch.behavior.normalBehavior.name == self.usecaseAlternativeFlows[i].behavior.behaviors[t].behavior['target']) {
                                            self.returnsMap[self.usecaseAlternativeFlows[i].name] = k+1;
                                        }
                                        done = true;
                                    }
                                    else {
                                    currentBranch = currentBranch.behavior.normalBehavior;
                                    }
                                }
                            }
                            else {
                                if (self.usecaseAlternativeFlows[i].behavior.behaviors[t].behavior['target'] == self.usecaseNormalFlow.behaviors[k].name) {
                                    self.returnsMap[self.usecaseAlternativeFlows[i].name] = k+1;
                                }
                            }
                        }
                    }
                }
            }
            for (var i = 0; i < self.usecaseExceptionalFlows.length; i++) {
                for (var t = 0; t < self.usecaseExceptionalFlows[i].behavior.behaviors.length; t++) {
                    if (self.usecaseExceptionalFlows[i].behavior.behaviors[t].type == 'return') {
                        for (var k = 0; k < self.usecaseNormalFlow.behaviors.length; k++) {
                            if (self.usecaseNormalFlow.behaviors[k].type == 'branchingPoint') {
                                var currentBranch = self.usecaseNormalFlow.behaviors[k];
                                var done = false;
                                while (!done) {
                                    if (currentBranch.behavior.normalBehavior.type == 'textualStep') {
                                        if (currentBranch.behavior.normalBehavior.name == self.usecaseExceptionalFlows[i].behavior.behaviors[t].behavior['target']) {
                                            self.returnsMap[self.usecaseExceptionalFlows[i].name] = k+1;
                                        }
                                        done = true;
                                    }
                                    else {
                                    currentBranch = currentBranch.behavior.normalBehavior;
                                    }
                                }
                            }
                            else {
                                if (self.usecaseExceptionalFlows[i].behavior.behaviors[t].behavior['target'] == self.usecaseNormalFlow.behaviors[k].name) {
                                    self.returnsMap[self.usecaseExceptionalFlows[i].name] = k+1;
                                }
                            }
                        }
                    }
                }
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the use case details, you\'re redirected back to the project page...', data);
            $location.path('/projects/' + self.projectName);
        });
    }
    else {
        Dialog.showErrorDialog('An error occured when loading the use case details. The use case name is not known. You\'re redirected back to the project page...');
        $location.path('/projects/' + self.projectName);
    }

    if (Hateoas.getProjectInMap(self.projectName)) {
        var projectUrl = Hateoas.getProjectInMap(self.projectName)['self'];

        $http.get(projectUrl).success(function(data, status, headers, config) {
            self.project = data.content;
            self.isProjectLeader = self.project.leader.id == Auth.getId();

            if (Auth.isAdmin() || self.isProjectLeader) {
                self.checkAnalysts();
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the project details. Because of this, you are redirected back to the home page.', data);
            $location.path('/projects');
        });
    }
    else {
        Dialog.showErrorDialog('An error occured when loading the project. The project with the given name is not known. You\'re redirected back to the home page.');
        $location.path('/projects');
    }

    /**
     * Looks for team analysts.
     */
    self.checkAnalysts = function() {
        var url = Hateoas.getProjectInMap(self.projectName)['analysts'];

        $http.get(url).success(function(data, status, headers, config) {
            self.members = data.content;
            self.currentAnalysts = [];
            for (var i = 0; i < self.members.length; i++) {
                self.members[i].analystHere = false;
                self.currentAnalysts[i] = false;
            }

            var analystUrl = self.usecase.links['analysts'];

            $http.get(analystUrl).success(function(data, status, headers, config) {
                self.analysts = data.content;

                for (var i = 0; i < self.analysts.length; i++) {
                    for (var j = 0; j < self.members.length; j++) {
                        if (self.analysts[i].id == self.members[j].user.id) {
                            self.members[j].analystHere = true;
                            self.currentAnalysts[j] = true;
                            break;
                        }
                    }
                }
            }).error(function(data, status, headers, config) {
                Dialog.showHttpErrorDialog('An error occured when loading the project analysts. Because of this, you are redirected back to the project details page.', data);
                $location.path('/projects');
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the project analysts. Because of this, you are redirected back to the project details page.', data);
            $location.path('/projects');
        });
    };

    /**
     * Prompts user to delete use case.
     */
    self.deleteUsecase = function(index) {
        if (self.referencesLoaded) {
            if (self.references.length == 0) {
                BootstrapDialog.show({
                    type: BootstrapDialog.TYPE_DANGER,
                    title: 'Delete use case ?',
                    closable: false,
                    cssClass: 'small-dialog',
                    message: 'Are you sure you want to delete this use case?\n<strong>This cannot be undone!</strong>',
                    buttons: [{
                        icon: 'glyphicon glyphicon-ok',
                        label: 'Delete',
                        cssClass: 'btn btn-success',
                        action: function(dialogItself) {
                            self.deleteUsecaseForSure();
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
            }
            else {
                var message = 'You cannot delete this use case because this concept is still referenced by some other use cases. See the \'Usecase referenced by\' section on this page to see an overview of these use cases.\n\nIf you do want to delete this concept, you will first have to delete these other use cases or at least remove the references to this use case in these other use cases.';
                Dialog.showErrorDialog(message);
            }
        }
    };

    /**
     * Irreversibly deletes the use case.
     */
    self.deleteUsecaseForSure = function() {
        var deleteUsecaseUrl = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['delete'];

        $http.delete(deleteUsecaseUrl).success(function(data, status, headers, config) {
            Dialog.setAlert('deleteUsecaseAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Deleting use case failed!', data);
        });
    };

    /**
     * Returns whether step is textual.
     */
    self.isTextualInStep = function(step) {
        if (step.type == 'textualStep') {
            if (step.behavior.description.type == 'textual') {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    };

    /**
     * Returns whether step is branch.
     */
    self.isBranch = function(step) {
        if (step.type == 'branchingPoint') {
            return true;
        }
        else {
            return false;
        }
    };

    /**
     * Returns whether step contains reference.
     */
    self.isReferenceInStep = function(step) {
        if (step.type == 'textualStep') {
            if (step.behavior.description.type == 'reference') {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    };

    /**
     * Returns whether step returns.
     */
    self.isReturn = function(step) {
        if (step.type == 'return') {
            return true;
        }
        else {
            return false;
        }
    };

    /**
     * Returns whether step is references.
     */
    self.isReference = function(step) {
        if (step.type == 'reference') {
            return true;
        }
        else {
            return false;
        }
    };

    /**
     * Returns whether step is textual.
     */
    self.isTextual = function(step) {
        if (step.type == 'textual') {
            return true;
        }
        else {
            return false;
        }
    };

    /**
     * Updates the team analyst.
     */
    self.updateAnalysts = function() {
        $scope.error = false;

        self.index = 0;
        var count = 0;

        for (var i = 0; i < self.members.length; i++) {
            if (self.members[i].analystHere != self.currentAnalysts[i]) {
                count++;
            }
        }

        for (var i = 0; i < self.members.length; i++) {
            if (self.members[i].analystHere && !self.currentAnalysts[i]) {
                var url = self.usecase.links['analyst_add'];
                url = url.replace(/{[a-z]*}/i, self.members[i].user.id);

                $http.post(url).success(function(data, status, headers, config) {
                    if (++self.index == count && !self.error) {
                        Dialog.setAlert('patchAnalystAlert');
                    }
                }).error(function(data, status, headers, config) {
                    $scope.error = true;
                });
            }
            else if (!self.members[i].analystHere && self.currentAnalysts[i]) {
                var url = self.usecase.links['analyst_remove'];
                url = url.replace(/{[a-z]*}/i, self.members[i].user.id);

                $http.delete(url).success(function(data, status, headers, config) {
                    if (++self.index == count && !self.error) {
                        Dialog.setAlert('patchAnalystAlert');
                    }
                }).error(function(data, status, headers, config) {
                    $scope.error = true;
                });
            }
        }
        Nav.setUpdateUsecaseAnalysts(false);
        self.checkAnalysts();
    };

    $scope.$watchCollection('error', function(newValue, oldValue) {
        if (newValue && newValue != oldValue) {
            Dialog.showErrorDialog('An error occured when updating the analysts for this use case. Some changes might not be saved...');
        }
    });

    /**
     * Recursively extract references from step.
     */
    self.linearizeReference = function(step) {
        var left;
        var right;
        if (step.data.left.type == 'reference') {
            left = self.linearizeReference(step.data.left);
        }
        else {
            left = step.data.left.data.text;
        }
        if (step.data.right.type == 'reference') {
            right = self.linearizeReference(step.data.right);
        }
        else {
            right = step.data.right.data.text;
        }
        var result = left + "@" + step.data.reference + "" + right;
        return result;
    };

    /**
     * Finds and returns textual representation of all steps in branch.
     */
    self.findStepInBranch = function(step) {
        var current = step;
        var done = false;
        while (!done) {
            if (current.type == 'textualStep') {
                done = true;
            }
            else {
                current = current.behavior.normalBehavior;
            }
        }
        if (current.behavior.description.type == 'textual') {
            return current.behavior.description.data.text;
        }
        else {
            return self.linearizeReference(current.behavior.description);
        }
    };

    /**
     * Returns the executor of a step in a branch.
     */
    self.findExecutorInBranch = function(step) {
        var current = step;
        var done = false;
        while (!done) {
            if (current.type == 'textualStep') {
                done = true;
            }
            else {
                current = current.behavior.normalBehavior;
            }
        }
        return current.behavior.executor;

    };

    /**
     * Finds and loads references to the current use case.
     */
    self.getReferences = function() {
        self.referencesLoaded = false;
        var referencesUrl = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['self'];
        var action = { action: 'reference' }

        $http.put(referencesUrl, action).success(function(data, status, headers, config) {
            self.referencesLoaded = true;
            self.references = [];

            data.content.actors = data.content.actors.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });

            for (var i = 0; i < data.content.actors.length; i++) {
                data.content.actors[i].type = 'actor';
                self.references.push(data.content.actors[i]);
            }

            data.content.useCases = data.content.useCases.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });
            for (var i = 0; i < data.content.useCases.length; i++) {
                data.content.useCases[i].type = 'usecase';
                self.references.push(data.content.useCases[i]);
            }

            data.content.concepts = data.content.concepts.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });
            for (var i = 0; i < data.content.concepts.length; i++) {
                data.content.concepts[i].type = 'concept';
                self.references.push(data.content.concepts[i]);
            }
        }).error(function(data, status, headers, config) {
            self.references = [];
            Dialog.showHttpErrorDialog('Failed to load references to this use case!', data);
        });
    };
    self.getReferences();

    /**
     * Schedule the current use case.
     */
    self.schedule = function(){
        var getUsecaseUrl = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['task'];
        var workInSeconds = self.workload * 3600;
        var task = {
            workload: workInSeconds,
            priority: self.priority
        };

        $http.post(getUsecaseUrl,task).success(function(data, status, headers, config) {
            Dialog.setAlert('scheduleUsecaseAlert');
            Dialog.setInitNeeded(false);
            var node = document.getElementById('scheduleUsecaseAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node, list.childNodes[0]);

            self.scheduleUsecase = false;
            self.usecaseIsScheduled = true;
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Failed to schedule this use case!', data);
        });
    };

    /**
     * Removes schedule from use case.
     */
    self.unschedule = function(){
        var getUsecaseUrl = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['task'];

        $http.delete(getUsecaseUrl).success(function(data, status, headers, config) {
            Dialog.setAlert('unscheduleUsecaseAlert');
            Dialog.setInitNeeded(false);
            var node = document.getElementById('unscheduleUsecaseAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node, list.childNodes[0]);

            self.usecaseIsScheduled = false;
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Failed to unschedule this use case!', data);
        });
    };

    /**
     * Update schedule on use case.
     */
    self.reschedule = function(){
        var getUsecaseUrl = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['task'];
        var workInSeconds = self.workload * 3600;
        var task = {
            workload: workInSeconds,
            priority: self.priority
        };

        $http.patch(getUsecaseUrl, task).success(function(data, status, headers, config) {
            Dialog.setAlert('rescheduleUsecaseAlert');
            Dialog.setInitNeeded(false);
            var node = document.getElementById('rescheduleUsecaseAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node, list.childNodes[0]);

            self.rescheduleUsecase = false;
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Failed to reschedule this use case!', data);
        });
    };

}]);

