

(function() {
	'use strict';

	angular.module('app.restfulServices').service('ConsumptionRestfulService',
			ConsumptionRestfulService);

	ConsumptionRestfulService.$inject = ['EntityRestFactory', 'PaginationService'];

	function ConsumptionRestfulService(EntityRestFactory, PaginationService) {
		var service;

		service = {
			searchConsumptions : searchConsumptions,
			loadDepartments : loadDepartments,
                        loadItems : loadItems,
                        getItemBatch : getItemBatch
			// searchConcepts : searchConcepts
			//  loadItemStock : loadItemStock,
			// loadItemAttributeTypes : loadItemAttributeTypes,
		};

		return service;

		function searchConsumptions(startIndex, limit, department_uuid,item_uuid, includeRetired, onLoadSuccessfulCallback){
			var requestParams = PaginationService.paginateParams(startIndex, limit, includeRetired);
			requestParams['rest_entity_name'] = 'consumption';
                        
			if(angular.isDefined(department_uuid)){
				requestParams['department_uuid'] = department_uuid;
			}
                        
                        if(angular.isDefined(item_uuid)){
				requestParams['item_uuid'] = item_uuid;
			}

//			if(angular.isDefined(q) && q !== '' && q !== null && q !== undefined){
//				requestParams['q'] = q;
//			}
//			else if(angular.isDefined('department_uuid') && department_uuid !== undefined){
//				requestParams['q'] = q;
//			}
//                        else if(angular.isDefined('item_uuid') && item_uuid !== undefined){
//				requestParams['q'] = q;
//			}

			EntityRestFactory.loadEntities(requestParams, onLoadSuccessfulCallback, errorCallback);
		}

		/**
		 * Temporary Function: It will ONLY be used until the Department module is done.
		 * @param onLoadDepartmentsSuccessful
		 */
		function loadDepartments(onLoadDepartmentsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'department';
                        requestParams['departmentType'] = 'lab';
			EntityRestFactory.loadEntities(requestParams,
					onLoadDepartmentsSuccessful, errorCallback);
		}
                
                function loadItems(onLoadItemsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'item';
                         requestParams['itemType'] = 'lab';
                        
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
                
                function getItemBatch(itemUUIDProperty, successCallback) {
			var requestParams = [];
			requestParams['resource'] = INVENTORY_MODULE_ITEMS_UTILITY_URL;
			requestParams['itemUUID'] = itemUUIDProperty;

			EntityRestFactory.setCustomBaseUrl(ROOT_URL);
			EntityRestFactory.loadResults(requestParams, successCallback, function(error) {
				console.log(error)
			});
                        
                        EntityRestFactory.setBaseUrl(INVENTORY_MODULE_NAME);
                        
                        console.log('finished calling item batch');
                        
		}
                
                function setBaseUrl(module_name) {
			EntityRestFactory.setBaseUrl(module_name);
		}
                
	}
})();
