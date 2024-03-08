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

    var app = angular.module('app.stockOperationFunctionsFactory', []);
    app.service('CreateOperationFunctions', CreateOperationFunctions);


    var NOT_DEFINED = ' - Not Defined - ';

    CreateOperationFunctions.$inject = ['EntityFunctions', '$filter'];

    function CreateOperationFunctions(EntityFunctions, $filter) {
        var service;

        service = {
            formatDate: formatDate,
            formatDateTwo: formatDateTwo,
            formatTime: formatTime,
            onChangeDatePicker: onChangeDatePicker,
            changeOperationDate: changeOperationDate,
            changeWarningDialog: changeWarningDialog,
            createExpirationDates: createExpirationDates,
            showOperationItemsSection: showOperationItemsSection,
            checkDatePickerExpirationSection: checkDatePickerExpirationSection,
            validateAttributeTypes: validateAttributeTypes,
            validateLineItems: validateLineItems,
            validatePatient: validatePatient,
            validateOperationNumber: validateOperationNumber,
            populateOccurDates: populateOccurDates,
            calculateSumItemStockDetailQuantities: calculateSumItemStockDetailQuantities,
            checkBatchItemExistSection: checkBatchItemExistSection,
            createItemBatches: createItemBatches,
            resolveLga: resolveLga,
            validateExpirationAndOperationDate: validateExpirationAndOperationDate,
        };

        return service;

        function formatDate(date, includeTime) {
            var format = 'dd-MM-yyyy';
            if (includeTime) {
                format += ' HH:mm';
            }
            return ($filter('date')(new Date(date), format));
        }
        
        function formatDateTwo(date) {
            var dates = date.split("-");
            var day = dates[0];
            var month = dates[1];
            var year = dates[2]; 
            var splitYear = year.split(" ");
            if(splitYear.length > 1){
                year = splitYear[0];
            }
            return [year, month, day].join('-');
        }

        function formatTime(time) {
            var format = 'HH:mm';
            return ($filter('date')(new Date(time), format));
        }

        function onChangeDatePicker(successfulCallback, id, lineItem) {
            var picker;
            if (id !== undefined) {
                picker = angular.element(document.getElementById(id));
                picker.bind('keyup change select', function () {
                    successfulCallback(this.value);
                });
            } else {
                var elements = angular.element(document.getElementsByTagName("input"));
                for (var i = 0; i < elements.length; i++) {
                    var element = elements[i];
                    if (element.id.indexOf("operationDate") == -1 &&
                            element.id.indexOf("display") > -1 && (element.id !== lineItem.id)) {
                        if (lineItem !== undefined) {
                            element.id = lineItem.id;
                        }

                        picker = angular.element(element);
                        picker.bind('keyup change select', function () {
                            lineItem.itemStockExpirationDate = formatDate(new Date(this.value));
                        });
                    }
                }
            }
        }

        function changeOperationDate($scope) {
            var dialog = emr.setupConfirmationDialog({
                selector: '#change-operation-date-dialog',
                actions: {
                    confirm: function () {
                        var operationDate = angular.element(document.getElementById('operationDateId-field'))[0].value;
                        if ($scope.operationOccurDate !== undefined) {
                            var time = $scope.operationOccurDate.time;
                            operationDate += ', ' + time;
                            $scope.operationDate = formatDate(new Date(operationDate), true);
                        } else {
                            $scope.operationDate = formatDate(new Date(operationDate));
                        }
                        $scope.$apply();
                        dialog.close();
                    },
                    cancel: function () {
                        dialog.close();
                    }
                }
            });

            dialog.show();

            EntityFunctions.disableBackground();
        }

        /**
         * Display a warning dialog box
         * @param $scope
         */
        function changeWarningDialog($scope, newVal, oldVal, source) {
            if (source === 'operationType') {
                $scope.operationType = JSON.parse(oldVal);
                $scope.warningTitle = emr.message('openhmis.inventory.operations.confirm.title.operationTypeChange');
                $scope.warningMessage = emr.message('openhmis.inventory.operations.confirm.operationTypeChange');
            } else if (source === 'stockroom') {
                $scope.sourceStockroom = JSON.parse(oldVal);
                $scope.warningTitle = emr.message('openhmis.inventory.operations.confirm.title.sourceStockroomChange');
                $scope.warningMessage = emr.message('openhmis.inventory.operations.confirm.sourceStockroomChange');
            }

            var dialog = emr.setupConfirmationDialog({
                selector: '#warning-dialog',
                actions: {
                    confirm: function () {
                        if (source === 'operationType') {
                            $scope.operationType = newVal;
                        } else if (source === 'stockroom') {
                            $scope.sourceStockroom = newVal;
                        }

                        $scope.lineItems = [];
                        $scope.addLineItem();

                        if (source === 'operationType') {
                            $scope.loadOperationTypeAttributes();
                        }

                        $scope.$apply();

                        dialog.close();
                    },
                    cancel: function () {
                        dialog.close();
                    }
                }
            });

            dialog.show();

            EntityFunctions.disableBackground();
        }

        /**
         * Create Item Stock Line Items expiration dates.
         * @param itemStocks
         * @returns {Array}
         */
        function createExpirationDates(itemStocks) {
            var itemStockExpirationDates = [];
            //create expiration date drop-down
            itemStockExpirationDates.push("Auto");
            if (itemStocks !== null && itemStocks.length > 0) {
                var itemStock = itemStocks[0];
                if (itemStock !== null && "details" in itemStock) {
                    var nullExpiration = false;
                    for (var i = 0; i < itemStock.details.length; i++) {
                        var detail = itemStock.details[i];
                        var expiration = detail.expiration;
                        if (expiration !== null) {
                            expiration = expiration.split("T")[0];
                            expiration = formatDate(expiration);
                        } else {
                            nullExpiration = true;
                            continue;
                        }

                        if (itemStockExpirationDates.indexOf(expiration) === -1) {
                            itemStockExpirationDates.push(expiration);
                        }
                    }
                    if (nullExpiration) {
                        itemStockExpirationDates.push("None");
                    }
                }
            }
            return itemStockExpirationDates;
        }

        function createItemBatches(itemStocks) {
            var itemBatches = [];
            //create expiration date drop-down
            //   itemBatches.push("Auto");
            if (itemStocks !== null && itemStocks.length > 0) {
                var itemStock = itemStocks[0];
                if (itemStock !== null && "details" in itemStock) {
                    var nullIemBatch = false;
                    for (var i = 0; i < itemStock.details.length; i++) {
                        var detail = itemStock.details[i];
                        var itemBatch = detail.itemBatch;
                        if (itemBatch !== null) {

                            // nothing for now
                        } else {
                            nullIemBatch = true;
                            continue;
                        }

                        if (itemBatches.indexOf(itemBatch) === -1) {
                            itemBatches.push(itemBatch);
                        }
                    }
                    if (nullIemBatch) {
                        itemBatches.push("None");
                    }
                }
            }
            return itemBatches;
        }

        function showOperationItemsSection($scope) {
            var operationType = $scope.operationType;
            if (operationType === undefined) {
                return false;
            } else if (operationType.hasRecipient && !operationType.hasSource && !operationType.hasDestination) {
                return true;
            } else if (operationType.hasSource && operationType.hasDestination) {
                if ($scope.sourceStockroom.name !== NOT_DEFINED && $scope.destinationStockroom.name !== NOT_DEFINED) {
                    return true;
                }
            } else if (operationType.hasSource || operationType.hasDestination) {
                if (operationType.hasSource) {
                    if ($scope.sourceStockroom !== undefined && $scope.sourceStockroom.name !== NOT_DEFINED) {
                        return true;
                    }
                } else {
                    if ((operationType.hasDestination && $scope.destinationStockroom.name !== NOT_DEFINED)) {
                        return true;
                    }
                }
            }
            return false;
        }

        function checkDatePickerExpirationSection(lineItem, $scope) {
            if (lineItem !== undefined && lineItem.itemStockHasExpiration) {
                if ($scope.operationType.name === 'Receipt' || $scope.operationType.name === 'Return' ||
                        $scope.operationType.name === 'Transfer-In' ||
                        $scope.operationType.name === 'Initial' || $scope.operationType.name === 'Inicial' ||
                        $scope.operationType.name === 'Recibo' || $scope.operationType.name === 'Retorno') {
                    lineItem.setExpirationHasDatePicker(true);
                }
            }
        }

        function checkBatchItemExistSection(lineItem, $scope) {
            if (lineItem !== undefined && lineItem.itemStockHasBatch) {
                if ($scope.operationType.name === 'Receipt' || $scope.operationType.name === 'Return' ||
                    $scope.operationType.name === 'Transfer-In' ||
                        $scope.operationType.name === 'Initial' || $scope.operationType.name === 'Inicial' ||
                        $scope.operationType.name === 'Recibo' || $scope.operationType.name === 'Retorno') {
                    lineItem.setBatchItemExist(true);
                }
            }
        }

        function validateAttributeTypes($scope) {
            // validate attribute types
            var requestAttributeTypes = [];
            if (EntityFunctions.validateAttributeTypes($scope.attributeTypeAttributes, $scope.attributes, requestAttributeTypes)) {
                $scope.entity.attributes = requestAttributeTypes;
                return true;
            }

            $scope.submitted = true;
            return false;
        }

        function validateOperationLineItems(lineItems, validatedItems) {
            if (lineItems !== undefined) {
                var failed = false;
                for (var i = 0; i < lineItems.length; i++) {
                    var lineItem = lineItems[i];
                    if (lineItem.selected) {
                        if (lineItem.itemStock.name === undefined) {
                            var errorMessage =
                                    emr.message("openhmis.inventory.operations.error.invalidItem") + " - " + lineItem.itemStock.toString();
                            emr.errorAlert(errorMessage);
                            failed = true;
                            lineItem.invalidEntry = true;
                            continue;
                        }

                        if (lineItem.itemStockQuantity === 0 ||
                                lineItem.itemStockQuantity === undefined ||
                                lineItem.itemStockQuantity === null) {
                            var errorMessage =
                                    emr.message("openhmis.inventory.operations.error.itemError") + " - " + lineItem.itemStock.name;
                            emr.errorAlert(errorMessage);
                            failed = true;
                            continue;
                        }

                        var calculatedExpiration;
                        var dateNotRequired = true;
                        var expiration = lineItem.itemStockExpirationDate;
                        if (lineItem.itemStockHasExpiration) {
                            if (expiration === undefined || expiration === "") {
                                var expDate = jQuery("#" + lineItem.id).val();
                                if (expDate !== undefined && expDate !== "") {
                                    expiration = formatDate(new Date(expDate));
                                    calculatedExpiration = true;
                                } else {
                                    dateNotRequired = false;
                                }
                            } else if (expiration === 'None') {
                                calculatedExpiration = false;
                                expiration = undefined;
                            } else if (expiration === 'Auto') {
                                calculatedExpiration = true;
                                expiration = undefined;
                            } else {
                                calculatedExpiration = true;
                            }
                        } else {
                            calculatedExpiration = false;
                            expiration = undefined;
                        }

                        if (dateNotRequired) {
                            var item = {
                                calculatedExpiration: calculatedExpiration,
                                item: lineItem.itemStock.uuid,
                                quantity: lineItem.itemStockQuantity,
                                itemBatch: lineItem.itemStockBatch,
                                itemDrugType:lineItem.itemDrugType,
                            };

                            if (expiration !== undefined) {
                                item['expiration'] = expiration;
                            }

                            validatedItems.push(item);
                        } else {
                            var errorMessage =
                                    emr.message("openhmis.inventory.operations.error.expiryDate") + " - " + lineItem.itemStock.name;
                            emr.errorAlert(errorMessage);
                            failed = true;
                            continue;
                        }
                    } else if (lineItem.itemStock !== "") {
                        var errorMessage =
                                emr.message("openhmis.inventory.operations.error.invalidItem") + " - " + lineItem.itemStock.toString();
                        emr.errorAlert(errorMessage);
                        lineItem.invalidEntry = true;
                        failed = true;
                    }
                }
            }

            if (validatedItems.length == 0 && !failed) {
                emr.errorAlert("openhmis.inventory.operations.error.itemQuantity");
            } else if (validatedItems.length > 0 && !failed) {
                return true;
            }

            return false;
        }

        function validateExpirationAndOperationDate($scope) {
            var lineItems = $scope.lineItems;
            var operationDate = $scope.operationDate;
            var operationType = $scope.operationType;
            var failed = false;
            if (lineItems !== undefined && operationDate !== undefined && operationType.name !== 'Disposed') {
                for (var i = 0; i < lineItems.length; i++) {
                    var lineItem = lineItems[i];
                    if (lineItem.selected) {                    
                          //format expiration and operation date
                          var exp = lineItem.itemStockExpirationDate;
                          var expiratn  = new Date(formatDateTwo(exp));
                          var operatDate = new Date(formatDateTwo(operationDate));
                          if (expiratn < operatDate) {
                              var errorMessage = emr.message("The expiration date cannot be less than operation date.") + " - " + exp;
                              emr.errorAlert(errorMessage);
                              failed = true;
                              continue;                         
                          }                                     
                    } 
                }
            }
            if (failed) {
                return false;
            }          
            return true;
        }


        function validateLineItems($scope) {
            var validatedItems = [];
            if (validateOperationLineItems($scope.lineItems, validatedItems)) {
                $scope.entity.items = validatedItems;
                return true;
            }

            $scope.submitted = true;
            return false;
        }

        // validate patient
        function validatePatient($scope) {
            if ($scope.operationType.hasRecipient) {
                if ($scope.selectedPatient !== '') {
                    $scope.entity.patient = $scope.selectedPatient.uuid;
                    $scope.entity.institution = '';
                    $scope.entity.department = '';
                } else {
                    $scope.submitted = true;
                    emr.errorAlert("openhmis.commons.general.requirePatient");
                    return false;
                }
            }
            return true;
        }

        // validate operation number
        function validateOperationNumber($scope) {
            if ($scope.entity.operationNumber === undefined ||
                    $scope.entity.operationNumber === '') {
                $scope.submitted = true;
                emr.errorAlert("openhmis.inventory.operations.error.number");
                return false;
            }
            return true;
        }

        function populateOccurDates($scope, operations) {
            $scope.operationOccurs = [];
            if (operations !== null && operations.length > 0) {
                var tempDate;
                for (var i = 0; i < operations.length; i++) {
                    var operation = operations[i];
                    var operationDate = new Date(operation.operationDate);
                    var display = formatTime(new Date(operationDate));

                    var operationDate = {};
                    operationDate['time'] = display;
                    operationDate['name'] =
                            emr.message("openhmis.inventory.operations.before") + " "
                            + operation.operationNumber + " (" + display + ")";
                    $scope.operationOccurs.push(operationDate);
                    tempDate = display;
                }

                if ($scope.operationOccurs.length > 0) {
                    var operationDate = {};
                    operationDate['time'] = tempDate;
                    operationDate['name'] = emr.message("openhmis.inventory.operations.afterLastOperation");
                    $scope.operationOccurs.push(operationDate);
                }
            }
        }

        function calculateSumItemStockDetailQuantities(itemStockDetails) {
            var totalQuantity = 0;
            for (var i = 0; i < itemStockDetails.length; i++) {
                totalQuantity += itemStockDetails[i].quantity;
            }

            return totalQuantity;
        }


        function resolveLga($scope, state) {
            console.log('triggered state switcg');

            switch (state) {
                case "Abia":
                    var data = ['Select item...', 'Aba North', 'Aba South', 'Arochukwu', 'Bende', 'Ikwuano', 'Isiala Ngwa North', 'Isiala Ngwa South', 'Isuikwuato', 'Obi Ngwa', 'Ohafia', 'Osisioma', 'Ugwunagbo', 'Ukwa East', 'Ukwa West', 'Umuahia North', 'muahia South', 'Umu Nneochi'];
                    break;

                case "Adamawa":
                    var data = ['Select item...', 'Demsa', 'Fufure', 'Ganye', 'Gayuk', 'Gombi', 'Grie', 'Hong', 'Jada', 'Larmurde', 'Madagali', 'Maiha', 'Mayo Belwa', 'Michika', 'Mubi North', 'Mubi South', 'Numan', 'Shelleng', 'Song', 'Toungo', 'Yola North', 'Yola South'];
                    break;
                case "AkwaIbom":
                    var data = ['Select item...', 'Abak', 'Eastern Obolo', 'Eket', 'Esit Eket', 'Essien Udim', 'Etim Ekpo', 'Etinan', 'Ibeno', 'Ibesikpo Asutan', 'Ibiono-Ibom', 'Ika', 'Ikono', 'Ikot Abasi', 'Ikot Ekpene', 'Ini', 'Itu', 'Mbo', 'Mkpat-Enin', 'Nsit-Atai', 'Nsit-Ibom', 'Nsit-Ubium', 'Obot Akara', 'Okobo', 'Onna', 'Oron', 'Oruk Anam', 'Udung-Uko', 'Ukanafun', 'Uruan', 'Urue-Offong Oruko', 'Uyo'];
                    break;
                case "Anambra":
                    var data = ['Select item...', 'Aguata', 'Anambra East', 'Anambra West', 'Anaocha', 'Awka North', 'Awka South', 'Ayamelum', 'Dunukofia', 'Ekwusigo', 'Idemili North', 'Idemili South', 'Ihiala', 'Njikoka', 'Nnewi North', 'Nnewi South', 'Ogbaru', 'Onitsha North', 'Onitsha South', 'Orumba North', 'Orumba South', 'Oyi'];
                    break;

                case "Anambra":
                    var data = ['Select item...', 'Aguata', 'Anambra East', 'Anambra West', 'Anaocha', 'Awka North', 'Awka South', 'Ayamelum', 'Dunukofia', 'Ekwusigo', 'Idemili North', 'Idemili South', 'Ihiala', 'Njikoka', 'Nnewi North', 'Nnewi South', 'Ogbaru', 'Onitsha North', 'Onitsha South', 'Orumba North', 'Orumba South', 'Oyi'];
                    break;
                case "Bauchi":
                    var data = ['Select item...', 'Alkaleri', 'Bauchi', 'Bogoro', 'Damban', 'Darazo', 'Dass', 'Gamawa', 'Ganjuwa', 'Giade', 'Itas-Gadau', 'Jama are', 'Katagum', 'Kirfi', 'Misau', 'Ningi', 'Shira', 'Tafawa Balewa', ' Toro', ' Warji', ' Zaki'];

                    break;

                case "Bayelsa":
                    var data = ['Select item...', 'Brass', 'Ekeremor', 'Kolokuma Opokuma', 'Nembe', 'Ogbia', 'Sagbama', 'Southern Ijaw', 'Yenagoa'];

                    break;
                case "Benue":
                    var data = ['Select item...', 'Agatu', 'Apa', 'Ado', 'Buruku', 'Gboko', 'Guma', 'Gwer East', 'Gwer West', 'Katsina-Ala', 'Konshisha', 'Kwande', 'Logo', 'Makurdi', 'Obi', 'Ogbadibo', 'Ohimini', 'Oju', 'Okpokwu', 'Oturkpo', 'Tarka', 'Ukum', 'Ushongo', 'Vandeikya'];

                    break;
                case "Borno":
                    var data = ['Select item...', 'Abadam', 'Askira-Uba', 'Bama', 'Bayo', 'Biu', 'Chibok', 'Damboa', 'Dikwa', 'Gubio', 'Guzamala', 'Gwoza', 'Hawul', 'Jere', 'Kaga', 'Kala-Balge', 'Konduga', 'Kukawa', 'Kwaya Kusar', 'Mafa', 'Magumeri', 'Maiduguri', 'Marte', 'Mobbar', 'Monguno', 'Ngala', 'Nganzai', 'Shani'];

                    break;
                case "Cross River":
                    var data = ['Select item...', 'Abi', 'Akamkpa', 'Akpabuyo', 'Bakassi', 'Bekwarra', 'Biase', 'Boki', 'Calabar Municipal', 'Calabar South', 'Etung', 'Ikom', 'Obanliku', 'Obubra', 'Obudu', 'Odukpani', 'Ogoja', 'Yakuur', 'Yala'];

                    break;

                case "Delta":
                    var data = ['Select item...', 'Aniocha North', 'Aniocha South', 'Bomadi', 'Burutu', 'Ethiope East', 'Ethiope West', 'Ika North East', 'Ika South', 'Isoko North', 'Isoko South', 'Ndokwa East', 'Ndokwa West', 'Okpe', 'Oshimili North', 'Oshimili South', 'Patani', 'Sapele', 'Udu', 'Ughelli North', 'Ughelli South', 'Ukwuani', 'Uvwie', 'Warri North', 'Warri South', 'Warri South West'];

                    break;

                case "Ebonyi":
                    var data = ['Select item...', 'Abakaliki', 'Afikpo North', 'Afikpo South', 'Ebonyi', 'Ezza North', 'Ezza South', 'Ikwo', 'Ishielu', 'Ivo', 'Izzi', 'Ohaozara', 'Ohaukwu', 'Onicha'];
                    break;
                case "Edo":
                    var data = ['Select item...', 'Akoko-Edo', 'Egor', 'Esan Central', 'Esan North-East', 'Esan South-East', 'Esan West', 'Etsako Central', 'Etsako East', 'Etsako West', 'Igueben', 'Ikpoba Okha', 'Orhionmwon', 'Oredo', 'Ovia North-East', 'Ovia South-West', 'Owan East', 'Owan West', 'Uhunmwonde'];
                    break;

                case "Ekiti":
                    var data = ['Select item...', 'Ado Ekiti', 'Efon', 'Ekiti East', 'Ekiti South-West', 'Ekiti West', 'Emure', 'Gbonyin', 'Ido Osi', 'Ijero', 'Ikere', 'Ikole', 'Ilejemeje', 'Irepodun-Ifelodun', 'Ise-Orun', 'Moba', 'Oye'];
                    break;
                case "Rivers":
                    var data = ['Select item...', 'Port Harcourt', 'Obio/Akpor', 'Okrika', 'Ogu–Bolo', 'Eleme', 'Tai', 'Gokana', 'Khana', 'Oyigbo', 'Opobo–Nkoro', 'Andoni', 'Bonny', 'Degema', 'Asari-Toru', 'Akuku-Toru', 'Abua/Odual', 'Ahoada West', 'Ahoada East', 'Ogba/Egbema/Ndoni', 'Emuoha', 'Ikwerre', 'Etche', 'Omuma'];
                    break;
                case "Enugu":
                    var data = ['Select item...', 'Aninri', 'Awgu', 'Enugu East', 'Enugu North', 'Enugu South', 'Ezeagu', 'Igbo Etiti', 'Igbo Eze North', 'Igbo Eze South', 'Isi Uzo', 'Nkanu East', 'Nkanu West', 'Nsukka', 'Oji River', 'Udenu', 'Udi', 'Uzo Uwani'];
                    break;
                case "FCT":
                    var data = ['Select item...', 'Abaji', 'Bwari', 'Gwagwalada', 'Kuje', 'Kwali', 'AMAC'];
                    break;
                case "Gombe":
                    var data = ['Select item...', 'Akko', 'Balanga', 'Billiri', 'Dukku', 'Funakaye', 'Gombe', 'Kaltungo', 'Kwami', 'Nafada', 'Shongom', 'Yamaltu-Deba'];
                    break;
                case "Imo":
                    var data = ['Select item...', 'Aboh Mbaise', 'Ahiazu Mbaise', 'Ehime Mbano', 'Ezinihitte', 'Ideato North', 'Ideato South', 'Ihitte-Uboma', 'Ikeduru', 'Isiala Mbano', 'Isu', 'Mbaitoli', 'Ngor Okpala', 'Njaba', 'Nkwerre', 'Nwangele', 'Obowo', 'Oguta', 'Ohaji-Egbema', 'Okigwe', 'Orlu', 'Orsu', 'Oru East', 'Oru West', 'Owerri Municipal', 'Owerri North', 'Owerri West', 'Unuimo'];
                    break;
                case "Jigawa":
                    var data = ['Select item...', 'Auyo', 'Babura', 'Biriniwa', 'Birnin Kudu', 'Buji', 'Dutse', 'Gagarawa', 'Garki', 'Gumel', 'Guri', 'Gwaram', 'Gwiwa', 'Hadejia', 'Jahun', 'Kafin Hausa', 'Kazaure', 'Kiri Kasama', 'Kiyawa', 'Kaugama', 'Maigatari', 'Malam Madori', 'Miga', 'Ringim', 'Roni', 'Sule Tankarkar', 'Taura', 'Yankwashi'];
                    break;
                case "Kaduna":
                    var data = ['Select item...', 'Birnin Gwari', 'Chikun', 'Giwa', 'Igabi', 'Ikara', 'Jaba', 'Jema a', 'Kachia', 'Kaduna North', 'Kaduna South', 'Kagarko', 'Kajuru', 'Kaura', 'Kauru', 'Kubau', 'Kudan', 'Lere', 'Makarfi', 'Sabon Gari', 'Sanga', 'Soba', 'Zangon Kataf', 'Zaria'];
                    break;
                case "Kano":
                    var data = ['Select item...', 'Ajingi', 'Albasu', 'Bagwai', 'Bebeji', 'Bichi', 'Bunkure', 'Dala', 'Dambatta', 'Dawakin Kudu', 'Dawakin Tofa', 'Doguwa', 'Fagge', 'Gabasawa', 'Garko', 'Garun Mallam', 'Gaya', 'Gezawa', 'Gwale', 'Gwarzo', 'Kabo', 'Kano Municipal', 'Karaye', 'Kibiya', 'Kiru', 'Kumbotso', 'Kunchi', 'Kura', 'Madobi', 'Makoda', 'Minjibir', 'Nasarawa', 'Rano', 'Rimin Gado', 'Rogo', 'Shanono', 'Sumaila', 'Takai', 'Tarauni', 'Tofa', 'Tsanyawa', 'Tudun Wada', 'Ungogo', 'Warawa', 'Wudil'];
                    break;
                case "Katsina":
                    var data = ['Select item...', 'Bakori', 'Batagarawa', 'Batsari', 'Baure', 'Bindawa', 'Charanchi', 'Dandume', 'Danja', 'Dan Musa', 'Daura', 'Dutsi', 'Dutsin Ma', 'Faskari', 'Funtua', 'Ingawa', 'Jibia', 'Kafur', 'Kaita', 'Kankara', 'Kankia', 'Katsina', 'Kurfi', 'Kusada', 'Mai Adua', 'Malumfashi', 'Mani', 'Mashi', 'Matazu', 'Musawa', 'Rimi', 'Sabuwa', 'Safana', 'Sandamu', 'Zango'];
                    break;
                case "Kebbi":
                    var data = ['Select item...', 'Aleiro', 'Arewa Dandi', 'Argungu', 'Augie', 'Bagudo', 'Birnin Kebbi', 'Bunza', 'Dandi', 'Fakai', 'Gwandu', 'Jega', 'Kalgo', 'Koko Besse', 'Maiyama', 'Ngaski', 'Sakaba', 'Shanga', 'Suru', 'Wasagu Danko', 'Yauri', 'Zuru'];
                    break;
                case "Kogi":
                    var data = ['Select item...', 'Adavi', 'Ajaokuta', 'Ankpa', 'Bassa', 'Dekina', 'Ibaji', 'Idah', 'Igalamela Odolu', 'Ijumu', 'Kabba Bunu', 'Kogi', 'Lokoja', 'Mopa Muro', 'Ofu', 'Ogori Magongo', 'Okehi', 'Okene', 'Olamaboro', 'Omala', 'Yagba East', 'Yagba West'];
                    break;
                case "Kwara":
                    var data = ['Select item...', 'Asa', 'Baruten', 'Edu', 'Ekiti', 'Ifelodun', 'Ilorin East', 'Ilorin South', 'Ilorin West', 'Irepodun', 'Isin', 'Kaiama', 'Moro', 'Offa', 'Oke Ero', 'Oyun', 'Pategi'];
                    break;
                case "Lagos":
                    var data = ['Select item...', 'Agege', 'Ajeromi-Ifelodun', 'Alimosho', 'Amuwo-Odofin', 'Apapa', 'Badagry', 'Epe', 'Eti Osa', 'Ibeju-Lekki', 'Ifako-Ijaiye', 'Ikeja', 'Ikorodu', 'Kosofe', 'Lagos Island', 'Lagos Mainland', 'Mushin', 'Ojo', 'Oshodi-Isolo', 'Shomolu', 'Surulere'];
                    break;
                case "Nasarawa":
                    var data = ['Select item...', 'Akwanga', 'Awe', 'Doma', 'Karu', 'Keana', 'Keffi', 'Kokona', 'Lafia', 'Nasarawa', 'Nasarawa Egon', 'Obi', 'Toto', 'Wamba'];
                    break;
                case "Niger":
                    var data = ['Select item...', 'Agaie', 'Agwara', 'Bida', 'Borgu', 'Bosso', 'Chanchaga', 'Edati', 'Gbako', 'Gurara', 'Katcha', 'Kontagora', 'Lapai', 'Lavun', 'Magama', 'Mariga', 'Mashegu', 'Mokwa', 'Moya', 'Paikoro', 'Rafi', 'Rijau', 'Shiroro', 'Suleja', 'Tafa', 'Wushishi'];
                    break;
                case "Ogun":
                    var data = ['Select item...', 'Abeokuta North', 'Abeokuta South', 'Ado-Odo Ota', 'Egbado North', 'Egbado South', 'Ewekoro', 'Ifo', 'Ijebu East', 'Ijebu North', 'Ijebu North East', 'Ijebu Ode', 'Ikenne', 'Imeko Afon', 'Ipokia', 'Obafemi Owode', 'Odeda', 'Odogbolu', 'Ogun Waterside', 'Remo North', 'Sagamu','Yewa South','Yelwa North','Imeko-Afon'];
                    break;
                case "Ondo":
                    var data = ['Select item...', 'Akoko North-East', 'Akoko North-West', 'Akoko South-West', 'Akoko South-East', 'Akure North', 'Akure South', 'Ese Odo', 'Idanre', 'Ifedore', 'Ilaje', 'Ile Oluji-Okeigbo', 'Irele', 'Odigbo', 'Okitipupa', 'Ondo East', 'Ondo West', 'Ose', 'Owo'];
                    break;
                case "Osun":
                    var data = ['Select item...', 'Atakunmosa East', 'Atakunmosa West', 'Aiyedaade', 'Aiyedire', 'Boluwaduro', 'Boripe', 'Ede North', 'Ede South', 'Ife Central', 'Ife East', 'Ife North', 'Ife South', 'Egbedore', 'Ejigbo', 'Ifedayo', 'Ifelodun', 'Ila', 'Ilesa East', 'Ilesa West', 'Irepodun', 'Irewole', 'Isokan', 'Iwo', 'Obokun', 'Odo Otin', 'Ola Oluwa', 'Olorunda', 'Oriade', 'Orolu', 'Osogbo'];
                    break;
                case "Oyo":
                    var data = ['Select item...', 'Afijio', 'Akinyele', 'Atiba', 'Atisbo', 'Egbeda', 'Ibadan North', 'Ibadan North-East', 'Ibadan North-West', 'Ibadan South-East', 'Ibadan South-West', 'Ibarapa Central', 'Ibarapa East', 'Ibarapa North', 'Ido', 'Irepo', 'Iseyin', 'Itesiwaju', 'Iwajowa', 'Kajola', 'Lagelu', 'Ogbomosho North', 'Ogbomosho South', 'Ogo Oluwa', 'Olorunsogo', 'Oluyole', 'Ona Ara', 'Orelope', 'Ori Ire', 'Oyo', 'Oyo East', 'Saki East', 'Saki West', 'Surulere'];
                    break;
                case "Plateau":
                    var data = ['Select item...', 'Bokkos', 'Barkin Ladi', 'Bassa', 'Jos East', 'Jos North', 'Jos South', 'Kanam', 'Kanke', 'Langtang South', 'Langtang North', 'Mangu', 'Mikang', 'Pankshin', 'Qua an Pan', 'Riyom', 'Shendam', 'Wase'];
                    break;
                case "Sokoto":
                    var data = ['Select item...', 'Binji', 'Bodinga', 'Dange Shuni', 'Gada', 'Goronyo', 'Gudu', 'Gwadabawa', 'Illela', 'Isa', 'Kebbe', 'Kware', 'Rabah', 'Sabon Birni', 'Shagari', 'Silame', 'Sokoto North', 'Sokoto South', 'Tambuwal', 'Tangaza', 'Tureta', 'Wamako', 'Wurno', 'Yabo'];
                    break;
                case "Taraba":
                    var data = ['Select item...', 'Ardo Kola', 'Bali', 'Donga', 'Gashaka', 'Gassol', 'Ibi', 'Jalingo', 'Karim Lamido', 'Kumi', 'Lau', 'Sardauna', 'Takum', 'Ussa', 'Wukari', 'Yorro', 'Zing'];
                    break;
                case "Yobe":
                    var data = ['Select item...', 'Bade', 'Bursari', 'Damaturu', 'Fika', 'Fune', 'Geidam', 'Gujba', 'Gulani', 'Jakusko', 'Karasuwa', 'Machina', 'Nangere', 'Nguru', 'Potiskum', 'Tarmuwa', 'Yunusari', 'Yusufari'];
                    break;
                case "Zamfara":
                    var data = ['Select item...', 'Anka', 'Bakura', 'Birnin Magaji Kiyaw', 'Bukkuyum', 'Bungudu', 'Gummi', 'Gusau', 'Kaura Namoda', 'Maradun', 'Maru', 'Shinkafi', 'Talata Mafara', 'Chafe', 'Zurmi'];

            }
            $scope.lgas = data;

        }

    }
})();
