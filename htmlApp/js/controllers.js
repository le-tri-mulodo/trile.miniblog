'use strict';

var postControllers = angular.module('postControllers', []);
var userControllers = angular.module('userControllers', []);

postControllers.controller('topPostCtrl', [ '$scope', 'postSer', function($scope, postSer) {

	postSer.getPosts('posts/top', $scope);

	// Set service function
	// delete post
	$scope.deletePost = postSer.deletePost;
	// active/deactive post
	$scope.activeDeactivePost = postSer.activeDeactivePost;
} ]);

postControllers.controller('allPostCtrl', [ '$scope', 'postSer', function($scope, postSer) {

	postSer.getPosts('posts/', $scope);

	// Set service function
	// delete post
	$scope.deletePost = postSer.deletePost;
	// active/deactive post
	$scope.activeDeactivePost = postSer.activeDeactivePost;
} ]);

postControllers.controller('searchCtrl', [ '$scope', '$routeParams', 'authSer', 'postSer', 'util',
		function($scope, $routeParams, authSer, postSer, util) {
			var query = 'search/' + $routeParams.query;

			// Check null & empty
			if (query) {
				// Search user
				authSer.search(query, $scope);

				// Search post
				postSer.getPosts('posts/' + query, $scope);

				// Add to nav
				util.addNavItems($routeParams.query, query, 'search');

				// Set service function
				// delete post
				$scope.deletePost = postSer.deletePost;
				// active/deactive post
				$scope.activeDeactivePost = postSer.activeDeactivePost;
			}
		} ]);

postControllers.controller('postOfUserCtrl', [ '$scope', '$routeParams', 'postSer',
		function($scope, $routeParams, postSer) {

			postSer.getPostsByUser($routeParams.userId, $scope);

			// Set service function
			// delete post
			$scope.deletePost = postSer.deletePost;
			// active/deactive post
			$scope.activeDeactivePost = postSer.activeDeactivePost;
		} ]);

postControllers.controller('postDetailCtrl', [ '$scope', '$http', '$routeParams', '$location', '$sce', 'authSer',
		'postSer', 'commentSer', function($scope, $http, $routeParams, $location, $sce, authSer, postSer, commentSer) {

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
							authSer.getUserInfo(comments[i].user_id);
						}
					}
				})
				// Error
				.error(function(data) {
					// Show notify
					util.showErrorMsgs(data.meta.messages);
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

			// add comment
			$scope.addComment = function(postId, content) {

				// Call service
				commentSer.addComment(postId, content)
				// Call rest complete
				.then(function(comment) {

					// Add to comments list to display
					$scope.comments.push(comment);

					// Clear input
					$('#commentTextArea').val(null);
				});
			}

			// delete comment
			$scope.deleteComment = function(postId, idx) {
				commentSer.deleteComment(postId)
				// Call rest complete
				.then(function(comment) {
					// remove from comments list
					$scope.comments.splice(idx, 1);
				});
			}

			// Show editbox for comment
			$scope.showCommentEditBox = function(content, idx) {
				$scope.editIdx = idx;
				$scope.commentEditContent = content
			}

			// edit comment
			$scope.editComment = function(idx, content) {

				var commentId = $scope.comments[idx].comment_id;
				// Call service
				commentSer.editComment(commentId, content)
				// Call rest complete
				.then(function(comment) {

					// Add to comments list to display
					$scope.comments[idx] = comment;

					// Hide editbox
					$scope.editIdx = null;
				});
			}

		} ]);

postControllers.controller('editPostCtrl', [ '$scope', '$rootScope', '$routeParams', '$location', '$sce', 'authSer',
		'postSer', 'RICHTEXTOPTION',
		function($scope, $rootScope, $routeParams, $location, $sce, authSer, postSer, RICHTEXTOPTION) {

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

postControllers.controller('previewPostCtrl', [ '$scope', '$location', '$sce', 'postSer',
		function($scope, $location, $sce, postSer) {
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

userControllers.controller('registerCtrl', [ '$scope', 'authSer', 'util', function($scope, authSer, util) {

	// Check is logged then rederect to home page
	if (authSer.authNotLogged()) {
		return;
	}

	// prepare form
	$scope.user = {};
	// process the form
	$scope.processForm = authSer.register;

	// Upload complement event
	util.uploadCompleteEvent($scope);
} ]);

userControllers.controller('profileCtrl', [ '$scope', '$rootScope', 'toaster', 'authSer', 'util',
		function($scope, $rootScope, toaster, authSer, util) {

			// Check not logged then redirect to home page
			if (authSer.authLogged()) {
				return;
			}

			// Load user info
			var user = authSer.getUserInfo($rootScope.currentUser.user_id,
			// success callback
			function(user) {
				user.token = $rootScope.token;
				// remove join date
				delete user.joindate;
				// Set form data
				$scope.user = user;
				// Set current user data
				$rootScope.currentUser = user;
				// set avatar image
				$scope.currentAvatarlink = user.avatarlink;
			},
			// error callback
			function() {
				toaster.pop('error', 'Error when get user info');
			});

			// set when process the form is edit profile
			$scope.editProfile = authSer.editProfile;

			// Upload complement event
			util.uploadCompleteEvent($scope);
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
