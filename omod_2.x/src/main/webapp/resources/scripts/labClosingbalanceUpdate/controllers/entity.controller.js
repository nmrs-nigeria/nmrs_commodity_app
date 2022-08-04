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
    base.controller("PharmacyStockTakeController", PharmacyStockTakeController);
    PharmacyStockTakeController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory',
        'PharmacyStockTakeModel', 'PharmacyStockTakeRestfulService', 'PaginationService', 'EntityFunctions', 'PharmacyStockTakeFunctions',
        'CookiesService'];

    function PharmacyStockTakeController($stateParams, $injector, $scope, $filter, EntityRestFactory, PharmacyStockTakeModel,
                                         PharmacyStockTakeRestfulService, PaginationService, EntityFunctions, PharmacyStockTakeFunctions,
                                         CookiesService) {
        var self = this;
        var entity_name_message_key = "openhmis.inventory.admin.stockTake.pharmacy";
        var REST_ENTITY_NAME = "inventoryClosingBalanceUpdateStockTake";

        // @Override
        self.setRequiredInitParameters = self.setRequiredInitParameters || function () {
            self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME, entity_name_message_key, INVENTORY_TASK_DASHBOARD_PAGE_URL);
            self.checkPrivileges(TASK_ACCESS_STOCK_TAKE_PAGE);
        }

        /**
         * Initializes and binds any required variable and/or function specific to entity.page
         * @type {Function}
         */
        // @Override
        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
            || function (uuid) {
                $scope.reportingPeriods = ["January-February","March-April","May-June","July-August","September-October","November-December"];
                $scope.years = ["2021","2022","2023","2024","2025","2026","2027","2028","2029","2030","2031","2032","2033","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"];
                $scope.reportingPeriod = $scope.reportingPeriods[0];
                $scope.year = $scope.years[0];
                // self.loadStockrooms();
                // self.loadDepartments();
                $scope.showNoStockroomSelected = true;
                $scope.showNoStockSummaries = false;
                $scope.showStockDetails = false;
                $scope.showStockChangeDetails = false;
                $scope.showStockDetailsTable = false;
                $scope.stockTakeDetails = [];

                $scope.loading = false;

                self.loadStockDetailsLabClosingBal('stockroomChange', 1);

                $scope.stockroomDialog = function (stockroomChange, stockTakeCurrentPage) {
                    console.log("start");
                    if ($scope.stockTakeDetails.length != 0) {
                        console.log("2");
                        $scope.stockTakeDetails = [];
                        self.stockroomChangeDialog(stockroomChange);
                    } else {
                        console.log("3");
                        $scope.loadStockDetails(stockTakeCurrentPage);
                    }
                }

                $scope.departmentDialog = function (departmentChange, stockTakeCurrentPage) {
                    console.log("Department Has been selected");
                    console.log("departmentChange: " +  departmentChange);
                    console.log("stockTakeCurrentPage: " +  stockTakeCurrentPage);
                    if ($scope.stockTakeDetails.length != 0) {
                        $scope.stockTakeDetails = [];
                        console.log("stockTakeDetails Details is not equal 0: Calling departmentChangeDialog function");
                        self.departmentChangeDialog(departmentChange);
                    } else {
                        console.log("stockTakeDetails Details is equal 0: Calling loadStockDetailsDepartment function");
                        $scope.loadStockDetailsDepartment(stockTakeCurrentPage);
                    }
                }

                $scope.loadStockDetailsDepartment = function (stockTakeCurrentPage) {
                    if ($scope.entity.department != null) {
                        var department_uuid = $scope.entity.department.uuid;
                        console.log("Inside loadStockDetailsDepartment (Department UUID): " + department_uuid);

                        self.loadStockDetailsDepartment(department_uuid, stockTakeCurrentPage);

                        $scope.stockTakeLimit = CookiesService.get(department_uuid + 'stockTakeLimit') || 5;
                        $scope.stockTakeCurrentPage = CookiesService.get(department_uuid + 'stockTakeCurrentPage') || 1;
                        $scope.stockTakePagingFrom = PaginationService.pagingFrom;
                        $scope.stockTakePagingTo = PaginationService.pagingTo;
                    } else {
                        $scope.showNoStockroomSelected = true;
                        $scope.showNoStockSummaries = false;
                        $scope.showStockChangeDetails = false;
                        $scope.stockTakeDetails = [];
                        $scope.showStockDetails = false;
                        $scope.showStockDetailsTable = false;
                    }
                }

                $scope.loadStockDetails = function (stockTakeCurrentPage) {
                    console.log("5");
                    if ($scope.entity.stockroom != null) {
                        console.log("6");
                        var stockroom_uuid = "2741bae2-c5de-43ef-891f-7ec2fd58f442";
                        self.loadStockDetails(stockroom_uuid, stockTakeCurrentPage);

                        $scope.stockTakeLimit = CookiesService.get(stockroom_uuid + 'stockTakeLimit') || 5;
                        $scope.stockTakeCurrentPage = CookiesService.get(stockroom_uuid + 'stockTakeCurrentPage') || 1;
                        $scope.stockTakePagingFrom = PaginationService.pagingFrom;
                        $scope.stockTakePagingTo = PaginationService.pagingTo;
                    } else {
                        console.log("7");
                        $scope.showNoStockroomSelected = true;
                        $scope.showNoStockSummaries = false;
                        $scope.showStockChangeDetails = false;
                        $scope.stockTakeDetails = [];
                        $scope.showStockDetails = false;
                        $scope.showStockDetailsTable = false;
                    }
                }

                $scope.showTableDetails = function () {
                    $scope.showStockDetailsTable = true;
                }

                $scope.hideTableDetails = function () {
                    $scope.showStockDetailsTable = false;
                }

                $scope.getLabStockQuantity = function (entity) {
                    console.log("labStockQuantity--");
                    console.log("labStockQuantity "+entity.labStockQuantity);
                    if (entity.labStockQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getActualQuantity = function (entity) {
                    console.log("ActualQuantity--");
                    console.log("ActualQuantity "+entity.actualQuantity);
                    if (entity.actualQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getAncQuantity = function (entity) {
                    console.log("ancQuantity--");
                    console.log("ancQuantity "+entity.ancQuantity);
                    if (entity.ancQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getComQuantity = function (entity) {
                    console.log("comQuantity--");
                    console.log("comQuantity "+entity.comQuantity);
                    if (entity.comQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getEidQuantity = function (entity) {
                    console.log("eidQuantity--");
                    console.log("eidQuantity "+entity.eidQuantity);
                    if (entity.eidQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getEmergQuantity = function (entity) {
                    console.log("emergQuantity--");
                    console.log("emergQuantity "+entity.emergQuantity);
                    if (entity.emergQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getFpQuantity = function (entity) {
                    console.log("fpQuantity--");
                    console.log("fpQuantity "+entity.fpQuantity);
                    if (entity.fpQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getInpatQuantity = function (entity) {
                    console.log("inpatQuantity--");
                    console.log("inpatQuantity "+entity.inpatQuantity);
                    if (entity.inpatQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getLanddQuantity = function (entity) {
                    console.log("landdQuantity--");
                    console.log("landdQuantity "+entity.landdQuantity);
                    if (entity.landdQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getLabQuantity = function (entity) {
                    console.log("labQuantity--");
                    console.log("labQuantity "+entity.labQuantity);
                    if (entity.labQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getMalQuantity = function (entity) {
                    console.log("malQuantity--");
                    console.log("malQuantity "+entity.malQuantity);
                    if (entity.malQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getMobQuantity = function (entity) {
                    console.log("mobQuantity--");
                    console.log("mobQuantity "+entity.mobQuantity);
                    if (entity.mobQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getOpdQuantity = function (entity) {
                    console.log("opdQuantity--");
                    console.log("opdQuantity "+entity.opdQuantity);
                    if (entity.opdQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getOssQuantity = function (entity) {
                    console.log("ossQuantity--");
                    console.log("ossQuantity "+entity.ossQuantity);
                    if (entity.ossQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getOthQuantity = function (entity) {
                    console.log("othQuantity--");
                    console.log("othQuantity "+entity.othQuantity);
                    if (entity.othQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getPaedQuantity = function (entity) {
                    console.log("paedQuantity--");
                    console.log("paedQuantity "+entity.paedQuantity);
                    if (entity.paedQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getPpQuantity = function (entity) {
                    console.log("ppQuantity--");
                    console.log("ppQuantity "+entity.ppQuantity);
                    if (entity.ppQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getStiQuantity = function (entity) {
                    console.log("stiQuantity--");
                    console.log("stiQuantity "+entity.stiQuantity);
                    if (entity.stiQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getTbQuantity = function (entity) {
                    console.log("tbQuantity--");
                    console.log("tbQuantity "+entity.tbQuantity);
                    if (entity.tbQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }

                $scope.getVctQuantity = function (entity) {
                    console.log("vctQuantity--");
                    console.log("vctQuantity "+entity.vctQuantity);
                    if (entity.vctQuantity >= 0) {

                        entity.id = entity.item.uuid;
                        self.getNewStock(entity);
                    }
                }





        }

        self.stockroomChangeDialog = self.stockroomChangeDialog || function (id) {
            console.log("4");
            PharmacyStockTakeFunctions.stockroomChangeDialog(id, $scope);
        }

        self.getNewStock = self.getNewStock || function (newStock) {
            console.log("stockTakeDetails--- "+$scope.stockTakeDetails);
            var index = EntityFunctions.findIndexByKeyValue($scope.stockTakeDetails,newStock.id);
            if (index < 0 ) {

                $scope.stockTakeDetails.push(newStock);
            } else {
                $scope.stockTakeDetails[index] = newStock;
            }

            /*
            * This loop is to remove any stock that had the actualQuantity updated and before saving changed again to either a value
            * equal to null or a value equal to the quantity
            * */
            // for (var i = 0; i < $scope.stockTakeDetails.length; i++) {
            //     console.log("stockTakeDetails"+$scope.stockTakeDetails[i].actualQuantity);
            //     if ($scope.stockTakeDetails[i].actualQuantity == $scope.stockTakeDetails[i].quantity || $scope.stockTakeDetails[i].actualQuantity == null) {
            //         $scope.stockTakeDetails.splice(i, 1);
            //     }
            // }

            if ($scope.stockTakeDetails.length > 0) {
                $scope.showStockChangeDetails = true;
            } else {
                $scope.showStockDetailsTable = false;
            }
            $scope.stockTakeDetails = $filter('orderBy')($scope.stockTakeDetails, ['item.name']);
        }

        self.loadStockrooms = self.loadStockrooms || function () {
            PharmacyStockTakeRestfulService.loadStockroomsCRRF(INVENTORY_MODULE_NAME, self.onLoadStockroomsSuccessful);
        }

        self.loadDepartments = self.loadDepartments || function () {
            console.log("loadDepartments restful service");
            PharmacyStockTakeRestfulService.loadDepartments(INVENTORY_MODULE_NAME, self.onLoadDepartmentsSuccessful);
        }

        self.loadStockDetails = self.loadStockDetails || function (stockroomUuid, stockTakeCurrentPage) {
            console.log("9");
            stockTakeCurrentPage = stockTakeCurrentPage || $scope.stockTakeCurrentPage;
            CookiesService.set(stockroomUuid + 'stockTakeCurrentPage', stockTakeCurrentPage);
            CookiesService.set(stockroomUuid + 'stockTakeLimit', $scope.stockTakeLimit);

            PharmacyStockTakeRestfulService.loadStockDetails(stockroomUuid, CookiesService.get(stockroomUuid + 'stockTakeCurrentPage'),
                CookiesService.get(stockroomUuid + 'stockTakeLimit'),
                self.onLoadStockDetailsSuccessful);
        }

        self.loadStockDetailsDepartment = self.loadStockDetailsDepartment || function (departmentUuid, stockTakeCurrentPage) {
            console.log("Inside loadStockDetailsDepartment 2: ");
            console.log("departmentUuid: " + departmentUuid);
            console.log("stockTakeCurrentPage: " + stockTakeCurrentPage);
            stockTakeCurrentPage = stockTakeCurrentPage || $scope.stockTakeCurrentPage;
            CookiesService.set(departmentUuid + 'stockTakeCurrentPage', stockTakeCurrentPage);
            CookiesService.set(departmentUuid + 'stockTakeLimit', $scope.stockTakeLimit);

            PharmacyStockTakeRestfulService.loadStockDetailsDepartment(departmentUuid, CookiesService.get(departmentUuid + 'stockTakeCurrentPage'),
                CookiesService.get(departmentUuid + 'stockTakeLimit'),
                self.onLoadStockDetailsSuccessful);
        }

        //callback
        self.onLoadStockroomsSuccessful = self.onLoadStockroomsSuccessful || function (data) {
            console.log("Log the CRRR Type");
            console.log(data.results);
            $scope.stockrooms = data.results;
        }

        self.onLoadDepartmentsSuccessful = self.onLoadDepartmentsSuccessful || function (data) {
            console.log("onLoadDepartmentsSuccessful: " + data.results);
            console.log("onLoadDepartmentsSuccessful: " + data.results[0]);
            $scope.departments = data.results;
        }


        self.loadStockDetailsLabClosingBal = function (stockroomChange, stockTakeCurrentPage) {
            if ($scope.stockTakeDetails.length != 0) {
                $scope.stockTakeDetails = [];
                self.stockroomChangeDialog(stockroomChange);
            } else {
                console.log(stockTakeCurrentPage);
                $scope.loadStockDetailsLabClosingBal(stockTakeCurrentPage);
            }
        }

        $scope.loadStockDetailsLabClosingBal = function (stockTakeCurrentPage) {
                var stockroom_uuid = "2741bae2-c5de-43ef-891f-7ec2fd58f442";
                self.loadStockDetails(stockroom_uuid, stockTakeCurrentPage);
                $scope.stockTakeLimit = CookiesService.get(stockroom_uuid + 'stockTakeLimit') || 200;
                $scope.stockTakeCurrentPage = CookiesService.get(stockroom_uuid + 'stockTakeCurrentPage') || 1;
                $scope.stockTakePagingFrom = PaginationService.pagingFrom;
                $scope.stockTakePagingTo = PaginationService.pagingTo;
        }


        self.onLoadStockDetailsSuccessful = self.onLoadStockDetailsSuccessful || function (data) {
            console.log(data.results);            
            $scope.fetchedEntities = data.results;
            console.log($scope.fetchedEntities);

            $scope.totalNumOfResults = data.length;

            if (data.results.length != 0) {
                $scope.showStockDetails = true;
                $scope.showNoStockroomSelected = false;
                $scope.showNoStockSummaries = false;
            } else {
                $scope.showNoStockroomSelected = false;
                $scope.showNoStockSummaries = true;
                $scope.showStockDetails = false;
            }
        }

        self.onChangeEntityError = self.onChangeEntityError || function (error) {
            emr.errorAlert(error);
            $scope.loading = false;
        }

        /**
         * All post-submit validations are done here.
         * @return boolean
         */
        // @Override
        self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function () {
       
            var stockObject = $scope.stockTakeDetails; 
            var stockroomTypeChoose = stockObject[0].item.itemType;
            var repPeriod = $scope.reportingPeriod;
            var repYear = $scope.year;
            
			for (var i = 0; i < stockObject.length; i++) {
				delete stockObject[i]['$$hashKey'];
				delete stockObject[i]['id'];					
				stockObject[i].item = stockObject[i].item.uuid;
			}
            var opNumber = repPeriod + "_" + repYear + "_" + stockroomTypeChoose;
            if ($scope.stockTakeDetails.length != 0) {
                $scope.entity = {
                    "itemStockSummaryList": stockObject,
                    "operationNumber": opNumber,
                    "stockroom": "5452ec3e-2fe1-46de-8a6e-28c6442e4cc0"
                };
                $scope.loading = true;
            } else {
                emr.errorAlert("openhmis.inventory.stocktake.adjustment.empty.error");
                return false;
            }

            return true;
        }

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericEntityController, self, {
            $scope: $scope,
            $filter: $filter,
            $stateParams: $stateParams,
            EntityRestFactory: EntityRestFactory,
            PaginationService: PaginationService,
            GenericMetadataModel: PharmacyStockTakeModel,
            EntityFunctions: EntityFunctions
        });
    }
})();
