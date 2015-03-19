'use strict';

miniBlog.service('authSer', [ '$rootScope', '$http', '$location', '$cookies', '$cookieStore', 'toaster', 'util',
		function($rootScope, $http, $location, $cookies, $cookieStore, toaster, util) {

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

					// Get user info
					// getUserInfo(user.user_id);

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
					// toaster.pop('success', 'Login success');
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
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

					// Clean scope
					$rootScope.isLogged = false;
					$rootScope.currentUser = null;
					// Clean cookies
					$cookieStore.remove("user_id");
					$cookieStore.remove("username");
					$cookieStore.remove("token");
					// Clean nav items list
					$rootScope.navItems = null;
					// Clean preview post
					// postSer.setPreviewPost(null);

					// Show notify
					toaster.pop('success', 'Logout success');
				} else {
					// Show notify
					toaster.pop('error', 'User not logged');
				}
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
					// toaster.pop('success', 'Change password success');
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
				});
			};

			// Change password
			this.register = function(user) {
				$http({
					method : 'POST',
					url : REST_API_URL + 'users',
					data : $.param(user), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {
					var user = data.data;
					// Save registered user data
					$rootScope.isLogged = true;
					$rootScope.currentUser = user;
					$rootScope.token = user.token;

					// Save user info to cache
					$rootScope.users[user.user_id] = user;

					// Set to cookies
					$cookies.user_id = user.user_id;
					$cookies.username = user.username;
					$cookies.token = user.token;

					// redirect to home page
					$location.path('#/');
					$location.replace();

					// Show notify
					// toaster.pop('success', 'Register success');
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
				});
			};

			// Edit profile
			this.editProfile = function(user) {
				$http({
					method : 'PUT',
					url : REST_API_URL + 'users',
					data : $.param(user), // pass in data as strings
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
					// toaster.pop('success', 'Update profile success');
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
				});
			};

			// Get user info by id. if user info doesn't exist in cache the call
			// rest else call rest to get
			// this function user callback to handle success and error event
			this.getUserInfo = function(user_id, successCallBack, errorCallBack) {
				var users = $rootScope.users;
				// Not existed in cache
				if (null === users[user_id] || undefined === users[user_id]) {
					$http.get(REST_API_URL + 'users/' + user_id)
					// Success
					.success(function(data, status, headers, config) {
						var user = data.data;
						// Set user to list users
						users[user.user_id] = user;

						// Callback
						successCallBack && successCallBack(user);
					}).error(function(data) {
						util.showErrorMsgs(data.meta.messages)

						// Callback
						errorCallBack && errorCallBack(data);
					});

					// existed in cache
				} else {
					successCallBack && successCallBack(users[user_id]);
				}
			}

			// Search username, firstname, lastname
			this.search = function(query, scope) {
				$http.get(REST_API_URL + 'users/' + query)
				// Success
				.success(function(data, status, headers, config) {
					// Set user to list users
					scope.searchResultUsers = data.data;
				});
			}
		} ]);
