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
    base.controller("PharmacyReportsController", PharmacyReportsController);
    PharmacyReportsController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'PharmacyReportsModel', 'PharmacyReportsRestfulService',
        'PharmacyReportsFunctions', 'EntityRestFactory', 'CommonsRestfulFunctions'];

    function PharmacyReportsController($stateParams, $injector, $scope, $filter, PharmacyReportsModel, PharmacyReportsRestfulService,
            PharmacyReportsFunctions, EntityRestFactory, CommonsRestfulFunctions) {

        var self = this;

        var entity_name_message_key = "openhmis.inventory.report.name";
        var REST_ENTITY_NAME = "pharmacyReports";

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



            PharmacyReportsFunctions.onChangeDatePicker('startDate-display', function (value) {
                $scope.startDate = value;
            });

            PharmacyReportsFunctions.onChangeDatePicker('endDate-display', function (value) {
                $scope.endDate = value;
            });

            PharmacyReportsFunctions.onChangeDatePicker('startDate_stc-display', function (value) {
                $scope.startDate_stc = value;
            });

            PharmacyReportsFunctions.onChangeDatePicker('endDate_stc-display', function (value) {
                $scope.endDate_stc = value;
            });

            PharmacyReportsFunctions.onChangeDatePicker('stockonhandDispensary_startDate-display', function (value) {
                $scope.stockonhandDispensary_startDate = value;
            });

            PharmacyReportsFunctions.onChangeDatePicker('stockonhandDispensary_endDate-display', function (value) {
                $scope.stockonhandDispensary_endDate = value;
            });

            PharmacyReportsFunctions.onChangeDatePicker('stockonhandStockroom_startDate-display', function (value) {
                $scope.stockonhandStockroom_startDate = value;
            });

            PharmacyReportsFunctions.onChangeDatePicker('stockonhandStockroom_endDate-display', function (value) {
                $scope.stockonhandStockroom_endDate = value;
            });
        }



        function printReport(reportFile) {
            //	var url = INVENTORY_REPORTS_PAGE_URL + "reportId=" + reportId + "&" + parameters;
            window.open(reportFile, "reportDownload");
            ///  window.location = reportFile;

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

        $scope.generateReport_DispensaryConsumption = function () {
            //   var stockroom = $scope.expiringStock_stockroom;
            var startDate = $scope.startDate;
            var endDate = $scope.endDate;

            console.log("Start Date: " + startDate);
            console.log("End Date: " + endDate);

            var parametersAreValid = checkParameters({
                "startDate": startDate,
                "endDate": endDate
            });

            console.log('parametersvalid output');
            console.log(parametersAreValid);

            if (parametersAreValid) {

                $scope.loading = true;

                PharmacyReportsRestfulService.getReport("dispensary_consumption", PharmacyReportsFunctions.formatDate(startDate),
                        PharmacyReportsFunctions.formatDate(endDate), function (data) {
                    //	$scope.expiringStockReport = data;
                    console.log('logging error data');
                    console.log(data.error);
                    if (data.error !== undefined) {
                        $scope.loading = false;
                        alert('error occurred\n' + data.error);
                    } else {
                        $scope.loading = false;
                        var final_url = ROOT_URL + 'openhmis.inventory/pharmacyReports/preview.page#/';
                        localStorage.setItem("preview_url", JSON.stringify(data.results));
                        window.location = final_url;

                        //  return printReport(data.results);
                    }

                });


            } else {
                $scope.loading = false;
                console.log("The start date is " + startDate);
                console.log("The end date is " + endDate);
                alert('select a start and end date to continue');
            }

        };

        $scope.generateReport_DispensaryStockOnHand = function () {
            //   var stockroom = $scope.expiringStock_stockroom;
            var startDate = $scope.stockonhandDispensary_startDate;
            var endDate = $scope.stockonhandDispensary_endDate;

            console.log("Start Date: " + startDate);
            console.log("End Date: " + endDate);

            var parametersAreValid = checkParameters({
                "startDate": startDate,
                "endDate": endDate
            });

            console.log('parametersvalid output');
            console.log(parametersAreValid);

            if (parametersAreValid) {

                PharmacyReportsRestfulService.getReport("dispensary_stockonhand", PharmacyReportsFunctions.formatDate(startDate),
                        PharmacyReportsFunctions.formatDate(endDate), function (data) {
                    //	$scope.expiringStockReport = data;
                    console.log('logging error data');
                    console.log(data.error);
                    if (data.error !== undefined) {
                        $scope.loading = false;
                        alert('error occurred\n' + data.error);
                    } else {
                        $scope.loading = false;
                        return printReport(data.results);
                    }

                });


            } else {
                $scope.loading = false;
                console.log("The start date is " + startDate);
                console.log("The end date is " + endDate);
                alert('select a start and end date to continue');
            }

        };

        $scope.generateReport_StockroomStockOnHand = function () {
            //   var stockroom = $scope.expiringStock_stockroom;
            var startDate = $scope.stockonhandStockroom_startDate;
            var endDate = $scope.stockonhandStockroom_endDate;

            console.log("Start Date: " + startDate);
            console.log("End Date: " + endDate);

            var parametersAreValid = checkParameters({
                "startDate": startDate,
                "endDate": endDate
            });

            console.log('parametersvalid output');
            console.log(parametersAreValid);

            if (parametersAreValid) {

                PharmacyReportsRestfulService.getReport("stockroom_stockonhand", PharmacyReportsFunctions.formatDate(startDate),
                        PharmacyReportsFunctions.formatDate(endDate), function (data) {
                    //	$scope.expiringStockReport = data;
                    console.log('logging error data');
                    console.log(data.error);
                    if (data.error !== undefined) {
                        $scope.loading = false;
                        alert('error occurred\n' + data.error);
                    } else {
                        $scope.loading = false;
                        return printReport(data.results);
                    }

                });


            } else {
                $scope.loading = false;
                console.log("The start date is " + startDate);
                console.log("The end date is " + endDate);
                alert('select a start and end date to continue');
            }

        };



        $scope.generateReport_StockroomConsumption = function () {
            //   var stockroom = $scope.expiringStock_stockroom;
            var startDate = $scope.startDate_stc;
            var endDate = $scope.endDate_stc;

            var parametersAreValid = checkParameters({
                "startDate": startDate,
                "endDate": endDate
            });

            console.log('parametersvalid output');
            console.log(parametersAreValid);

            if (parametersAreValid) {

                $scope.loading = true;

                PharmacyReportsRestfulService.getReport("stockroom_consumption", PharmacyReportsFunctions.formatDate(startDate),
                        PharmacyReportsFunctions.formatDate(endDate), function (data) {
                    //	$scope.expiringStockReport = data;
                    console.log('logging error data');
                    console.log(data.error);
                    if (data.error !== undefined) {
                        $scope.loading = false;
                        alert('error occurred\n' + data.error);
                    } else {
                        $scope.loading = false;
                        return printReport(data.results);
                    }

                });


            } else {
                $scope.loading = false;
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
            PharmacyReportsRestfulService: PharmacyReportsRestfulService,
            EntityRestFactory: EntityRestFactory,
            GenericMetadataModel: PharmacyReportsModel
        });
    }
})();
