"use strict";

var beenApp = angular.module("beenApp", [
    "oc.lazyLoad", // https://github.com/ocombe/ocLazyLoad
    "ui.router.stateHelper", // https://github.com/marklagendijk/ui-router.stateHelper
    "ui.bootstrap"
])

beenApp.filter('dateFormat', function ($filter) {
    return function (date, format) {
        return $filter('date')(new Date(date), format);
    }
});

beenApp.filter('timeDiffToString', function () {
    return function (date1, date2) {
        var milliseconds = new Date(date2).getTime() - new Date(date1).getTime();
        var seconds = Math.floor(milliseconds / 1000);
        var days = Math.floor(seconds / 86400);
        var hours = Math.floor((seconds % 86400) / 3600);
        var minutes = Math.floor(((seconds % 86400) % 3600) / 60);
        var andSeconds = Math.floor(((seconds % 86400 ) % 60));
        var timeString = '';
        if (days > 0) timeString += days + "d ";
        if (hours > 0 || days > 0) timeString += hours + "h ";
        if (minutes > 0 || hours > 0 || days > 0) timeString += minutes + "m ";
        timeString += andSeconds + "s";
        return timeString;
    }
});

beenApp.filter('bytesToString', function ($filter) {
    return function (bytes) {
        var mbs = bytes / 1024 / 1024;
        var gbs = mbs / 1024;
        if (gbs <= 1) {
            return Math.floor(mbs) + " MB";
        } else {
            return $filter('number')(gbs, 3) + " GB";
        }
    }
});

beenApp.config(function (stateHelperProvider, $urlRouterProvider, $ocLazyLoadProvider) {
    $ocLazyLoadProvider.config({
        debug: true,
        events: false
    })

    $urlRouterProvider.otherwise('')

    stateHelperProvider.state({
        url: '',
        name: 'root',
        templateUrl: "views/main.html",
        resolve: {
            loadJsCss: function ($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: "beenApp",
                    files: [
                        "scripts/directives/header/header.js",
                        "scripts/directives/sidebar/sidebar.js"
                    ]
                })
            }
        },
        children: [
            {
                name: 'node',
                url: '/node',
                template: '<div ui-view/>',
                resolve: {
                    loadJsCss: function ($ocLazyLoad) {
                        return $ocLazyLoad.load({
                            name: "beenApp",
                            files: ["scripts/controllers/nodes.js"]
                        })
                    }
                },
                children: [
                    {name: 'list', url: '/list', controller: "NodesCtrl", templateUrl: "views/nodes/list.html"},
                    {name: 'detail', url:'/detail/:id', controller: "NodeDetailCtrl", templateUrl: "views/nodes/detail.html"}
                ]
            }
        ]
    })
});