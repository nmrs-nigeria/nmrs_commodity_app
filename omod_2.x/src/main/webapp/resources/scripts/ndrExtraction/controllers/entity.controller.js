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
(function () {
    'use strict';

    var base = angular.module('app.genericEntityController');
    base.controller("NdrExtractionController", NdrExtractionController);
    NdrExtractionController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'NdrExtractionModel', 'NdrExtractionRestfulService',
        'NdrExtractionFunctions', 'EntityRestFactory', 'CommonsRestfulFunctions'];

    function NdrExtractionController($stateParams, $injector, $scope, $filter, NdrExtractionModel, NdrExtractionRestfulService,
            NdrExtractionFunctions, EntityRestFactory, CommonsRestfulFunctions) {

        var self = this;

        var entity_name_message_key = "openhmis.inventory.report.name";
        var REST_ENTITY_NAME = "ndrextraction";

        // @Override
        self.setRequiredInitParameters = self.setRequiredInitParameters || function () {
            self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME, entity_name_message_key, RELATIVE_CANCEL_PAGE_URL);
            self.checkPrivileges(TASK_ACCESS_INVENTORY_REPORTS_PAGE);
        }

        /**
         * Initializes and binds any required variable and/or function specific
         * to report.page
         * 
         * @type {Function}
         */
        // @Override
        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope || function (uuid) {
            /* bind variables.. */
            //	self.loadStockRooms();

            $scope.searchReportItems = self.searchReportItems;

            // Load in the 5 reports from their string names in
            // ModuleSettings.java



            // Set change listeners for all datepickers used in
            // reports/entity.page

            NdrExtractionFunctions.onChangeDatePicker('startDate-display', function (value) {
                $scope.startDate = value;
            });

            NdrExtractionFunctions.onChangeDatePicker('endDate-display', function (value) {
                $scope.endDate = value;
            });
        }



        function printReport(reportFile) {
            //	var url = INVENTORY_REPORTS_PAGE_URL + "reportId=" + reportId + "&" + parameters;
            window.open(reportFile, "pdfDownload");
         //  window.location = reportFile;

            return false;
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
                        default:
                            break;
                    }
                    return false;
                }
            }
            console.log('check paramaters was successful');
            return true;
        }





        $scope.generateReport_ExpiringStock = function () {
            //   var stockroom = $scope.expiringStock_stockroom;
            var startDate = $scope.startDate;
            var endDate = $scope.endDate;

            var parametersAreValid = checkParameters({
                "startDate": startDate,
                "endDate": endDate
            });

            console.log('parametersvalid output');
            console.log(parametersAreValid);

            if (parametersAreValid) {
//				var reportId = $scope.expiringStockReport.reportId;
//				var parameters = "expiresBy=" + NdrExtractionFunctions.formatDate(expiryDate);
//				if (stockroom != null) {
//					parameters += "&stockroomId=" + stockroom.id;
//				}

                NdrExtractionRestfulService.getReport(NdrExtractionFunctions.formatDate(startDate), 
                NdrExtractionFunctions.formatDate(endDate), function (data) {
                    //	$scope.expiringStockReport = data;
                    console.log('logging error data');
                    console.log(data.error);
                    if(data.error !== undefined){
                      alert('error occurred\n'+data.error);  
                    }else{
                        return printReport(data.results); 
                    }
                   
                });


            } else {

                console.log("The start date is " + startDate);
                console.log("The end date is " + endDate);
                alert('select a start and end date to continue');
            }

        };

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericEntityController, self, {
            $scope: $scope,
            $filter: $filter,
            $stateParams: $stateParams,
            NdrExtractionRestfulService: NdrExtractionRestfulService,
            EntityRestFactory: EntityRestFactory,
            GenericMetadataModel: NdrExtractionModel
        });
    }
})();
