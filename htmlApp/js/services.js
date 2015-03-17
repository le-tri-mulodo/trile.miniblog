miniBlog.service('authSer', [ '$rootScope', '$http', '$location', '$cookies', '$cookieStore', 'toaster',
		function($rootScope, $http, $location, $cookies, $cookieStore, toaster) {

			// Check login status is logged
			this.isLogged = function() {

				// Check in cookie
				if (null != $cookies.token && undefined != $cookies.token) {
					return true;
				}

				// Check in cache
				if (null != $rootScope.currentUser && undefined != $rootScope.currentUser) {
					return true;
				}

				// Not login
				return false;
			}

			// Authenticate user is NOT logged. If logged then redirect to home
			// page and show notify
			this.authNotLogged = function() {
				if (true == this.isLogged()) {
					this.errorHandler('User logged');
					return true;
				}
				return false;
			};

			// Authenticate user is logged. If NOT logged then redirect to home
			// page and show notify
			this.authLogged = function() {
				if (false == this.isLogged()) {
					this.errorHandler('User not logged yet');
					return true;
				}
				return false;
			};

			// redirect to home page and show error notify
			this.errorHandler = function(message) {
				// redirect to home page
				$location.path('#/');
				$location.replace();
				// show notify
				toaster.pop('error', message);
			};

			// Login
			this.login = function(user, rememberLogin) {
				$http({
					method : 'POST',
					url : REST_API_URL + 'tokens/login',
					data : $.param(user), // pass in data as strings
					// set the headers so angular passing info as form data
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				}).success(function(data) {
					var user = data.data;

					// Save login data
					$rootScope.isLogged = true;
					$rootScope.currentUser = user;
					$rootScope.token = user.token;

					// Set to cookies
					if (true === rememberLogin) {
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
					toaster.pop('success', 'Login success');
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			};

			// Logout
			this.logout = function() {
				// Check is logged to call rest
				if (this.isLogged()) {

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
			}

			// Change password
			this.changePassword = function(user) {
				$http({
					method : 'PUT',
					url : REST_API_URL + 'users/pass',
					data : $.param(user), // pass in data as strings
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
					showErrorMsgs(data.meta.messages, toaster);
				});
			};
		} ]);

miniBlog.service('postSer', [ '$rootScope', '$http', '$location', '$cookies', 'toaster',
		function($rootScope, $http, $location, $cookies, toaster) {

			var previewPost = {};

			// Set preview post
			this.setPreviewPost = function(post) {

				previewPost = post
			}
			// Get preview post
			this.getPreviewPost = function() {
				return previewPost;
			};

			// Get post info
			this.getPostInfo = function(postId, successCallBack, errorCallBack) {
				$http.get(REST_API_URL + 'posts/' + postId)
				// When success
				.success(function(data, status, headers, config) {
					var post = data.data;
					// get user info if not exist
					getUserInfo(post.user_id, $rootScope.users, $http);

					// Call the success call back
					successCallBack(post);
				})
				// When error;
				.error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);

					// Call the error call back
					errorCallBack(data);
				});
			}

			// Add post
			this.addPost = function(post) {
				$http({
					method : 'POST',
					url : REST_API_URL + 'posts',
					data : $.param(post), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					var id = data.data.post_id;
					// redirect to post detail
					$location.path('#/posts/' + id);
					$location.replace();

					// Reset preview post
					previewPost = {};
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			};

			// Edit post
			this.editPost = function(post) {
				$http({
					method : 'PUT',
					url : REST_API_URL + 'posts',
					data : $.param(post), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					var id = data.data.post_id;
					// redirect to post detail
					$location.path('#/posts/' + id);
					$location.replace();

					// Reset preview post
					previewPost = {};
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			};

			// Preview post
			this.previewPost = function(pPost) {
				previewPost = pPost;

				$location.path('#/preview_post');
				$location.replace();
			};

			// Delete post
			this.deletePost = function(post) {
				// Call rest
				$http({
					method : 'DELETE',
					url : REST_API_URL + 'posts',
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8',
						// Set post id
						'post_id' : post.post_id,
						// Set user id and token
						'user_id' : $rootScope.currentUser.user_id,
						'token' : post.token = $rootScope.token
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					// redirect to home
					$location.path('#/');
					$location.replace();

					// Show notify
					toaster.pop('success', 'Delete post success');
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			};

			// Active/deactive post
			this.activeDeactivePost = function(post, publicFlg) {
				var active = false;
				// Check public flag
				if (null !== publicFlg && undefined !== publicFlg && true == publicFlg) {
					active = true;
				}

				// Set param
				var myParam = {};
				myParam.post_id = post.post_id;
				myParam.active = active;
				// User id and token
				myParam.user_id = $rootScope.currentUser.user_id;
				myParam.token = $rootScope.token;

				// Call rest
				$http({
					method : 'PUT',
					url : REST_API_URL + 'posts/pub',
					data : $.param(myParam), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8',
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					// redirect to home
					$location.path('#/');
					$location.replace();

					// Show notify
					toaster.pop('success', 'Active/Deactive post success');
				}).error(function(data) {
					// Show notify
					showErrorMsgs(data.meta.messages, toaster);
				});
			};
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

function uploadCompleteEvent(scope, toaster, complementDelegate) {
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
			// Call delegate
			complementDelegate(data.response);

			// Show notify
			toaster.pop('success', 'Upload success');
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

function showErrorMsgs(messages, toaster) {
	for (var i = 0; i < messages.length; i++) {
		toaster.pop('error', messages[i]);
	}
}
