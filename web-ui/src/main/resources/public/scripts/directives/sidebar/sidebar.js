"use strict";
angular.module("beenApp").directive("sidebar", ["$location", function () {
    return {
        templateUrl: "scripts/directives/sidebar/sidebar.html"
    }
}]);