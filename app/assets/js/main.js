// `main.js` is the file that sbt-web will use as an entry point
(function (requirejs) {
    'use strict';

    // -- RequireJS config --
    requirejs.config({
        // Packages = top-level folders; loads a contained file named 'main.js"
        packages: ['common', 'home', 'dashboard', 'services'],
        shim: {
            'jsRoutes': {
                deps: [],
                // it's not a RequireJS module, so we have to tell it what var is returned
                exports: 'jsRoutes'
            },
            // Hopefully this all will not be necessary but can be fetched from WebJars in the future
            'angular': {
                deps: ['jquery'],
                exports: 'angular'
            },
            'angular-route': ['angular'],
            'angular-cookies': ['angular'],
            'ui-bootstrap': ['angular'],
            'ui-bootstrap-tpls': ['angular'],
            'angular-chart': ['angular'],
            'bootstrap': ['jquery']
        },
        paths: {
            'requirejs': ['../lib/requirejs/require'],
            'underscore': ['../lib/underscorejs/underscore'],
            'jquery': ['../lib/jquery/jquery'],
            'angular': ['../lib/angularjs/angular'],
            'angular-route': ['../lib/angularjs/angular-route'],
            'angular-cookies': ['../lib/angularjs/angular-cookies'],
            'ui-bootstrap': ['../lib/angular-ui-bootstrap/ui-bootstrap'],
            'ui-bootstrap-tpls': ['../lib/angular-ui-bootstrap/ui-bootstrap-tpls'],
            'angular-chart': ['../lib/angular-chart.js/angular-chart'],
            'bootstrap': ['../lib/bootstrap/js/bootstrap'],
            'd3': ['../lib/d3js/d3'],
            'jsRoutes': ['/jsroutes']
        }
    });

    requirejs.onError = function (err) {
        console.log(err);
    };

    // Load the app. This is kept minimal so it doesn't need much updating.
    require(['angular', 'angular-cookies', 'angular-route', 'ui-bootstrap', 'ui-bootstrap-tpls','angular-chart','jquery', 'bootstrap', 'd3', './app'],
        function (angular) {
            angular.bootstrap(document, ['app']);
        }
    );
})(requirejs);
