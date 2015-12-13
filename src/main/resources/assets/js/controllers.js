var autoBio = angular.module('AutoBioControllersModule', ['autoBioFactories']);

autoBio.controller('EmptyController', ['$scope', function ($scope) {

}]);

autoBio.controller('LoginController', ['$scope', '$routeParams', function ($scope, $routeParams) {
    var redirectParam = $routeParams.redir;
    var redirectQueryParam = redirectParam == undefined ? "" : "?redir=" + redirectParam;
    $scope.submitLogin = function () {
        $scope.submitAction = "/login" + redirectQueryParam;
        return true;
    };

    $scope.submitRegister = function () {
        $scope.submitAction = "/register" + redirectQueryParam;
        return true;
    };

}]);

autoBio.controller('ProfileController', ['$scope', 'ProfileLoaded', '$location', '$window', function ($scope, ProfileLoaded, $location, $window) {
    $scope.profile = {};
    var personId = $location.path().split("/")[3];
    ProfileLoaded.get({personId: personId}, function (loadedProfile) {
        $scope.profile = loadedProfile;
    });

    $scope.updateProfile = function () {
        ProfileLoaded.add($scope.profile, function () {
            $window.location.href = '/profile';
        });
    };
}]);


autoBio.controller('LogoutController', ['$scope', '$window', function ($scope, $window) {
    $window.location.href = '/';
}]);

autoBio.controller('NotFoundController', ['$scope', '$window', function ($scope, $window) {
    redirectToMain($window);

}]);

autoBio.controller('NotAuthorizedController', ['$scope', '$window', function ($scope, $window) {
    redirectToMain($window);
}]);

autoBio.controller('ErrorController', ['$scope', '$window', function ($scope, $window) {
    redirectToMain($window);

}]);

function redirectToMain($window) {
    setTimeout(function () {
        if (isLoggedIn()) {
            $window.location.href = '/main';
        } else {
            $window.location.href = '/';
        }
    }, 5000);
}

function isLoggedIn() {
    var isLoggedInEl = document.getElementById("isLoggedIn");
    return isLoggedInEl != undefined;
}