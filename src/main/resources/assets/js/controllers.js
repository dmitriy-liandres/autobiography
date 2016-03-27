var autoBio = angular.module('AutoBioControllersModule', ['autoBioFactories']);

autoBio.controller('EmptyController', ['$scope', '$http', '$window', function ($scope, $http, $window) {
    $scope.searchProfiles = function (val) {
        return $http.get('data/search/' + val, {}).then(function (response) {
            return response.data;
        });
    };

    $scope.selectProfile = function ($item, $model, $label, $event) {
        $window.location.href = '/autobiography-for-work/' + $item.id;
    };
}]);

autoBio.controller('AllController', ['$scope', 'AllLoaderFactory', function ($scope, AllLoaderFactory) {
    AllLoaderFactory.query(function (all) {
        $scope.personGroups = [];
        var size = 3;
        for (var groupIndex = 0, numGroups = Math.ceil(all.length / size); groupIndex < numGroups; groupIndex++) {
            var startIndex = groupIndex * size;
            $scope.personGroups.push(all.slice(startIndex, startIndex + size));
        }
    });

    $scope.getName = getName;
}]);

autoBio.controller('UserController', ['$scope', '$http', '$location', 'ProfileFactory', function ($scope, $http, $location, ProfileFactory) {
    $scope.currentUserProfile = {};
    $scope.currentUserName = "";
    $scope.idForLeftMenu = "";

    var currentUserPersonId = getPersonId($location);

    ProfileFactory.get({personId: currentUserPersonId}, function (loadedProfile) {
        $scope.currentUserProfile = loadedProfile;
        $scope.currentUserProfile = loadedProfile;
        $scope.currentUserName = getNameForLeftMenu($scope.currentUserProfile);
        $scope.idForLeftMenu = currentUserPersonId;
    });

}]);

autoBio.controller('LoginController', ['$scope', '$window', '$routeParams', '$location', function ($scope, $window, $routeParams, $location) {
    if (isLoggedIn()) {
        $window.location.href = '/profile';
        return;
    }
    //if next field is not empty, it means that login page was loaded after wrong loginsubmit
    $scope.error = $location.search().e;
    //param which used to decide where we should redirect user after successful login
    var redirectParam = $routeParams.redir;
    var redirectQueryParam = isNull(redirectParam)? "" : "?redir=" + redirectParam;

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

autoBio.controller('ProfileController', ['$scope', 'ProfileFactory', '$location', '$window', function ($scope, ProfileFactory, $location, $window) {
    $scope.profile = {};
    ProfileFactory.get({personId: ""}, function (loadedProfile) {
        $scope.profile = loadedProfile;
    });


    $scope.updateProfile = function (profileForm) {
        if (isValid(profileForm)) {
            ProfileFactory.add($scope.profile, function () {
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

autoBio.controller('AutoBiographyFullController', ['$scope', '$location', 'AutobioTextFactory', function ($scope, $location, AutobioTextFactory) {
    autoBioTextController($scope, $location, AutobioTextFactory, "FULL", 10000);
}]);

autoBio.controller('AutoBiographyFullReadController', ['$scope', '$location', '$sce', '$routeParams', 'AutobioTextFactory', function ($scope, $location, $sce, $routeParams, AutobioTextFactory) {
    var currentUserPersonId = getPersonId($location);
    var autoBioTextType = document.getElementById("autoBioTextTypeId").value;
    if (isNull(autoBioTextType)) {
        autoBioTextType = "FULL";
    } else {
        autoBioTextType = autoBioTextType.toUpperCase();
    }


    AutobioTextFactory.get({
        personId: currentUserPersonId,
        autoBioTextType: autoBioTextType
    }, function (autobioTextLoaded) {
        if (autobioTextLoaded != null) {
            $scope.autobioText = autobioTextLoaded.text;
        }
        if ($scope.autobioText == null || $scope.autobioText.length == 0) {
            $scope.autobioText = document.getElementById("noAutobiographyId").value;
        }
        $scope.autobioText = $sce.trustAsHtml($scope.autobioText);
    });

}]);


autoBio.controller('AutoBiographyForWorkController', ['$scope', '$location', '$sce', 'AutobioTextFactory', 'AutoBioTemplatesFactory', function ($scope, $location, $sce, AutobioTextFactory, AutoBioTemplatesFactory) {
    var CKEDITORWrapper = autoBioTextController($scope, $location, AutobioTextFactory, "FOR_WORK", 5000);
    var lang = document.getElementById("lang-input-id").value;
    $scope.autoBioTemplates = [];
    $scope.autoBioExample = "";
    AutoBioTemplatesFactory.query(function (autoBioTemplates) {
        $scope.autoBioTemplates = autoBioTemplates;
    });

    $scope.setTemplate = function () {
        AutoBioTemplatesFactory.get({templateId: $scope.templateSelection}, function (autoBioTemplateContent) {
            CKEDITORWrapper.CKEDITOR.instances.autobioText.setData(autoBioTemplateContent.template);
            $scope.autoBioExample = $sce.trustAsHtml(autoBioTemplateContent.example);
        });
    }
}]);

autoBio.controller('AutoBiographyInterestingController', ['$scope', '$location', '$compile', 'AutoBioInterestingChaptersLoaderFactory', 'AutoBioInterestingAnswerFactory', function ($scope, $location, $compile, AutoBioInterestingChaptersLoaderFactory, AutoBioInterestingAnswerFactory) {
    $scope.chapters = [];
    $scope.anySubChapterClicked = false;
    $scope.currentChapterId = null;
    $scope.currentSubChapterId = null;
    $scope.ckEditorObject = null;

    AutoBioInterestingChaptersLoaderFactory.query(function (chapters) {
        $scope.chapters = chapters;
    });

    $scope.loadSubChapterAnswer = function (chapterId, subChapterId) {
        if ($scope.ckEditorObject != null) {
            $scope.ckEditorObject.CKEDITORWrapper.CKEDITOR.instances.autobioText.destroy(true);
        }

        var currentUserPersonId = getPersonId($location);
        $scope.currentChapterId = chapterId;
        $scope.currentSubChapterId = subChapterId;
        AutoBioInterestingAnswerFactory.get({
            personId: currentUserPersonId,
            chapterId: chapterId,
            subChapterId: subChapterId
        }, function (answer) {

            $scope.anySubChapterClicked = true;

            var node = document.getElementById("autobioInterestingTextBlockId");
            document.getElementById('subChapter-' + chapterId + '-' + subChapterId + '-id').parentElement.appendChild(node);

            $scope.ckEditorObject = loadCkeditor(100);
            if (!isNull(answer.text)) {
                $scope.ckEditorObject.setText(answer.text);
            }

        });
    };

    $scope.saveAutobioInterestingText = function () {
        var currentUserPersonId = getPersonId($location);
        AutoBioInterestingAnswerFactory.save({
            personId: currentUserPersonId,
            chapterId: $scope.currentChapterId,
            subChapterId: $scope.currentSubChapterId
        }, CKEDITOR.instances.autobioText.getData(), function (answer) {
            //saved  alert(answer);
        });
    };
}]);

function getName(currentUserProfile) {
    if(isNull(currentUserProfile)){
        return "";
    }
    var name = (currentUserProfile.name == null ? "" : (currentUserProfile.name + " "))
        + (currentUserProfile.surname == null ? "" : currentUserProfile.surname);
    return name;
}

function getNameForLeftMenu(currentUserProfile) {
    var name = getName(currentUserProfile);
    if (name.length == 0) {
        return name;
    }
    return "(" + name + ")";
}

function getIdForLeftMenu(currentUserProfile, currentUserPersonId) {
    if (currentUserPersonId == "") {
        return "";
    }
    return currentUserProfile.id;
}

function autoBioTextController($scope, $location, AutobioTextFactory,
                               autobioTextType, maxCharCount) {
    var currentUserPersonId = getPersonId($location);
    var autobioText = null;
    var ckEditorObject = loadCkeditor(maxCharCount);
    AutobioTextFactory.get({
        personId: currentUserPersonId,
        autoBioTextType: autobioTextType
    }, function (autobioTextLoaded) {
        if (autobioTextLoaded != null) {
            autobioText = autobioTextLoaded.text;
        } else {
            autobioText = "";
        }
        ckEditorObject.setText(autobioText);
    });

    $scope.saveAutobioText = function () {
        var data = CKEDITOR.instances.autobioText.getData();
        console.info(data);
        AutobioTextFactory.add({autoBioTextType: autobioTextType}, data, function () {

        });
    };

    return ckEditorObject.CKEDITORWrapper;
}
function isEditorLoaded() {
    return typeof CKEDITOR !== 'undefined' && CKEDITOR != null && CKEDITOR.status == 'loaded';
}

/**
 * CKEDITOR is loaded to autobioText field
 * @returns {{setText: Function}}
 */
function loadCkeditor(maxCharCount) {
    var CKEDITORWrapper = {
        CKEDITOR: null,
        isEditorReady: false,
        ckEditorText: null
    };

    if (isEditorLoaded()) {
        initCkeditor(CKEDITORWrapper, maxCharCount);
    } else {
        loadScript("../../../assets/js/ckeditor/ckeditor.js", function () {
            var isEditorLoaded = typeof CKEDITOR !== 'undefined' && CKEDITOR != null && CKEDITOR.status == 'loaded';
            console.info("check CKEDITOR initialization" + isEditorLoaded);
            return isEditorLoaded
        }, function () {

            initCkeditor(CKEDITORWrapper, maxCharCount);
        });
    }

    return {
        CKEDITORWrapper: CKEDITORWrapper,
        setText: function (ckEditorTextLocal) {
            CKEDITORWrapper.ckEditorText = ckEditorTextLocal;
            if (CKEDITORWrapper.isEditorReady) {
                CKEDITOR.instances.autobioText.setData(CKEDITORWrapper.ckEditorText);
            }
        }
    }
}

function initCkeditor(CKEDITORWrapper, maxCharCount) {
    /**Added plugins:
     * wordcount, Enhanced Image, Find / Replace, Font Size and Family, Image Browser,
     * Justify, Print,  Upload Widget, Upload Image
     * Removed plugins:
     * About CKEditor, WebSpellChecker, SCAYT
     */
    CKEDITOR.replace('autobioText', {
        imageBrowser_listUrl: '/data/files',
        filebrowserUploadUrl: '/data/file',
        filebrowserImageBrowseUrl: '/assets/js/ckeditor/plugins/imagebrowser/browser/browser.html?listUrl=%2Fdata%2Ffiles',
        language: document.getElementById("lang-input-id").value,
        height: 500,
        wordcount: {
            // Whether or not you want to show the Paragraphs Count
            showParagraphs: false,

            // Whether or not you want to show the Word Count
            showWordCount: true,

            // Whether or not you want to show the Char Count
            showCharCount: true,

            // Whether or not you want to count Spaces as Chars
            countSpacesAsChars: true,

            // Whether or not to include Html chars in the Char Count
            countHTML: false,

            // Maximum allowed Word Count, -1 is default for unlimited
            maxWordCount: -1,

            // Maximum allowed Char Count, -1 is default for unlimited
            maxCharCount: maxCharCount
        }

    }).on('instanceReady', function (event) {

        if (CKEDITORWrapper.ckEditorText != null && !CKEDITORWrapper.isEditorReady) {
            CKEDITOR.instances.autobioText.setData(CKEDITORWrapper.ckEditorText);
        }
        CKEDITORWrapper.isEditorReady = true;
        CKEDITORWrapper.CKEDITOR = CKEDITOR;

    });
}

function getPersonId($location) {
    var currentUserPersonId = $location.path().split("/")[2];
    if (isNull(currentUserPersonId)) {
        currentUserPersonId = "";
    }
    return currentUserPersonId;
}


function loadScript(url, checkWhetherScriptIsLoadedFunction, callback) {

    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;

    if (script.readyState) {  //IE
        script.onreadystatechange = function () {
            if (script.readyState == "loaded" ||
                script.readyState == "complete") {
                script.onreadystatechange = null;
                checkScriptIsLoaded(checkWhetherScriptIsLoadedFunction, callback);
            }
        };
    } else {  //Others
        script.onload = function () {

            // Some browsers, such as Safari, may call the onLoad function
            // immediately. Which will break the loading sequence. (#3661)
            setTimeout(function () {
                checkScriptIsLoaded(checkWhetherScriptIsLoadedFunction, callback);
            }, 0);
        };
    }

    document.body.appendChild(script);


}

function checkScriptIsLoaded(checkWhetherScriptIsLoadedFunction, callBackAfterLoaded) {
    var timerVar = setInterval(function () {
        checkWhetherScriptIsLoadedFunction2(checkWhetherScriptIsLoadedFunction, callBackAfterLoaded, timerVar);
    }, 10);
    checkWhetherScriptIsLoadedFunction2(checkWhetherScriptIsLoadedFunction, callBackAfterLoaded, timerVar);
}

function checkWhetherScriptIsLoadedFunction2(checkWhetherScriptIsLoadedFunction, callBackAfterLoaded, timerVar) {
    if (checkWhetherScriptIsLoadedFunction()) {
        clearInterval(timerVar);
        callBackAfterLoaded();
    }
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
            $window.location.href = '/profile';
        } else {
            $window.location.href = '/';
        }
    }, 5000);
}

function isLoggedIn() {
    return getLoggedInPersonId() >= 0;
}

function getLoggedInPersonId() {
    var loggedInPersonId = document.getElementById("loggedInPersonId");
    return loggedInPersonId.value;
}

function isNull(value) {
    return value == undefined || value == null;
}
