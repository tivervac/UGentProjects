/**
 * Controller for updating analysts.
 */
var updateAnalystsController = angular.module('updateAnalystsController', []);

updateAnalystsController.controller('UpdateAnalystsCtrl', ['$scope', '$routeParams', '$http', '$location', 'Auth', 'Path', 'Dialog', 'Hateoas', function($scope, $routeParams, $http, $location, Auth, Path, Dialog, Hateoas) {

    var self = this;

    Dialog.initAlerts();

    self.projectName = $routeParams.projectName;

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
     * Looks for analysts in team members.
     */
    self.checkAnalysts = function() {
        var eligibleAnalystUrl = self.project.links['eligible_analyst'];

        $http.get(eligibleAnalystUrl).success(function(data, status, headers, config) {
            self.members = data.content;
            self.currentWork = [];
            self.currentAnalysts = [];
            for (var i = 0; i < self.members.length; i++) {
                self.members[i].analystHere = false;
                self.currentAnalysts[i] = false;
                self.currentWork[i] = 0;
            }

            var analystUrl = self.project.links['analysts'];

            $http.get(analystUrl).success(function(data, status, headers, config) {
                self.analysts = data.content;

                for (var i = 0; i < self.analysts.length; i++) {
                    for (var j = 0; j < self.members.length; j++) {
                        if (self.analysts[i].user.id == self.members[j].id) {
                            self.members[j].analystHere = true;
                            self.currentAnalysts[j] = true;
                            self.members[j].work = self.analysts[i].work / 3600;
                            self.currentWork[j] = self.analysts[i].work / 3600;
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
     * Updates the team Analysts.
     */
    self.updateAnalysts = function() {
        $scope.error = false;

        self.index = 0;
        var count = 0;

        for (var i = 0; i < self.members.length; i++) {
            if (self.members[i].analystHere != self.currentAnalysts[i] || (self.members[i].analystHere && self.currentAnalysts[i] && (self.members[i].work != self.currentWork[i]))) {
                count++;
            }
        }

        for (var i = 0; i < self.members.length; i++) {
            if (self.members[i].analystHere && !self.currentAnalysts[i]) {
                var url = self.project.links['analyst_add'];
                url = url.replace(/{[a-z]*}/i, self.members[i].id);
                var workInSeconds = self.members[i].work * 3600;
                var body = {
                    work: workInSeconds
                }

                $http.post(url, body).success(function(data, status, headers, config) {
                    if (++self.index == count && !self.error) {
                        Dialog.setAlert('patchAnalystAlert');
                        Dialog.setInitNeeded(false);
                        $location.path('/projects/' + self.projectName);
                    }
                }).error(function(data, status, headers, config) {
                    $scope.error = true;
                });
            }
            else if (!self.members[i].analystHere && self.currentAnalysts[i]) {
                var url = self.project.links['analyst_remove'];
                url = url.replace(/{[a-z]*}/i, self.members[i].id);

                $http.delete(url).success(function(data, status, headers, config) {
                    if (++self.index == count && !self.error) {
                        Dialog.setAlert('patchAnalystAlert');
                        Dialog.setInitNeeded(false);
                        $location.path('/projects/' + self.projectName);
                    }
                }).error(function(data, status, headers, config) {
                    $scope.error = true;
                });
            } else if (self.members[i].analystHere && self.currentAnalysts[i] && (self.members[i].work != self.currentWork[i])){
                var url = self.project.links['analyst_add'];
                url = url.replace(/{[a-z]*}/i, self.members[i].id);
                var workInSeconds = self.members[i].work * 3600;
                var body = {
                    work: workInSeconds
                }
                // and http patch him/her
                $http.patch(url,body).success(function(data, status, headers, config) {
                    // check count again
                    if (++self.index == count && !self.error) {
                        Dialog.setAlert('patchAnalystAlert');
                        Dialog.setInitNeeded(false);
                        $location.path('/projects/' + self.projectName);
                    }
                }).error(function(data, status, headers, config) {
                    // error during Patch analyst
                    $scope.error = true;
                });
            }
        }
    };

    /**
     * Defines error function when number of analysts added or removed is
     * incorrect. One or more analysts failed to update, other changes might
     * be saved.
     */
    $scope.$watchCollection('error', function(newValue, oldValue) {
        if (newValue && newValue != oldValue) {
            Dialog.showErrorDialog('An error occured when updating the analysts for this project. Some changes might not be saved...');
        }
    });

    /**
     * Cancels updating and reverts to team details.
     */
    self.cancelUpdating = function() {
        $location.path('/projects/' + self.projectName);
    };

}]);

