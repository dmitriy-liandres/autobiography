var autoBio = angular.module('AutoBioControllersModule',[]);

autoBio.controller('AutoBioController', ['$scope', function($scope) {

    $scope.submit = function() {
       console.info("okkkkkk");
        return true;
    };
}]);