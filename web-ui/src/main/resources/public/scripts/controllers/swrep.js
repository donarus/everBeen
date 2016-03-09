"use strict";
var app = angular.module("beenApp");

app.service('swRepService', function ($http) {
    delete $http.defaults.headers.common['X-Requested-With'];
    this.list = function () {
        return $http({
            method: 'GET',
            url: '/software-repository'
        })
    };
});

app.controller('SwRepCtrl', function ($scope, swRepService) {
    $scope.bpks = null;
    swRepService.list().then(function (data) {
        $scope.bpks = data.data
    });
});