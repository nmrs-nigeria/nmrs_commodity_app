

(function() {
	'use strict';

	var baseModel = angular.module('app.genericMetadataModel');

	/* Define model fields */
	function ConsumptionModel(GenericMetadataModel) {

		var extended = angular.extend(GenericMetadataModel, {});

		var defaultFields = extended.getModelFields();

		// @Override
		extended.getModelFields = function() {

                        var fields = ["consumptionDate",
					"item", "department",
					"quantity", "wastage","batchNumber","testPurpose","dataSystem"
					];

			return fields.concat(defaultFields);
                return fields;
		};

		return extended;
	}

	baseModel.factory("ConsumptionModel", ConsumptionModel);

	ConsumptionModel.$inject = ['GenericMetadataModel'];

})();
