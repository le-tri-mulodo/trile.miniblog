'use strict';

miniBlog.service('util', [ '$rootScope', '$http', '$location', '$cookies', '$cookieStore', 'toaster',
		function($rootScope, $http, $location, $cookies, $cookieStore, toaster) {

			// Create upload element and handle upload success event
			this.uploadCompleteEvent = function(scope) {
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
						var meta = data.response.meta;
						if (200 === meta.code) {
							// add path of image
							scope.user.avatarlink = 'img/' + meta.messages[0];

							// Show notify
							toaster.pop('success', 'Upload success');
						} else {
							showErrorMsgs(meta.messages)
						}
					});
				});
			}

			// Add new item into navigation bar
			this.addNavItems = function(title, url, type) {
				// Define item
				var navItem = {};
				navItem.title = title;
				navItem.url = url;
				navItem.type = type;

				// Add to nav items list
				var navItems = $rootScope.navItems;
				if (null === navItems || undefined === navItems) {
					// Define nav items list if not exist
					$rootScope.navItems = [ navItem ];
				} else {
					// Remove existed item
					for (var i = 0; i < navItems.length; i++) {
						// Check existed
						if (navItems[i].url === url) {
							// Remove
							$rootScope.navItems.splice(i, 1);
							break;
						}
					}
					// Add to top
					$rootScope.navItems.unshift(navItem);
				}
			}

			// Show error messages. input is array error messages
			this.showErrorMsgs = function(messages) {
				for (var i = 0; i < messages.length; i++) {
					toaster.pop('error', messages[i]);
				}
			}
		} ]);