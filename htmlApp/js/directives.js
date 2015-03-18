miniBlog.directive('isActiveNav', [ '$location', function($location) {
	return {
		restrict : 'A',
		link : function(scope, element) {
			scope.location = $location;
			scope.$watch('location.path()', function(currentPath) {
				if ('#' + currentPath === element[0].attributes['href'].nodeValue) {
					element.parent().addClass('active');
				} else {
					element.parent().removeClass('active');
				}
			});
		}
	};
} ]);

miniBlog.directive("compareTo", function() {
	return {
		require : "ngModel",
		scope : {
			otherModelValue : "=compareTo"
		},
		link : function(scope, element, attributes, ngModel) {

			ngModel.$validators.compareTo = function(modelValue) {
				return modelValue == scope.otherModelValue;
			};

			scope.$watch("otherModelValue", function() {
				ngModel.$validate();
			});
		}
	};
});

miniBlog.directive('toggle', function() {
	return {
		restrict : 'A',
		link : function(scope, element, attrs) {
			if (attrs.toggle == "tooltip") {
				$(element).tooltip();
			}
			if (attrs.toggle == "popover") {
				$(element).popover();
			}
		}
	};
});
