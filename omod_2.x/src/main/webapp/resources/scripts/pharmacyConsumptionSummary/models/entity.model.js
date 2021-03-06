

(function() {
	'use strict';

	var baseModel = angular.module('app.genericMetadataModel');

	/* Define model fields */
	function ConsumptionSummaryModel(GenericMetadataModel) {

		var extended = angular.extend(GenericMetadataModel, {});

	//	var defaultFields = extended.getModelFields();

		// @Override
		extended.getModelFields = function() {

                        var fields = ["totalQuantityReceived",
					"uuid",
					"item","groupUuid","drugCategory"
					];

		//	return fields.concat(defaultFields);
                return fields;
		};

		return extended;
	}

	baseModel.factory("ConsumptionSummaryModel", ConsumptionSummaryModel);

	ConsumptionSummaryModel.$inject = ['GenericMetadataModel'];

})();
