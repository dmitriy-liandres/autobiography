var autobiographyApp = angular.module('autobiographyApp', ["AutoBioControllersModule", "ngRoute"]);


autobiographyApp.config(['$routeProvider', '$locationProvider',
    function ($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true).hashPrefix('!');
        $routeProvider.
            when('/', {
                templateUrl: 'ajax/login',
                controller: 'LoginController'
            })
            .when('/main', {
                templateUrl: 'ajax/main',
                controller: 'EmptyController'
            })
            .when('/profile', {
                templateUrl: 'ajax/profile',
                controller: 'ProfileController'
            })
            .when('/logout', {
                templateUrl: 'ajax/logout',
                controller: 'LogoutController'
            }).
            otherwise({
                redirectTo: '/'
            });
    }]);
