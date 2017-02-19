/**
 * Controller for the create or update use case page. This page shows a
 * use case with adjustable fields. All required functionalities to be able
 * to dynamically desing a use case are provided through the use case form, which
 * is managed by this controller.
 */
var createOrUpdateUsecaseController = angular.module('createOrUpdateUsecaseController', []);

createOrUpdateUsecaseController.run(['$anchorScroll', function($anchorScroll) {
    $anchorScroll.yOffset = 60;
}]);

createOrUpdateUsecaseController.controller('CreateOrUpdateUsecaseCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$location', '$anchorScroll', '$timeout', 'Path', 'Dialog', 'Hateoas', 'Format', 'Nav', function($scope, $rootScope, $routeParams, $http, $location, $anchorScroll, $timeout, Path, Dialog, Hateoas, Format, Nav) {

    var self = this;

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);
    self.projectName = $routeParams.projectName;

    self.actorsInProject = [];
    self.conceptsInProject = [];
    self.usecasesInProject = [];
    self.referenceList = [];

    if (!Hateoas.getProjectInMap(self.projectName)) {
        Dialog.showErrorDialog('Something went wrong when loading the use case details. The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    Nav.setUsecaseDetails(false);

    self.name = "";
    self.objective = "";
    self.preconditions = [""];
    self.postconditions = [""];
    self.actors = [];
    self.concepts = [];
    self.normalFlow = [];
    self.alternativeFlows = [];
    self.exceptionalFlows = [];

    self.newActorName = "";
    self.addActorFlowType = '';
    self.addActorFlowIndex = -1;
    self.addActorStepIndex = -1;

    self.newConceptName = "";
    self.newConceptDefinition = "";
    self.newConceptAttributes = [''];
    self.hasNoAttributes = false;

    self.matchingConcepts = [];
    self.conceptIndex = 0;
    self.focusNF = [];
    self.focusAF = [];
    self.focusEF = [];
    self.indexAF = 0;
    self.indexEF = 0;
    self.objRefFocus = false;
    self.normalFlowFocus = false;
    self.alternateFlowFocus = false;
    self.exceptionalFlowFocus = false;
    self.handleKeyEvent = false;
    self.conceptsFound = false;

    self.indexFlow = 0;

    self.create = true;
    self.action = 'Create';
    if ($routeParams.usecaseName) {
        self.create = false;
        self.action = 'Update';

        self.name = Format.formatToHtml($routeParams.usecaseName);
        self.oldName = $routeParams.usecaseName;

        if (!Hateoas.getUsecaseInMap(self.projectName, self.oldName)) {
            Dialog.showErrorDialog('Something went wrong when loading the use case details. The given use case name is not known. You\'re redirected back to the project details.');
            $location.path('/projects/' + self.projectName);
            return;
        }

        var getUsecaseUrl = Hateoas.getUsecaseInMap(self.projectName, self.oldName)['self'];

        $http.get(getUsecaseUrl).success(function(data, status, headers, config) {
            self.objective = self.parseCondition(data.content.objective);
            self.actors = [];
            self.concepts = [];

            self.preconditions = [];
            for (var i = 0; i < data.content.preconditions.length; i++) {
                self.preconditions.push(self.parseCondition(data.content.preconditions[i]));
            }

            self.postconditions = [];
            for (var i = 0; i < data.content.postconditions.length; i++) {
                self.postconditions.push(self.parseCondition(data.content.postconditions[i]));
            }

            var normalFlow = data.content.normalFlow;
            var alternativeFlows = data.content.alternativeFlows;
            var exceptionalFlows = data.content.exceptionalFlows;

            for (var i = 0; i < normalFlow.behaviors.length; i++) {
                var current = normalFlow.behaviors[i];
                while (current.type != 'textualStep') {
                    current = current.behavior.normalBehavior;
                }

                var newStep = {
                    executor: self.findExecutorInData(normalFlow.behaviors[i]),
                    hash: current.name,
                    action: self.findStepInData(normalFlow.behaviors[i])
                };
                self.normalFlow.push(newStep);
            }

            for (var i = 0; i < alternativeFlows.length; i++) {
                var newFlow = {
                    steps: [],
                    returnTo: -1,
                    condition: self.parseCondition(alternativeFlows[i].condition),
                    shown: true,
                    hash: alternativeFlows[i].name
                };

                for (var j = 0; j < alternativeFlows[i].behavior.behaviors.length; j++) {
                    var step = alternativeFlows[i].behavior.behaviors[j];
                    if (step.type == 'return') {
                        for (var k = 0; k < self.normalFlow.length; k++) {
                            if (step.behavior.target == self.normalFlow[k].hash) {
                                newFlow.returnTo = k;
                                break;
                            }
                        }
                    }
                    else {
                        var newStep = {
                            executor: self.findExecutorInData(step),
                            action: self.findStepInData(step)
                        };
                        newFlow.steps.push(newStep);
                    }
                }

                self.alternativeFlows.push(newFlow);
            }

            for (var i = 0; i < exceptionalFlows.length; i++) {
                var newFlow = {
                    steps: [],
                    returnTo: -1,
                    condition: self.parseCondition(exceptionalFlows[i].condition),
                    shown: true,
                    hash: exceptionalFlows[i].name
                };

                for (var j = 0; j < exceptionalFlows[i].behavior.behaviors.length; j++) {
                    var step = exceptionalFlows[i].behavior.behaviors[j];
                    if (step.type == 'return') {
                        for (var k = 0; k < self.normalFlow.length; k++) {
                            if (step.behavior.target == self.normalFlow[k].hash) {
                                newFlow.returnTo = k;
                                break;
                            }
                        }
                    }
                    else {
                        var newStep = {
                            executor: self.findExecutorInData(step),
                            action: self.findStepInData(step)
                        };
                        newFlow.steps.push(newStep);
                    }
                }

                self.exceptionalFlows.push(newFlow);
            }

            for (var i = 0; i < normalFlow.behaviors.length; i++) {
                var current = normalFlow.behaviors[i];
                while (current.type == 'branchingPoint') {
                    for (var j = 0; j < self.alternativeFlows.length; j++) {
                        if (current.behavior.targetReference == self.alternativeFlows[j].hash) {
                            self.alternativeFlows[j].branchIndex = i;
                        }
                    }
                    for (var j = 0; j < self.exceptionalFlows.length; j++) {
                        if (current.behavior.targetReference == self.exceptionalFlows[j].hash) {
                            self.exceptionalFlows[j].branchIndex = i;
                        }
                    }

                    current = current.behavior.normalBehavior;
                }
            }

            self.sortFlows(self.alternativeFlows);
            self.sortFlows(self.exceptionalFlows);
        }).error(function(data, status, headers, config) {
            $location.path('/projects/' + self.projectName);
        });
    }

    Nav.setCreateActorOnUsecase(false);
    Nav.setCreateConceptOnUsecase(false);

    /**
     * Handles key events so they can be used to navigate the reference drop down
     * and select a value.
     */
    self.dropDown = function(k, e) {
        if (k == 64 || k == 8) {
            self.conceptIndex = 0;
            self.conceptsFound = true;
            return;
        }
        if (k == 38) {
            self.handleKeyEvent = !self.handleKeyEvent;
            if (self.conceptIndex == 0) {
               self.conceptIndex = self.matchingConcepts.length - 1;
            } else {
               self.conceptIndex--;
            }
        }
        if (k == 13) {
            value = self.matchingConcepts[self.conceptIndex]
            if (self.nFlow) {
                self.insertConceptNFlow(value);
            }
            if (self.aFlow) {
                self.insertConceptAFlow(value);
            }
            if (self.eFlow) {
                self.insertConceptEFlow(value);
            }
            if (self.objRef) {
                self.insertConceptObj(value);
            }
            if (self.condARef) {
                self.insertAConceptCond(value);
            }
            if (self.condERef) {
                self.insertEConceptCond(value);
            }
            if (self.postRef) {
                self.insertConceptPost(value);
            }
            if (self.preRef) {
                self.insertConceptPre(value);
            }
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
     * Finds the index of the @ whose reference should be altered.
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
    }

    /**
     * Matches written text to references to auto complete and insert them.
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

    self.noFlow = function() {
        self.matchingConcepts = [];
        self.conceptIndex = 0;
            self.conceptsFound = false;
    };

    /**
     * Exits the normal flow reference autocomplete.
     */
    self.noNFlow = function () {
        self.noFlow();
        self.nFlow = false;
        self.focusNF[self.index+1] = false;
    };

    /**
     * Exits the exceptional flow reference autocomplete.
     */
    self.noEFlow = function () {
        self.noFlow();
        self.eFlow = false;
        self.focusEF[self.index+1] = false;
    };

    /**
     * Exits postcondition reference completion.
     */
    self.noPost = function () {
        self.noFlow();
            self.postRef = false;
        self.postFocus = true;
    };

    /**
     * Exits precondition reference completion.
     */
    self.noPre = function () {
        self.noFlow();
            self.preRef = false;
        self.preFocus = true;
    };

    /**
     * Exits objective reference completion.
     */
    self.noObj = function () {
        self.noFlow();
            self.objRef = false;
        self.objRefFocus = true;
    };

    /**
     * Exits the alternative flow reference completion.
     */
    self.noAFlow = function () {
        self.noFlow()
        self.aFlow = false;
        self.focusAF[self.index+1] = false;
    };

    /**
     * Inserts a reference in string at the correct position.
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
     * Inserts a reference in an alternate flow step.
     */
    self.insertAConceptCond = function(choice) {
        if (!choice) {
            return;
        }
        var ix=self.indexAF;
        var string = self.alternativeFlows[ix].condition;
        self.alternativeFlows[ix].condition = self.regexReplace(string, choice);
        self.condARefFocus = true;
        self.condARef = false;
    }

    /**
     * Inserts a reference in concept definition.
     */
    self.insertConcept = function(choice) {
        if (!choice) {
            return;
        }
        var string = self.newConceptDefinition;
        self.newConceptDefinition = self.regexReplace(string, choice);
        self.refFocus = true;
    }

    /**
     * Inserts a reference in an exceptional flow step.
     */
    self.insertEConceptCond = function(choice) {
        if (!choice) {
            return;
        }
        var ix=self.indexEF;
        var string = self.exceptionalFlows[ix].condition;
        self.exceptionalFlows[ix].condition = self.regexReplace(string, choice);
        self.condARefFocus = true;
        self.condARef = false;
    }

    /**
     * Inserts a reference in a postcondition.
     */
    self.insertConceptPost = function(choice) {
        if (!choice) {
            return;
        }

        var ix = self.index;
        var string = self.postconditions[ix];
        self.postconditions[ix] = self.regexReplace(string, choice);
        self.postRefFocus = true;
    }


    /**
     * Inserts a reference in a precondition.
     */
    self.insertConceptPre = function(choice) {
        if (!choice) {
            return;
        }

        var ix = self.index;
        var string = self.preconditions[ix];
        self.preconditions[ix] = self.regexReplace(string, choice);
        self.preRefFocus = true;
    }

    /**
     * Inserts a reference in an objective.
     */
    self.insertConceptObj = function(choice) {
        if (!choice) {
            return;
        }
        self.objective = self.regexReplace(self.objective, choice)
        self.objRefFocus = true;
        self.objRef = false;
    }

    /**
     * Inserts a reference in a normal flow textfield.
     */
    self.insertConceptNFlow = function(choice) {
        if (!choice) {
            return;
        }
        var ix=self.index;
        self.focusNF[ix+1] = true;
        var string = self.normalFlow[ix].action;
        self.normalFlow[ix].action = self.regexReplace(string, choice);
        self.normalFlowFocus = true;
        self.nFlow = false;
    };

    /**
     * Inserts a reference in an exceptional flow textfield.
     */
    self.insertConceptEFlow = function(choice) {
        if (!choice) {
            return;
        }
        var ix=self.index;
        var index = self.indexEF;
        self.focusEF[ix+1] = true;
        var string = self.exceptionalFlows[index].steps[ix].action;
        self.exceptionalFlows[index].steps[ix].action = self.regexReplace(string, choice);
        self.exceptionalFlowFocus = true;
    };

    /**
     * Inserts a reference in an alternative flow textfield.
     */
    self.insertConceptAFlow = function(choice) {
        if (!choice) {
            return;
        }
        var ix=self.index;
        var index = self.indexAF;
        self.focusAF[ix+1] = true;
        var string = self.alternativeFlows[index].steps[ix].action;
        self.alternativeFlows[index].steps[ix].action = self.regexReplace(string, choice);
        self.alternateFlowFocus = true;
    };

    /**
     * Generates a SHA1 hash value based on current time and random value.
     */
    self.generateHash = function() {
        var current_date = (new Date()).valueOf().toString();
        var random = Math.random().toString();
        return CryptoJS.SHA1(current_date + random).toString();
    };

    /**
     * Gets all actors in a project.
     */
    self.getActorsInProject = function() {
        var getActorsUrl = Hateoas.getProjectInMap(self.projectName)['actors'];

        $http.get(getActorsUrl).success(function(data, status, headers, config) {
            for (var i = 0; i < data.content.length; i++) {
                self.actorsInProject.push(data.content[i].name);
                self.referenceList.push(data.content[i].name);
            }

            self.actorsInProject = self.actorsInProject.sort(function(a, b) {
                return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
            });
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
                self.conceptsInProject.push(data.content[i].name);
                self.referenceList.push(data.content[i].name);
            }

            self.conceptsInProject = self.conceptsInProject.sort(function(a, b) {
                return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
            });
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
            var result = [];
            for (var i = 0; i < data.content.length; i++) {
                if (data.content[i].name != self.name) {
                    result.push(data.content[i].name);
                }
            }
            self.usecasesInProject = result.sort(function(a, b) {
                return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
            });

            for (var i = 0; i < result.length; i++) {
                self.referenceList.push(result[i]);
            }
            self.referenceList = self.referenceList.sort(function(a, b) {
                return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
            });
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when loading the use cases in this project...', data);
        });
    };

    /**
     * Adds a precondition to the use case.
     */
    self.addPrecondition = function() {
        self.preconditions.push('');
    };

    /**
     * Removes a precondition from the use case at the given index.
     */
    self.removePrecondition = function(index) {
        if (self.preconditions.length > 1) {
            self.preconditions.splice(index, 1);
        }
    };

    /**
     * Adds a postcondition to the use case.
     */
    self.addPostcondition = function() {
        self.postconditions.push('');
    };

    /**
     * Removes a precondition from the use case at the given index.
     */
    self.removePostcondition = function(index) {
        if (self.postconditions.length > 1) {
            self.postconditions.splice(index, 1);
        }
    };

    /**
     * Adds a step to the normal flow.
     * Adds an empty step BEHIND this given index, so at index+1.
     */
    self.addNormalFlowStep = function(index) {
        var newStep = new Object();
        newStep.executor = "";
        newStep.action = "";
        newStep.hash = self.generateHash();
        self.normalFlow.splice(index + 1, 0, newStep);

        for (var i = self.normalFlow.length - 1; i > index + 1; i--) {
            self.adaptOtherFlowsIndex(i - 1, i);
        }
    };

    if (self.create) {
        self.addNormalFlowStep(-1);
    }

    /**
     * Removes a flow step from the normal flow.
     */
    self.removeNormalFlowStep = function(index) {
        if (self.normalFlow.length > 1) {
            var branched = false;
            self.alternativeFlows.forEach(function(flow) {
                if (flow.branchIndex == index || flow.returnTo == index) {
                    branched = true;
                    return;
                }
            });
            self.exceptionalFlows.forEach(function(flow) {
                if (flow.branchIndex == index || flow.returnTo == index) {
                    branched = true;
                    return;
                }
            });
            if (!branched) {
                for (var i = index + 1; i < self.normalFlow.length; i++) {
                    self.adaptOtherFlowsIndex(i, i - 1);
                }
                self.normalFlow.splice(index, 1);
            }
            else {
                Dialog.showErrorDialog("You cannot remove this step, because some alternative and/or exceptional flows are branching from or returning to this step.");
            }
        }
    };

    /**
     * Swaps two normal flow steps.
     */
    self.swapNormalFlowSteps = function(index1, index2) {
        var b = self.normalFlow[index1];
        self.normalFlow[index1] = self.normalFlow[index2];
        self.normalFlow[index2] = b;

        self.adaptOtherFlowsIndex(index1, -2);
        self.adaptOtherFlowsIndex(index2, index1);
        self.adaptOtherFlowsIndex(-2, index2);

        self.sortFlows(self.alternativeFlows);
        self.sortFlows(self.exceptionalFlows);
    };

    /**
     * Adds an alternative flow to the use case.
     */
    self.addAlternativeFlow = function(index) {
        var newFlow = new Object();
        newFlow.branchIndex = index;
        newFlow.steps = [];
        newFlow.returnTo = -1;
        newFlow.condition = '';
        newFlow.shown = true;
        newFlow.hash = self.generateHash();

        self.alternativeFlows.push(newFlow);
        self.addAlternativeFlowStep(self.alternativeFlows.length - 1, -1);

        self.sortFlows(self.alternativeFlows);

        for (var i = 0; i < self.alternativeFlows.length; i++) {
            if (self.alternativeFlows[i].hash == newFlow.hash) {
                $timeout(function() {
                    self.setLocationOnPage('alt-flow-' + i);
                }, 500);
                return;
            }
        };
    };

    /**
     * Removes an alternative flow step.
     */
    self.removeAlternativeFlow = function(index) {
        self.alternativeFlows.splice(index, 1);
    };

    /**
     * Adds an alternative flowstep to the alternate flow.
     */
    self.addAlternativeFlowStep = function(flowIndex, stepIndex) {
        var newStep = new Object();
        newStep.executor = "";
        newStep.action = "";
        self.alternativeFlows[flowIndex].steps.splice(stepIndex + 1, 0, newStep);
    };

    /**
     * Removes a step from the alternative flow.
     */
    self.removeAlternativeFlowStep = function(flowIndex, stepIndex) {
        if (self.alternativeFlows[flowIndex].steps.length > 1) {
            self.alternativeFlows[flowIndex].steps.splice(stepIndex, 1);
        }
    };

    /**
     * Swaps two steps in an alternative flow.
     */
    self.swapAlternativeFlowSteps = function(flowIndex, stepIndex1, stepIndex2) {
        var b = self.alternativeFlows[flowIndex].steps[stepIndex1];
        self.alternativeFlows[flowIndex].steps[stepIndex1] = self.alternativeFlows[flowIndex].steps[stepIndex2];
        self.alternativeFlows[flowIndex].steps[stepIndex2] = b;
    };

    /**
     * Adds an exceptional flow to the use case.
     */
    self.addExceptionalFlow = function(index) {
        var newFlow = new Object();
        newFlow.branchIndex = index;
        newFlow.steps = [];
        newFlow.returnTo = -1;
        newFlow.condition = '';
        newFlow.shown = true;
        newFlow.hash = self.generateHash();

        self.exceptionalFlows.push(newFlow);
        self.addExceptionalFlowStep(self.exceptionalFlows.length - 1, -1);

        self.sortFlows(self.exceptionalFlows);

        for (var i = 0; i < self.exceptionalFlows.length; i++) {
            if (self.exceptionalFlows[i].hash == newFlow.hash) {
                $timeout(function() {
                    self.setLocationOnPage('exc-flow-' + i);
                }, 500);
                return;
            }
        }
    };

    /**
     * Removes an exceptional flow from the use case.
     */
    self.removeExceptionalFlow = function(index) {
        self.exceptionalFlows.splice(index, 1);
    };

    /**
     * Adds a step to the exceptional flow.
     */
    self.addExceptionalFlowStep = function(flowIndex, stepIndex) {
        var newStep = new Object();
        newStep.executor = "";
        newStep.action = "";
        self.exceptionalFlows[flowIndex].steps.splice(stepIndex + 1, 0, newStep);
    };

    /**
     * Removes a step from an exceptional flow.
     */
    self.removeExceptionalFlowStep = function(flowIndex, stepIndex) {
        if (self.exceptionalFlows[flowIndex].steps.length > 1) {
            self.exceptionalFlows[flowIndex].steps.splice(stepIndex, 1);
        }
    };

    /**
     * Swaps two steps in an exceptional flow.
     */
    self.swapExceptionalFlowSteps = function(flowIndex, stepIndex1, stepIndex2) {
        var b = self.exceptionalFlows[flowIndex].steps[stepIndex1];
        self.exceptionalFlows[flowIndex].steps[stepIndex1] = self.exceptionalFlows[flowIndex].steps[stepIndex2];
        self.exceptionalFlows[flowIndex].steps[stepIndex2] = b;
    };

    /**
     * Sorts flows based by branching index.
     */
    self.sortFlows = function(flows) {
        function compare(a,b) {
            if (a.branchIndex < b.branchIndex) {
                return -1;
            }
            if (a.branchIndex > b.branchIndex) {
                return 1;
            }
            return 0;
        }

        flows.sort(compare);
    };

    /**
     * Adapts correct indexes.
     */
    self.adaptOtherFlowsIndex = function(oldIndex, newIndex) {
        self.alternativeFlows.forEach(function(flow) {
            if (flow.branchIndex == oldIndex) {
                flow.branchIndex = newIndex;
            }
            if (flow.returnTo == oldIndex) {
                flow.returnTo = newIndex;
            }
        });

        self.exceptionalFlows.forEach(function(flow) {
            if (flow.branchIndex == oldIndex) {
                flow.branchIndex = newIndex;
            }
            if (flow.returnTo == oldIndex) {
                flow.returnTo = newIndex;
            }
        });
    };

    /**
     * Adds an actor to the actors list if not not in actors already.
     */
    self.addActor = function(name) {
        for (var i = 0; i < self.actors.length; i++) {
            if (self.actors[i] == name) {
                return;
            }
        }
        self.actors.push(name);
    };

    /**
     * Adds a concept to the concept list, if not contained in it already.
     */
    self.addConcept = function(name) {
        for (var i = 0; i < self.concepts.length; i++) {
            if (self.concepts[i] == name) {
                return;
            }
        }
        self.concepts.push(name);
    };

    /* Use case create and cancel functions */

    /**
     * Creates or updates a use case.
     */
    self.createOrUpdateUsecase = function() {
        self.valid = true;
        self.errorMessages = [];
        self.actors = [];
        self.concepts = [];

        for (var i = 0; i < self.preconditions.length; i++) {
            if (self.isEmpty(self.preconditions[i])) {
                self.valid = false;
                if (self.preconditions.length == 1) {
                    self.errorMessages.push('At least one non-empty precondition must be entered');
                }
                else {
                    self.errorMessages.push('Precondition fields cannot be empty');
                }
                break;
            }
        }
        for (var i = 0; i < self.postconditions.length; i++) {
            if (self.isEmpty(self.postconditions[i])) {
                self.valid = false;
                if (self.postconditions.length == 1) {
                    self.errorMessages.push('At least one non-empty postcondition must be entered');
                }
                else {
                    self.errorMessages.push('Postcondition fields cannot be empty');
                }
                break;
            }
        }

        var preconditions = [];
        for (var i = 0; i < self.preconditions.length; i++) {
            var conditionSuffix = '(in precondition ' + (i+1) + ')';
            var condition = self.createReferenceData(self.preconditions[i], conditionSuffix);
            preconditions.push(condition);
        }

        var postconditions = [];
        for (var i = 0; i < self.postconditions.length; i++) {
            var conditionSuffix = '(in postcondition ' + (i+1) + ')';
            var condition = self.createReferenceData(self.postconditions[i], conditionSuffix);
            postconditions.push(condition);
        }

        var objective = self.createReferenceData(self.objective, '(in use case objective)');

        var normalFlow = new Object();
        normalFlow.behaviors = [];
        for (var i = 0; i < self.normalFlow.length; i++) {
            var step = new Object();

            step.name = self.normalFlow[i].hash;
            step.type = 'textualStep';
            step.behavior = new Object();

            if (self.isEmpty(self.normalFlow[i].executor)) {
                self.valid = false;
                self.errorMessages.push('Executor of step ' + (i+1) + ' of the normal flow cannot be empty');
            }
            if (self.normalFlow[i].action.match(/^ *$/) !== null) {
                self.valid = false;
                self.errorMessages.push('Description of step ' + (i+1) + ' of the normal flow cannot be empty');
            }

            step.behavior.executor = self.normalFlow[i].executor;
            self.addActor(self.normalFlow[i].executor);

            var text = self.normalFlow[i].action;
            var suffix = '(in step ' + (i+1) + ' of the normal flow)';
            step.behavior.description = self.createReferenceData(text, suffix);

            for (var j = 0; j < self.alternativeFlows.length + self.exceptionalFlows.length; j++) {
                var flow;
                if (j < self.alternativeFlows.length) {
                    flow = self.alternativeFlows[j];
                }
                else {
                    flow = self.exceptionalFlows[j - self.alternativeFlows.length];
                }
                if (flow.branchIndex == i) {
                    step = {
                        name: self.generateHash(),
                        type: 'branchingPoint',
                        behavior: {
                            targetReference: flow.hash,
                            normalBehavior: step
                        }
                    };
                }
            }

            normalFlow.behaviors.push(step);
        }

        var alternativeFlows = [];
        for (var i = 0; i < self.alternativeFlows.length; i++) {
            var alt_flow = new Object();

            if (self.isEmpty(self.alternativeFlows[i].condition)) {
                self.valid = false;
                self.errorMessages.push('Condition of alternative flow ' + (i+1) + ' cannot be empty');
            }

            var condition = self.alternativeFlows[i].condition;
            var conditionSuffix = '(in condition of alternative flow ' + (i+1) + ')';
            alt_flow.condition = self.createReferenceData(condition, conditionSuffix);

            alt_flow.name = self.alternativeFlows[i].hash;
            alt_flow.behavior = { behaviors: [] };

            for (var j = 0; j < self.alternativeFlows[i].steps.length; j++) {
                var step = new Object();

                step.name = self.generateHash();
                step.type = 'textualStep';
                step.behavior = new Object();

                if (self.isEmpty(self.alternativeFlows[i].steps[j].executor)) {
                    self.valid = false;
                    self.errorMessages.push('Executor of step ' + (j+1) + ' of alternative flow ' + (i+1) + ' cannot be empty');
                }
                if (self.alternativeFlows[i].steps[j].action.match(/^ *$/) !== null) {
                    self.valid = false;
                    self.errorMessages.push('Description of step ' + (j+1) + ' of alternative flow ' + (i+1) + ' cannot be empty');
                }

                step.behavior.executor = self.alternativeFlows[i].steps[j].executor;
                self.addActor(self.alternativeFlows[i].steps[j].executor);

                var text = self.alternativeFlows[i].steps[j].action;
                var suffix = '(in step ' + (j+1) + ' of alternative flow ' + (i+1) + ')';
                step.behavior.description = self.createReferenceData(text, suffix);

                alt_flow.behavior.behaviors.push(step);
            }

            if (self.alternativeFlows[i].returnTo != -1) {
                var returnstep = {
                    type: 'return',
                    label: self.generateHash(),
                    behavior: {
                        executor: 'System',
                        target: self.normalFlow[self.alternativeFlows[i].returnTo].hash
                    }
                };
                alt_flow.behavior.behaviors.push(returnstep);
            }
			else {
				self.valid = false;
				self.errorMessages.push('Please select a return point for alternative flow ' + (i+1));
			}
            alternativeFlows.push(alt_flow);
        }

        var exceptionalFlows = [];
        for (var i = 0; i < self.exceptionalFlows.length; i++) {
            var exc_flow = new Object();

            if (self.isEmpty(self.exceptionalFlows[i].condition)) {
                self.valid = false;
                self.errorMessages.push('Condition of exceptional flow ' + (i+1) + ' cannot be empty');
            }

            var condition = self.exceptionalFlows[i].condition;
            var conditionSuffix = '(in condition of exceptional flow ' + (i+1) + ')';
            exc_flow.condition = self.createReferenceData(condition, conditionSuffix);

            exc_flow.name = self.exceptionalFlows[i].hash;
            exc_flow.behavior = { behaviors: [] };

            for (var j = 0; j < self.exceptionalFlows[i].steps.length; j++) {
                var step = new Object();

                step.name = self.generateHash();
                step.type = 'textualStep';
                step.behavior = new Object();

                if (self.isEmpty(self.exceptionalFlows[i].steps[j].executor)) {
                    self.valid = false;
                    self.errorMessages.push('Executor of step ' + (j+1) + ' of exceptional flow ' + (i+1) + ' cannot be empty');
                }
                if (self.exceptionalFlows[i].steps[j].action.match(/^ *$/) !== null) {
                    self.valid = false;
                    self.errorMessages.push('Description of step ' + (j+1) + ' of exceptional flow ' + (i+1) + ' cannot be empty');
                }

                step.behavior.executor = self.exceptionalFlows[i].steps[j].executor;
                self.addActor(self.exceptionalFlows[i].steps[j].executor);

                var text = self.exceptionalFlows[i].steps[j].action;
                var suffix = '(in step ' + (j+1) + ' of exceptional flow ' + (i+1) + ')';
                step.behavior.description = self.createReferenceData(text, suffix);

                exc_flow.behavior.behaviors.push(step);
            }

            if (self.exceptionalFlows[i].returnTo != -1) {
                var returnstep = {
                    type: 'return',
                    label: self.generateHash(),
                    behavior: {
                        executor: 'System',
                        target: self.normalFlow[self.exceptionalFlows[i].returnTo].hash
                    }
                };
                exc_flow.behavior.behaviors.push(returnstep);
            }
            exceptionalFlows.push(exc_flow);
        }

        if (self.valid) {
            var newUsecase = {
                name: Format.formatToJs(self.name),
                objective: objective,
                preconditions: preconditions,
                postconditions: postconditions,
                normalFlow: normalFlow,
                alternativeFlows: alternativeFlows,
                exceptionalFlows: exceptionalFlows,
                actors: self.actors,
                concepts: self.concepts
            };

            if (self.create) {
                self.createUsecase(newUsecase);
            }
            else {
                self.updateUsecase(newUsecase);
            }
        }
        else {
            var message = '<strong>Some fields are not valid</strong>.\n\nMake sure these rules are satisfied:<ul>';
            for (var i = 0; i < self.errorMessages.length; i++) {
                message += '<li>' + self.errorMessages[i] + '</li>';
            }
            message += '</ul>';
            Dialog.showLargeErrorDialog(message);
        }
    };

    /**
     * Creates a reference Json object.
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
            else {
                if (self.projectContainsActor(second)) {
                    self.addActor(second);
                }
                else if (self.projectContainsConcept(second)) {
                    self.addConcept(second);
                }
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
     * Creates a use case.
     */
    self.createUsecase = function(usecase) {
        var usecaseUrl = Hateoas.getProjectInMap(self.projectName)['usecase_add'];

        $http.post(usecaseUrl, usecase).success(function (data, status, headers, config) {
            Dialog.setAlert('createUsecaseAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function (data, status, headers, config) {
            var message = 'Creating use case failed!';
            if (data && data.code && data.code == 409 && data.message && data.message != '') {
                message += '\n\nCause of error (returned by server): ' + data.message;
            }
            message += '\n\nDo you want to stay on this page';

            BootstrapDialog.show({
                type: BootstrapDialog.TYPE_DANGER,
                title: 'Failed!',
                closable: false,
                cssClass: 'small-dialog',
                message: message,
                buttons: [{
                    icon: 'glyphicon glyphicon-ok',
                    label: 'Yes',
                    cssClass: 'btn btn-success',
                    action: function(dialogItself) {
                        dialogItself.close();
                    }
                }, {
                    icon: 'glyphicon glyphicon-remove',
                    label: 'No',
                    cssClass: 'btn btn-danger',
                    action: function(dialogItself) {
                        $location.path('/projects/' + self.projectName);
                        $scope.$apply();
                        dialogItself.close();
                    }
                }]
            });
        });
    };

    /**
     * Patches the use case.
     */
    self.updateUsecase = function(usecase) {
        var usecaseUrl = Hateoas.getUsecaseInMap(self.projectName, self.oldName)['patch'];

        $http.patch(usecaseUrl, usecase).success(function (data, status, headers, config) {
            Dialog.setAlert('patchUsecaseAlert');
            $location.path('/projects/' + self.projectName);
        }).error(function (data, status, headers, config) {
            var message = 'Updating use case failed!';
            if (data && data.code && data.code == 409 && data.message && data.message != '') {
                message += '\n\nCause of error (returned by server): ' + data.message;
            }
            message += '\n\nDo you want to stay on this page';

            BootstrapDialog.show({
                type: BootstrapDialog.TYPE_DANGER,
                title: 'Failed!',
                closable: false,
                cssClass: 'small-dialog',
                message: message,
                buttons: [{
                    icon: 'glyphicon glyphicon-ok',
                    label: 'Yes',
                    cssClass: 'btn btn-success',
                    action: function(dialogItself) {
                        dialogItself.close();
                    }
                }, {
                    icon: 'glyphicon glyphicon-remove',
                    label: 'No',
                    cssClass: 'btn btn-danger',
                    action: function(dialogItself) {
                        $location.path('/projects/' + self.projectName);
                        $scope.$apply();
                        dialogItself.close();
                    }
                }]
            });
        });
    };

    /**
     * Cancels creating a use case.
     */
    self.cancelCreateUsecase = function() {
        if (Nav.isUsecaseDetails()) {
            var label;
            if (self.create) {
                label = 'creation';
            }
            else {
                label = 'updating';
            }
            BootstrapDialog.show({
                type: BootstrapDialog.TYPE_DANGER,
                title: 'Cancel?',
                closable: false,
                cssClass: 'small-dialog',
                    message: 'Are you sure you want to cancel the ' + label + ' of this use case? <strong>Nothing will be saved!</strong>',
                    buttons: [{
                        icon: 'glyphicon glyphicon-ok',
                        label: 'Yes, cancel',
                        cssClass: 'btn btn-success',
                        action: function(dialogItself) {
                            Nav.back();
                            $rootScope.$apply();
                            dialogItself.close();
                        }
                    }, {
                        icon: 'glyphicon glyphicon-remove',
                        label: 'No',
                        cssClass: 'btn btn-danger',
                        action: function(dialogItself) {
                            dialogItself.close();
                        }
                    }]
                });
        }
        else {
            Nav.back();
        }
    };

    /**
     * Navigate to details, used for navigation.
     */
    self.moveToDetails = function() {
        if (self.name == 'new') {
            Dialog.showErrorDialog('<strong>Input error!</strong>\n\nIt is forbidden to name your use case \'new\'.');
            return;
        }
        if (!self.validUsecaseName(self.name)) {
            Dialog.showErrorDialog('<strong>Input error!</strong>\n\nYou entered an invalid use case name. Make sure the use case name is <strong>not empty</strong>.');
            return;
        }

        var getUsecaseUrl = Hateoas.getProjectInMap(self.projectName)['usecases'];

        $http.get(getUsecaseUrl).success(function(data, status, headers, config) {
            var usecases = data.content;
            var unique = true;

            for (var i = 0; i < usecases.length; i++) {
                if (usecases[i].name == Format.formatToJs(self.name)) {
                    if (self.create || $routeParams.usecaseName != Format.formatToJs(self.name)) {
                        unique = false;
                    }
                }
            }

            if (unique) {
                Nav.setUsecaseDetails(true);

                self.referenceList = [];
                self.actorsInProject = [];
                self.conceptsInProject = [];

                self.getConceptsInProject();
                self.getUsecasesInProject();
                self.getActorsInProject();
            }
            else {
                if (self.create) {
                    BootstrapDialog.show({
                        type: BootstrapDialog.TYPE_WARNING,
                        title: 'Warning!',
                        closable: false,
                        cssClass: 'medium-dialog',
                        message: 'A use case with the chosen name \'' + self.name + '\' <strong>already exists</strong>.\n\n<strong>You can now choose what you want to do:</strong><ul><li>You can view the details of the existing use case so you can decide wether you can use this existing use case or not.</li><li>You can continue the creation of the new use case. Be aware you will then have to choose a new use case name first, because the use case name in a project must be unique.</li></ul>',
                        buttons: [{
                            icon: 'glyphicon glyphicon-open',
                            label: 'View use case details',
                            cssClass: 'btn btn-primary',
                            action: function(dialogItself) {
                                $location.path('/projects/' + self.projectName + '/usecases/' + Format.formatToJs(self.name));
                                $rootScope.$apply();
                                dialogItself.close();
                            }
                        }, {
                            icon: 'glyphicon glyphicon-wrench',
                            label: 'Continue creation',
                            cssClass: 'btn btn-success',
                            action: function(dialogItself) {
                                dialogItself.close();
                            }
                        }]
                    });
                }
                else {
                    BootstrapDialog.show({
                        type: BootstrapDialog.TYPE_DANGER,
                        title: 'Error!',
                        closable: false,
                        cssClass: 'small-dialog',
                        message: 'A use case with the chosen name \'' + self.name + '\' <strong>already exists</strong>.\n\nPlease choose another new name for the use case.',
                        buttons: [{
                            label: 'Ok',
                            cssClass: 'btn btn-danger',
                            action: function(dialogItself) {
                                dialogItself.close();
                            }
                        }]
                    });
                }
            }
        }).error(function(data, status, headers, config) {
            Dialog.showHttpErrorDialog('An error occured when validating the use case name...', data);
        });
    };

    self.moveToNoDetails = function() {
        Nav.setUsecaseDetails(false);
    };

    /**
     * Creates an actor.
     */
    self.createActor = function() {
        if (!self.isEmpty(self.newActorName)) {
            Nav.setCreateActorOnUsecase(false);

            var newActor = { name: Format.formatToJs(self.newActorName) };

            var createActorUrl = Hateoas.getProjectInMap(self.projectName)['actor_add'];

            $http.post(createActorUrl, newActor).success(function(data, status, headers, config) {
                if (self.addActorFlowType == 'normal') {
                    self.normalFlow[self.addActorStepIndex].executor = Format.formatToJs(self.newActorName);
                    self.setLocationOnPage('flow-step-' + self.addActorStepIndex);
                }
                else if (self.addActorFlowType == 'alternative') {
                    self.alternativeFlows[self.addActorFlowIndex].steps[self.addActorStepIndex].executor = Format.formatToJs(self.newActorName);
                    self.setLocationOnPage('alt-flow-' + self.addActorFlowIndex + '-step-' + self.addActorStepIndex);
                }
                else if (self.addActorFlowType == 'exceptional') {
                    self.exceptionalFlows[self.addActorFlowIndex].steps[self.addActorStepIndex].executor = Format.formatToJs(self.newActorName);
                    self.setLocationOnPage('exc-flow-' + self.addActorFlowIndex + '-step-' + self.addActorStepIndex);
                }

                self.actorsInProject.push(Format.formatToJs(self.newActorName));
                self.actorsInProject = self.actorsInProject.sort(function(a, b) {
                    return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
                });

                self.referenceList.push(Format.formatToJs(self.newActorName));
                self.referenceList = self.referenceList.sort(function(a, b) {
                    return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
                });

                self.newActorName = '';
                self.addActorFlowType = '';
                self.addActorFlowIndex = -1;
                self.addActorStepIndex = -1;
            }).error(function(data, status, headers, config) {
                Dialog.showHttpErrorDialog('Creating actor failed!', data);
            });
        }
        else {
            Dialog.showErrorDialog('No valid actor name entered!\n\nMake sure the actor name is <strong>not empty</strong>.');
        }
    };

    /**
     * Calcels creating an actor.
     */
    self.cancelCreateActor = function() {
        self.newActorName = "";
        Nav.setCreateActorOnUsecase(false);
    };

    /**
     * Cancels creating a concept.
     */
    self.cancelCreateConcept = function() {
        self.newConceptName = "";
        self.newConceptDefinition = "";
        self.newConceptAttributes = [''];
        self.hasNoAttributes = false;

        Nav.setCreateConceptOnUsecase(false);
    };

    /**
     * Creates a concept.
     */
    self.createConcept = function() {
        self.valid = true;
        self.errorMessages = [];

        if (self.isEmpty(self.newConceptName)) {
            self.valid = false;
            self.errorMessages.push('Concept name cannot be empty');
        }
        if (self.isEmpty(self.newConceptDefinition)) {
            self.valid = false;
            self.errorMessages.push('Definition cannot be empty');
        }
        if (!self.hasNoAttributes) {
            for (var i = 0; i < self.newConceptAttributes.length; i++) {
                if (self.isEmpty(self.newConceptAttributes[i])) {
                    self.valid = false;
                    self.errorMessages.push('Attribute fields cannot be empty');
                    break;
                }
            }
        }

        var definition = self.createReferenceData(self.newConceptDefinition, '(in concept definition)');

        if (self.valid) {
            if (self.hasNoAttributes) {
                self.newConceptAttributes = [];
            }

            var newConcept = {
                name: Format.formatToJs(self.newConceptName),
                definition: definition,
                attributes: self.newConceptAttributes
            };

            var createConceptUrl = Hateoas.getProjectInMap(self.projectName)['concept_add'];

            $http.post(createConceptUrl, newConcept).success(function(data, status, headers, config) {
                self.conceptsInProject.push(Format.formatToJs(self.newConceptName));
                self.conceptsInProject = self.conceptsInProject.sort(function(a, b) {
                    return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
                });

                self.referenceList.push(Format.formatToJs(self.newConceptName));
                self.referenceList = self.referenceList.sort(function(a, b) {
                    return a.toLowerCase() < b.toLowerCase() ? -1 : 1 ;
                });

                self.newConceptName = '';
                self.newConceptDefinition = '';
                self.newConceptAttributes = [''];
                self.hasNoAttributes = false;

                Nav.setCreateConceptOnUsecase(false);
                Dialog.setAlert('createConceptAlert');
            }).error(function(data, status, headers, config) {
                Dialog.showHttpErrorDialog('Creating concept failed!', data);
            });
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
     * Adds a concept attribute.
     */
    self.addConceptAttribute = function() {
        self.newConceptAttributes.push('');
    };

    /**
     * Removes a concept attribute.
     */
    self.removeConceptAttribute = function(index) {
        if (self.newConceptAttributes.length > 1) {
            self.newConceptAttributes.splice(index, 1);
        }
    };

    /**
     * Sets navigation for optimal viewing.
     */
    self.setLocationOnPage = function(name) {
        $location.hash('nav-' + name);
        $anchorScroll();
        $location.hash('');
    };

    /**
     * Validates a use case name.
     */
    self.validUsecaseName = function(usecaseName) {
        if (usecaseName == '') {
            return false;
        }
        return true;
    };

    self.isEmpty = function(str) {
        return str == '';
    };

    /**
     * Checks whether this project contains a concept with the given name.
     */
    self.projectContainsConcept = function(name) {
        for (var i = 0; i < self.conceptsInProject.length; i++) {
            if (self.conceptsInProject[i] == name) {
                return true;
            }
        }
        return false;
    };

    /**
     * Checks whether this project contains a use case with the given name.
     */
    self.projectContainsUsecase = function(name) {
        for (var i = 0; i < self.usecasesInProject.length; i++) {
            if (self.usecasesInProject[i] == name) {
                return true;
            }
        }
        return false;
    };

    /**
     * Checks whether this project contains an actor with the given name.
     */
    self.projectContainsActor = function(name) {
        for (var i = 0; i < self.actorsInProject.length; i++) {
            if (self.actorsInProject[i] == name) {
                return true;
            }
        }
        return false;
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
     * Recursively parses json object to obtain all references.
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
     * Finds all step attributes in a json object.
     */
    self.findStepInData = function(step) {
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
     * Finds the executor in a given json object.
     */
    self.findExecutorInData = function(step) {
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
     * Parses a json object to retrieve both a condition and all references.
     */
    self.parseCondition = function(condition) {
        if (condition.type == 'textual') {
            return condition.data.text;
        }
        else {
            return self.linearizeReference(condition);
        }
    };

}]);
