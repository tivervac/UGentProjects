/**
 * Controller for the project details page, this page consists of all
 * information about a project, including actors, use cases and concepts.
 */
var projectDetailsController = angular.module('projectDetailsController', []);

projectDetailsController.controller('ProjectDetailsCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', 'Path', 'List', 'Dialog', 'Hateoas', 'Format', 'Auth', 'Nav', function($scope, $rootScope, $routeParams, $http, $location, Path, List, Dialog, Hateoas, Format, Auth, Nav) {

    var self = this;

    self.projectName = $routeParams.projectName;

    if (Dialog.getInitNeeded()) {
        Dialog.initAlerts();
    }
    else {
        Dialog.setInitNeeded(true);
    }
    self.actorList = [];
    self.conceptList = [];
    self.usecaseList = [];
    self.processList = [];
    self.members = [];

    self.exportProjects = [];

    self.actorName = "";

    self.patchActor = [];
    self.patchConcept = [];
    self.patchUsecase = [];
    self.patchProcess = [];
    self.importableUsecases = [];

    self.showConceptForm = false;
    self.showUsecaseForm = false;

    Nav.setPatchProjectLeader(false);
    self.patchProjectName = false;
    Nav.setImportUsecase(false);
    self.patchSchedule = false;

    self.year = "yyyy";
    self.month = "mm";
    self.day = "dd";

    $scope.currentUsecasePage = 1;
    $scope.currentActorPage = 1;
    $scope.currentConceptPage = 1;
    $scope.currentProcessPage = 1;
    self.options = [
        { name: '5', value: 5 },
        { name: '10', value: 10 },
        { name: '20', value: 20 },
        { name: 'All', value: -1 }
    ];

    if (Hateoas.getProjectInMap(self.projectName)) {
        var url = Hateoas.getProjectInMap(self.projectName)['self'];

        $http.get(url).success(function(data, status, headers, config) {
            self.project = data.content;

            self.getActorsByProject();
            self.getConceptsByProject();
            self.getUsecasesByProject();
            self.getProcessesByProject();

            self.isProjectLeader = self.project.leader.id == Auth.getId();

            if (Auth.isAdmin() || self.isProjectLeader) {
                self.getProjectMembers();
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the project name and leader...', data);
        });
    }
    else {
        Dialog.showErrorDialog('Something went wrong when loading the project details. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    /**
     * Gets all exported projects of each page.
     */
    self.getExportProjects = function(url) {
        $http.get(url).success(function(data, status, headers, config) {
            angular.forEach(data.content.page, function(value, key) {
                if (value.name != self.projectName) {
                    self.exportProjects.push(value);
                }
            });
            if (data.content.metadata.hasNextPage) {
                self.getExportProjects(data.content.links.next);
            }
            else {
                self.exportProjects = self.exportProjects.sort(function(a, b) {
                    return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
                });
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Retrieving list of export projects failed. This means exporting projects will not be possible.', data);
        });
    };

    /**
     * Finds use cases which can be imported.
     */
    self.getImportableUsecases = function(url, teamName) {
        $http.get(url).success(function(data, status, headers, config) {
            angular.forEach(data.content, function(value, key) {
                var project = value;
                if (self.projectName != value.name) {
                    $http.get(value.links['usecases']).success(function(data, status, headers, config) {
                        angular.forEach(data.content,function(value,key) {
                            var temp = new Object();
                            temp.name = value.name;
                            temp.project = project.name;
                            temp.team = teamName;
                            temp.toImport = false;
                            temp.links = value.links;
                            self.importableUsecases.push(temp);
                        });
                        self.importableUsecases = self.importableUsecases.sort(function(a, b) {
                            return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
                        });
                    }).error(function(data, status, headers, config) {
                        $scope.loadError = true;
                    });
                }
            });
        }).error(function(data, status, headers, config) {
            $scope.loadError = true;
        });
    };

    /**
     * Imports an admin.
     */
    self.importAdmin = function(url) {
        $http.get(url).success(function(data, status, headers, config) {
            angular.forEach(data.content.page, function(value, key) {
                self.getImportableUsecases(value.links['projects'], value.name);
            });
            if (data.content.metadata.hasNextPage) {
                self.importAdmin(data.content.links.next);
            }
        }).error(function(data, status, headers, config) {
            $scope.loadError = true;
        });
    };

    if (Auth.isAdmin()) {
        var url = Path.getEndpoint('team')
        self.importAdmin(url);
    }
    else {
        var output = [];
        var getUserUrl = Path.getEndpoint('login');
        $http.get(getUserUrl).success(function(data, status, headers, config) {
            $http.get(data.content.links['teams']).success(function(data, status, headers, config) {
                angular.forEach(data.content, function(value, key) {
                    self.getImportableUsecases(value.links['projects'], value.name);
                });
            }).error(function(data, status, headers, config) {
                $scope.loadError = true;
            });
        }).error(function(data, status, headers, config) {
            $scope.loadError = true;
        });
    }

    /**
     * Imports use cases.
     */
    self.importUsecases = function() {
        angular.forEach(self.importableUsecases,function(value,key) {
            if (value.toImport == true) {
                var url = value.links['self'];
                var requestBody = {
                    destination: self.projectName,
                    action: 'copy'
                };
                $http.put(url, requestBody).success(function(data, status, headers, config) {
                    Dialog.setAlert('importUsecaseAlert');
                    Dialog.setInitNeeded(false);
                    var node = document.getElementById('importUsecaseAlert');
                    var list = document.getElementById('alerts');
                    list.insertBefore(node, list.childNodes[0]);
                    Hateoas.setUsecaseInMap(self.projectName,value.name,value.links);
                    self.getUsecasesByProject();
                }).error(function(data, status, headers, config) {
                    Dialog.showHttpErrorDialog('Importing use case failed!', data);
                });
            }
        });
        Nav.setImportUsecase(false);
    };

    if (Auth.isAdmin()) {
        self.getExportProjects(Path.getEndpoint('project'));
    }
    else {
        var output = [];
        var getUserUrl = Path.getEndpoint('login');

        $http.get(getUserUrl).success(function(data, status, headers, config) {
            var getTeamUrl = data.content.links['teams'];
            $http.get(getTeamUrl).success(function(data, status, headers, config) {
                var teams = data.content;

                angular.forEach(teams, function(value, key) {
                    var getProjectsUrl = value.links['projects'];

                    $http.get(getProjectsUrl).success(function(data, status, headers, config) {
                        angular.forEach(data.content, function(value, key) {
                            if (value.name != self.projectName) {
                                output.push(value);
                            }
                        });
                        self.exportProjects = output.sort(function(a, b) {
                            return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
                        });
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
    }

    /**
     * Defines a load error action, displays a dialog.
     */
    $scope.$watchCollection('loadError', function(newValue, oldValue) {
        if (newValue && newValue != oldValue) {
            Dialog.showErrorDialog('Retrieving list of export projects or import use cases failed. This means exporting to projects and importing use cases will not be possible.');
        }
    });

    /**
     * Retrieves all memebers of a project.
     */
    self.getProjectMembers = function() {
        var url = self.project.links['eligible_analyst'];

        $http.get(url).success(function(data, status, headers, config) {
            self.members = data.content;
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Retrieving list of possible project leaders failed. This means updating the project leader will not be possible.', data);
        });
    };

    /**
     * Updates a project name.
     */
    self.updateProjectName = function() {
        var updateProjectUrl = self.project.links['patch'];

        if (self.project.name != Format.formatToJs(self.newProjectName)) {
            if (self.validName(Format.formatToJs(self.newProjectName))) {
                var newProject = {
                    name: Format.formatToJs(self.newProjectName),
                    leader: {
                        email: self.project.leader.email
                    }
                };

                $http.patch(updateProjectUrl, newProject).success(function(data, status, headers, config) {
                    Dialog.setAlert('patchProjectNameAlert');
                    Dialog.setInitNeeded(false);
                    var node = document.getElementById('patchProjectNameAlert');
                    var list = document.getElementById('alerts');
                    list.insertBefore(node,list.childNodes[0]);

                    Auth.updateAnalystProjects();
                    Hateoas.setProjectInMap(self.projectName, null);
                    Hateoas.setProjectInMap(Format.formatToJs(self.newProjectName), data.content.links);
                    $location.path('/projects/' + Format.formatToJs(self.newProjectName));

                }).error(function(data, status, headers, config) {
                    Dialog.showHttpErrorDialog('Updating project name failed!', data);
                });
            }
            else {
                Dialog.showErrorDialog('No valid project name entered!\n\nMake sure the project name is <strong>not empty</strong>.');
            }
        }
        else {
            self.patchProjectName = false;
        }
    };

    /**
     * Updates the project leader.
     */
    self.updateProjectLeader = function(leaderEmail) {
        var updateProjectUrl = self.project.links['patch'];

        var newProject = {
            name: self.projectName,
            leader: {
                email: leaderEmail
            }
        };
        $http.patch(updateProjectUrl, newProject).success(function(data, status, headers, config) {
            Dialog.setAlert('patchProjectLeaderAlert');
            var node = document.getElementById('patchProjectLeaderAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node,list.childNodes[0]);

            self.project = data.content;
            self.isProjectLeader = self.project.leader.id == Auth.getId();
            Auth.updateAnalystProjects();
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Updating project leader failed!', data);
        });
        Nav.setPatchProjectLeader(false);
    };

    /**
     * Creates a new actor in the project.
     */
    self.createActor = function(actorName) {
        if (self.validName(actorName)) {
            var createActorUrl = Hateoas.getProjectInMap(self.projectName)['actor_add'];
            var newActor = { name: Format.formatToJs(actorName) };

            $http.post(createActorUrl, newActor).success(function(data, status, headers, config) {
                document.getElementById('createActor').value = "";
                Dialog.setAlert('createActorAlert');
                var node = document.getElementById('createActorAlert');
                var list = document.getElementById('alerts');
                list.insertBefore(node,list.childNodes[0]);
                self.getActorsByProject();
                if (!self.currentActorPage) {
                    self.currentActorPage = 1;
                }
            }).error(function(data, status, headers, config) {
                Dialog.showHttpErrorDialog('Creating actor failed!', data);
            });
        }
        else {
            Dialog.showErrorDialog('No valid actor name entered!\n\nMake sure the actor name is <strong>not empty</strong>.');
        }
    };

    /**
     * Gets all actors of a project.
     */
    self.getActorsByProject = function() {
        var getAllActorsUrl = Hateoas.getProjectInMap(self.projectName)['actors'];
        Hateoas.cleanActorMap(self.projectName);

        $http.get(getAllActorsUrl).success(function(data, status, headers, config) {
            self.actorList = data.content.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });
            List.setWholeActorList(self.actorList);
            angular.forEach(self.actorList, function(value, key) {
                self.patchActor[value.name] = false;
                Hateoas.setActorInMap(self.projectName, value.name, value.links);
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the actors in this project...', data);
        });
    };

    /**
     * Deletes an actor from the project.
     */
    self.deleteActor = function(index) {
        var checkReferencesUrl = self.actorList[index].links['self'];
        var action = { action: 'reference' };

        $http.put(checkReferencesUrl, action).success(function(data, status, headers, config) {
            if (data.content.actors.length == 0 && data.content.concepts.length == 0 && data.content.useCases.length == 0) {
                BootstrapDialog.show({
                    type: BootstrapDialog.TYPE_DANGER,
                    title: 'Delete actor?',
                    closable: false,
                    cssClass: 'small-dialog',
                    message: 'Are you sure you want to delete this actor?',
                    buttons: [{
                        icon: 'glyphicon glyphicon-ok',
                        label: 'Delete',
                        cssClass: 'btn btn-success',
                        action: function(dialogItself) {
                            var deleteActorUrl = self.actorList[index].links['delete'];
                            $http.delete(deleteActorUrl).success(function(data, status, headers, config) {
                                Dialog.setAlert('deleteActorAlert');
                                var node = document.getElementById('deleteActorAlert');
                                var list = document.getElementById('alerts');
                                list.insertBefore(node,list.childNodes[0]);
                                self.getActorsByProject();

                            }).error(function(data, status, headers, config) {
                                Dialog.showHttpErrorDialog('Deleting actor failed!', data);
                            });
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
                var message = 'You cannot delete this actor because the this actor is still referenced by the following entities:<ul>';
                for (var i = 0; i < data.content.actors.length; i++) {
                    message += '<li>' + data.content.actors[i].name + ' (actor)</li>';
                }
                for (var i = 0; i < data.content.concepts.length; i++) {
                    message += '<li>' + data.content.concepts[i].name + ' (concept)</li>';
                }
                for (var i = 0; i < data.content.useCases.length; i++) {
                    message += '<li>' + data.content.useCases[i].name + ' (use case)</li>';
                }
                message += '</ul>\nIf you do want to delete this actor, you will first have to delete these entities or at least remove the references to this actor in these entities.';
                Dialog.showErrorDialog(message);
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Checking references to this actor failed. Because of this, it can not be assured that this actor is no longer referenced in this project. Deleting this actor will therefore not be possible.', data);
        });
    };

    /**
     * Edits an actor in the project.
     */
    self.patchActor = function(index) {
        if (self.validName(self.actorName)) {
            var updateActorUrl = self.actorList[index].links['patch'];
            var newActor = { name: Format.formatToJs(self.actorName) };

            $http.patch(updateActorUrl, newActor).success(function(data, status, headers, config) {
                Dialog.setAlert('patchActorAlert');
                var node = document.getElementById('patchActorAlert');
                var list = document.getElementById('alerts');
                list.insertBefore(node,list.childNodes[0]);
                self.patchActor[self.actorList[index].name] = false;
                self.getActorsByProject();
            }).error(function(data, status, headers, config) {
                Dialog.showHttpErrorDialog('Updating actor failed!', data);
            });
        }
        else {
            Dialog.showErrorDialog('No valid actor name entered!\n\nMake sure the actor name is <strong>not empty</strong>.');
        }
    };

    /**
     * Gets all concepts in the current project.
     */
    self.getConceptsByProject = function() {
        var getAllConceptsUrl = Hateoas.getProjectInMap(self.projectName)['concepts'];
        Hateoas.cleanConceptMap(self.projectName);
        List.setWholeConceptList([]);

        $http.get(getAllConceptsUrl).success(function(data, status, headers, config) {
            self.conceptList = data.content.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });
            List.setWholeConceptList(self.conceptList);
            angular.forEach(data.content, function(value, key) {
                self.patchConcept[value.name] = false;
                Hateoas.setConceptInMap(self.projectName, value.name, value.links);
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the concepts in this project...', data);
        });
    };

    /**
     * Deletes a concept from the project.
     */
    self.deleteConcept = function(index) {
        var checkReferencesUrl = self.conceptList[index].links['self'];
        var action = { action: 'reference' };

        $http.put(checkReferencesUrl, action).success(function(data, status, headers, config) {
            if (data.content.actors.length == 0 && data.content.concepts.length == 0 && data.content.useCases.length == 0) {
                BootstrapDialog.show({
                    type: BootstrapDialog.TYPE_DANGER,
                    title: 'Delete concept ?',
                    closable: false,
                    cssClass: 'small-dialog',
                    message: 'Are you sure you want to delete this concept?',
                    buttons: [{
                        icon: 'glyphicon glyphicon-ok',
                        label: 'Delete',
                        cssClass: 'btn btn-success',
                        action: function(dialogItself) {
                            var deleteConceptUrl = self.conceptList[index].links['delete'];
                            $http.delete(deleteConceptUrl).success(function(data, status, headers, config) {
                                Dialog.setAlert('deleteConceptAlert');
                                var node = document.getElementById('deleteConceptAlert');
                                var list = document.getElementById('alerts');
                                list.insertBefore(node,list.childNodes[0]);
                                self.getConceptsByProject();
                            }).error(function(data, status, headers, config) {
                                Dialog.showHttpErrorDialog('Deleting concept failed!', data)
                            });
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
                var message = 'You cannot delete this concept because this concept is still referenced by the following entities:<ul>';
                for (var i = 0; i < data.content.actors.length; i++) {
                    message += '<li>' + data.content.actors[i].name + ' (actor)</li>';
                }
                for (var i = 0; i < data.content.concepts.length; i++) {
                    message += '<li>' + data.content.concepts[i].name + ' (concept)</li>';
                }
                for (var i = 0; i < data.content.useCases.length; i++) {
                    message += '<li>' + data.content.useCases[i].name + ' (use case)</li>';
                }
                message += '</ul>\nIf you do want to delete this concept, you will first have to delete these entities or at least remove the references to this concept in these entities.';
                Dialog.showErrorDialog(message);
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Checking references to this concept failed. Because of this, it can not be assured that this concept is no longer referenced in this project. Deleting this concept will therefore not be possible.', data);
        });
    };

    /**
     * Get all use cases contained in this project.
     */
    self.getUsecasesByProject = function() {
        var getAllUsecasesUrl = Hateoas.getProjectInMap(self.projectName)['usecases'];
        Hateoas.cleanUsecaseMap(self.projectName);
        List.setWholeUseCaseList([]);

        $http.get(getAllUsecasesUrl).success(function(data, status, headers, config) {
            self.usecaseList = data.content.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });
            List.setWholeUseCaseList(self.usecaseList);
            angular.forEach(self.usecaseList, function(value, key) {
                self.patchUsecase[value.name] = false;
                Hateoas.setUsecaseInMap(self.projectName, value.name, value.links);
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the use cases in this project...', data);
        });
    };

    /**
     * Prompts user to delete use case.
     */
    self.deleteUsecase = function(index) {
        var checkReferencesUrl = self.usecaseList[index].links['self'];
        var action = { action: 'reference' };

        $http.put(checkReferencesUrl, action).success(function(data, status, headers, config) {
            if (data.content.actors.length == 0 && data.content.concepts.length == 0 && data.content.useCases.length == 0) {
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
                            self.deleteUsecaseForSure(index);
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
                var message = 'You cannot delete this use case because this use case is still referenced by the following entities:<ul>';
                for (var i = 0; i < data.content.actors.length; i++) {
                    message += '<li>' + data.content.actors[i].name + ' (actor)</li>';
                }
                for (var i = 0; i < data.content.concepts.length; i++) {
                    message += '<li>' + data.content.concepts[i].name + ' (concept)</li>';
                }
                for (var i = 0; i < data.content.useCases.length; i++) {
                    message += '<li>' + data.content.useCases[i].name + ' (use case)</li>';
                }
                message += '</ul>\nIf you do want to delete this use case, you will first have to delete these entities or at least remove the references to this use case in these entities.';
                Dialog.showErrorDialog(message);
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Checking references to this use case failed. Because of this, it can not be assured that this concept is no longer referenced in this project. Deleting this use case will therefore not be possible.', data);
        });
    };

    /**
     * Irreversibly deletes a use case from the project.
     */
    self.deleteUsecaseForSure = function(index) {
        var deleteUsecaseUrl = self.usecaseList[index].links['delete'];

        $http.delete(deleteUsecaseUrl).success(function(data, status, headers, config) {
            Dialog.setAlert('deleteUsecaseAlert');
            var node = document.getElementById('deleteUsecaseAlert');
            var list = document.getElementById('alerts');
            list.insertBefore(node,list.childNodes[0]);
            self.getUsecasesByProject();
        }).error(function(data, status, headers, config) {
           Dialog.showHttpErrorDialog('Deleting use case failed!', data);
        });
    };

    /**
     * Validates a use case.
     */
    self.validateUsecase = function(index) {
        var validationUrl = self.usecaseList[index].links['self'];
        var validationObject = { action: "validation" };

        $http.put(validationUrl, validationObject).success(function(data, status, headers, config) {
            if (data.content.length == 0) {
                BootstrapDialog.show({
                    type: BootstrapDialog.TYPE_SUCCESS,
                    title: 'Success!',
                    closable: false,
                    cssClass: 'small-dialog',
                    message: '<span class="glyphicon glyphicon-thumbs-up"></span> <strong>Usecase succesfully validated</strong> <span class="glyphicon glyphicon-thumbs-up"></span>',
                    buttons: [{
                        label: 'Ok',
                        cssClass: 'btn btn-success',
                        action: function(dialogItself) {
                            dialogItself.close();
                        }
                    }]
                });
            }
            else {
                var message = '<strong>The use case is not valid. The validation problems are:</strong><br><br>';
                for (var i = 0; i < data.content.length; i++) {
                    message += '<span class="glyphicon glyphicon-thumbs-down"></span> &nbsp;' + data.content[i] + '<br>';
                }

                BootstrapDialog.show({
                    type: BootstrapDialog.TYPE_DANGER,
                    title: 'Usecase not valid',
                    closable: false,
                    cssClass: 'medium-dialog',
                    message: message,
                    buttons: [{
                        icon: "glyphicon glyphicon-pencil",
                        label: 'Edit',
                        cssClass: 'btn btn-warning',
                        action: function(dialogItself) {
                            $location.path('/projects/' + self.projectName + '/usecases/' + self.usecaseList[index].name + '/edit');
                            $rootScope.$apply();
                            dialogItself.close();
                        }
                    }, {
                        label: 'Ok',
                        cssClass: 'btn btn-danger',
                        action: function(dialogItself) {
                            dialogItself.close();
                        }
                    }]
                });
            }

        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Validating use case failed!\n\nThis means something went wrong when validating the use case. This doesn\'t necessarily mean that the use case is invalid.', data);
        });
    };

    /**
     * Exports a use case.
     */
    self.exportUsecase = function(usecaseIndex, projectName) {
        var exportUrl = self.usecaseList[usecaseIndex].links['self'];
        var requestBody = {
            destination: projectName,
            action: 'copy'
        };

        $http.put(exportUrl, requestBody).success(function(data, status, headers, config) {
            Dialog.setAlert('exportUsecaseAlert');
            Hateoas.setUsecaseInMap(projectName,self.usecaseList[usecaseIndex].name,self.usecaseList[usecaseIndex].links);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Exporting use case failed!', data);
        });
    };

    /**
     * Returns use case pages from db.
     */
    self.getUsecasePages = function() {
        if (self.usecasesPerPage.value != -1) {
            var output = [];
            var total = Math.ceil(List.getUseCaseList().length / self.usecasesPerPage.value);
            for (var i = 1; i <= total; i++) {
                output.push(i);
            }
            return output;
        }
        else {
            return [];
        }
    };

    /**
     * Returns actor pages from db.
     */
    self.getActorPages = function() {
        if (self.actorsPerPage.value != -1) {
            var output = [];
            var total = Math.ceil(List.getActorList().length / self.actorsPerPage.value);
            for (var i = 1; i <= total; i++) {
                output.push(i);
            }
            return output;
        }
        else {
            return [];
        }
    };

    /**
     * Returns concept pages from db.
     */
    self.getConceptPages = function() {
        if (self.conceptsPerPage.value != -1) {
            var output = [];
            var total = Math.ceil(List.getConceptList().length / self.conceptsPerPage.value);
            for (var i = 1; i <= total; i++) {
                output.push(i);
            }
            return output;
        }
        else {
            return [];
        }
    };

    /**
     * Returns process pages from db.
     */
    self.getProcessPages = function() {
        if (self.processesPerPage.value != -1) {
            var output = [];
            var total = Math.ceil(List.getProcessList().length / self.processesPerPage.value);
            for (var i = 1; i <= total; i++) {
                output.push(i);
            }
            return output;
        }
        else {
            return [];
        }
    };

    /**
     * Prompts the use for process deletion.
     */
    self.deleteProcess = function(index) {
        var checkReferencesUrl = self.processList[index].links['self'];
        var action = { action: 'reference' };

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
                    var deleteProcessUrl = self.processList[index].links['delete'];
                    $http.delete(deleteProcessUrl).success(function(data, status, headers, config) {
                        Dialog.setAlert('deleteProcessAlert');
                        var node = document.getElementById('deleteProcessAlert');
                        var list = document.getElementById('alerts');
                        list.insertBefore(node, list.childNodes[0]);
                        self.getProcessesByProject();
                    }).error(function(data, status, headers, config) {
                        Dialog.showHttpErrorDialog('Deleting process failed!', data)
                    });
                    dialogItself.close();
                    self.getProcessesByProject();
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
     * Gets all processes corresponding to the current project.
     */
    self.getProcessesByProject = function() {
        var getAllProcessesUrl = Hateoas.getProjectInMap(self.projectName)['processes'];
        Hateoas.cleanProcessMap(self.projectName);
        List.setWholeProcessList([]);

        $http.get(getAllProcessesUrl).success(function(data, status, headers, config) {
            self.processList = data.content.sort(function(a, b) {
                return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : 1 ;
            });
            List.setWholeProcessList(self.processList);
            angular.forEach(self.processList, function(value, key) {
                self.patchProcess[value.name] = false;
                Hateoas.setProcessInMap(self.projectName, value.name, value.links);
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the processes in this project...', data);
        });
    };

    /* Handle pages */

    $scope.$watch('currentUsecasePage', function(newValue, oldValue) {
        if (!newValue) {
            $scope.currentUsecasePage = 1;
        }
    });

    $scope.$watch('currentActorPage', function(newValue, oldValue) {
        if (!newValue) {
            $scope.currentActorPage = 1;
        }
    });

    $scope.$watch('currentConceptPage', function(newValue, oldValue) {
        if (!newValue) {
            $scope.currentConceptPage = 1;
        }
    });

    $scope.$watch('currentProcessPage', function(newValue, oldValue) {
        if (!newValue) {
            $scope.currentProcessPage = 1;
        }
    });

    /**
     * Validates actor name.
     */
    self.validName = function(name) {
        if (name == '') {
            return false;
        }
        return true;
    };

    /**
     * Creates a schedule for the current project.
     */
    self.schedule = function(){
        var date = self.year + '-' + self.month + '-' + self.day;
        if (self.validateDate(self.year, self.month, self.day)) {
            var scheduleObject = {
                action: 'schedule',
                start: date
            };
            var scheduleUrl = Hateoas.getProjectInMap(self.projectName)['schedule'];

            $http.put(scheduleUrl,scheduleObject).success(function(data, status, headers, config) {
                Dialog.setAlert('scheduleAlert');
                var node = document.getElementById('scheduleAlert');
                var list = document.getElementById('alerts');
                list.insertBefore(node, list.childNodes[0]);
            }).error(function(data, status, headers, config) {
                Dialog.showErrorDialog('An error occured when creating the schedule for this project...');
            });
            self.patchSchedule = false;
        }
        else {
            Dialog.showErrorDialog('You entered an <strong>invalid date</strong>.');
        }
    };

    /**
     * Validates the given date.
     */
    self.validateDate = function(year,month,day) {
        if (self.isEmpty(year) || self.isEmpty(month) || self.isEmpty(day)) {
            return false;
        }
        if (year.length != 4) {
            return false;
        }
        if (month.length != 2) {
            return false;
        }
        if (day.length != 2) {
            return false;
        }
        var date = year + "-" + month + "-" + day;
        if ((new Date(date)).toString() == "Invalid Date") {
            return false;
        }
        return true;
    };

    /**
     * Returns whether the given string is empty.
     */
    self.isEmpty = function(value) {
        if (value == "") {
            return true;
        }
        return false;
    };

}]);

