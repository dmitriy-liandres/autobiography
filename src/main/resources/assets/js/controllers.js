var autoBio = angular.module('AutoBioControllersModule', []);

autoBio.controller('AutoBioController', ['$scope', function ($scope) {
    $scope.submitAction = "/login";
    $scope.submitLogin = function () {
        return true;
    };

    $scope.submitRegister = function () {
        $scope.submitAction = "/register";
        return true;
    };
}]);