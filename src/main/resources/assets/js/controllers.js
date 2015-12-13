var autoBio = angular.module('AutoBioControllersModule', ['autoBioFactories']);

autoBio.controller('EmptyController', ['$scope', function ($scope) {

}]);

autoBio.controller('LoginController', ['$scope', '$routeParams', '$location', function ($scope, $routeParams, $location) {
    //if next field is not empty, it means that login page was loaded after wrong loginsubmit
    $scope.error = $location.search().e;
    //param which used to decide where we should redirect user after successful login
    var redirectParam = $routeParams.redir;
    var redirectQueryParam = redirectParam == undefined ? "" : "?redir=" + redirectParam;

    $scope.submitted = false;
    $scope.submitLogin = function (loginForm, $event) {
        $scope.submitAction = "/login" + redirectQueryParam;
        return $scope.submitLoginOrRegister(loginForm, $event);
    };

    $scope.submitRegister = function (loginForm, $event) {
        $scope.submitAction = "/register" + redirectQueryParam;
        return $scope.submitLoginOrRegister(loginForm, $event);
    };

    $scope.submitLoginOrRegister = function (loginForm, $event) {

        Object.keys(loginForm.$error).forEach(function (key) {
            var errorText = null;
            var elements = loginForm.$error[key];
            if (key == "required") {
                errorText = "This field is required";
            } else if (key == "minlength") {
                errorText = "Value is too short";
            } else if (key == "maxlength") {
                errorText = "This field is big (long)";
            }

            if (errorText != null) {
                var node = document.createElement("div");
                var textNode = document.createTextNode(errorText);
                node.appendChild(textNode);
                elements.forEach(function(element){
                    document.getElementById(element.$name).parentElement.appendChild(node);
                })


            }

        });
        $scope.submitted = true;
        if (!loginForm.$valid) {
            $event.preventDefault();
        }
        return loginForm.$valid;
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