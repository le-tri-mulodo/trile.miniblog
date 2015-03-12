'use strict';

var postControllers = angular.module('postControllers', []);
var userControllers = angular.module('userControllers', []);

postControllers.controller('topPostCtrl', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {
	// $http.get(REST_API_URL + "posts/top").success(function(data, status,
	// headers, config) {
	// var posts = data.data
	// $scope.posts = posts;
	//
	// // Get all user info
	// if (null !== posts && undefined !== posts) {
	// for (var i = 0; i < posts.length; i++) {
	// getUserInfo(posts[i].user_id, $rootScope.users, $http);
	// }
	// }
	// });
	getPosts('posts/top', $scope, $rootScope, $http);
} ]);

postControllers.controller('allPostCtrl', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {
	getPosts('posts/', $scope, $rootScope, $http);
} ]);

postControllers.controller('postOfUserCtrl', [ '$scope', '$rootScope', '$http', '$routeParams',
		function($scope, $rootScope, $http, $routeParams) {
			getPosts('posts/users/' + $routeParams.userId, $scope, $rootScope, $http);
		} ]);

postControllers.controller('postDetailCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$routeParams',
		function($scope, $rootScope, $http, $routeParams) {
			$http.get(REST_API_URL + 'posts/' + $routeParams.postId).success(
					function(data, status, headers, config) {
						var post = data.data;
						$scope.post = post;
						// Get comments of post
						$http.get(REST_API_URL + 'comments/posts/' + $routeParams.postId).success(
								function(data, status, headers, config) {
									var comments = data.data;
									$scope.comments = comments;

									// Get all user info
									if (null !== comments && undefined !== comments) {
										for (var i = 0; i < comments.length; i++) {
											getUserInfo(comments[i].user_id, $rootScope.users, $http);
										}
									}
								});
					});
		} ]);

userControllers.controller('registerCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookies',
		function($scope, $rootScope, $http, $location, $cookies) {
			// Check is logged then rederect to home page
			var loggedFlg = $rootScope.loggedFlg;
			if (null !== loggedFlg && undefined !== loggedFlg && true === loggedFlg) {
				$location.path('#/');
				$location.replace();
			}

			// prepare form
			$scope.user = {};
			// process the form
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : REST_API_URL + 'users',
					data : $.param($scope.user), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					var user = data.data;
					// Save registered user data
					$rootScope.loggedFlg = true;
					$rootScope.currentUser = user;

					// Set to cookies
					$cookies.user_id = user.user_id;
					$cookies.username = user.username;
					$cookies.token = user.token;

					// redirect to home page
					$location.path('#/');
					$location.replace();
				}).error(function(data) {
					$scope.errMsg = data.meta.description;
				});
			};

			// Upload complement event
			uploadCompleteEvent($scope, function(response) {
				var meta = response.meta;
				if (200 === meta.code) {
					// add path of image
					$scope.user.avatarlink = 'img/' + meta.messages[0];
				}
			});
		} ]);

userControllers.controller('profileCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$location',
		'$cookies',
		function($scope, $rootScope, $http, $location, $cookies) {
			// Check not logged then rederect to home page
			var loggedFlg = $rootScope.loggedFlg;
			if (null !== loggedFlg && undefined !== loggedFlg && false === loggedFlg) {
				$location.path('#/');
				$location.replace();
			}

			// Load user info from rest
			$http.get(REST_API_URL + 'users/' + $rootScope.currentUser.user_id).success(
					function(data, status, headers, config) {
						var user = data.data;
						// set token
						user.token = $rootScope.token;
						// remove join date
						delete user.joindate;
						// Set form data
						$scope.user = user;
						// Set current user data
						$rootScope.currentUser = user;
						// set avatar image
						$scope.currentAvatarlink = user.avatarlink;
					});

			// process the form
			$scope.processForm = function() {
				$http({
					method : 'PUT',
					url : REST_API_URL + 'users',
					data : $.param($scope.user), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					var user = data.data;
					// Save updated user data from rest to global variable
					$rootScope.currentUser = user;

					// redirect to home page
					$location.path('#/');
					$location.replace();
				}).error(function(data) {
					$scope.errMsg = data.meta.description;
				});
			};

			// Upload complement event
			uploadCompleteEvent($scope, function(response) {
				var meta = response.meta;
				if (200 === meta.code) {
					// add path of image
					$scope.user.avatarlink = 'img/' + meta.messages[0];
				}
			});
		} ]);

userControllers.controller('loginCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookies',
		function($scope, $rootScope, $http, $location, $cookies) {
			// Check is logged then rederect to home page
			var loggedFlg = $rootScope.loggedFlg;
			if (null !== loggedFlg && undefined !== loggedFlg && true === loggedFlg) {
				$location.path('#/');
				$location.replace();
			}

			// prepare form
			// create a blank object to hold our form information
			// $scope will allow this to pass between controller and view
			$scope.user = {};
			// process the form
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : REST_API_URL + 'tokens/login',
					data : $.param($scope.user), // pass in data as strings
					// set the headers so angular passing info as form data
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				}).success(function(data) {
					var user = data.data;

					// Save login data
					$rootScope.loggedFlg = true;
					$rootScope.currentUser = user;

					// Set to cookies
					if (true === $scope.rememberLogin) {
						$cookies.user_id = user.user_id;
						$cookies.username = user.username;
						$cookies.token = user.token;
					}

					// redirect to home page
					$location.path('#/');
					$location.replace();
					// // back to previous page
					// window.history.back();
				}).error(function(data) {
					$scope.errMsg = data.meta.description;
				});
			};

		} ]);

userControllers.controller('logoutCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookieStore',
		function($scope, $rootScope, $http, $location, $cookieStore) {
			// Check is logged to call rest
			var loggedFlg = $rootScope.loggedFlg;
			if (null !== loggedFlg && undefined !== loggedFlg && true === loggedFlg) {
				// Prepare data to call logout rest
				var user = {};
				user.token = $rootScope.token;
				// Call rest
				$http({
					method : 'POST',
					url : REST_API_URL + 'tokens/logout',
					data : $.param(user), // pass in data as strings
					// set the headers so angular passing info as form data
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				});
			}
			// Clean scope
			$rootScope.loggedFlg = false;
			$rootScope.currentUser = null;
			// Clean cookies
			$cookieStore.remove("user_id");
			$cookieStore.remove("username");
			$cookieStore.remove("token");
		} ]);

function getUserInfo(user_id, users, $http) {
	// Only get user info not got
	if (null === users[user_id] || undefined === users[user_id]) {
		$http.get(REST_API_URL + 'users/' + user_id).success(function(data, status, headers, config) {
			var user = data.data;
			// Set user to list users
			users[user.user_id] = user;
		});
	}
}

function getPosts(url, scope, rootScope, http) {
	http.get(REST_API_URL + url).success(function(data, status, headers, config) {
		var posts = data.data
		scope.posts = posts;

		// Get all user info
		if (null !== posts && undefined !== posts) {
			for (var i = 0; i < posts.length; i++) {
				getUserInfo(posts[i].user_id, rootScope.users, http);
			}
		}
	});
}

function uploadCompleteEvent(scope, complementDelegate) {
	// event to create upload element

	scope.$on('$routeChangeSuccess', function() {
		$("#fileUploader").fileinput({
			uploadUrl : REST_API_URL + "users/upload",
			uploadAsync : true,
			// dropZoneEnabled:false,
			previewFileType : "image",
			browseClass : "btn btn-success",
			browseLabel : "Pick Image",
			browseIcon : '<i class="glyphicon glyphicon-picture"></i>',
			removeClass : "btn btn-danger",
			removeLabel : "Delete",
			removeIcon : '<i class="glyphicon glyphicon-trash"></i>',
			uploadClass : "btn btn-info",
			uploadLabel : "Upload",
			uploadIcon : '<i class="glyphicon glyphicon-upload"></i>'
		});

		// get file name when upload success
		$("#fileUploader").on('fileuploaded', function(event, data) {
			// var response = data.response.meta;
			// if (200 === response.code) {
			// scope.user.avatarlink = response.messages[0];
			// }
			// Call delegate
			complementDelegate(data.response);
		});
	});
}
