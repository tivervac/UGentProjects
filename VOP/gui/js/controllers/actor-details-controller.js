/**
 * Controller for the actor details page, this page shows a certain actor
 * including all it's assets and attributes.
 */
var actorDetailsController = angular.module('actorDetailsController', []);

actorDetailsController.controller('ActorDetailsCtrl', ['$scope', '$routeParams', '$http', '$location', 'Dialog', 'Path', 'Hateoas', 'Format', 'Auth', function($scope, $routeParams, $http, $location, Dialog, Path, Hateoas, Format, Auth) {

    var self = this;

    if (Dialog.getInitNeeded()) {
        Dialog.initAlerts();
    }
    else {
        Dialog.setInitNeeded(true);
    }
    self.projectName = $routeParams.projectName;
    self.actorName = $routeParams.actorName;

    self.references = [];

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
        Dialog.showErrorDialog('Something went wrong when loading the actor details. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    if (!Hateoas.getActorInMap(self.projectName, self.actorName)) {
        $location.path('/projects/' + self.projectName);
        Dialog.showErrorDialog('An error occured when loading the actor details. The given actor is unknown. You\'re redirected back to the project page...');
    }

    /**
     * Requests user input on whether an actor should be deleted.
     */
    self.deleteActor = function() {
        if (self.referencesLoaded) {
            if (self.references.length == 0) {
                BootstrapDialog.show({
                    type: BootstrapDialog.TYPE_DANGER,
                    title: 'Delete actor ?',
                    closable: false,
                    cssClass: 'small-dialog',
                    message: 'Are you sure you want to delete this actor?',
                    buttons: [{
                        icon: 'glyphicon glyphicon-ok',
                        label: 'Delete',
                        cssClass: 'btn btn-success',
                        action: function(dialogItself) {
                            self.deleteActorForSure();
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
                var message = 'You cannot delete this actor because this actor is still referenced by some use cases. See the \'Actor referenced by\' section on this page to see an overview of these use cases.\n\nIf you do want to delete this actor, you will first have to delete these use cases or at least remove the references to this actor in these use cases.';
                Dialog.showErrorDialog(message);
            }
        }
    };

    /**
     * Deletes an actor.
     */
    self.deleteActorForSure = function() {
        var deleteActorUrl = Hateoas.getActorInMap(self.projectName, self.actorName)['delete'];

        $http.delete(deleteActorUrl).success(function(data, status, headers, config) {
            Dialog.setInitNeeded(false);
            Dialog.setAlert('deleteActorAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Deleting actor failed!', data);
        });
    };

    /**
     * Edits an actor.
     */
    self.updateActorName = function(index) {
        if (self.validName(self.newActorName)) {
            var updateActorUrl = Hateoas.getActorInMap(self.projectName, self.actorName)['patch'];
            var newActor = { name: Format.formatToJs(self.newActorName) };

            $http.patch(updateActorUrl, newActor).success(function(data, status, headers, config) {
                    Dialog.setAlert('patchActorAlert');
                    Dialog.setInitNeeded(false);
                    Hateoas.setActorInMap(self.projectName, self.actorName, null);
                    Hateoas.setActorInMap(self.projectName, Format.formatToJs(self.newActorName), data.content.links);
                    $location.path('/projects/' + self.projectName + '/actors/' + Format.formatToJs(self.newActorName));
            }).error(function(data, status, headers, config) {
                Dialog.showHttpErrorDialog('Updating actor failed!', data);
            });
        }
        else {
            Dialog.showErrorDialog('No valid actor name entered!\n\nMake sure the actor name is <strong>not empty</strong>.');
        }
    };

    /**
     * Returns all references, i.e. use cases, concepts and actors.
     */
    self.getReferences = function() {
        self.referencesLoaded = false;
        var referencesUrl = Hateoas.getActorInMap(self.projectName, self.actorName)['self'];
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
            Dialog.showHttpErrorDialog('Failed to load references to this actor!', data);
        });
    };
    self.getReferences();

    /**
     * Validates actor name.
     */
    self.validName = function(name) {
        if (name == '') {
            return false;
        }
        return true;
    };

}]);

