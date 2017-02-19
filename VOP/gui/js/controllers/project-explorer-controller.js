/**
 * Controller for the project explorer page, this page displays all
 * projects, teams and some admin functionality.
 */
var projectExplorerController = angular.module('projectExplorerController', []);

projectExplorerController.controller('ProjectExplorerCtrl', ['$scope', '$http', 'Path', 'List', 'Auth', 'Dialog', 'Hateoas', 'Format', function($scope, $http, Path, List, Auth, Dialog, Hateoas, Format) {

    var self = this;

    self.projectList = new Object();
    self.teams = [];

    self.create = false;
    self.projectName = "";

    $scope.loadError = false;

    /**
     * Gets all projects.
     */
    self.getProjects = function() {
        var output = [];
        var getUserUrl = Path.getEndpoint('login');
        $scope.loadError = false;
        Hateoas.cleanTeamMap();
        Hateoas.cleanProjectMap();
        List.setWholeProjectList([]);

        $http.get(getUserUrl).success(function(data, status, headers, config) {
            var getTeamUrl = data.content.links['teams'];

            $http.get(getTeamUrl).success(function(data, status, headers, config) {
                self.teams = data.content;

                angular.forEach(self.teams, function(value, key) {
                    var getProjectsUrl = value.links['projects'];
                    Hateoas.setTeamInMap(value.id, value.links);

                    $http.get(getProjectsUrl).success(function(data, status, headers, config) {
                        output = [];

                        angular.forEach(data.content, function(value, key) {
                            Hateoas.setProjectInMap(value.name, value.links);
                            output.push(value);
                        });
                        self.projectList[value.name] = output.sort(function(a, b) {
                            return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
                        });
                        List.setWholeProjectList(self.projectList);
                    }).error(function(data, status, headers, config) {
                        $scope.loadError = true;
                    });
                });
            }).error(function(data, status, headers, config) {
                $scope.loadError = true;
            });
        }).error(function(data, status, headers, config) {
            $scope.loadError = true;
        });
    };
    self.getProjects();

    /**
     * Defines a load error action, displays a dialog.
     */
    $scope.$watchCollection('loadError', function(newValue, oldValue) {
        if (newValue && newValue != oldValue) {
            Dialog.showLoadErrorDialog('An error occured when loading the app data. Because of this loading error, not only this page, but also all other pages of this app might not work as expected and showing you incorrect/incomplete information.\n\nTherefore we recommend you to logout and try again when the errors are solved. Click \'Logout\' to do so.\nIf you want to stay logged in, click \'Continue\'.');
        }
    });

}]);

