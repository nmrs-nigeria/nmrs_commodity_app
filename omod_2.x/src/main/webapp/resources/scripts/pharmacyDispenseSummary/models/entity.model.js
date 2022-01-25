

(function() {
	'use strict';

	var baseModel = angular.module('app.genericMetadataModel');

	/* Define model fields */
	function ARVPharmacyDispenseModel(GenericMetadataModel) {

		var extended = angular.extend(GenericMetadataModel, {});

	//	var defaultFields = extended.getModelFields();

		// @Override
		extended.getModelFields = function() {

            var fields = ["patientID","patientCategory","treatmentType","visitType","pickupReason","dateOfDispensed","items","patientDBId","encounterId","treatmentAge","currentLine","currentRegimen"];

		//	return fields.concat(defaultFields);
            return fields;
		};

		return extended;
	}

	baseModel.factory("ARVPharmacyDispenseModel", ARVPharmacyDispenseModel);

	ARVPharmacyDispenseModel.$inject = ['GenericMetadataModel'];

})();
