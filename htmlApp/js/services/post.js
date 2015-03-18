'use strict';

miniBlog.service('postSer', [
		'$rootScope',
		'$http',
		'$location',
		'$cookies',
		'toaster',
		'authSer',
		'util',
		function($rootScope, $http, $location, $cookies, toaster, authSer, util) {

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
					authSer.getUserInfo(post.user_id);

					// Call the success call back
					successCallBack(post);
				})
				// When error;
				.error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);

					// Call the error call back
					errorCallBack(data);
				});
			}

			this.getPosts = function(url, scope) {
				$http.get(REST_API_URL + url).success(function(data, status, headers, config) {
					var posts = data.data
					scope.posts = posts;

					// Get all user info
					if (null !== posts && undefined !== posts) {
						for (var i = 0; i < posts.length; i++) {
							authSer.getUserInfo(posts[i].user_id);
						}
					}
				});
			}

			// Get post by user
			this.getPostsByUser = function(userId, scope) {
				// Browse posts of different user
				if (null === $rootScope.currentUser || undefined == $rootScope.currentUser
						|| userId != $rootScope.currentUser.user_id) {
					var user;
					// get posts
					this.getPosts('posts/users/' + userId, scope);
					// get user if not existed in cache to ensure have info
					// about
					// user
					if (!$rootScope.users[userId]) {
						$http.get(REST_API_URL + 'users/' + userId)
						// Success
						.success(function(data, status, headers, config) {
							user = data.data;
							// Set user to list users
							$rootScope.users[user.user_id] = user;
							// add to nav items
							util.addNavItems(user.username, 'users/' + userId, 'user');
						});
					} else {
						// Get form cache
						user = $rootScope.users[userId];
						// add to nav items
						util.addNavItems(user.username, 'users/' + userId, 'user');
					}

					// Browse posts of logged user
				} else {
					var myParam = {};
					myParam.token = $rootScope.token;

					// Get my post (include deactive post)
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
						scope.posts = posts;

						// Get user info if not existed
						authSer.getUserInfo(userId);
					}).error(function(data) {
						// Show notify
						util.showErrorMsgs(data.meta.messages);
					});
				}
			};

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

					// Notify
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
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

					// Notify
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
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
					// toaster.pop('success', 'Delete post success');
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
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
					// toaster.pop('success', 'Active/Deactive post success');
					toaster.pop('success', data.meta.message);
				}).error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
				});
			};
		} ]);