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
    $scope.swreps = null;
    swRepService.list().then(function (data) {
        $scope.swreps = data.data
    });
});

app.controller('SwRepDetailCtrl', function ($scope, swRepService, $stateParams) {
    $scope.sr = $stateParams.id;
});