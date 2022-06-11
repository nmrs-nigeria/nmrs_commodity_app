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

    var app = angular.module('app.arvpharmacyDispenseFunctionsFactory', []);
    app.service('CrrfReportsFunctions', CrrfReportsFunctions);

    CrrfReportsFunctions.$inject = ['EntityFunctions','$filter'];

    function CrrfReportsFunctions(EntityFunctions,$filter) {
        var service;

        service = {
            addMessageLabels: addMessageLabels,
            formatDate: formatDate,
            formatTime: formatTime,
            onChangeDatePicker: onChangeDatePicker
        };

        return service;



        /**
         * All message labels used in the UI are defined here
         * @returns {{}}
         */
        function addMessageLabels() {
            var messages = {};

            messages['openhmis.inventory.department.name'] = emr
                    .message('openhmis.inventory.department.name');
            messages['openhmis.inventory.consumption.consumptionDate'] = emr
                    .message('openhmis.inventory.consumption.consumptionDate');
            messages['openhmis.inventory.consumption.quantity'] = emr
                    .message('openhmis.inventory.consumption.quantity');
            messages['openhmis.inventory.consumption.testingPoint'] = emr
                    .message('openhmis.inventory.consumption.testingPoint');
            messages['openhmis.inventory.consumption.wastage'] = emr
                    .message('openhmis.inventory.consumption.wastage');
             messages['openhmis.inventory.consumption.batchNumber'] = emr
                    .message('openhmis.inventory.consumption.batchNumber');
            messages['openhmis.inventory.item.name'] =
                    emr.message('openhmis.inventory.item.name');
            
            messages['openhmis.inventory.summary.quantityConsumed'] =
                    emr.message('openhmis.inventory.summary.quantityConsumed');
              messages['openhmis.inventory.summary.quantityReceived'] =
                    emr.message('openhmis.inventory.summary.quantityReceived');
             messages['openhmis.inventory.summary.startDate'] =
                    emr.message('openhmis.inventory.summary.startDate');
                  messages['openhmis.inventory.summary.endDate'] =
                    emr.message('openhmis.inventory.summary.endDate');
              messages['openhmis.inventory.summary.totalWastage'] =
                    emr.message('openhmis.inventory.summary.totalWastage');
            messages['openhmis.inventory.summary.stockBalance'] =
                    emr.message('openhmis.inventory.summary.stockBalance');


            messages['openhmis.inventory.stockroom.name'] = emr
                    .message('openhmis.inventory.stockroom.name');

            messages['openhmis.commons.general.add'] = emr
                    .message('openhmis.commons.general.add');
            messages['openhmis.commons.general.edit'] = emr
                    .message('openhmis.commons.general.edit');

                    messages['openhmis.inventory.summary.endDate'] =
                    emr.message('openhmis.inventory.summary.endDate');
              messages['openhmis.inventory.summary.totalWastage'] =
                    emr.message('openhmis.inventory.summary.totalWastage');
            messages['openhmis.inventory.summary.stockBalance'] =
                    emr.message('openhmis.inventory.summary.stockBalance');


            messages['openhmis.inventory.stockroom.name'] = emr
                    .message('openhmis.inventory.stockroom.name');

            messages['openhmis.commons.general.add'] = emr
                    .message('openhmis.commons.general.add');
            messages['openhmis.commons.general.edit'] = emr
                    .message('openhmis.commons.general.edit');


            messages['openhmis.inventory.patient.id'] =
                    emr.message('openhmis.inventory.patient.id');
            messages['openhmis.inventory.patient.category'] =
                    emr.message('openhmis.inventory.patient.category');
            messages['openhmis.inventory.treatment.type'] =
                    emr.message('openhmis.inventory.treatment.type');
            messages['openhmis.inventory.visit.type'] = emr
                    .message('openhmis.inventory.visit.type');
            messages['openhmis.inventory.pickupreason'] = emr
                    .message('openhmis.inventory.pickupreason');
            messages['openhmis.inventory.dateofdispense'] = emr
                    .message('openhmis.inventory.dateofdispense');
            messages['openhmis.inventory.pharmacyDispenseSummary.name'] = emr
                    .message('openhmis.inventory.pharmacyDispenseSummary.name');
 			messages['openhmis.inventory.admin.dispenseSummarys'] = emr
                    .message('openhmis.inventory.admin.dispenseSummarys');

            return messages;
        }

        function formatDate(date) {
            
            return $filter('date')(new Date(date), "yyyy-MM-dd");
        }

        function formatTime(time) {
            var format = 'HH:mm';
            return ($filter('date')(new Date(time), format));
        }

        function onChangeDatePicker(id, successfulCallback) {
            var datePicker = angular.element(document.getElementById(id));
            datePicker.bind('keyup change select checked', function () {
                var input = this.value;
                successfulCallback(input);
            });
        }

    }
})();
