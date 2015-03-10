'use strict';

var postControllers = angular.module('postControllers', []);
var userControllers = angular.module('userControllers', []);

postControllers.controller('topPostCtrl', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {
	$http.get(REST_API_URL + "posts/top").success(function(data, status, headers, config) {
		var posts = data.data
		$scope.posts = posts;

		// Get all user info
		if (null !== posts && undefined !== posts) {
			for (var i = 0; i < posts.length; i++) {
				getUserInfo(posts[i].user_id, $rootScope.users, $http);
			}
		}
	});
} ]);

postControllers.controller('postDetailCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$routeParams',
		function($scope, $rootScope, $http, $routeParams) {
			$http.get(REST_API_URL + "posts/" + $routeParams.postId).success(
					function(data, status, headers, config) {
						var post = data.data;
						$scope.post = post;
						// Get comments of post
						$http.get(REST_API_URL + "comments/posts/" + $routeParams.postId).success(
								function(data, status, headers, config) {
									var comments = data.data;
									$scope.comments = comments;

									// Get all user info
									console.log(comments.length);
									if (null !== comments && undefined !== comments) {
										for (var i = 0; i < comments.length; i++) {
											getUserInfo(comments[i].user_id, $rootScope.users, $http);
										}
									}
								});
					});
		} ]);

userControllers.controller('registerCtrl', [ '$scope', '$http', '$route', function($scope, $http, $route) {
	// $scope.$on('$routeChangeSuccess', function() {
	// $("#fileUploader").fileinput({
	// uploadUrl : REST_API_URL + "users/upload",
	// uploadAsync : true,
	// // dropZoneEnabled:false,
	// previewFileType : "image",
	// browseClass : "btn btn-success",
	// browseLabel : "Pick Image",
	// browseIcon : '<i class="glyphicon glyphicon-picture"></i>',
	// removeClass : "btn btn-danger",
	// removeLabel : "Delete",
	// removeIcon : '<i class="glyphicon glyphicon-trash"></i>',
	// uploadClass : "btn btn-info",
	// uploadLabel : "Upload",
	// uploadIcon : '<i class="glyphicon glyphicon-upload"></i>',
	// });
	// });
	//			
	// console.log('asdas');
} ]);

userControllers.controller('logginCtrl', [ '$scope', '$http', '$route', function($scope, $http, $route) {
} ]);

function getUserInfo(user_id, users, $http) {
	// Only get user info not got
	if (null === users[user_id] || undefined === users[user_id]) {
		$http.get(REST_API_URL + "users/" + user_id).success(function(data, status, headers, config) {
			var user = data.data;
			// Set user to list users
			users[user.user_id] = user;
		});
	}
}
