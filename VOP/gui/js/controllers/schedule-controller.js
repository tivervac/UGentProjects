/**
 * Controller for the scheduler page. Here it's possible to add,
 * edit and remove schedules from projects.
 */
var scheduleController = angular.module('scheduleController', []);

scheduleController.controller('ScheduleCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', 'Path', 'Nav', 'Dialog', 'Hateoas', 'Format', 'Auth', function($scope, $rootScope, $routeParams, $http, $location, Path, Nav, Dialog, Hateoas, Format, Auth) {

    var self = this;

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);

    self.projectName = $routeParams.projectName;

    self.tasks = []


    if (Hateoas.getProjectInMap(self.projectName)) {
        var url = Hateoas.getProjectInMap(self.projectName)['self'];

        $http.get(url).success(function(data, status, headers, config) {
            self.project = data.content;
            self.isProjectLeader = self.project.leader.id == Auth.getId();
            var getScheduleUrl =  Hateoas.getProjectInMap(self.projectName)['schedule'];
            self.getSchedule(getScheduleUrl);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the project leader. This means it is possible that you won\'t have all usual permissions when you are the leader of this project (and no admin or project analyst).', data);
        });
    }
    else {
        Dialog.showErrorDialog('Something went wrong when loading the schedule. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    /**
     * Returns the schedule for the current project.
     */
    self.getSchedule = function(url){
        $http.get(url).success(function(data, status, headers, config) {
            angular.forEach(data.content.page, function(value, key) {
                self.tasks.push(value);
            });
            if (data.content.metadata.hasNextPage) {
                self.getSchedule(data.content.links.next);
            }
        }).error(function(data, status, headers, config) {
            $location.path('/projects/' + self.projectName);
            Dialog.showHttpErrorDialog('An error occured when loading the schedule, you\'re redirected back to the project page...', data);
        });
    };

	self.formatDate = function(date){
		return date.replace("T"," ");
	}
}]);
