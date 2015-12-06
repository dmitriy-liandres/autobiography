//this js includes all factories (services which are used to load data)
var autoBioFactories = angular.module('autoBioFactories', ['ngResource']);



autoBioFactories.factory('ProfileLoaded', ['$resource',
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