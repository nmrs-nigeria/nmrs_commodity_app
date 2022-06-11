

(function() {
	'use strict';

	angular.module('app.restfulServices').service('CrrfReportsRestfulService', CrrfReportsRestfulService);

	CrrfReportsRestfulService.$inject = ['EntityRestFactory', 'PaginationService'];

	function CrrfReportsRestfulService(EntityRestFactory, PaginationService) {
		var service;

		service = {
				generateCRFFReport : generateCRFFReport
		};

		return service;
		
		function generateCRFFReport(reportId, startDate, endDate, crrfCategory, repPeriod, repYear, currentReportingPeriod, currentReportingYear,  startIndex, limit, includeRetired, onLoadSuccessfulCallback){
					         
			var requestParams = PaginationService.paginateParams(startIndex, limit, includeRetired);
					requestParams['rest_entity_name'] = 'crrfReports';
					requestParams['reportId'] = reportId;
					requestParams['crrfCategory'] = crrfCategory; 
					requestParams['repPeriod'] = repPeriod;
					requestParams['repYear'] = repYear; 
					requestParams['currentReportingPeriod'] = currentReportingPeriod;
					requestParams['currentReportingYear'] = currentReportingYear; 
                        
                        if(angular.isDefined(startDate)){
                            requestParams['startDate'] = startDate;
                        }
                        
                        if(angular.isDefined(endDate)){
                            requestParams['endDate'] = endDate;
                        }

						
                        console.log('about to call crrf endpoint');

			EntityRestFactory.loadEntities(requestParams, onLoadSuccessfulCallback, errorCallback);
		}

		/**
		 * Temporary Function: It will ONLY be used until the Department module is done.
		 * @param onLoadDepartmentsSuccessful
		 */
		

		/**
		 * An auto-complete function to search concepts given a query term.
		 * @param module_name
		 * @param q - search term
		 * @param limit
		 */
	

	
		function errorCallback(error) {
			emr.errorAlert(error);
		}
	}
})();
