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
    base.controller("CrrfReportsController", CrrfReportsController);
    CrrfReportsController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'CrrfReportsModel', 'CrrfReportsRestfulService',
        'CrrfReportsFunctions', 'EntityRestFactory', 'CommonsRestfulFunctions'];

    function CrrfReportsController($stateParams, $injector, $scope, $filter, CrrfReportsModel, CrrfReportsRestfulService,
            CrrfReportsFunctions, EntityRestFactory, CommonsRestfulFunctions) {

        var self = this;

        var entity_name_message_key = "openhmis.inventory.report.name";
        var REST_ENTITY_NAME = "crrfReports";

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

              // $scope.categories = ["Adult ART","Pediatric ART","OI Prophylaxis/Treatment","Anti-TB Drugs"];
              //  $scope.category = $scope.categories[0];
           // $scope.selectedCRRFTypes = ["ARV Cotrim", "HIV RTKS and DBS","Other OIs"];
             $scope.categories = ["ARV Cotrim", "HIV RTKS and DBS","Other OIs"];
             $scope.category = $scope.categories[0];

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

        $scope.generateReport_CRFFReport = function () {
            //   var stockroom = $scope.expiringStock_stockroom;
            var startDate = $scope.startDate;
            var endDate = $scope.endDate;
           // var selectedCRRFType = $scope.selectedCRRFType;
            var crrfCategory = $scope.category;

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

                CrrfReportsRestfulService.getReport("crrf_report", CrrfReportsFunctions.formatDate(startDate),
                        CrrfReportsFunctions.formatDate(endDate),crrfCategory, function (data) {
                    //	$scope.expiringStockReport = data;
                    console.log('logging error data');
                    console.log(data.error);
                    if (data.error !== undefined) {
                        $scope.loading = false;
                        alert('error occurred\n' + data.error);
                    } else {
                        $scope.loading = false;
                        console.log(JSON.stringify(data.results));
                        var final_url = ROOT_URL + 'openhmis.inventory/crrfReports/preview.page#/';
                        localStorage.setItem("preview_url", JSON.stringify(data.results));
                        window.location = final_url;

                        //  return printReport(data.results);
                    }

                });


            } else {
                $scope.loading = false;
                console.log("The start date is " + startDate);
                console.log("The end date is " + endDate);
                console.log("CRRF Report crrfCategory " +  crrfCategory);
                alert('select a start date, end date and crff report type to continue');
            }

        };





        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericEntityController, self, {
            $scope: $scope,
            $filter: $filter,
            $stateParams: $stateParams,
            CrrfReportsRestfulService: CrrfReportsRestfulService,
            EntityRestFactory: EntityRestFactory,
            GenericMetadataModel: CrrfReportsModel
        });
    }
})();
