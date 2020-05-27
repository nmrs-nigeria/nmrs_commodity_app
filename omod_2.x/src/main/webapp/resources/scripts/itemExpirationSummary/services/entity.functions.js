

(function () {
	'use strict';
	
	var app = angular.module('app.itemExpirationSummaryFunctionsFactory', []);
	app.service('ItemExpirationSummaryFunctions', ItemExpirationSummaryFunctions);
	
	ItemExpirationSummaryFunctions.$inject = ['$filter'];
	
	function ItemExpirationSummaryFunctions($filter) {
		var service;
		
		service = {
			formatDate: formatDate,
			stockroomChangeDialog: stockroomChangeDialog
		};
		
		return service;
		
		/**
		 * Formats the date to allow proper updating of the stocks
		 * @params date
		 * @returns formattedDate
		 * */
		function formatDate(date) {
			return ($filter('date')(new Date(date), 'dd-MM-yyyy'));
		}
		
		/**
		 * Disable and gray-out background when a dialog box opens up.
		 */
		function disableBackground() {
			var backgroundElement = angular.element('.simplemodal-overlay');
			backgroundElement.addClass('disable-background');
		}
		
		/**
		 * Show the generate report popup
		 * @param selectorId - div id
		 */
		function stockroomChangeDialog(selectorId, $scope) {
			var dialog = emr.setupConfirmationDialog({
				selector: '#' + selectorId,
				actions: {
					cancel: function () {
						dialog.close();
					},
					confirm: function () {
						$scope.loadStockDetails($scope.itemExpiryCurrentPage);
						$scope.$apply();
						dialog.close();
					}
				}
			});
			
			dialog.show();
			disableBackground();
		}
	}
})();
