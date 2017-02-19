/**
 * Controller for the page where concepts are updated or created.
 */
var createOrUpdateConceptController = angular.module('createOrUpdateConceptController', []);

createOrUpdateConceptController.controller('CreateOrUpdateConceptCtrl', ['$scope', '$routeParams', '$http', '$location', 'Path', 'Dialog', 'Hateoas', 'Format', 'Nav', function($scope, $routeParams, $http, $location, Path, Dialog, Hateoas, Format, Nav) {

    var self = this;
    self.conceptIndex = 0;
    Dialog.initAlerts();
    Dialog.setInitNeeded(false);
    self.projectName = $routeParams.projectName;

    /**
     * Gets all actors in a project.
     */
    self.getActorsInProject = function() {
        var getActorsUrl = Hateoas.getProjectInMap(self.projectName)['actors'];

        $http.get(getActorsUrl).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.length; i++) {
                self.referenceList.push(data.content[i].name);
            }
            self.referenceList = self.referenceList.sort(function(a, b) {
                return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the actors in this project...', data);
        });
    };

    /**
     * Gets all concepts in a project.
     */
    self.getConceptsInProject = function() {
        var getConceptsUrl = Hateoas.getProjectInMap(self.projectName)['concepts'];

        $http.get(getConceptsUrl).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.length; i++) {
                self.referenceList.push(data.content[i].name);
            }
            self.referenceList = self.referenceList.sort(function(a, b) {
                return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the concepts in this project...', data);
        });
    };

    /**
     * Gets all use cases in a project.
     */
    self.getUsecasesInProject = function() {
        var getUsecasesUrl = Hateoas.getProjectInMap(self.projectName)['usecases'];

        $http.get(getUsecasesUrl).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.length; i++) {
                self.referenceList.push(data.content[i].name);
            }
            self.referenceList = self.referenceList.sort(function(a, b) {
                return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the use cases in this project...', data);
        });
    };

    if (Hateoas.getProjectInMap(self.projectName)) {
        self.referenceList = [];
        self.getConceptsInProject();
        self.getUsecasesInProject();
        self.getActorsInProject();
    }
    else {
        Dialog.showErrorDialog('Something went wrong when loading the concept details. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    self.conceptName = "";
    self.conceptDefinition = "";
    self.conceptAttributes = [''];

    self.create = true;
    self.action = 'Create';
    if ($routeParams.conceptName) {
        self.create = false;
        self.action = 'Update';

        self.conceptName = Format.formatToHtml($routeParams.conceptName);
        self.conceptOldName = $routeParams.conceptName;

        if (!Hateoas.getConceptInMap(self.projectName, self.conceptOldName)) {
            Dialog.showErrorDialog('Something went wrong when loading the concept details. The given concept name is not known. You\'re redirected back to the project details.');
            $location.path('/projects/' + self.projectName);
            return;
        }

        var getConceptUrl = Hateoas.getConceptInMap(self.projectName, self.conceptOldName)['self'];

        $http.get(getConceptUrl).success(function(data, status, headers, config) {
            self.conceptDefinition = self.parseDefinition(data.content.definition);
            if (!data.content.attributes || data.content.attributes.length == 0) {
                self.hasNoAttributes = true;
            }
            else {
                self.conceptAttributes = data.content.attributes;
            }
        }).error(function(data, status, headers, config) {
            $location.path('/projects/' + self.projectName);
        });
    }

    /**
     * Creates a new concept.
     */
    self.createConcept = function(newConcept) {
        var createConceptUrl = Hateoas.getProjectInMap(self.projectName)['concept_add'];

        $http.post(createConceptUrl, newConcept).success(function(data, status, headers, config) {
            Dialog.setAlert('createConceptAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Creating concept failed!', data);
            self.conceptAttributes = [''];
            self.hasNoAttributes = false;
        });
    };

    /**
     * Edits a concept.
     */
    self.updateConcept = function(newConcept) {
        var updateConceptUrl = Hateoas.getConceptInMap(self.projectName, self.conceptOldName)['patch'];

        $http.patch(updateConceptUrl, newConcept).success(function(data, status, headers, config) {
            Dialog.setAlert('patchConceptAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('Updating concept failed!', data);
            self.conceptAttributes = [''];
            self.hasNoAttributes = false;
        });
    };

    /**
     * Creates or updates a concept, based on which page we're actually on.
     */
    self.createOrUpdateConcept = function() {
        self.valid = true;
        self.errorMessages = [];

        if (self.isEmpty(self.conceptName)) {
            self.valid = false;
            self.errorMessages.push('Concept name cannot be empty');
        }
        if (self.conceptName == 'new') {
            self.valid = false;
            self.errorMessages.push('Concept name \'new\' is not allowed');
        }
        if (self.isEmpty(self.conceptDefinition)) {
            self.valid = false;
            self.errorMessages.push('Definition cannot be empty');
        }
        if (!self.hasNoAttributes) {
            for (var i = 0; i < self.conceptAttributes.length; i++) {
                if (self.isEmpty(self.conceptAttributes[i])) {
                    self.valid = false;
                    self.errorMessages.push('Attribute fields cannot be empty');
                    break;
                }
            }
        }

        var definition = self.createReferenceData(self.conceptDefinition, '(in concept definition)');

        if (self.valid) {
            if (self.hasNoAttributes) {
                self.conceptAttributes = [];
            }

            var newConcept = {
                name: Format.formatToJs(self.conceptName),
                definition: definition,
                attributes: self.conceptAttributes
            };

            if (self.create) {
                self.createConcept(newConcept);
            }
            else {
                self.updateConcept(newConcept);
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
     * Cancels a create or update request.
     */
    self.cancelCreateOrUpdateConcept = function() {
        Nav.back();
    };

    /**
     * Adds an attribute to the concept.
     */
    self.addConceptAttribute = function() {
        self.conceptAttributes.push('');
    };

    /**
     * Removes an attribute from the concept.
     */
    self.removeConceptAttribute = function(index) {
        if (self.conceptAttributes.length > 1) {
            self.conceptAttributes.splice(index, 1);
        }
    };

    /**
     * Checks if a string is empty.
     */
    self.isEmpty = function(str) {
        return str == "";
    };

    /**
     * Creates json object of references.
     */
    self.createReferenceData = function(text, errorSuffix) {
        var tokenIndex = text.indexOf('@');
        var referenceData;

        if (tokenIndex == -1) {
            referenceData = {
                type: 'textual',
                data: {
                    text: text
                }
            };
        }
        else {
            var first = text.substring(0, tokenIndex);
            var second = text.substring(tokenIndex);
            var splitIndex = second.indexOf(' ');
            var third;
            if (splitIndex == -1) {
                third = '';
                second = second.substring(1);
            }
            else {
                third = second.substring(splitIndex);
                second = second.substring(1, splitIndex);
            }

            if (self.isEmpty(second)) {
                self.valid = false;
                self.errorMessages.push('You cannot reference an entity with an empty name ' + errorSuffix);
            }
            else if (!self.validReference(second)) {
                self.valid = false;
                self.errorMessages.push('You cannot reference the unexisting entity \'' + second + '\' ' + errorSuffix);
            }

            referenceData = {
                type: 'reference',
                data: {
                    left: {
                        type: 'textual',
                        data: {
                            text: first
                        }
                    },
                    reference: second,
                    right: self.createReferenceData(third, errorSuffix)
                }
            };
        }

        return referenceData;
    };

    /**
     * Checks whether the given name is a valid reference name.
     */
    self.validReference = function(name) {
        for (var i = 0; i < self.referenceList.length; i++) {
            if (self.referenceList[i] == name) {
                return true;
            }
        }
        return false;
    };

    /**
     * Parses json to retrieve definition.
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
     * Recursively parses json to retrieve (possibly nested) references.
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

    /**
     * Inserts a reference in the concept definition.
     */
    self.insertConcept = function(choice) {
        if (!choice) {
            return;
        }
        var string = self.conceptDefinition;
        self.conceptDefinition = self.regexReplace(string, choice);
    };

    /**
     * Actually inserts a string (i.e. reference) in a given string.
     */
    self.regexReplace = function(string, insert) {
        self.matchingConcepts = [];
        self.conceptindex = 0;
        var base = string.substring(0, self.refIndex);
        var end = string.substring(self.refIndex);
        string = base + end.replace(/@[^ ]*/, " @" + insert + " ");
        string = string.replace(/^ */, "");
        string = string.replace(/(.*)/, "$1 ");
        return string.replace(/  */g, " ");
    };

    /**
     * Finds the index of the @ which should be completed or has been changed last.
     */
    self.findIndex = function(st, s) {
        var ret = 0;
        var j = 0; var i = 0;
        if (s != null && st != null && s != undefined && st != undefined) {
            if (st.charAt(st.length-1) == "@") {
                return st.length - 1;
            }
            while (st.charAt(i) == s.charAt(j) && i < st.length && j < s.length) {
                i++; j++;
                if (st.charAt(i) == "@") {
                    ret = i;
                    if (st.charAt(i+1) == " ") {
                        return i-1;
                    }
                    if (s.charAt(j+1) == " ") {
                        return i+1;
                    }
                }
            }
        }
        else if (self.conceptsFound) {
            return st.length-2;
        }
        else {
            return 0
        }
        return ret
    };


    /**
     * Handles key events, used to intercept arrow keys and navigate through the reference drop down list.
     */
    self.dropDown = function(k, e) {
    if (k == 64 || k == 8) {
        self.conceptIndex = 0;
        self.conceptsFound = true;
        self.ref = true;
            return;
        }
        if (k == 38) {
            self.handleKeyEvent = !self.handleKeyEvent;
            if (self.conceptIndex == 0) {
                self.conceptIndex = self.matchingConcepts.length - 1;
            }
            else {
                self.conceptIndex--;
            }
        }
        if (k == 13) {
           value = self.matchingConcepts[self.conceptIndex]
           if (self.ref) {
               self.insertConcept(value);
            }
        }
        if (k == 40) {
            self.handleKeyEvent = !self.handleKeyEvent;
            self.conceptIndex = (self.conceptIndex + 1) % self.matchingConcepts.length;
        }
        if (self.conceptsFound)
            e.preventDefault();
        if (k == 13)
            self.conceptsFound = false;
    };

    /**
     * Inserts a reference in concept definition.
     */
    self.insertConcept = function(choice) {
        if (!choice) {
            return;
        }
        var string = self.conceptDefinition;
        self.conceptDefinition = self.regexReplace(string, choice);
        self.refFocus = true;
    }

    /**
     * Sets variables so no dropdowns are shown.
     */
    self.noFlow = function() {
        self.matchingConcepts = [];
        self.conceptIndex = 0;
        self.conceptsFound = false;
        self.ref = false;
    };

    /**
     * Matches written text to concepts to auto complete and insert them.
     */
    self.matchConcepts = function(string, index) {
        var tmpString = string;
        self.refIndex = self.findIndex(string, self.previousString);
        string = string.substring(self.refIndex);
        self.previousString = tmpString;
        var match = string.match(/@[^ ]*/);
        self.index = index;
        var list = [];
        if (match) {
            var word = match[0].substring(1, match[0].length);
            var ix = 0;
            angular.forEach(self.referenceList, function(value, key) {
                if (value.toLowerCase().indexOf(word.toLowerCase()) == 0) {
                    list[ix++] = value;
                }
            });
        }
        self.matchingConcepts = list;
    };
}]);

