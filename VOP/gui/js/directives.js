/**
 * Directives for the vopro frontend app.
 */
var voproDirectives = angular.module('voproDirectives', []);

/**
 * A directive to interpret keyboard presses and events, applying an
 * 'ng-keyboard' tag to your html element allows you to specify a function
 * to be called. The key id is supplied as argument to the function but at
 * the moment only arrow up, arrow down and enter are caught.
 */
voproDirectives.directive('ngKeyboard', function () {
    return function (scope, element, attrs) {
        element.bind('keypress', function (event) {
            var key = (event.keyCode) ? event.keyCode : event.which ;

            if (key == 8 || key == 64 || key == 13 || key == 38) {
                scope.$apply(function() {
                    var func = scope.$eval(attrs.ngKeyboard);
                    func(key, event);
                });
            }
        });
        element.bind('keydown', function (event) {
            var key = (event.keyCode) ? event.keyCode : event.which ;

            if (key == 8 || key == 64 || key == 13 || key == 40 || key == 38) {
                scope.$apply(function() {
                    var func = scope.$eval(attrs.ngKeyboard);
                    func(key, event);
                });
            }
        });

    };
});

/**
 * Provides route specific css.
 * Every time the route changes, the provided function is called. This
 * function assures that the page specific css is incluced in the html
 * (these are the css filed specified in the routeprovider confing in app.js)
 */
voproDirectives.directive('head', function($rootScope, $compile) {
    return {
        restrict: 'E',
        link: function(scope, elem) {
            var html = '<link rel="stylesheet" ng-repeat="(routeCtrl, cssUrl) in routeStyles" ng-href="{{cssUrl}}" />';
            elem.append($compile(html)(scope));
            scope.routeStyles = {};
            $rootScope.$on('$routeChangeStart', function (e, next, current) {
                if (current && current.$$route && current.$$route.css){
                    if (!angular.isArray(current.$$route.css)){
                        current.$$route.css = [current.$$route.css];
                    }
                    angular.forEach(current.$$route.css, function(sheet) {
                        delete scope.routeStyles[sheet];
                    });
                }
                if (next && next.$$route && next.$$route.css) {
                    if (!angular.isArray(next.$$route.css)) {
                        next.$$route.css = [next.$$route.css];
                    }
                    angular.forEach(next.$$route.css, function(sheet) {
                        scope.routeStyles[sheet] = sheet;
                    });
                }
            });
        }
    };
});

