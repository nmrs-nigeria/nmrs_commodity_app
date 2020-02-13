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

	var app = angular.module('app.consumptionFunctionsFactory', []);
	app.service('ConsumptionFunctions', ConsumptionFunctions);

	ConsumptionFunctions.$inject = ['EntityFunctions'];

	function ConsumptionFunctions(EntityFunctions) {
		var service;

		service = {
			// editItemCode : editItemCode,
			addMessageLabels : addMessageLabels
		};

		return service;





		/**
		 * All message labels used in the UI are defined here
		 * @returns {{}}
		 */
		function addMessageLabels() {
			var messages = {};
			
			messages['openhmis.inventory.department.name'] = emr
					.message('openhmis.inventory.department.name');
			messages['openhmis.inventory.consumption.consumptionDate'] = emr
					.message('openhmis.inventory.consumption.consumptionDate');
			messages['openhmis.inventory.consumption.quantity'] = emr
					.message('openhmis.inventory.consumption.quantity');
			messages['openhmis.inventory.consumption.testingPoint'] = emr
                                .message('openhmis.inventory.consumption.testingPoint');
			messages['openhmis.inventory.consumption.testType'] = emr
					.message('openhmis.inventory.consumption.testType');
                                      
		
			messages['openhmis.inventory.stockroom.name'] = emr
					.message('openhmis.inventory.stockroom.name');
		
			messages['openhmis.commons.general.add'] = emr
					.message('openhmis.commons.general.add');
			messages['openhmis.commons.general.edit'] = emr
					.message('openhmis.commons.general.edit');
			return messages;
		}
	}
})();
