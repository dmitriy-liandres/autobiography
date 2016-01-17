//this js includes all factories (services which are used to load data)
var autoBioFactories = angular.module('autoBioFactories', ['ngResource']);

autoBioFactories.factory('ProfileFactory', ['$resource',
    function ($resource, personId) {
        return $resource('data/profile?personId=:personId', {}, {
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
        return $resource('data/autobio-text/:autoBioTextType?personId=:personId', {}, {
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
                isArray:true,
                cache: false,
                params: {time: Date.now()}
            }
        });
    }
]);