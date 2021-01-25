/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific language governing rights and
 * limitations under the License.
 *
 * Copyright (C) OpenHMIS.  All Rights Reserved.
 *
 */

(function () {
	'use strict';
	
	angular.module('app.restfulServices').service('ItemExpirationSummaryRestfulService', ItemExpirationSummaryRestfulService);
	
	ItemExpirationSummaryRestfulService.$inject = ['EntityRestFactory', 'PaginationService'];
	
	function ItemExpirationSummaryRestfulService(EntityRestFactory, PaginationService) {
		var service;
		
		service = {
			loadStockrooms: loadStockrooms,
			loadDepartments: loadDepartments,
			loadStockDetailsDepartment: loadStockDetailsDepartment,
			loadStockDetails: loadStockDetails
		};
		
		return service;
		
		/**
		 * Retrieve all Stockrooms
		 * @param onLoadStockroomsSuccessful
		 * @param onLoadDepartmentsSuccessful
		 * @param module_name
		 */
		function loadStockrooms(module_name, onLoadStockroomsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'stockroom';
			EntityRestFactory.loadEntities(requestParams,
				onLoadStockroomsSuccessful,
				errorCallback
			);
		}
		/**
		 * Retrieve all departments
		 */
		function loadDepartments(module_name, onLoadDepartmentsSuccessful) {
			var requestParams = [];
			requestParams['rest_entity_name'] = 'department';
			EntityRestFactory.loadEntities(requestParams,
				onLoadDepartmentsSuccessful,
				errorCallback
			);
		}

		/**
		 * Retrieve all the stock in the selected stockroom
		 * @param stockroomUuid
		 * @param rest_entity_name
		 * @param currentPage
		 * @param limit
		 * @param onLoadStockDetailsSuccessful
		 * */
		function loadStockDetails(stockroomUuid, currentPage, limit, onLoadStockDetailsSuccessful) {
			console.log("inside loadStockDetails entity.restful.service");
			currentPage = currentPage || 1;
			if (angular.isDefined(stockroomUuid) && stockroomUuid !== '' && stockroomUuid !== undefined) {
				var requestParams = PaginationService.paginateParams(currentPage, limit, false);
				requestParams['rest_entity_name'] = 'itemExpirationSummary';
				requestParams['stockroom_uuid'] = stockroomUuid;
				EntityRestFactory.loadEntities(requestParams,
					onLoadStockDetailsSuccessful,
					errorCallback
				);
			}
		}
		
		function loadStockDetailsDepartment(departmentUuid, currentPage, limit, onLoadStockDetailsSuccessful) {
			console.log("inside loadStockDetailsDepartment entity.restful.service");
			currentPage = currentPage || 1;
			if (angular.isDefined(departmentUuid) && departmentUuid !== '' && departmentUuid !== undefined) {
				var requestParams = PaginationService.paginateParams(currentPage, limit, false);
				requestParams['rest_entity_name'] = 'itemExpirationSummary';
				requestParams['department_uuid'] = departmentUuid;
				EntityRestFactory.loadEntities(requestParams,
					onLoadStockDetailsSuccessful,
					errorCallback
				);
			}
		}

		function errorCallback(error) {
			emr.errorAlert(error);
		}
	}
})();
