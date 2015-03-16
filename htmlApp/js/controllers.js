'use strict';

var postControllers = angular.module('postControllers', []);
var userControllers = angular.module('userControllers', []);

postControllers.controller('topPostCtrl', [ '$scope', '$rootScope', '$http', 'toaster', 'authSer', 'postSer',
		function($scope, $rootScope, $http, toaster, authSer, postSer) {
			// $http.get(REST_API_URL + "posts/top").success(function(data,
			// status,
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
			getPosts('posts/top', $scope, $rootScope, $http, toaster);

			// delete post
			$scope.deletePost = postSer.deletePost;
		} ]);

postControllers.controller('allPostCtrl', [ '$scope', '$rootScope', '$http', 'postSer',
		function($scope, $rootScope, $http, postSer) {
			getPosts('posts/', $scope, $rootScope, $http);

			// delete post
			$scope.deletePost = postSer.deletePost;
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

				// delete post
				$scope.deletePost = postSer.deletePost;
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
					var messages = data.meta.messages;
					for (var i = 0; i < messages.length; i++) {
						toaster.pop('error', messages[i]);
					}
				});
			}

			// delete post
			$scope.deletePost = postSer.deletePost;
		} ]);

postControllers.controller('postDetailCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$routeParams',
		'$location',
		'$sce',
		'toaster',
		'authSer',
		'postSer',
		function($scope, $rootScope, $http, $routeParams, $location, $sce, toaster, authSer, postSer) {

			$http.get(REST_API_URL + 'posts/' + $routeParams.postId)
			// When success
			.success(
					function(data, status, headers, config) {
						var post = data.data;
						$scope.post = post;

						// set content
						$scope.contentMarkup = $sce.trustAsHtml(post.content);

						// get user info if not exist
						getUserInfo(post.user_id, $rootScope.users, $http);
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
								}).error(function(data) {
							// Show notify
							var messages = data.meta.messages;
							for (var i = 0; i < messages.length; i++) {
								toaster.pop('error', messages[i]);
							}
						});
					})
			// When error;
			.error(function(data) {
				// Show notify
				var messages = data.meta.messages;
				for (var i = 0; i < messages.length; i++) {
					toaster.pop('error', messages[i]);
				}

				// Redirect to home page
				$location.path('#/');
				$location.replace();
			});

			// delete post
			$scope.deletePost = postSer.deletePost;
		} ]);

postControllers.controller('newPostCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$routeParams',
		'$location',
		'authSer',
		'postSer',
		function($scope, $rootScope, $http, $routeParams, $location, authSer, postSer) {

			// Check is not logged then rederect to home page
//			var loggedFlg = $rootScope.loggedFlg;
//			if (null === loggedFlg || undefined === loggedFlg || false === loggedFlg) {
			// $location.path('#/');
			// $location.replace();
			// }
			if (!authSer.isLogged()) {
				$location.path('#/');
				$location.replace();
				return;
			}

			// Config rich text box
			$scope.options = {
				height : 300,
				toolbar : [
					// [groupname, [button list]]
						[ 'style', [ 'bold', 'italic', 'underline', 'clear' ] ],
						[ 'font', [ 'strikethrough' ] ],
						[ 'fontsize', [ 'fontsize' ] ],
						[ 'color', [ 'color' ] ],
						[ 'para', [ 'ul', 'ol', 'paragraph' ] ],
						[ 'insert', [ 'link', 'picture' ] ],
						[ 'view', [ 'fullscreen', 'codeview' ] ],
						[ 'help', [ 'help' ] ]
					]
			};

			// prepare form
			$scope.post = {};
			$scope.post.user_id = $rootScope.currentUser.user_id;
			$scope.post.token = $rootScope.token;
			// process the form
			$scope.processForm = function() {
				$http({
					method : 'POST',
					url : REST_API_URL + 'posts',
					data : $.param($scope.post), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					var id = data.data.post_id;
					// redirect to post detail
					$location.path('#/posts/' + id);
					$location.replace();
				}).error(function(data) {
					// Show notify
					var messages = data.meta.messages;
					for (var i = 0; i < messages.length; i++) {
						toaster.pop('error', messages[i]);
					}
				});
			};

			// Prepare data to preview post
			$scope.previewPost = function(previewPost) {
				postSer.setPreviewPost(previewPost);

				$location.path('#/preview_post');
				$location.replace();
			};

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
			// var loggedFlg = $rootScope.loggedFlg;
			// if (null !== loggedFlg && undefined !== loggedFlg && true ===
			// loggedFlg) {
			// $location.path('#/');
			// $location.replace();
			// }
			if (authSer.isLogged()) {
				$location.path('#/');
				$location.replace();
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
					// $rootScope.loggedFlg = true;
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
					var messages = data.meta.messages;
					for (var i = 0; i < messages.length; i++) {
						toaster.pop('error', messages[i]);
					}
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
			// var loggedFlg = $rootScope.loggedFlg;
			// if (null !== loggedFlg && undefined !== loggedFlg && false ===
			// loggedFlg) {
			// }
			// $location.path('#/');
			// $location.replace();
			if (!authSer.isLogged()) {
				$location.path('#/');
				$location.replace();
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
					var messages = data.meta.messages;
					for (var i = 0; i < messages.length; i++) {
						toaster.pop('error', messages[i]);
					}
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

userControllers.controller('loginCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookies', 'toaster',
		'authSer', function($scope, $rootScope, $http, $location, $cookies, toaster, authSer) {

			// Check is logged then rederect to home page
			// var loggedFlg = $rootScope.loggedFlg;
			// if (null !== loggedFlg && undefined !== loggedFlg && true ===
			// loggedFlg) {
			// $location.path('#/');
			// $location.replace();
			// }
			if (authSer.isLogged()) {
				$location.path('#/');
				$location.replace();
				return;
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
					$rootScope.isLogged = true;
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
					
					// Show notify
					toaster.pop('success','Login success');
				}).error(function(data) {
					// Show notify
					var messages = data.meta.messages;
					for (var i = 0; i < messages.length; i++) {
						toaster.pop('error', messages[i]);
					}
				});
			};

		} ]);

userControllers.controller('chpwdCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookies', 'toaster',
		'authSer', function($scope, $rootScope, $http, $location, $cookies, toaster, authSer) {
			// Check is not logged then rederect to home page
			// var loggedFlg = $rootScope.loggedFlg;
			// if (null === loggedFlg || undefined === loggedFlg || false ===
			// loggedFlg) {
			// $location.path('#/');
			// $location.replace();
			// }
			if (!authSer.isLogged()) {
				$location.path('#/');
				$location.replace();
				return;
			}

			// prepare form
			// create a blank object to hold our form information
			// $scope will allow this to pass between controller and view
			$scope.user = {};
			// set user id
			$scope.user.user_id = $rootScope.currentUser.user_id;
			// process the form
			$scope.processForm = function() {
				$http({
					method : 'PUT',
					url : REST_API_URL + 'users/pass',
					data : $.param($scope.user), // pass in data as strings
					// set the headers so angular passing info as form data
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				}).success(function(data) {
					var token = data.data.token;

					// Set to cookies
					if (null !== $cookies.token && undefined !== $cookies.token) {
						$cookies.token = token;
					}
					// Set token to cache
					$rootScope.token = token

					// redirect to home page
					$location.path('#/');
					$location.replace();
					// // back to previous page
					// window.history.back();

					// Show notify
					toaster.pop('success', 'Change password success');
				}).error(function(data) {
					// Show notify
					var messages = data.meta.messages;
					for (var i = 0; i < messages.length; i++) {
						toaster.pop('error', messages[i]);
					}
				});
			};

		} ]);

userControllers.controller('logoutCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookieStore', 'toaster',
		'authSer', function($scope, $rootScope, $http, $location, $cookieStore, toaster, authSer) {

			// Check is logged to call rest
			// var loggedFlg = $rootScope.loggedFlg;
			// if (null !== loggedFlg && undefined !== loggedFlg && true ===
			// loggedFlg) {
			if (authSer.isLogged()) {

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
			$rootScope.isLogged = false;
			$rootScope.currentUser = null;
			// Clean cookies
			$cookieStore.remove("user_id");
			$cookieStore.remove("username");
			$cookieStore.remove("token");
			// Clean nav items list
			$rootScope.navItems = null;

			// Show notify
			toaster.pop('success', 'Logout success');
		} ]);

