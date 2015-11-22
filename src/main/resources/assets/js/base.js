var autobiographyApp = angular.module('autobiographyApp', ["AutoBioControllersModule", "ngRoute"]);


autobiographyApp.config(['$routeProvider', '$locationProvider',
    function($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true).hashPrefix('!');
        $routeProvider.
            when('/login', {
                templateUrl: 'ajax/login',
                controller: 'AutoBioController'
            }).when('/profile', {
                templateUrl: 'ajax/profile',
                controller: 'AutoBioController'
            }).
            otherwise({
                redirectTo: '/login'
            });
    }]);
