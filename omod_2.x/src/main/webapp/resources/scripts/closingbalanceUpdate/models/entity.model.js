

(function() {
	'use strict';

	var baseModel = angular.module('app.genericMetadataModel');

	/* Define model fields */
	function CrrfReportsModel(GenericMetadataModel) {

		var extended = angular.extend(GenericMetadataModel, {});

	//	var defaultFields = extended.getModelFields();

		// @Override
		extended.getModelFields = function() {

            var fields = ["crrfReportId","facilityName","facilityCode","lga","state","reportingPeriodStart","reportingPeriodEnd","datePrepared","crrfAdultRegimenCategory","crrfPediatricRegimenCategory","crrfOIRegimenCategory","crrfAdvanceHIVRegimenCategory","crrfTBRegimenCategory","crrfSTIRegimenCategory","crrfRTKRegimenCategory"];

		//	return fields.concat(defaultFields);
            return fields;
		};

		return extended;
	}

	baseModel.factory("CrrfReportsModel", CrrfReportsModel);

	CrrfReportsModel.$inject = ['GenericMetadataModel'];

})();