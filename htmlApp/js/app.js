'use strict';

var REST_API_URL = "/miniblog.api/"

var miniBlog = angular.module('MiniBlog', [ 'ngRoute', 'postControllers', 'userControllers' ]).run(
		function($rootScope) {
			// Create user hashmap
			var users = {};
			$rootScope.users = users;
		});

miniBlog.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'partials/posts.html',
		controller : 'topPostCtrl'
	}).when('/posts/:postId', {
		templateUrl : 'partials/post_detail.html',
		controller : 'postDetailCtrl'
	}).when('/loggin', {
		templateUrl : 'partials/loggin.html',
		controller : 'logginCtrl'
	}).when('/register', {
		templateUrl : 'partials/register.html',
		controller : 'registerCtrl'
	}).otherwise({
		redirectTo : '/'
	});
} ]);

$("document").ready(function() {
	// Check cookie to display nav
	displayNav();
});

// Display nav
function displayNav() {
	$(".dy-view").addClass("hide");

	var tokenCookie = getCookie("token");
	if (null != tokenCookie) {
		$(".logged").addClass("show");
		$(".logged").removeClass("hide");
	} else {
		$(".notlogged").addClass("show");
		$(".notlogged").removeClass("hide");
	}
}

function getCookie(name) {
	var dc = document.cookie;
	var prefix = name + "=";
	var begin = dc.indexOf("; " + prefix);
	if (begin == -1) {
		begin = dc.indexOf(prefix);
		if (begin != 0)
			return null;
	} else {
		begin += 2;
		var end = document.cookie.indexOf(";", begin);
		if (end == -1) {
			end = dc.length;
		}
	}
	return unescape(dc.substring(begin + prefix.length, end));
}
