/**
 * Controller for the team details page, this page contains and displays all
 * information concerning teams. This includes users, projects etc.
 */
var teamDetailsController = angular.module('teamDetailsController', []);

teamDetailsController.controller('TeamDetailsCtrl', ['$scope', '$routeParams', '$http', '$anchorScroll', '$location', '$timeout', 'List', 'Path', 'Auth', 'Dialog', 'Hateoas', 'Format', 'Nav', function($scope, $routeParams, $http, $anchorScroll, $location, $timeout, List, Path, Auth, Dialog, Hateoas, Format, Nav) {

    var self = this;

    if (Dialog.getInitNeeded()) {
        Dialog.initAlerts();
    }
    else {
        Dialog.setInitNeeded(true);
    }

    self.id = $routeParams.teamId;
    self.userId = Auth.getId();

    self.adding = false;
    self.removing = false;
    self.otherUsers = [];
    Nav.setAddExistingProjects(false);

    self.team = new Object();
    self.isTeamLeader = false;
    Nav.setPatchTeamLeader(false);

    $scope.addUsersError = false;
    $scope.removeUsersError = false;

    self.projectLeader = -1;
    self.selectLeader = false;

    if (Hateoas.getTeamInMap(self.id)) {
        var url = Hateoas.getTeamInMap(self.id)['self'];

        $http.get(url).success(function(data, status, headers, config) {
            self.team = data.content;
            self.getTeamMembers();
            self.getProjectsInTeam();
            self.isTeamLeader = self.team.leader.id == Auth.getId();
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the team name...', data);
        });
    }
    else {
        Dialog.showErrorDialog('An error occured when loading the team. The team with the given id is not known. You\'re redirected back to the team list.');
        $location.path('/team/all');
    }

    /**
     * Gets all team members for the current team (self.team).
     */
    self.getTeamMembers = function() {
        $scope.loadMembersError = false;

        var url = self.team.links['members'];

        $http.get(url).success(function(data, status, headers, config) {
            self.members = data.content;
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the team members. Because of this, the functionality of adding users to the team and removing users from the team will not work as expected and should not be used.', data);
        });
    };

    /**
     * Edits a team name.
     */
    self.updateTeamName = function() {
        var updateTeamUrl = self.team.links['patch'];

        if (self.team.name != self.newTeamName) {
            if (self.validTeamName(self.newTeamName)) {
                var newTeam = {
                    name: self.newTeamName,
                    leader: {
                        email: self.team.leader.email
                    }
                };
                $http.patch(updateTeamUrl, newTeam).success(function(data, status, headers, config) {
                    Dialog.setAlert('patchTeamNameAlert');
                    var node = document.getElementById('patchTeamNameAlert');
                    var list = document.getElementById('alerts');
                    list.insertBefore(node,list.childNodes[0]);
                    self.team.name = self.newTeamName;
                    self.patchTeamName = false;
                }).error(function(data, status, headers, config) {
                    Dialog.showHttpErrorDialog('Updating team name failed!', data);
                });
            }
            else {
                Dialog.showErrorDialog('No valid team name entered!\n\nMake sure the team name is <strong>not empty</strong>.');
            }
        }
        else {
            self.patchTeamName = false;
        };
    };

    /**
     * Updates the team leader.
     */
    self.updateTeamLeader = function(leaderEmail) {
        var updateTeamUrl = self.team.links['patch'];

        var newTeam = {
            name: self.team.name,
            leader: {
                email: leaderEmail
            }
        };

        $http.patch(updateTeamUrl, newTeam).success(function(data, status, headers, config) {
            Dialog.setAlert('patchTeamLeaderAlert');
            var node = document.getElementById('patchTeamLeaderAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node, list.childNodes[0]);
            self.team = data.content;
            self.isTeamLeader = self.team.leader.id == Auth.getId();
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Updating team team leader failed!', data);
        });

        Nav.setPatchTeamLeader(false);
    };

    /**
     * Shows a hidden add user portion in the html page.
     */
    self.showUserAddList = function() {
        self.adding = true;
        self.otherUsers = [];

        for (var i = 0; i < List.getWholeUserList().length; i++) {
            if (!self.teamContainsUser(List.getWholeUserList()[i].id)) {
                self.otherUsers.push(List.getWholeUserList()[i]);
                self.otherUsers[self.otherUsers.length - 1].show = true;
                self.otherUsers[self.otherUsers.length - 1].add = false;
            }
        }
        self.otherUsers.sort(function(a, b) {
            return a.lastName.toLowerCase() < b.lastName.toLowerCase() ? -1 : 1 ;
        });
    };

    self.filterOtherUsers = function() {
        angular.forEach(self.otherUsers, function(value, key) {
            value.show = (value.lastName.toLowerCase().indexOf(self.userQuery.toLowerCase()) >= 0 ||
                          value.firstName.toLowerCase().indexOf(self.userQuery.toLowerCase()) >= 0);
        });
    };

    /**
     * Shows remove icons in the html page.
     */
    self.showRemoveIcons = function() {
        self.removing = true;
    };

    /**
     * Adds a user to a team.
     */
    self.addUsersToTeam = function() {
        $scope.addUsersError = false;

        var newUsers = [];
        for (var i = 0; i < self.otherUsers.length; i++) {
            if (self.otherUsers[i].add) {
                newUsers.push(self.otherUsers[i]);
            }
        }

        var count = newUsers.length;
        self.addIndex = 0;

        for (var i = 0; i < newUsers.length; i++) {
            var url = self.team.links['member_post'];
            url = url.replace(/{[a-z]*}/i, newUsers[i].id);

            $http.post(url).success(function(data, status, headers, config) {
                if (++self.addIndex == count) {
                    self.getTeamMembers();
                    Dialog.setAlert('addUserAlert');
                    var node = document.getElementById('addUserAlert');
                    var list = document.getElementById('alerts');
                    list.insertBefore(node,list.childNodes[0]);
                }
            }).error(function(data, status, headers, config) {
                $scope.addUsersError = true;
            });
        }
        self.adding = false;
    };

    /**
     * Defines error function for adding users.
     */
    $scope.$watchCollection('addUsersError', function(newValue, oldValue) {
        if (newValue && newValue != oldValue) {
            Dialog.showErrorDialog('An error occured when adding the new users to the team. Some changes might not be saved...');
        }
    });

    /**
     * Cancels the action of adding a user, changes html viewing.
     */
    self.cancelAdding = function() {
        self.adding = false;
    };

    /**
     * Removes a user from the current team.
     */
    self.removeUsersFromTeam = function() {
        $scope.removeUsersError = false;

        var oldUsers = [];
        for (var i = 0; i < self.members.length; i++) {
            if (self.members[i].remove) {
                oldUsers.push(self.members[i]);
            }
        }

        var count = oldUsers.length;
        self.removeIndex = 0;

        for (var i = 0; i < oldUsers.length; i++) {
            var url = self.team.links['member_delete'];
            url = url.replace(/{[a-z]*}/i, oldUsers[i].id);

            $http.delete(url).success(function(data, status, headers, config) {
                if (++self.removeIndex == count) {
                    self.getTeamMembers();
                    Dialog.setAlert('removeUserAlert');
                    var node = document.getElementById('removeUserAlert');
                    var list = document.getElementById('alerts');
                    list.insertBefore(node,list.childNodes[0]);
                }
            }).error(function(data, status, headers, config) {
                $scope.removeUsersError = true;
            });
        }
        self.removing = false;
    };

    /**
     * Defines an error function for deleting users.
     */
    $scope.$watchCollection('removeUsersError', function(newValue, oldValue) {
        if (newValue && newValue != oldValue) {
            Dialog.showErrorDialog('An error occured when removing the selected users from the team. Some changes might not be saved...');
        }
    });

    /**
     * Cancels the action of removing users, changes html viewing.
     */
    self.cancelRemoving = function() {
        self.removing = false;
    };

    /**
     * Checks whether a team contains a user.
     */
    self.teamContainsUser = function(id) {
        for (var i = 0; i < self.members.length; i++) {
            if (self.members[i].id == id) {
                return true;
            }
        }
        return false;
    };

    self.projectList = new Object();
    self.teams = [];

    self.create = false;
    self.projectName = "";
    self.patch = [];

    self.allOtherProjects = [];

    /**
     * Gets all projects.
     */
    self.getAllProjects = function() {
        self.allOtherProjects = [];
        var url = Path.getEndpoint('project_team_assignable');
        self.getAllProjectsOnPage(url);
    };

    /**
     * Gets all projects on the supplied url.
     */
    self.getAllProjectsOnPage = function(url) {
        $http.get(url).success(function(data, status, headers, config) {
            var result = data.content.page;

            for (var i = result.length - 1; i >= 0; i--) {
                for (var j = 0; j < self.projectList.length; j++) {
                    if (self.projectList[j].name == result[i].name) {
                        result.splice(i, 1);
                        break;
                    }
                }
            }

            for (var i = 0; i < result.length; i++) {
                self.allOtherProjects.push(result[i]);
                self.allOtherProjects[self.allOtherProjects.length - 1].toAdd = false;
            }

            if (data.content.metadata.hasNextPage) {
                self.getAllProjectsOnPage(data.content.links.next);
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the projects for this page...', data);
        });
    };

    /**
     * Creates a project by using http POST.
     */
    self.createProject = function() {
        var createProjectUrl = Path.getEndpoint('project');

        var newProject = {
            name: Format.formatToJs(self.projectName),
            leader: {
                email: self.projectLeaderEmail
            }
        };

        if (self.validProjectName(self.projectName)) {
            if (self.projectLeader != -1) {
                $http.post(createProjectUrl, newProject).success(function(data, status, headers, config) {
                    var addProjectToTeamUrl = self.team.links['project_post'];
                    addProjectToTeamUrl = addProjectToTeamUrl.replace(/{[a-z]*}/i, Format.formatToJs(self.projectName));
                    $http.post(addProjectToTeamUrl).success(function(data, status, headers, config) {
                        self.projectName = "";
                        self.create = false;
                        self.getProjectsInTeam();
                        Dialog.setAlert('createProjectAlert');
                        var node = document.getElementById('createProjectAlert');
                        var list = document.getElementById('alerts');
                        list.insertBefore(node,list.childNodes[0]);
                    }).error(function(data, status, headers, config) {
                        self.projectName = "";
                        Dialog.showHttpErrorDialog('Adding project to team failed!', data);
                    });
                }).error(function(data, status, headers, config) {
                    self.projectName = "";
                    Dialog.showHttpErrorDialog('Creating project failed!', data);
                });
            }
            else {
                Dialog.showErrorDialog('No valid project leader selected! Make sure to select a project leader!');
            }
        }
        else {
            self.projectName = "";
            Dialog.showErrorDialog('No valid project name entered!\n\nMake sure the project name is <strong>not empty</strong> and <strong>contains no points</strong>.');
        }
        self.projectLeader = -1;
        self.selectLeader = false;
    };

    /**
     * Prompts user to delete the project.
     */
    self.deleteProject = function(projectName) {
        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_DANGER,
            title: 'Delete project ?',
            closable: false,
            cssClass: 'small-dialog',
            message: 'Are you sure you want to delete this project? All entities in this project will be deleted as well.\n<strong>This cannot be undone!</strong>',
            buttons: [{
                icon: 'glyphicon glyphicon-ok',
                label: 'Delete',
                cssClass: 'btn btn-success',
                action: function(dialogItself) {
                    self.deleteProjectForSure(projectName);
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
     * Irreversibly removes a project.
     */
    self.deleteProjectForSure = function(projectName) {
        var deleteProjectUrl = Hateoas.getProjectInMap(projectName)['delete'];

        $http.delete(deleteProjectUrl).success(function(data, status, headers, config) {
            Dialog.setAlert('deleteProjectAlert');
            var node = document.getElementById('deleteProjectAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node,list.childNodes[0]);
            self.getProjectsInTeam();
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Deleting project failed!', data);
        });
    };

    /**
     * Prompts the user to delete project from team.
     */
    self.removeProjectFromTeam = function(projectName) {
        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_DANGER,
            title: 'Remove project from team?',
            closable: false,
            cssClass: 'small-dialog',
            message: 'Are you sure you want to remove this project from the team? This means the project will not be permanently deleted, but will no longer be assigned to this team.',
            buttons: [{
                icon: 'glyphicon glyphicon-ok',
                label: 'Remove from team',
                cssClass: 'btn btn-success',
                action: function(dialogItself) {
                    self.removeProjectFromTeamForSure(projectName);
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
     * Removes a project from this team.
     */
    self.removeProjectFromTeamForSure = function(projectName) {
        var url = self.team.links['project_delete'];
        url = url.replace(/{[a-z]*}/i, projectName);

        $http.delete(url).success(function(data, status, headers, config) {
            self.getProjectsInTeam();
            Dialog.setAlert('removeProjectAlert');
            var node = document.getElementById('removeProjectAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node,list.childNodes[0]);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Removing project from team failed!', data);
        });
    };

    /**
     * Adds existing projects to a team.
     */
    self.addExistingProjectsToTeam = function() {
        var addProjectToTeamUrl = self.team.links['project_post'];

        self.addCount = 0;
        self.addingCount = 0;
        for (var i = 0; i < self.allOtherProjects.length; i++) {
            if (self.allOtherProjects[i].toAdd == true) {
                self.addCount++;
            }
        }

        for (var i = 0; i < self.allOtherProjects.length; i++) {
            if (self.allOtherProjects[i].toAdd) {
                var currentAddProjectToTeamUrl = addProjectToTeamUrl.replace(/{[a-z]*}/i, self.allOtherProjects[i].name);

                $http.post(currentAddProjectToTeamUrl).success(function(data, status, headers, config) {
                    self.addingCount++;
                    if (self.addingCount == self.addCount) {
                        self.getProjectsInTeam();
                        Dialog.setAlert('addProjectAlert');
                        var node = document.getElementById('addProjectAlert');
                        var list = document.getElementById('alerts');
                        list.insertBefore(node,list.childNodes[0]);
                        Nav.setAddExistingProjects(false);
                    }
                }).error(function(data, status, headers, config) {
                    $scope.error = true;
                    if (self.addingCount == self.addCount) {
                        Nav.setAddExistingProjects(false);
                    }
                });
            }
        }
    };

    $scope.$watchCollection('error', function(newValue, oldValue) {
        if (newValue && newValue != oldValue) {
            Dialog.showErrorDialog('Adding existing projects to team failed!');
        }
    });

    /**
     * Retrieves all projects corresponding to the current team.
     */
    self.getProjectsInTeam = function() {
        var url = self.team.links['projects'];
        $http.get(url).success(function(data, status, headers, config) {
            var output = [];
            Hateoas.cleanProjectMap();
            angular.forEach(data.content, function(value, key) {
                Hateoas.setProjectInMap(value.name, value.links);
                self.patch[value.name] = false;
                output.push(value);
            });

            self.projectList = output.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });

            if (Auth.isAdmin() || Auth.getId() == self.team.leader.id) {
                self.getAllProjects();
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the projects for this team...', data);
        });
    };

    /**
     * Verifies if a project name is valid.
     */
    self.validProjectName = function(projectName) {
        if (projectName == '') {
            return false;
        }
        if (projectName.indexOf('.') != -1) {
            return false;
        }
        return true;
    };

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

