/**
 * Service to handle formatting issues. These mainly concern displaying
 * data in a different way than it's stored in the database.
 * <p>
 * An example
 * is that a space will internally be stored as an underscore, which in
 * turn should be displayed with a space instead of an underscore.
 */
var formatService = angular.module('formatService', []);

formatService.service('Format', function() {

    /**
     * Formatting function making the string more readable in html.
     * Removes underscores and replaces them with spaces.
     */
    var formatToHtml = function(str) {
        if (str) {
            return str.replace(/_/g, ' ');
        }
        else {
            return str;
        }
    };

    /**
     * Formatting function converting a html readable string into a
     * functional string that can be further used in the javascript code.
     * Removes spaces and replaces them with underscores.
     */
    var formatToJs = function(str) {
        if (str) {
            return str.replace(/ /g, '_');
        }
        else {
            return str;
        }
    };

    return {
        formatToHtml: formatToHtml,
        formatToJs: formatToJs
    };

});
