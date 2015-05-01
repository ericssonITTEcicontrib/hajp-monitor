/**
 * Dashboard routes.
 */
define(['angular', './controllers', 'common'], function (angular, controllers) {
    'use strict';

    var mod = angular.module('services.routes', ['yourprefix.common']);
    mod.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/services', {
                templateUrl: '/assets/partials/services/index.html',
                controller: controllers.ServicesCtrl
            });
    }]);
    return mod;
});
