/**
 * Controller for the concept details page, this page shows a certain concept
 * including all it's assets and attributes.
 */
var conceptDetailsController = angular.module('conceptDetailsController', []);

conceptDetailsController.controller('ConceptDetailsCtrl', ['$scope', '$routeParams', '$http', '$location', 'Dialog', 'Path', 'Hateoas', 'Auth', function($scope, $routeParams, $http, $location, Dialog, Path, Hateoas, Auth) {

    var self = this;

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);

    self.projectName = $routeParams.projectName;
    self.conceptName = $routeParams.conceptName;

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
        Dialog.showErrorDialog('Something went wrong when loading the concept details. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    if (Hateoas.getConceptInMap(self.projectName, self.conceptName)) {
        var getConceptUrl =  Hateoas.getConceptInMap(self.projectName, self.conceptName)['self'];
        $http.get(getConceptUrl).success(function(data, status, headers, config) {
            self.conceptDefinition = self.parseDefinition(data.content.definition);
            self.conceptAttributes = data.content.attributes;
        }).error(function(data, status, headers, config) {
            $location.path('/projects/' + self.projectName);
            Dialog.showHttpErrorDialog('An error occured when loading the concept details, you\'re redirected back to the project page...', data);
        });
    }
    else {
        $location.path('/projects/' + self.projectName);
        Dialog.showErrorDialog('An error occured when loading the concept details. The given concept is unknown. You\'re redirected back to the project page...');
    }

    /**
     * Prompts user for concept deletion.
     */
    self.deleteConcept = function() {
        if (self.referencesLoaded) {
            if (self.references.length == 0) {
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
                            self.deleteConceptForSure();
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
                var message = 'You cannot delete this concept because this concept is still referenced by some use cases. See the \'Concept referenced by\' section on this page to see an overview of these use cases.\n\nIf you do want to delete this concept, you will first have to delete these use cases or at least remove the references to this concept in these use cases.';
                Dialog.showErrorDialog(message);
            }
        }
    }

    /**
     * Deletes a concept.
     */
    self.deleteConceptForSure = function() {
        var deleteConceptUrl = Hateoas.getConceptInMap(self.projectName, self.conceptName)['delete'];

        $http.delete(deleteConceptUrl).success(function(data, status, headers, config) {
            Dialog.setAlert('deleteConceptAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Deleting Concept failed!', data);
        });
    };

    /**
     * Get all references, i.e. concepts, actors...
     */
    self.getReferences = function() {
        self.referencesLoaded = false;
        var referencesUrl = Hateoas.getConceptInMap(self.projectName, self.conceptName)['self'];
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
            Dialog.showHttpErrorDialog('Failed to load references to this concept!', data);
        });
    };
    self.getReferences();

    /**
     * Retrieve textual content of definition.
     */
    self.parseDefinition = function(definition) {
        if (definition.type == 'textual') {
            return definition.data.text;
        }
        else {
            return self.linearizeReference(definition);
        }
    };

    /**
     * Parses a Json object recursively to filter textual representation of possibly nested references.
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
        var result = left + '@' + step.data.reference + right;
        return result;
    };

}]);

