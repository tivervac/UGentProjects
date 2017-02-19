/**
 * Service to handle locations and paths.
 * Contains the basic endpoints urls retrieved from the server when
 * initializing the app. These endpoints are used to construct some of
 * the http request urls.
 * Also stores the base url which is used to contact the server when
 * initializing the app. This url is the ONLY hard coded url in the whole
 * frontend application.
 */
var pathService = angular.module('pathService', []);

pathService.service('Path', function() {

    var baseUrl = 'http://localhost:9000';
    var manualUrl = 'http://vopro1.ugent.be/data/documents/GebruikersHandleiding.pdf';

    var urls = null;

    /**
     * Returns base url.
     */
    var getBaseUrl = function() {
        return baseUrl;
    };

    /**
     * Sets urls of endpoints.
     */
    var setUrls = function(data) {
        urls = new Object();
        var links = data.content.links;
        angular.forEach(links, function(value, key) {
            urls[key] = value;
        });
    };

    /**
     * Retrieves urls of endpoints.
     */
    var getEndpoint = function(name) {
        if (urls) {
            return urls[name];
        }
        else {
            return '';
        }
    };

    /**
     * Returns manual url.
     */
    var getManualUrl = function() {
        return manualUrl;
    };

    return {
        getBaseUrl: getBaseUrl,
        setUrls: setUrls,
        getEndpoint: getEndpoint,
        getManualUrl: getManualUrl
    };

});
