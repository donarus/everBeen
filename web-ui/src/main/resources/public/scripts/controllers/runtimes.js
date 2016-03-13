"use strict";
var app = angular.module("beenApp");

app.service('runtimeService', function ($http) {
    delete $http.defaults.headers.common['X-Requested-With'];

    this.list = function () {
        return $http({
            method: 'GET',
            url: '/runtimes'
        })
    };

    this.get = function (id) {
        return $http({
            method: 'GET',
            url: '/nodes',
            params: {
                id: id
            }
        })
    };
});

app.controller('RuntimeCtrl', function ($scope, runtimeService) {
    $scope.runtimes = null;
    $scope.currentTime = new Date()
    nodeService.list().then(function (data) {
        $scope.runtimes = data.data
    });
});