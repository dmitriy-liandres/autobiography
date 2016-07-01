var autobiographyApp = angular.module('autobiographyApp', ["AutoBioControllersModule", "ngRoute", "ui.bootstrap"]);


autobiographyApp.config(['$routeProvider', '$locationProvider',
    function ($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true).hashPrefix('!');
        $routeProvider.
            when('/', {
                templateUrl: 'ajax/login',
                controller: 'LoginController'
            })
            .when('/reset-password', {
                templateUrl: 'ajax/resetPassword',
                controller: 'ResetPasswordController'
            })
            .when('/profile', {
                templateUrl: 'ajax/profile',
                controller: 'ProfileController'
            })
            .when('/profile/:personId', {
                templateUrl: 'ajax/profile-read',
                controller: 'ProfileController'
            })
            .when('/logout', {
                templateUrl: 'ajax/logout',
                controller: 'LogoutController'
            })
            .when('/not-authorized', {
                templateUrl: 'ajax/not-authorized',
                controller: 'NotAuthorizedController'
            })
            .when('/autobiography-full', {
                templateUrl: 'ajax/autobiography-full',
                controller: 'AutoBiographyFullController'
            })
            .when('/autobiography-full/:personId', {
                templateUrl: 'ajax/autobiography-full-read',
                controller: 'AutoBiographyFullReadController'
            })
            .when('/autobiography-for-work', {
                templateUrl: 'ajax/autobiography-for-work',
                controller: 'AutoBiographyForWorkController'
            })
            .when('/autobiography-for-work/:personId', {
                templateUrl: 'ajax/autobiography-for-work-read',
                controller: 'AutoBiographyFullReadController'
            }).
            when('/autobiography-interesting', {
                templateUrl: 'ajax/autobiography-interesting',
                controller: 'AutoBiographyInterestingController'
            }).
            when('/autobiography-interesting/:personId', {
                templateUrl: 'ajax/autobiography-interesting-read',
                controller: 'AutoBiographyFullReadController'
            }).
            when('/all', {
                templateUrl: 'ajax/all',
                controller: 'AllController'
            }).
            when('/about', {
                templateUrl: 'ajax/about',
                controller: 'AboutController'
            }).
            when('/contact', {
                templateUrl: 'ajax/contact',
                controller: 'ContactController'
            }).
            otherwise({
                templateUrl: 'ajax/not-found',
                controller: 'NotFoundController'
            });
    }]);


// register the interceptor as a service
autobiographyApp.factory('myHttpInterceptor', ['$q', '$location', function ($q, $location) {
    return {

        'responseError': function (rejection) {
            var status = rejection.status;
            var url = '';
            if (status == 401) {
                //401 Unauthorized response should be used for missing or bad authentication.
                url = '/';// + encodeURIComponent($location.path());
                $location.search('redir', $location.path());
                $location.path(url);
            } else if (status == 403) {
                //403 Forbidden response should be used afterwards,
                //when the user is authenticated but isn’t authorized to perform the requested operation on the given resource.
                url = '/not-authorized';
                $location.path(url);
            } else if (status == 422) {
                //422 validation error
                rejection.data.errors.forEach(function(error){
                    var fieldNameAndId = error.split(" ")[0];
                    error = error.replace(fieldNameAndId + " ", "");
                    var node = document.createElement("div");
                    node.setAttribute("class", "validation-error-ui");
                    var textNode = document.createTextNode(error);
                    node.appendChild(textNode);
                    document.getElementById(fieldNameAndId).parentElement.appendChild(node.cloneNode(true));

                })

            } else {
                url = '/error';
                $location.path(url);
            }

            return $q.reject(rejection);
        }
    };
}]);


autobiographyApp.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('myHttpInterceptor');
}]);




