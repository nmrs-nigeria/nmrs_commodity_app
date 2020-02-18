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

    var app = angular.module('app.consumptionFunctionsFactory', []);
    app.service('ConsumptionFunctions', ConsumptionFunctions);

    ConsumptionFunctions.$inject = ['EntityFunctions','$filter'];

    function ConsumptionFunctions(EntityFunctions,$filter) {
        var service;

        service = {
            // editItemCode : editItemCode,
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
            messages['openhmis.inventory.item.name'] =
                    emr.message('openhmis.inventory.item.name');



            messages['openhmis.inventory.stockroom.name'] = emr
                    .message('openhmis.inventory.stockroom.name');

            messages['openhmis.commons.general.add'] = emr
                    .message('openhmis.commons.general.add');
            messages['openhmis.commons.general.edit'] = emr
                    .message('openhmis.commons.general.edit');
            return messages;
        }


//        function formatDate(date, includeTime) {
//            var format = 'dd-MM-yyyy';
//            if (includeTime) {
//                format += ' HH:mm';
//            }
//            return ($filter('date')(new Date(date), format));
//        }

        function formatDate(date) {
            return $filter('date')(new Date(date), "dd-MM-yyyy");
        }

        function formatTime(time) {
            var format = 'HH:mm';
            return ($filter('date')(new Date(time), format));
        }


//
//        function changeConsumptionDate($scope) {
//
//            var operationDate = angular.element(document.getElementById('consumptionDateId-field'))[0].value;
//
//            $scope.operationDate = formatDate(new Date(operationDate));
//
//            $scope.$apply();
//
//         //   EntityFunctions.disableBackground();
//        }
//        

        function onChangeDatePicker(id, successfulCallback) {
            var datePicker = angular.element(document.getElementById(id));
            datePicker.bind('keyup change select checked', function () {
                var input = this.value;
                successfulCallback(input);
            });
        }

    }
})();
