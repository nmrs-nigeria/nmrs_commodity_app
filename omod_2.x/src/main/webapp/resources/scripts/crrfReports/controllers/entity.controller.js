
(function () {
    'use strict';

    var base = angular.module('app.genericManageController');
    base.controller("CrrfReportsController", CrrfReportsController);
    CrrfReportsController.$inject = ['$injector', '$scope', '$filter',
        'EntityRestFactory', 'CssStylesFactory', 'PaginationService',
        'CrrfReportsModel', 'CookiesService', 'CrrfReportsRestfulService', 'CrrfReportsFunctions'];

    function CrrfReportsController($injector, $scope, $filter,
            EntityRestFactory, CssStylesFactory, PaginationService, CrrfReportsModel,
            CookiesService, CrrfReportsRestfulService, CrrfReportsFunctions) {

        var self = this;

        var entity_name = emr.message("openhmis.inventory.report.name");
        var REST_ENTITY_NAME = "crrfReports";

        // @Override
        self.getModelAndEntityName = self.getModelAndEntityName
                || function () {
                    self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME,
                            entity_name);
                    self.checkPrivileges(TASK_MANAGE_METADATA);
                }

        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope || function () {
        	$scope.categories = ["ARV Cotrim", "HIV RTKS and DBS"];
        	$scope.reportingPeriods = ["January-February","March-April","May-June","July-August","September-October","November-December"];
        	$scope.years = ["2021","2022","2023","2024","2025","2026","2027","2028","2029","2030","2031","2032","2033","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"];
        	
        	$scope.category = $scope.categories[0];
            $scope.reportingPeriod = $scope.reportingPeriods[0];
            $scope.year = $scope.years[0];

            $scope.generateCRFFReport = self.generateCRFFReport;
//            $scope.startDate = CookiesService.get('startDate') || {};
//            $scope.endDate = CookiesService.get('endDate') || {};

            CrrfReportsFunctions.onChangeDatePicker('startDate-display', function (value) {
               $scope.startDate = value;
            });

            CrrfReportsFunctions.onChangeDatePicker('endDate-display', function (value) {
               $scope.endDate = value;
            });
        }
        
        function checkParameters(parameterObject) {
            var objectKeys = Object.keys(parameterObject);
            for (var i = 0; i < objectKeys.length; i++) {
                var name = objectKeys[i];
                var value = parameterObject[objectKeys[i]];
                if (!value) {
                    switch (name) {
                        case "startDate":
                            emr.errorAlert('openhmis.inventory.report.error.beginDateRequired');
                            break;
                        case "endDate":
                            emr.errorAlert('openhmis.inventory.report.error.endDateRequired');
                            break;
                        case "selectedCRRFType":
                            emr.errorAlert('Please Select CRFF Report Type');
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            }
            console.log('check paramaters was successful');
            return true;
        }

      
        self.generateCRFFReport = self.generateCRFFReport || function (currentPage) {
                console.log('about to call crrf controller');
               
	           	if(currentPage === undefined){
					currentPage = $scope.currentPage;
				}
				else{
					$scope.currentPage = currentPage;
				}			
	           	
	        	CookiesService.set('startIndex', $scope.startIndex);
	            CookiesService.set('limit', $scope.limit);
	            CookiesService.set('includeRetired', $scope.includeRetired);
	            CookiesService.set('currentPage', currentPage);
	            CookiesService.set('startDate', $scope.startDate);
	            CookiesService.set('endDate', $scope.endDate);
			
              // var startDate = $scope.startDate;
              // var endDate = $scope.endDate;
               var crrfCategory = $scope.category;
               var repPeriod = $scope.reportingPeriod;
               var repYear = $scope.year;
               
               var startDay = "";
        	   var endDay = "";
        	   var startMonth = "";
        	   var endMonth = "";
               var prevReportingPeriod = "";
        	   var prevReportingYear = "";
               
               console.log('about to call crrf controller2');
               
               if(repPeriod == 'January-February'){
            	   startDay = "01";
            	   endDay = "29";
            	   startMonth = "Jan";
            	   endMonth = "Feb";
                   prevReportingPeriod = "November-December";
                   var prevRepYear = parseInt(repYear) - 1;
                   prevReportingYear = prevRepYear.toString();
               }
               if(repPeriod == 'March-April'){
            	   startDay = "01";
            	   endDay = "30";
            	   startMonth = "Mar";
            	   endMonth = "Apr";
                   prevReportingPeriod = "January-February";
                   prevReportingYear = repYear;
               }
               if(repPeriod == 'May-June'){
            	   startDay = "01";
            	   endDay = "30";
            	   startMonth = "May";
            	   endMonth = "Jun";
                   prevReportingPeriod = "March-April";
                   prevReportingYear = repYear;
               }
               if(repPeriod == 'July-August'){
            	   startDay = "01";
            	   endDay = "31";
            	   startMonth = "Jul";
            	   endMonth = "Aug";
                   prevReportingPeriod = "May-June";
                   prevReportingYear = repYear;
               }
               if(repPeriod == 'September-October'){
            	   startDay = "01";
            	   endDay = "31";
            	   startMonth = "Sep";
            	   endMonth = "Oct";
                   prevReportingPeriod = "July-August";
                   prevReportingYear = repYear;
               }
               if(repPeriod == 'November-December'){
            	   startDay = "01";
            	   endDay = "31";
            	   startMonth = "Nov";
            	   endMonth = "Dec";
                   prevReportingPeriod = "September-October";
                   prevReportingYear = repYear;
               }
               var startDate = startDay + " " + startMonth + " " + repYear;
               var endDate = endDay + " " + endMonth + " " + repYear;
               
               console.log("repPeriod: " + startDate);
               console.log("repYear: " + endDate);
               console.log("Start Date: " + startDate);
               console.log("End Date: " + endDate);
               console.log("Selected CRRF Category: " + crrfCategory);

               var parametersAreValid = checkParameters({
                   "startDate": startDate,
                   "endDate": endDate,
                   "CRRF Category": crrfCategory
               });


               console.log('parametersvalid output');
               console.log(parametersAreValid);

               if (parametersAreValid) {

                   $scope.loading = true;
                   
                   console.log('After loading');
              
                   CrrfReportsRestfulService.generateCRFFReport("crrf_report", CrrfReportsFunctions.formatDate(startDate), CrrfReportsFunctions.formatDate(endDate), crrfCategory, prevReportingPeriod, prevReportingYear, currentPage, $scope.limit, $scope.includeRetired, self.onLoadCRRFReportSuccessful) 

               } else {
                   $scope.loading = false;
                   console.log("The start date is " + startDate);
                   console.log("The end date is " + endDate);
                   console.log("CRRF Report crrfCategory " +  crrfCategory);
                   alert('select a start date, end date and crff report type to continue');
               }
               
        }

        self.onLoadCRRFReportSuccessful = self.onLoadCRRFReportSuccessful || function (data) {
            $scope.fetchedEntities = data.results[0];
            var resultReturn = $scope.fetchedEntities;
            localStorage.setItem("resultReturn", resultReturn);
            console.log(data.results[0]);
            var cat = $scope.category; 
            console.log(cat);
            
            if(cat == "ARV Cotrim"){
            	var final_url = ROOT_URL + 'openhmis.inventory/crrfReports/preview.page#/';
            }
            if(cat == "HIV RTKS and DBS"){
            	var final_url = ROOT_URL + 'openhmis.inventory/crrfReports/preview_rtk.page#/';
            }
            if(cat == "Other OIs"){
            	var final_url = ROOT_URL + 'openhmis.inventory/crrfReports/preview_oi.page#/';
            }            
            localStorage.setItem("preview_url", JSON.stringify(data.results[0]));        
            window.location = final_url;
        }

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericManageController, self, {
            $scope: $scope,
            $filter: $filter,
            EntityRestFactory: EntityRestFactory,
            PaginationService: PaginationService,
            CssStylesFactory: CssStylesFactory,
            GenericMetadataModel: CrrfReportsModel,
            CookiesService: CookiesService
        });
    }
    
})();
