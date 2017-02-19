/**
 * Service to handle dialogs, mainly for displaying positive feedback and
 * error messages as feedback on a user action or on loading a page.
 * Uses the bootstrap dialog service to display these dialogs. The files which
 * make it possible to use the bootstrap dialog service are included in the
 * subfolder /assets.
 */
var dialogService = angular.module('dialogService', []);

dialogService.service('Dialog', function(Auth, $location, $rootScope) {

    /* Div alert part (success messages) */

    var alertMap = new Object();
    var initNeeded = true;

    var getInitNeeded = function() {
        return initNeeded;
    };

    var setInitNeeded = function(value) {
        initNeeded = value;
    };

    var initAlerts = function() {
        alertMap.deleteActorAlert = false;
        alertMap.deleteConceptAlert = false;
        alertMap.deleteUsecaseAlert = false;
        alertMap.deleteProjectAlert = false;
        alertMap.deleteTeamAlert = false;
        alertMap.deleteUserAlert = false;
        alertMap.createActorAlert = false;
        alertMap.createConceptAlert = false;
        alertMap.createUsecaseAlert = false;
        alertMap.createProjectAlert = false;
        alertMap.createTeamAlert = false;
        alertMap.patchActorAlert = false;
        alertMap.patchConceptAlert = false;
        alertMap.patchUsecaseAlert = false;
        alertMap.patchProjectNameAlert = false;
        alertMap.addProjectAlert = false;
        alertMap.addUserAlert = false;
        alertMap.removeUserAlert = false;
        alertMap.removeProjectAlert = false;
        alertMap.patchAnalystAlert = false;
        alertMap.patchAdminAlert = false;
        alertMap.registerUserAlert = false;
        alertMap.patchProjectLeaderAlert = false;
        alertMap.patchTeamLeaderAlert = false;
        alertMap.patchTeamNameAlert = false;
        alertMap.importUsecaseAlert = false;
        alertMap.exportUsecaseAlert = false;
        alertMap.scheduleAlert = false;
        alertMap.scheduleUsecaseAlert = false;
        alertMap.rescheduleUsecaseAlert = false;
        alertMap.unscheduleUsecaseAlert = false;
		alertMap.deleteProcessAlert = false;
		alertMap.patchProcessAlert = false;
		alertMap.createProcessAlert = false;
    };

    var setAlert = function(key) {
        alertMap[key] = true;
    };

    var getAlert = function(key) {
        return alertMap[key];
    };

    var clearAlert = function(key) {
        alertMap[key] = false;
    };

    /* Bootstrap dialog part (error messages) */

    /**
     * Displays a dialog containing an error message.
     * This dialog only contains some information and therefore only includes an 'OK'
     * which will close the dialog when clicked on.
     */
    var showSizedErrorDialog = function(message, size) {
        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_DANGER,
            title: 'Error',
            closable: false,
            cssClass: size,
            message: message,
            buttons: [{
                label: 'Ok',
                cssClass: 'btn btn-danger',
                action: function(dialogItself) {
                    dialogItself.close();
                }
            }]
        });
    };

    /**
     * Displays an error dialog with a default small size.
     */
    var showErrorDialog = function(message) {
        showSizedErrorDialog(message, 'small-dialog');
    };

    /**
     * Displays an error dialog with a larger size.
     */
    var showLargeErrorDialog = function(message) {
        showSizedErrorDialog(message, 'medium-dialog');
    };

    /**
     * Displays an error dialog when an error occurred while doing a http request.
     * The return data from the server is passed to this function, so that the error
     * message returned by the server can be appended to the message shown in the dialog,
     * when the cause of the error is a conflict (e.g. a naming conflict) and can be
     * solved by the user.
     */
    var showHttpErrorDialog = function(message, data) {
        var mess = message;
        if (data && data.code && data.code == 409 && data.message && data.message != '') {
            mess += '\n\nCause of error (returned by server): ' + data.message;
        }
        showErrorDialog(mess);
    };

    /**
     * Displays dialog containing a load error when loading the main page of
     * the application.
     * Gives the user the possibility to logout (because the app won't
     * be functional so staying logged in the user won't be able to do
     * something significant). It's also possible to continue on the page,
     * although the user won't be able to do anything significant.
     */
    var showLoadErrorDialog = function(message) {
        BootstrapDialog.show({
            type: BootstrapDialog.TYPE_DANGER,
            title: 'Error',
            closable: false,
            cssClass: 'medium-dialog',
            message: message,
            buttons: [{
                icon: 'glyphicon glyphicon-log-out',
                label: 'Logout',
                cssClass: 'btn btn-info',
                action: function(dialogItself) {
                    Auth.logout();
                    dialogItself.close();
                }
            }, {
                label: 'Continue',
                cssClass: 'btn btn-danger',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
        });
    };

    return {
        showErrorDialog: showErrorDialog,
        showLargeErrorDialog: showLargeErrorDialog,
        showHttpErrorDialog: showHttpErrorDialog,
        showLoadErrorDialog: showLoadErrorDialog,
        setAlert: setAlert,
        getAlert: getAlert,
        clearAlert: clearAlert,
        initAlerts: initAlerts,
        getInitNeeded: getInitNeeded,
        setInitNeeded: setInitNeeded
    };

});

