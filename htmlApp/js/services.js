miniBlog.service('authSer', [ '$rootScope', '$http', '$location', '$cookies',
		function($rootScope, $http, $location, $cookies) {

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
			// Login
			this.login = function(user) {

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
			// Delete post
			this.deletePost = function(post, posts) {
				// Call rest
				$http({
					method : 'DELETE',
					url : REST_API_URL + 'posts',
					data : $.param(post), // pass in data as strings
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
					// remove
					// var index = posts.indexOf(post);
					// if (index > -1) {
					// posts.splice(index, 1);
					// }

					// Show notify
					toaster.pop('success', 'Delete post success');
				}).error(function(data) {
					// Show notify
					var messages = data.meta.messages;
					for (var i = 0; i < messages.length; i++) {
						toaster.pop('error', messages[i]);
					}
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
			// var response = data.response.meta;
			// if (200 === response.code) {
			// scope.user.avatarlink = response.messages[0];
			// }
			// Call delegate
			complementDelegate(data.response);

			// Show notify
			toaster.pop('success', 'Upload success');
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
					[ 'style', [ 'bold', 'italic', 'underline', 'clear' ] ], [ 'font', [ 'strikethrough' ] ],
							[ 'fontsize', [ 'fontsize' ] ], [ 'color', [ 'color' ] ],
							[ 'para', [ 'ul', 'ol', 'paragraph' ] ], [ 'insert', [ 'link', 'picture' ] ],
							[ 'view', [ 'fullscreen', 'codeview' ] ], [ 'help', [ 'help' ] ] ]

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
