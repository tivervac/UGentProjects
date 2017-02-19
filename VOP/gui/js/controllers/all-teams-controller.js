/**
 * Controller for team overview page.
 */
var allTeamsController = angular.module('allTeamsController', []);

allTeamsController.controller('AllTeamsCtrl', ['$scope', '$routeParams', '$http', 'List', 'Auth', 'Path', 'Dialog', 'Hateoas', 'Nav', function($scope, $routeParams, $http, List, Auth, Path, Dialog, Hateoas, Nav) {

    var self = this;

    Dialog.initAlerts();

    self.create = false;
    self.teamName = "";
    self.patch = [];
    self.searchBar = false;
    self.queryName = "";
    self.teams = [];
    self.newTeamName = "";
    self.teamLeader = -1;
    self.selectLeader = false;

    /**
     * Creates a team.
     */
    self.createTeam = function() {
        var createTeamUrl = Path.getEndpoint('team');
        var team = {
            name: self.teamName,
            leader: {
                email: self.teamLeaderEmail
            }
        }
        var id = -1;

        if (self.validTeamName(self.teamName)) {
            if (self.teamLeader != -1) {
                Nav.setCreateTeam(false);
                self.teamLeader = -1;
                self.selectLeader = false;

                $http.post(createTeamUrl, team).success(function(data, status, headers, config) {
                    self.teamName = "";
                    self.create = false;
                    id = data.content.id;
                    var teamAddUserUrl = data.content.links['member_post'];
                    teamAddUserUrl = teamAddUserUrl.replace(/{[a-z]*}/i, data.content.leader.id);
                    $http.post(teamAddUserUrl).success(function(data, status, headers, config) {
                        self.getTeams();
                        Dialog.setAlert('createTeamAlert');
                        var node = document.getElementById('createTeamAlert');
                        var list = document.getElementById('alerts');
                        list.insertBefore(node,list.childNodes[0]);
                    }).error(function(data, status, headers, config) {
                        Dialog.showHttpErrorDialog('Team created, but adding admin to team failed', data);
                    });
                }).error(function(data, status, headers, config) {
                    Dialog.showHttpErrorDialog('Creating team failed!', data);
                });
            }
            else {
                Dialog.showErrorDialog('No valid teamleader selected! Make sure to select a teamleader!');
            }
        }
        else {
             Dialog.showErrorDialog('No valid team name entered!\n\nMake sure the team name is <strong>not empty</strong>.');
        }
    };

    /**
     * Requests user input to delete team.
     */
    self.deleteTeam = function(teamId) {
        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_DANGER,
            title: 'Delete team ?',
            closable: false,
            cssClass: 'small-dialog',
            message: 'Are you sure you want to delete this team ?\n\nThe projects in this team will not be removed. After removing this the team you will be able to add these projects to another team.',
            buttons: [{
                icon: 'glyphicon glyphicon-ok',
                label: 'Delete',
                cssClass: 'btn btn-success',
                action: function(dialogItself) {
                    self.deleteTeamForSure(teamId);
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
     * Deletes a team.
     */
    self.deleteTeamForSure = function(teamId) {
        var deleteTeamUrl = Hateoas.getTeamInMap(teamId)['delete'];

        $http.delete(deleteTeamUrl).success(function(data, status, headers, config) {
            Dialog.setAlert('deleteTeamAlert');
            var node = document.getElementById('deleteTeamAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node,list.childNodes[0]);
			Hateoas.cleanTeamMap();
            self.getTeams();
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Deleting team failed!', data);
        });
    };

    /**
     * Edits a team.
     */
    self.patchTeam = function(teamId,oldTeamName) {
        var updateTeamUrl = Hateoas.getTeamInMap(teamId)['patch'];

        if (oldTeamName != self.newTeamName) {
            if (self.validTeamName(self.newTeamName)) {
                var newTeam = { name: self.newTeamName };
                $http.patch(updateTeamUrl, newTeam).success(function(data, status, headers, config) {
                    Dialog.setAlert('patchTeamAlert');
                    var node = document.getElementById('patchTeamAlert');
                    var list = document.getElementById('alerts');
                    list.insertBefore(node,list.childNodes[0]);
                    self.patch[oldTeamName] = false;
                    self.patch[self.newTeamName] = false;
                    self.getTeams();
                }).error(function(data, status, headers, config) {
                    Dialog.showHttpErrorDialog('Updating team failed!', data);
                });
            }
            else {
                Dialog.showErrorDialog('No valid team name entered!\n\nMake sure the team name is <strong>not empty</strong>.');
            }
        }
        else {
            self.patch[oldTeamName] = false;
            self.patch[self.newTeamName] = false;
        };
    };

    /**
     * Gets all teams.
     */
    self.getTeams = function() {
        self.teams = [];
        var getTeamUrl = Path.getEndpoint('team');
        self.getTeamsOnPage(getTeamUrl);
    };

    /**
     * Gets all teams on page, these are the ones that should be shown in the GUI.
     */
    self.getTeamsOnPage = function(getTeamUrl) {
        $http.get(getTeamUrl).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.page.length; i++) {
                self.teams.push(data.content.page[i]);
            }
            if (data.content.metadata.hasNextPage) {
                self.getTeamsOnPage(data.content.links.next);
            }
            else {
                var output = [];
                Hateoas.cleanTeamMap();
                List.setWholeTeamList([]);
                angular.forEach(self.teams, function(value, key) {
                    Hateoas.setTeamInMap(value.id, value.links);
                    self.patch[value.name] = false;
                    output.push(value);
                });
                self.teams = output.sort(function(a, b) {
                    return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
                });
                List.setWholeTeamList(self.teams);
            }
        }).error(function(data, status, headers, config) {
            List.setWholeTeamList([]);
            Dialog.showHttpErrorDialog('An error occured when loading the data on this page...', data);
        });
    };

    if (Auth.isAdmin()) {
        self.getTeams();
    }

    /**
     * Validates a teamname.
     */
    self.validTeamName = function(teamName) {
        if (teamName == '') {
            return false;
        }
        return true;
    };

}]);

