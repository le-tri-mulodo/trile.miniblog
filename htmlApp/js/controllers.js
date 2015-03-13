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

postControllers.controller('searchCtrl', [ '$scope', '$rootScope', '$http', '$routeParams',
		function($scope, $rootScope, $http, $routeParams) {
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
			}
		} ]);

postControllers.controller('postOfUserCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$routeParams',
		function($scope, $rootScope, $http, $routeParams) {
			var userId = $routeParams.userId;
			// get posts
			getPosts('posts/users/' + userId, $scope, $rootScope, $http);

			// add to nav items
			// if get posts of logged user then don't add
			if (null === $rootScope.currentUser || undefined == $rootScope.currentUser
					|| userId != $rootScope.currentUser.user_id) {
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
			}
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

postControllers.controller('newPostCtrl', [ '$scope', '$rootScope', '$http', '$routeParams',
		function($scope, $rootScope, $http, $routeParams) {
	console.log('hehe');
			$scope.options = {
					height: 300,
					toolbar: [
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

userControllers.controller('chpwdCtrl', [ '$scope', '$rootScope', '$http', '$location', '$cookies',
		function($scope, $rootScope, $http, $location, $cookies) {
			// Check is not logged then rederect to home page
			var loggedFlg = $rootScope.loggedFlg;
			if (null === loggedFlg || undefined === loggedFlg || false === loggedFlg) {
				$location.path('#/');
				$location.replace();
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
			// Clean nav items list
			$rootScope.navItems = null;
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

function createRichTextFormater(scope) {
	// event to create rich text formater element
	scope.$on('$routeChangeSuccess', function() {
		$("#summernote").summernote(
		{
			height : 200, // set editor height
			minHeight : null, // set minimum height of editor
			maxHeight : null, // set maximum height of editor
			focus : true, // set focus to editable area after
							// initializing summernote
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

		});
	});
}

function addNavItems(rootScope, title, url, type) {
	// Define item
	var navItem = {};
	navItem.title = title;
	navItem.url = url;
	navItem.type = type;

	// Add to nav items list
	var navItems = rootScope.navItems;
	if (null === navItems || undefined === navItems) {
		// Define nav items list if not exist
		rootScope.navItems = [ navItem ];
	} else {
		// Remove existed item
		for (var i = 0; i < navItems.length; i++) {
			// Check existed
			if (navItems[i].url === url) {
				// Remove
				rootScope.navItems.splice(i, 1);
				break;
			}
		}
		// Add to top
		rootScope.navItems.unshift(navItem);
	}
}
