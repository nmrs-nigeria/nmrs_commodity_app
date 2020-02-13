

(function() {
	'use strict';

	var baseModel = angular.module('app.genericMetadataModel');

	/* Define model fields */
	function ConsumptionModel(GenericMetadataModel) {

		var extended = angular.extend(GenericMetadataModel, {});

		var defaultFields = extended.getModelFields();

		// @Override
		extended.getModelFields = function() {
//			var fields = ["buyingPrice", "codes", "concept",
//					"defaultExpirationPeriod", "defaultPrice", "department",
//					"description", "hasExpiration", "hasPhysicalInventory",
//					"minimumQuantity", "prices", "attributes"];

                        var fields = ["consumptionDate",
					"item", "department",
					"quantity", "testType"
					];

			return fields.concat(defaultFields);
		};

		return extended;
	}

	baseModel.factory("ConsumptionModel", ConsumptionModel);

	ConsumptionModel.$inject = ['GenericMetadataModel'];

})();
