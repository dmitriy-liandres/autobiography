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
        var isFormValid = isValid(loginForm);
        $scope.submitted = true;
        if (!isFormValid) {
            $event.preventDefault();
        }
        return isFormValid;
    };
}]);

autoBio.controller('ProfileController', ['$scope', 'ProfileLoaded', '$location', '$window', function ($scope, ProfileLoaded, $location, $window) {
    $scope.profile = {};
    var personId = $location.path().split("/")[3];
    if (personId == undefined || personId == null) {
        personId = "";
    }

    ProfileLoaded.get({personId: personId}, function (loadedProfile) {
        $scope.profile = loadedProfile;
    });

    $scope.updateProfile = function (profileForm) {
        if (isValid(profileForm)) {
            ProfileLoaded.add($scope.profile, function () {
                $window.location.href = '/profile';
            });
        }
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

autoBio.controller('AutoBiographyFullController', ['$scope', '$location', 'AutobiofullSaver', function ($scope, $location, AutobiofullSaver) {
    //$scope.autobioFull = "";
    var personId = $location.path().split("/")[3];
    if (personId == undefined || personId == null) {
        personId = "";
    }
    var isEditorReady = false;
    var autobioFullText = null;

    loadScript("../../../assets/js/ckeditor/ckeditor.js", function () {
        var lang = document.getElementById("lang-input-id").value;
        /**Added plugins:
         * Enhanced Image, Find / Replace, Font Size and Family, Image Browser,
         * Justify, Print, Upload Image
         * Removed plugins:
         * About CKEditor
         */

        CKEDITOR.replace('autobioText', {

            imageBrowser_listUrl: '/data/files',
            filebrowserUploadUrl: '/data/file',
            language: lang,
            height: 500
            // Configure your file manager integration. This example uses CKFinder 3 for PHP.


        });

        CKEDITOR.on("instanceReady", function (event) {
            if (autobioFullText != null && !isEditorReady) {
                CKEDITOR.instances.autobioText.setData(autobioFullText);
            }
            isEditorReady = true;


        });


    });
    AutobiofullSaver.get({personId: personId}, function (autobioFull) {
        if (autobioFull != null) {
            autobioFullText = autobioFull.text;
        } else {
            autobioFullText = "";
        }
        if (isEditorReady) {
            CKEDITOR.instances.autobioText.setData(autobioFullText);
        }
    });

    $scope.saveAutobioFull = function () {
        var data = CKEDITOR.instances.autobioText.getData();
        console.info(data);
        AutobiofullSaver.add(data, function () {

        });
    }
}]);

function loadScript(url, callback) {

    var script = document.createElement("script");
    script.type = "text/javascript";

    if (script.readyState) {  //IE
        script.onreadystatechange = function () {
            if (script.readyState == "loaded" ||
                script.readyState == "complete") {
                script.onreadystatechange = null;
                callback();
            }
        };
    } else {  //Others
        script.onload = function () {
            callback();
        };
    }

    script.src = url;
    document.getElementsByTagName("head")[0].appendChild(script);
}


function isValid(formName) {
    var errorElements = document.getElementsByClassName("validation-error-ui");

    var previousErrorsNumber = errorElements.length;
    for (var i = previousErrorsNumber - 1; i >= 0; i--) {
        errorElements[i].parentNode.removeChild(errorElements[i]);
    }


    Object.keys(formName.$error).forEach(function (key) {
        var errorText = null;
        var angularElements = formName.$error[key];
        if (key == "required") {
            errorText = VALIDATION_REQUIRED;
        } else if (key == "minlength") {
            errorText = VALIDATION_MIN_LENGTH;
        } else if (key == "maxlength") {
            errorText = VALIDATION_MAX_LENGTH;
        }

        if (errorText != null) {
            var node = document.createElement("div");
            node.setAttribute("class", "validation-error-ui");
            var textNode = document.createTextNode(errorText);
            node.appendChild(textNode);
            angularElements.forEach(function (angularElement) {
                document.getElementById(angularElement.$name).parentElement.appendChild(node.cloneNode(true));
            })
        }

    });

    return formName.$valid;
}

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