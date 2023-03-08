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

(function() {
	'use strict';

	angular.module('app.restfulServices').service('DepartmentRestfulService', DepartmentRestfulService);

    DepartmentRestfulService.$inject = ['EntityRestFactory'];

	function DepartmentRestfulService(EntityRestFactory) {
		var service;

		service = {
			loadDepartments: loadDepartments,
		};

		return service
        /**
         * Retrieve all Stockrooms
         * @param onLoadDepartmentsSuccessful
         * @param module_name
         */

		function loadDepartments(module_name,onLoadDepartmentsSuccessful) {
			//setBaseUrl(module_name);
			var requestParams = {};
			requestParams['rest_entity_name'] = 'department';
                         requestParams['departmentType'] = 'pharmacy';
			requestParams['limit'] = 100;
            EntityRestFactory.loadEntities(requestParams,
                onLoadDepartmentsSuccessful,
                errorCallback
            );
		}

		function setBaseUrl(module_name) {
			EntityRestFactory.setBaseUrl(module_name);
		}

		function errorCallback(error) {
			emr.errorAlert(error);
		}




	}
})();
