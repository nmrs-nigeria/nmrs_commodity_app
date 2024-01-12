
(function () {
	'use strict';
	
	var baseModel = angular.module('app.genericMetadataModel');
	
	/* Define model fields */
	function ItemExpirationSummaryModel(GenericMetadataModel) {
		
		var extended = angular.extend(GenericMetadataModel, {});
		
		var defaultFields = extended.getModelFields();
		
		// @Override
		extended.getModelFields = function () {
			var fields = ["item", "quantity", "expiration","department"];
			return fields.concat(defaultFields);
		};
		
		return extended;
	}
	
	baseModel.factory("ItemExpirationSummaryModel", ItemExpirationSummaryModel);
	
	ItemExpirationSummaryModel.$inject = ['GenericMetadataModel'];
	
})();
