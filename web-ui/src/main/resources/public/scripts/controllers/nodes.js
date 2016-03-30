"use strict";
var app = angular.module("beenApp");

app.service('nodeService', function ($http) {
    delete $http.defaults.headers.common['X-Requested-With'];

    this.list = function () {
        return $http({
            method: 'GET',
            url: '/nodes'
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

app.controller('NodeCtrl', function ($scope, nodeService, $interval) {
    $scope.nodes = null;
    $scope.currentTime = new Date()
    nodeService.list().then(function (data) {
        $scope.nodes = data.data
    });

    $interval(function () {
        $scope.currentTime = new Date()
    }, 1000);
});

app.controller('NodeDetailCtrl', function ($scope, nodeService, $interval, $stateParams) {
    $scope.node = null;
    $scope.currentTime = new Date()

    nodeService.get($stateParams.id).then(function (data) {
        $scope.node = data.data;
    });

    $interval(function () {
        $scope.currentTime = new Date()
    }, 1000);
});