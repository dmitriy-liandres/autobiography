var autobiographyApp = angular.module('autobiographyApp', ["AutoBioControllersModule", "ngRoute"]);


autobiographyApp.config(['$routeProvider', '$locationProvider',
    function ($routeProvider, $locationProvider) {
        $locationProvider.html5Mode(true).hashPrefix('!');
        $routeProvider.
            when('/', {
                templateUrl: 'ajax/login',
                controller: 'LoginController'
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
            })
            .when('/autobiography-full', {
                templateUrl: 'ajax/autobiography-full',
                controller: 'AutoBiographyFullController'
            })
            .when('/autobiography-for-work', {
                templateUrl: 'ajax/autobiography-for-work',
                controller: 'AutoBiographyForWorkController'
            }).
            when('/autobiography-interesting', {
                templateUrl: 'ajax/autobiography-interesting',
                controller: 'AutoBiographyInterestingController'
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




