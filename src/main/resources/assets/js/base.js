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
            })
            .when('/not-authorized', {
                templateUrl: 'ajax/not-authorized',
                controller: 'NotAuthorizedController'
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
            } else if (status == 403) {
                //403 Forbidden response should be used afterwards,
                //when the user is authenticated but isn’t authorized to perform the requested operation on the given resource.
                url = '/not-authorized';
            } else {
                url = '/error';
            }
            $location.path(url);

            return $q.reject(rejection);
        }
    };
}]);


autobiographyApp.config(['$httpProvider', function ($httpProvider) {
    $httpProvider.interceptors.push('myHttpInterceptor');
}]);




