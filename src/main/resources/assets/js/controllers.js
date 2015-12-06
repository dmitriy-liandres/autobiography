var autoBio = angular.module('AutoBioControllersModule', ['autoBioFactories']);

autoBio.controller('EmptyController', ['$scope', function ($scope) {

}]);

autoBio.controller('LoginController', ['$scope', function ($scope) {

    $scope.submitLogin = function () {
        $scope.submitAction = "/login";
        return true;
    };

    $scope.submitRegister = function () {
        $scope.submitAction = "/register";
        return true;
    };

}]);

autoBio.controller('ProfileController', ['$scope', 'ProfileLoaded', '$location', function ($scope, ProfileLoaded, $location) {
    $scope.profile = {};
    var personId = $location.path().split("/")[3];
    if (typeof personId == "undefined") {
        personId = "1";
    }
    ProfileLoaded.get({personId:personId}, function (loadedProfile) {
        $scope.profile = loadedProfile;
    });

    $scope.updateProfile = function () {
        ProfileLoaded.add($scope.profile, function () {
            console.info($scope.profile);
        });
    };
}]);


autoBio.controller('LogoutController', ['$scope', '$window', function ($scope, $window) {
    $window.location.href = '/';
}]);