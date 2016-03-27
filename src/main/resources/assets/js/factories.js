//this js includes all factories (services which are used to load data)
var autoBioFactories = angular.module('autoBioFactories', ['ngResource']);

autoBioFactories.factory('ProfileFactory', ['$resource',
    function ($resource, personId) {
        return $resource('data/profile/:personId', {}, {
            query: {
                method: 'GET',
                cache: false,
                params: {time: Date.now()}
            },
            add: {
                method: 'POST'
            }
        });
    }
]);


autoBioFactories.factory('AutobioTextFactory', ['$resource',
    function ($resource, personId, autoBioTextType) {
        return $resource('data/autobio-text/:autoBioTextType/:personId', {}, {
            query: {
                method: 'GET',
                cache: false,
                params: {time: Date.now()}
            },
            add: {
                method: 'POST'
            }
        });
    }
]);


autoBioFactories.factory('AutoBioTemplatesFactory', ['$resource',
    function ($resource, templateId) {
        return $resource('data/autobio-text/templates/:templateId', {}, {
            query: {
                method: 'GET',
                isArray: true,
                cache: false,
                params: {time: Date.now()}
            }
        });
    }
]);

autoBioFactories.factory('AutoBioInterestingChaptersLoaderFactory', ['$resource',
    function ($resource) {
        return $resource('data/autobio-interesting/chapters/', {}, {
            query: {
                method: 'GET',
                isArray: true,
                cache: false,
                params: {time: Date.now()}
            }
        });
    }
]);

autoBioFactories.factory('AutoBioInterestingAnswerFactory', ['$resource',
    function ($resource, personId, chapterId, subChapterId) {
        return $resource('data/autobio-interesting/answer/chapter/:chapterId/subChapter/:subChapterId?personId=:personId');
    }
]);

autoBioFactories.factory('AllLoaderFactory', ['$resource',
    function ($resource) {
        return $resource('data/all/', {}, {
            query: {
                method: 'GET',
                isArray: true,
                cache: false,
                params: {time: Date.now()}
            }
        });
    }
]);