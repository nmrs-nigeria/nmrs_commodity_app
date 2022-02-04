
(function () {
    'use strict';

    var base = angular.module('app.genericEntityController');
    base.controller("ConsumptionController", ConsumptionController);
    ConsumptionController.$inject = ['$stateParams', '$injector', '$scope', '$filter', 'EntityRestFactory',
        'ConsumptionModel', 'ConsumptionFunctions', 'ConsumptionRestfulService'];

    function ConsumptionController($stateParams, $injector, $scope, $filter, EntityRestFactory, ConsumptionModel, ConsumptionFunctions, ConsumptionRestfulService) {

        var self = this;

        var entity_name_message_key = "openhmis.inventory.consumption.name";
        var REST_ENTITY_NAME = "consumption";

        // @Override
        self.setRequiredInitParameters = self.setRequiredInitParameters || function () {
            self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME, entity_name_message_key, RELATIVE_CANCEL_PAGE_URL);
            self.checkPrivileges(TASK_MANAGE_METADATA);
        }

        /**
         * Initializes and binds any required variable and/or function specific to consumption.page
         * @type {Function}
         */
        // @Override
        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
                || function (uuid) {
                    /* bind variables.. */
                    //  $scope.itemPrice = {};
                    //   $scope.tmpItemPrice = {};

                    //     $scope.consumptionDate = ConsumptionFunctions.formatDate(new Date(), true);
                    //    $scope.consumptionDate = '';
                    // $scope.testTypes = ["screening Test","confirmatory test","tie breaker"];
                    $scope.quantity = '';
                    $scope.wastage = '';
                    $scope.batchNumber = '';
                    $scope.testPurposeTypes = ["Initial Screening", "Confirmation","Controls","Tie Breaker","Recency"];
                    $scope.uuid = uuid;
                    $scope.currDate = '';
                    $scope.itemBatchs = {};
                    $scope.changeItemBatch = self.changeItemBatch;

                    /* bind functions.. */

                    // retrieve stocks (if any) associated to the item





                    /* bind functions to scope */

                    // Set change listeners for all datepickers used in
                    // consumption/entity.page
                    ConsumptionFunctions.onChangeDatePicker('consumptionDate-display', function (value) {
                        $scope.consumptionDate = value;
                        console.log(value);
                    });

                  $scope.retireUnretire = self.retireUnretire;
                    $scope.delete = self.delete;

                    // call functions..
                    ConsumptionRestfulService.loadDepartments(self.onLoadDepartmentsSuccessful);
                    ConsumptionRestfulService.loadItems(self.onLoadItemsSuccessful);
                    // ItemRestfulService.loadItemStock($scope.uuid, self.onLoadItemStockSuccessful);


                    if ($scope.entity !== undefined) {
                        if ("department" in $scope.entity) {
                            $scope.department = $scope.entity.department;
                        }

                        if ("item" in $scope.entity) {
                            $scope.item = $scope.entity.item;
                        }
                    }



                }

        /**
         * All post-submit validations are done here.
         * @return boolean
         */
        // @Override
        self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function () {
            $scope.submitted = false;


             $scope.entity.dataSystem = 'emr';

            // validate quantity

            if (!angular.isDefined($scope.entity.quantity) || $scope.entity.quantity === '') {
                $scope.submitted = true;
                emr.errorAlert(emr.message("openhmis.commons.general.required.quantity"));
            }
            console.log("done with quantity");

            //validate item
            if (!angular.isDefined($scope.item) || $scope.item === '' || $scope.item === '=Select Item') {
                $scope.submitted = true;
                emr.errorAlert(emr.message("openhmis.commons.general.required.item"));
            }

            console.log("done with item");

            if ($scope.consumptionDate !== undefined) {
                // $scope.entity.consumptionDate = ConsumptionFunctions.formatDate($scope.consumptionDate);
               $scope.currDate = formatDate(new Date());

               console.log('currDate: '+$scope.currDate);
               var time = formatTime(new Date());
               // var time = $scope.currDate.time;
            console.log('the time is '+time);
            var tempDate = $scope.consumptionDate + ', ' + time;
            console.log('tempdate is :'+ tempDate);

                $scope.entity.consumptionDate = formatDate(tempDate);
            }

            console.log("done with consumption date");

            console.log("printing value of submit");

            console.log($scope.submitted);

            if ($scope.submitted) {
                return false;
            }



            //an empty wastage should resolve to null and not empty string
            if (!angular.isDefined($scope.entity.wastage) || $scope.entity.wastage === '') {
                $scope.entity.wastage = null;
            }



            // bind department uuid
            var department = $scope.department;
            if (angular.isDefined(department)) {
                $scope.entity.department = department.uuid;
                //  $scope.entity.department = department.departmentId;
            }

            //bind item uuid
            var item = $scope.item;
            if (angular.isDefined(item)) {
                $scope.entity.item = item.uuid;
                //  $scope.entity.item = item.itemId;
            }

            $scope.loading = true;
            return true;


        }

        function formatTime(time) {
			var format = 'HH:mm';
			return ($filter('date')(new Date(time), format));
		}

        function formatDate(date) {
            //  return $filter('date')(new Date(date), "yyyy-MM-dd");


         //   date = new Date(date);
            var format = 'yyyy-MM-dd';

            format += ' HH:mm';
            console.log('final date b4 the format: '+date);

            return ($filter('date')(new Date(date), format));
        }

        // @Override
        self.setAdditionalMessageLabels = self.setAdditionalMessageLabels || function () {
            return ConsumptionFunctions.addMessageLabels();
        }
        
           self.retireUnretire = self.retireUnretire || function(){         
            $scope.retireOrUnretireCall();
        }

        /* call-back functions. */
        // handle returned department list
        self.onLoadDepartmentsSuccessful = self.onLoadDepartmentsSuccessful || function (data) {
            if (angular.isDefined($scope.entity)) {
                $scope.departments = data.results;
                //$scope.department = $scope.department || $scope.departments[0];
                $scope.department = "=Select Item";
            }
        }

        /* call-back functions. */
        // handle returned item list
        self.onLoadItemsSuccessful = self.onLoadItemsSuccessful || function (data) {
            if (angular.isDefined($scope.entity)) {
                $scope.items = data.results;
                //$scope.item = $scope.item || $scope.items[0];
                $scope.item = "=Select Item";
            }
        }

        self.onLoadItemBatchSuccessful = self.onLoadItemBatchSuccessful || function (data) {
            console.log(data);
            console.log('ACTUAL RESULT');
            console.log(data.results);
            if(angular.isDefined($scope.entity)){
                $scope.itemBatchs = data.results;
            }
        }


         self.changeItemBatch = self.changeItemBatch || function (itemUUID) {
         ConsumptionRestfulService.getItemBatch(itemUUID,self.onLoadItemBatchSuccessful);
         ConsumptionRestfulService.setBaseUrl(INVENTORY_MODULE_NAME);
        }




        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericEntityController, self, {
            $scope: $scope,
            $filter: $filter,
            $stateParams: $stateParams,
            EntityRestFactory: EntityRestFactory,
            GenericMetadataModel: ConsumptionModel
        });
    }
})();
