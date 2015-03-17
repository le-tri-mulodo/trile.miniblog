'use strict';

var postControllers = angular.module('postControllers', []);
var userControllers = angular.module('userControllers', []);

postControllers.controller('topPostCtrl', [ '$scope', '$rootScope', '$http', 'toaster', 'authSer', 'postSer',
		function($scope, $rootScope, $http, toaster, authSer, postSer) {
			getPosts('posts/top', $scope, $rootScope, $http, toaster);

			// Set service function
			// delete post
			$scope.deletePost = postSer.deletePost;
			// active/deactive post
			$scope.activeDeactivePost = postSer.activeDeactivePost;
		} ]);

postControllers.controller('allPostCtrl', [ '$scope', '$rootScope', '$http', 'postSer',
		function($scope, $rootScope, $http, postSer) {
			getPosts('posts/', $scope, $rootScope, $http);

			// Set service function
			// delete post
			$scope.deletePost = postSer.deletePost;
			// active/deactive post
			$scope.activeDeactivePost = postSer.activeDeactivePost;
		} ]);

postControllers.controller('searchCtrl', [ '$scope', '$rootScope', '$http', '$routeParams', 'postSer',
		function($scope, $rootScope, $http, $routeParams, postSer) {
			var query = $routeParams.query;

			// Check null & empty
			if (query) {
				// Search user
				$http.get(REST_API_URL + 'users/search/' + query).success(function(data, status, headers, config) {
					// Set user to list users
					$scope.searchResultUsers = data.data;
				});

				// Search post
				getPosts('posts/search/' + query, $scope, $rootScope, $http);

				// Add to nav
				addNavItems($rootScope, query, 'search/' + query, 'search');

				// Set service function
				// delete post
				$scope.deletePost = postSer.deletePost;
				// active/deactive post
				$scope.activeDeactivePost = postSer.activeDeactivePost;
			}
		} ]);

postControllers.controller('postOfUserCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$routeParams',
		'authSer',
		'postSer',
		function($scope, $rootScope, $http, $routeParams, authSer, postSer) {

			var userId = $routeParams.userId;

			// add to nav items
			// if get posts of logged user then don't add
			if (null === $rootScope.currentUser || undefined == $rootScope.currentUser
					|| userId != $rootScope.currentUser.user_id) {

				// get posts
				getPosts('posts/users/' + userId, $scope, $rootScope, $http);
				// get user if not existed to ensure have info about user
				if (!$rootScope.users[userId]) {
					$http.get(REST_API_URL + 'users/' + userId).success(function(data, status, headers, config) {
						var user = data.data;
						// Set user to list users
						$rootScope.users[user.user_id] = user;
						// add to items list
						addNavItems($rootScope, user.username, 'users/' + userId, 'user');
					});
				} else {
					// add to items list
					addNavItems($rootScope, $rootScope.users[userId].username, 'users/' + userId, 'user');
				}
			} else {
				var myParam = {};
				myParam.token = $rootScope.token;

				// Get my post
				$http({
					method : 'POST',
					url : REST_API_URL + 'posts/users/' + userId,
					data : $.param(myParam), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					// Set posts to display
					var posts = data.data
					$scope.posts = posts;

					// Get user info if not existed
					getUserInfo(userId, $rootScope.users, $http);
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			}

			// Set service function
			// delete post
			$scope.deletePost = postSer.deletePost;
			// active/deactive post
			$scope.activeDeactivePost = postSer.activeDeactivePost;
		} ]);

postControllers.controller('postDetailCtrl', [ '$scope', '$rootScope', '$http', '$routeParams', '$location', '$sce',
		'toaster', 'authSer', 'postSer',
		function($scope, $rootScope, $http, $routeParams, $location, $sce, toaster, authSer, postSer) {

			// Call rest to get post info
			postSer.getPostInfo($routeParams.postId,
			// Success call back
			function(post) {
				// Display data
				$scope.post = post;
				// set content with markup
				$scope.contentMarkup = $sce.trustAsHtml(post.content);

				// Get comments of post
				$http.get(REST_API_URL + 'comments/posts/' + $routeParams.postId)
				// Success
				.success(function(data, status, headers, config) {

					var comments = data.data;
					$scope.comments = comments;

					// Get all user info
					if (null !== comments && undefined !== comments) {
						for (var i = 0; i < comments.length; i++) {
							getUserInfo(comments[i].user_id, $rootScope.users, $http);
						}
					}
				})
				// Error
				.error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			},
			// Error call back
			function(data) {
				// Redirect to home page
				$location.path('#/');
				$location.replace();
			});

			// Set service function
			// delete post
			$scope.deletePost = postSer.deletePost;
			// active/deactive post
			$scope.activeDeactivePost = postSer.activeDeactivePost;
		} ]);

postControllers.controller('editPostCtrl', [ '$scope', '$rootScope', '$http', '$routeParams', '$location', '$sce',
		'toaster', 'authSer', 'postSer', 'RICHTEXTOPTION',
		function($scope, $rootScope, $http, $routeParams, $location, $sce, toaster, authSer, postSer, RICHTEXTOPTION) {

			// Check is not logged then rederect to home page
			if (authSer.authLogged()) {
				return;
			}

			// Config rich text box
			$scope.options = RICHTEXTOPTION;

			// Call rest to get post info
			postSer.getPostInfo($routeParams.postId,
			// Success call back
			function(post) {
				// Check owner
				console.log($rootScope.currentUser.user_id);
				console.log(post.user_id);
				if ($rootScope.currentUser.user_id != post.user_id) {
					authSer.errorHandler('You\'re not owner of post ');
					return;
				}

				// Display data
				$scope.post = post;

				// prepare form
				$scope.submit_title = 'Update';
				$scope.post.user_id = $rootScope.currentUser.user_id;
				$scope.post.token = $rootScope.token;
				// process the form
				$scope.processForm = postSer.editPost;

				// Prepare data to preview post
				$scope.previewPost = postSer.previewPost;
			},
			// Error call back
			function(data) {
				// Redirect to home page
				$location.path('#/');
				$location.replace();
			});

			// Set service function
			// delete post
			$scope.deletePost = postSer.deletePost;
			// active/deactive post
			$scope.activeDeactivePost = postSer.activeDeactivePost;
		} ]);

postControllers.controller('newPostCtrl', [ '$scope', '$rootScope', '$http', '$routeParams', '$location', 'toaster',
		'authSer', 'postSer', 'RICHTEXTOPTION',
		function($scope, $rootScope, $http, $routeParams, $location, toaster, authSer, postSer, RICHTEXTOPTION) {

			// Check is not logged then rederect to home page
			if (authSer.authLogged()) {
				return;
			}

			// Config rich text box
			$scope.options = RICHTEXTOPTION;

			// prepare form
			$scope.post = postSer.getPreviewPost();
			$scope.submit_title = 'Post';
			$scope.post.user_id = $rootScope.currentUser.user_id;
			$scope.post.token = $rootScope.token;
			// process the form
			$scope.processForm = postSer.addPost;

			// Prepare data to preview post
			$scope.previewPost = postSer.previewPost;

		} ]);

postControllers.controller('previewPostCtrl', [ '$scope', '$rootScope', '$http', '$location', '$sce', 'postSer',
		function($scope, $rootScope, $http, $location, $sce, postSer) {
			$scope.post = postSer.getPreviewPost();

			// If preview post don't exist then redirect to home page
			if (null === $scope.post || undefined === $scope.post || jQuery.isEmptyObject($scope.post)) {
				$location.path('#/');
				$location.replace();
			}

			// Set flag
			$scope.previewFlg = true;

			// Set content
			$scope.contentMarkup = $sce.trustAsHtml($scope.post.content);
		} ]);

userControllers.controller('registerCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookies', 'toaster',
		'authSer', function($scope, $rootScope, $http, $location, $cookies, toaster, authSer) {

			// Check is logged then rederect to home page
			if (authSer.authNotLogged()) {
				return;
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
					$rootScope.isLogged = true;
					$rootScope.currentUser = user;

					// Set to cookies
					$cookies.user_id = user.user_id;
					$cookies.username = user.username;
					$cookies.token = user.token;

					// redirect to home page
					$location.path('#/');
					$location.replace();

					// Show notify
					toaster.pop('success', 'Register success');
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			};

			// Upload complement event
			uploadCompleteEvent($scope, toaster, function(response) {
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
		'toaster',
		'authSer',
		function($scope, $rootScope, $http, $location, $cookies, toaster, authSer) {

			// Check not logged then redirect to home page
			if (authSer.authLogged()) {
				return;
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

					// Show notify
					toaster.pop('success', 'Update profile success');
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			};

			// Upload complement event
			uploadCompleteEvent($scope, toaster, function(response) {
				var meta = response.meta;
				if (200 === meta.code) {
					// add path of image
					$scope.user.avatarlink = 'img/' + meta.messages[0];
				}
			});
		} ]);

userControllers.controller('loginCtrl', [ '$scope', 'authSer', function($scope, authSer) {

	// Check is logged then rederect to home page
	if (authSer.authNotLogged()) {
		return;
	}
	// prepare form
	// create a blank object to hold our form information
	// $scope will allow this to pass between controller and view
	$scope.user = {};
	// process the form
	$scope.login = authSer.login;

} ]);

userControllers.controller('chpwdCtrl', [ '$scope', '$rootScope', 'authSer', function($scope, $rootScope, authSer) {
	// Check is not logged then rederect to home page
	if (authSer.authLogged()) {
		return;
	}

	// prepare form
	// create a blank object to hold our form information
	// $scope will allow this to pass between controller and view
	$scope.user = {};
	// set user id
	$scope.user.user_id = $rootScope.currentUser.user_id;
	// process the form
	$scope.changePassword = authSer.changePassword;

} ]);

userControllers.controller('logoutCtrl', [ 'authSer', function(authSer) {

	// Call service
	authSer.logout();
} ]);
