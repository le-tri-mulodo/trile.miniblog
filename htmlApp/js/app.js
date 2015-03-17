'use strict';

var REST_API_URL = "/miniblog.api/"

var miniBlog = angular.module('MiniBlog', [ 'ngRoute', 'ngCookies', 'summernote', 'toaster', 'postControllers',
		'userControllers' ]);

miniBlog.constant('REST_API_URL', "/miniblog.api/");
miniBlog.constant('RICHTEXTOPTION', {
	height : 300,
	toolbar : [
	// [groupname, [button list]]
	[ 'style', [ 'bold', 'italic', 'underline', 'clear' ] ],
	// font
	[ 'font', [ 'strikethrough' ] ], [ 'fontsize', [ 'fontsize' ] ],
	// color
	[ 'color', [ 'color' ] ],
	// param
	[ 'para', [ 'ul', 'ol', 'paragraph' ] ],
	// insert
	[ 'insert', [ 'link', 'picture' ] ],
	// view
	[ 'view', [ 'fullscreen', 'codeview' ] ],
	// help
	[ 'help', [ 'help' ] ] ]
});

miniBlog.run(function($rootScope, $cookies, $location) {
	// Create user hashmap
	$rootScope.users = {};

	// logged flag
	var token = $cookies.token;
	var username = $cookies.username;
	var user_id = $cookies.user_id;
	if (null != token) {
		// get cookie info and set to root scope
		$rootScope.isLogged = true;
		$rootScope.currentUser = {};
		$rootScope.currentUser.username = username;
		$rootScope.currentUser.user_id = user_id;
		$rootScope.token = token;
	}

	// Create routing function
	$rootScope.search = function(url) {
		$location.path('search/' + url);
		$location.replace();
	};
});

miniBlog.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/', {
		// Top 10 posts newest
		templateUrl : 'partials/posts.html',
		controller : 'topPostCtrl'
	}).when('/all_posts', {
		// All posts
		templateUrl : 'partials/posts.html',
		controller : 'allPostCtrl'
	}).when('/users/:userId', {
		// List all posts of user
		templateUrl : 'partials/posts.html',
		controller : 'postOfUserCtrl'
	}).when('/posts/:postId', {
		// Detail of post
		templateUrl : 'partials/post_detail.html',
		controller : 'postDetailCtrl'
	}).when('/login', {
		// Login
		templateUrl : 'partials/login.html',
		controller : 'loginCtrl'
	}).when('/logout', {
		// Logout
		controller : 'logoutCtrl',
		template : " ",
		redirectTo : '/'
	}).when('/register', {
		// Register new user
		templateUrl : 'partials/register.html',
		controller : 'registerCtrl'
	}).when('/profile', {
		// Register new user
		templateUrl : 'partials/profile.html',
		controller : 'profileCtrl'
	}).when('/change_password', {
		// Register new user
		templateUrl : 'partials/change_password.html',
		controller : 'chpwdCtrl'
	}).when('/search/:query', {
		// Register new user
		templateUrl : 'partials/search.html',
		controller : 'searchCtrl'
	}).when('/new_post', {
		// Register new user
		templateUrl : 'partials/post.html',
		controller : 'newPostCtrl'
	}).when('/preview_post', {
		// Register new user
		templateUrl : 'partials/post_detail.html',
		controller : 'previewPostCtrl'
	}).when('/edit_post/:postId', {
		// Register new user
		templateUrl : 'partials/post.html',
		controller : 'editPostCtrl'
	}).otherwise({
		redirectTo : '/'
	});
} ]);
