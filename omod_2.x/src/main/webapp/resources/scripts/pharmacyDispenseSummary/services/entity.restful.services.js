

(function() {
	'use strict';

	angular.module('app.restfulServices').service('ARVPharmacyDispenseRestfulService',
	ARVPharmacyDispenseRestfulService);

	ARVPharmacyDispenseRestfulService.$inject = ['EntityRestFactory', 'PaginationService'];

	function ARVPharmacyDispenseRestfulService(EntityRestFactory, PaginationService) {
		var service;

		service = {
			searchDispenseSummarys : searchDispenseSummarys,
			loadDepartments : loadDepartments,
            loadItems : loadItems
			// searchConcepts : searchConcepts
			//  loadItemStock : loadItemStock,
			// loadItemAttributeTypes : loadItemAttributeTypes,
		};

		return service;

		function searchDispenseSummarys(startIndex, limit, department_uuid,startDate,endDate, includeRetired, onLoadSuccessfulCallback){
			var requestParams = PaginationService.paginateParams(startIndex, limit, includeRetired);
			requestParams['rest_entity_name'] = 'pharmacyDispenseSummary';
                        
                        console.log('about to call pharm dispense summary endpoint');
                        
//			if(angular.isDefined(department_uuid)){
//				requestParams['department_uuid'] = department_uuid;
//			}
//                        
//                        if(angular.isDefined(item_uuid)){
//				requestParams['item_uuid'] = item_uuid;
//			}
                        
                        if(angular.isDefined(startDate)){
                            requestParams['startDate'] = startDate;
                        }
                        
                        if(angular.isDefined(endDate)){
                            requestParams['endDate'] = endDate;
                        }



			EntityRestFactory.loadEntities(requestParams, onLoadSuccessfulCallback, errorCallback);
		}

		/**
		 * Temporary Function: It will ONLY be used until the Department module is done.
		 * @param onLoadDepartmentsSuccessful
		 */
		function loadDepartments(onLoadDepartmentsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'department';
			EntityRestFactory.loadEntities(requestParams,
					onLoadDepartmentsSuccessful, errorCallback);
		}
                
        function loadItems(onLoadItemsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'item';
			EntityRestFactory.loadEntities(requestParams,
					onLoadItemsSuccessful, errorCallback);
		}

		/**
		 * An auto-complete function to search concepts given a query term.
		 * @param module_name
		 * @param q - search term
		 * @param limit
		 */
	

	
		function errorCallback(error) {
			emr.errorAlert(error);
		}
	}
})();
