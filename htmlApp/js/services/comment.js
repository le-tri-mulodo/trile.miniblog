'use strict';

miniBlog.factory('commentSer', [ '$rootScope', '$http', '$location', '$cookies', '$q', 'toaster', 'authSer', 'util',
		function($rootScope, $http, $location, $cookies, $q, toaster, authSer, util) {

			// interface
			var service = {
				comments : [],
				getComments : getComments,
				addComment : addComment,
				editComment : editComment,
				deleteComment : deleteComment
			};
			return service;

			// implementation
			// Get all comments by post id
			function getComments(postId) {
			}

			// Add comment
			function addComment(postId, content) {
				var def = $q.defer();

				// Prepare param
				var comment = {};
				comment.content = content;
				comment.post_id = postId;
				// User id and token
				comment.user_id = $rootScope.currentUser.user_id;
				comment.token = $rootScope.token;

				// Call rest
				$http({
					method : 'POST',
					url : REST_API_URL + 'comments',
					data : $.param(comment), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {

					// Notify
					toaster.pop('success', data.meta.message);

					def.resolve(data.data);
				}).error(function(data) {

					// Show notify
					util.showErrorMsgs(data.meta.messages);

					def.reject("Failed to get albums");
				});

				return def.promise;
			}

			// Edit comment
			function editComment(commentId, content) {
				var def = $q.defer();

				// Prepare param
				var comment = {};
				comment.content = content;
				comment.comment_id = commentId;
				// User id and token
				comment.user_id = $rootScope.currentUser.user_id;
				comment.token = $rootScope.token;

				// Call rest
				$http({
					method : 'PUT',
					url : REST_API_URL + 'comments',
					data : $.param(comment), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {

					// Notify
					toaster.pop('success', data.meta.message);

					def.resolve(data.data);
				}).error(function(data) {

					// Show notify
					util.showErrorMsgs(data.meta.messages);

					def.reject("Failed to get albums");
				});

				return def.promise;
			}

			// Delete comment
			function deleteComment(commentId) {
				var def = $q.defer();

				// Call rest
				$http({
					method : 'DELETE',
					url : REST_API_URL + 'comments',
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8',
						// User id and token
						'user_id' : $rootScope.currentUser.user_id,
						'token' : $rootScope.token,
						// Comment Id
						'comment_id' : commentId
					}
				// set the headers so angular passing info as form data
				}).success(function(data) {

					// Notify
					toaster.pop('success', data.meta.message);

					def.resolve(data.meta.message);
				}).error(function(data) {

					// Show notify
					util.showErrorMsgs(data.meta.messages);

					def.reject("Failed to get albums");
				});

				return def.promise;
			}

		} ]);
