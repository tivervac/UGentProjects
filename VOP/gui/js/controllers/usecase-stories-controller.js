/**
 * Controller for the use case stories page, here many different scenarios of use case
 * executions (i.e. stories) are displayed. The controller also supports a downloadable
 * file which is also available when end user is offline and has a local copy.
 */
var usecaseStoriesController = angular.module('usecaseStoriesController', []);

usecaseStoriesController.controller('UsecaseStoriesCtrl', ['$scope', '$routeParams', '$http', 'Hateoas', 'Dialog', function($scope, $routeParams, $http, Hateoas, Dialog) {
    var self = this;

    self.numBranch = 2;

    self.projectName = $routeParams.projectName;
    self.usecaseName = $routeParams.usecaseName;

    self.stories = null;
    self.count = 0;

    self.html = "";

    self.numBranchOptions = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

    Dialog.initAlerts();
    Dialog.setInitNeeded(false);

    if (!Hateoas.getProjectInMap(self.projectName)) {
        Dialog.showErrorDialog('The given project name is not known. You\'re redirected back to the project list.');
        $location.path('/projects');
        return;
    }

    /**
     * Recursively extract references from flow step.
     */
    var linearizeReference = function(step) {
        var left;
        var right;
        if (step.data.left.type == 'reference') {
            left = linearizeReference(step.data.left);
        }
        else {
            left = step.data.left.data.text;
        }
        if (step.data.right.type == 'reference') {
            right = linearizeReference(step.data.right);
        }
        else {
            right = step.data.right.data.text;
        }
        var result = left + '@' + step.data.reference.replace(/_/g, " ") + right;
        return result;
    };

    var parseReferenceData = function(arg) {
        if (arg.type == 'textual') {
            return arg.data.text;
        }
        else {
            return linearizeReference(arg);
        }
    };

    /**
     * Returns precondition text.
     */
    var preconditionText = function (condition) {
        return parseReferenceData(condition);
    };

    /**
     * Returns postcondition text.
     */
    var postconditionText = function (condition) {
        return parseReferenceData(condition);
    };

    /**
     * Returns objective text.
     */
    var objectiveText = function (objective) {
        return parseReferenceData(objective);
    };

    /**
     * Returns whether json element is a step.
     */
    var isStep = function (element) {
        return element.type == 'step';
    };

    /**
     * Returns the text of a step.
     */
    var stepText = function (element) {
        var executor = element.data.executor.replace(/_/g, " ");
        var step = parseReferenceData(element.data.description);
        return "<strong>Executor:</strong> " + executor + "</br><strong>Description:</strong> " + step;
    };

    /**
     * Returns the text of a condition.
     */
    var conditionText = function (element) {
        return parseReferenceData(element.data);
    };

    /**
     * Update the stories html, this html is downloadable.
     */
    self.update = function () {
        var action = {
            action: 'story',
            numBranch: self.numBranch
        };

        if (Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)) {
            var url = Hateoas.getUsecaseInMap(self.projectName, self.usecaseName)['self'];

            $http.put(url, action).success(function(data, status, headers, config) {
                $http.get(url).success(function(useCaseData, status, headers, conf) {

                    self.html = baseHTML();
                    $('.storyTables').html("");
                    var useCaseDetails = useCaseData.content;

                    var conditions = useCaseDetails['preconditions'];
                    var label = (conditions.length == 1) ? "Precondition" : "Preconditions" ;
                    self.pre = "<tr><td>" + label + "</td><td>";
                    for (i = 0; i < conditions.length; i++) {
                        self.pre += conditions.length > 1 ? (i+1) + ". " : "";
                        self.pre += preconditionText(conditions[i]) + "<br>";
                    }
                    self.pre += "</td></tr>\n";

                    var conditions = useCaseDetails['postconditions'];
                    var label = (conditions.length == 1) ? "Postcondition" : "Postconditions" ;
                    self.post = "<tr><td>" + label + "</td><td>";
                    for (i = 0; i < conditions.length; i++) {
                        self.post += conditions.length > 1 ? (i+1) + ". " : "";
                        self.post += postconditionText(conditions[i]) + "<br>";
                    }
                    self.post += "</td></tr>\n";

                    self.objective = objectiveText(useCaseDetails['objective']);

                    var tableBase = '</h3>\n<div class="CSSTableGenerator">\n<table>\n<tr><td>Type</td><td>Description</td></tr>\n'
                                  + "<tr><td>Objective</td><td>" + self.objective + "</td></tr>\n" + self.pre;

                    self.count = 0;
                    var stories = data.content;

                    for (i = stories.length - 1; i >= 0; i--) {
                        var table = "<hr/><div style='width:880px'>\n<h3>Story " + (stories.length - i) + tableBase;
                        var elements = stories[i];
                        for (j = 0; j < elements.length; j++) {
                            table += "<tr><td>"
                            if (isStep(elements[j])) {
                                table += "Flow Step</td><td>" + stepText(elements[j]) + "</td></tr>\n";
                            }
                            else {
                                table += "Condition</td><td>" + conditionText(elements[j]) + "</td></tr>\n";
                            }
                        }
                        table += self.post + "</table>\n</div>\n</div>\n";
                        $('.storyTables').append(table);
                        self.html += table;
                    }
                    self.html += endHTML();

                }).error(function(data, status, header, config) {
                    Dialog.showHttpErrorDialog('An error occured retrieving the use case details...', data);
                });
            }).error(function(data, status, header, config) {
                Dialog.showHttpErrorDialog('An error occured retrieving the stories.', data);
            });
            }
        else {
            Dialog.showErrorDialog('The requested use case cannot be found...');
        }
    };
    self.update();

    /**
     * Defines CSS for table layout.
     */
    var tableCSS = function () {
        return "div.CSSTableGenerator {\n"
            + "    margin:0px;padding:0px;\n"
            + "    width:100%;\n"
            + "    box-shadow: 10px 10px 5px #888888;\n"
            + "    border:1px solid #000000;\n"
            + "    -moz-border-radius-bottomleft:0px;\n"
            + "    -webkit-border-bottom-left-radius:0px;\n"
            + "    border-bottom-left-radius:0px;\n"
            + "    -moz-border-radius-bottomright:0px;\n"
            + "    -webkit-border-bottom-right-radius:0px;\n"
            + "    border-bottom-right-radius:0px;\n"
            + "    -moz-border-radius-topright:0px;\n"
            + "    -webkit-border-top-right-radius:0px;\n"
            + "    border-top-right-radius:0px;\n"
            + "    -moz-border-radius-topleft:0px;\n"
            + "    -webkit-border-top-left-radius:0px;\n"
            + "    border-top-left-radius:0px;\n"
            + "}.CSSTableGenerator table{\n"
            + "    border-collapse: collapse;\n"
            + "    border-spacing: 0;\n"
            + "    width:100%;\n"
            + "    height:100%;\n"
            + "    margin:0px;padding:0px;\n"
            + "}.CSSTableGenerator tr:last-child td:last-child {\n"
            + "    -moz-border-radius-bottomright:0px;\n"
            + "    -webkit-border-bottom-right-radius:0px;\n"
            + "    border-bottom-right-radius:0px;\n"
            + "}\n"
            + ".CSSTableGenerator table tr:first-child td:first-child {\n"
            + "    -moz-border-radius-topleft:0px;\n"
            + "    -webkit-border-top-left-radius:0px;\n"
            + "    border-top-left-radius:0px;\n"
            + "}\n"
            + ".CSSTableGenerator table tr:first-child td:last-child {\n"
            + "    -moz-border-radius-topright:0px;\n"
            + "    -webkit-border-top-right-radius:0px;\n"
            + "    border-top-right-radius:0px;\n"
            + "}.CSSTableGenerator tr:last-child td:first-child{\n"

            + "    -moz-border-radius-bottomleft:0px;\n"
            + "    -webkit-border-bottom-left-radius:0px;\n"
            + "    border-bottom-left-radius:0px;\n"
            + "}.CSSTableGenerator tr:hover td{\n"
            + "}\n"
            + ".CSSTableGenerator tr:nth-child(odd){ background-color:#9ec9e2; }.CSSTableGenerator td{}\n"
            + ".CSSTableGenerator tr:nth-child(even)    { background-color:#ffffff; }.CSSTableGenerator td{\n"
            + "    vertical-align:middle;\n"
            + "    border:1px solid #000000;\n"
            + "    border-width:0px 1px 1px 0px;\n"
            + "    text-align:left;\n"
            + "    padding:7px;\n"
            + "    font-size:12px;\n"
            + "    font-weight:normal;\n"
            + "    color:#000000;\n"
            + "}.CSSTableGenerator tr:last-child td{\n"
            + "    border-width:0px 1px 0px 0px;\n"
            + "}.CSSTableGenerator tr td:last-child{\n"
            + "    border-width:0px 0px 1px 0px;\n"
            + "}.CSSTableGenerator tr:last-child td:last-child{\n"
            + "    border-width:0px 0px 0px 0px;\n"
            + "}\n"
            + ".CSSTableGenerator tr:first-child td{\n"
            + "    background:-o-linear-gradient(bottom, #346596 5%, #346596 100%);    background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #346596), color-stop(1, #346596) );\n"
            + "    background:-moz-linear-gradient( center top, #346596 5%, #346596 100% );\n"
            + "    filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#346596', endColorstr='#346596');  background: -o-linear-gradient(top,#346596,346596);\n"
            + "    background-color:#346596;\n"
            + "    border:0px solid #000000;\n"
            + "    text-align:center;\n"
            + "    border-width:0px 0px 1px 1px;\n"
            + "    font-size:14px;\n"
            + "    font-weight:bold;\n"
            + "    color:#ffffff;\n"
            + "}\n"
            + ".CSSTableGenerator tr:first-child:hover td{\n"
            + "    background:-o-linear-gradient(bottom, #346596 5%, #346596 100%);    background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #346596), color-stop(1, #346596) );\n"
            + "    background:-moz-linear-gradient( center top, #346596 5%, #346596 100% );\n"
            + "    filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#346596', endColorstr='#346596');  background: -o-linear-gradient(top,#346596,346596);\n"
            + "    background-color:#346596;\n"
            + "}\n"
            + ".CSSTableGenerator tr:first-child td:first-child{\n"
            + "    border-width:0px 0px 1px 0px;\n"
            + "}\n"
            + ".CSSTableGenerator tr:first-child td:last-child{\n"
            + "    border-width:0px 0px 1px 1px;\n"
            + "}\n"
            + "h1 { font-size:32px }\n"
            + "h3 { font-size:24px }\n"
            + "h1, h2, h3 {\n"
            + "     font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;\n"
            + "}\n"
            + ".CSSTableGenerator td {\n"
            + "    font-family: 'Helvetica';\n"
            + "}\n";
    };

    /**
     * Defines first part of the html document.
     */
    var baseHTML = function () {
        return "<!DOCTYPE html>\n"
             + "<html>\n"
             + "<head>\n"
             + "<!-- Table style generated by http://www.csstablegenerator.com/ all credit reserved for site owner -->\n"
             + "<style>\n"
             + tableCSS()
             + "</style>\n"
             + "<title>\n"
             + "Stories\n"
             + "</title>\n"
             + "</head>\n"
             + "<body style='width:880px; margin-left:auto; margin-right:auto'>\n"
             + "<h1>" + self.usecaseName + "</h1>\n"
             + "<h2> Stories </h1>\n";
    };

    /**
     * Download stories to local system.
     */
    self.downloadStories = function () {
        download(self.html, self.usecaseName + "-stories.html", "text/html");
    };

    /**
     * Defines end of html string.
     */
    var endHTML = function () {
        return "</body>\n</html>";
    };

}]);
