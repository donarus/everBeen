"use strict";
var app = angular.module("beenApp");

app.service('swRepoService', function ($http) {
    delete $http.defaults.headers.common['X-Requested-With'];
    this.list = function () {
        return $http({
            method: 'GET',
            url: '/software-repository'
        })
    };
});

app.controller('SwRepoCtrl', function ($scope, swRepoService, $interval) {
    $scope.bpks = null;
    swRepoService.list().then(function (data) {
        $scope.bpks = data.data
    });
});