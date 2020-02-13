
(function() {
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
        self.setRequiredInitParameters = self.setRequiredInitParameters || function() {
                self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME, entity_name_message_key, RELATIVE_CANCEL_PAGE_URL);
                self.checkPrivileges(TASK_MANAGE_METADATA);
            }

        /**
         * Initializes and binds any required variable and/or function specific to item.page
         * @type {Function}
         */
        // @Override
        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope
            || function(uuid) {
                /* bind variables.. */
              //  $scope.itemPrice = {};
             //   $scope.tmpItemPrice = {};
             
                $scope.consumptionDate = '';
                $scope.testTypes = ["screening Test","confirmatory test","tie breaker"];
                $scope.quantity = '';

              
                $scope.uuid = uuid;
                

                /* bind functions.. */
               
                // retrieve stocks (if any) associated to the item
                
             


            
                /* bind functions to scope */
               
             //   $scope.retireUnretire = self.retireUnretire;
                $scope.delete = self.delete;
              
                // call functions..
                ConsumptionRestfulService.loadDepartments(self.onLoadDepartmentsSuccessful);
                ConsumptionRestfulService.loadItems(slf.onLoadItemsSuccessful);
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
        self.validateBeforeSaveOrUpdate = self.validateBeforeSaveOrUpdate || function(){
                $scope.submitted = false;
               

                // validate quantity
               
                if(!angular.isDefined($scope.entity.quantity) || $scope.entity.quantity === ''){
                    $scope.submitted = true;
                    emr.errorAlert(emr.message("openhmis.commons.general.required.quantity"));
                }
                
                //validate item
                 if(!angular.isDefined($scope.entity.item) || $scope.entity.quantity === ''){
                    $scope.submitted = true;
                    emr.errorAlert(emr.message("openhmis.commons.general.required.item"));
                }
                


           

                if($scope.submitted){
                    return false;
                }

          

                // bind department uuid
                var department = $scope.department;
                if(angular.isDefined(department)){
                    $scope.entity.department = department.uuid;
                }
                
                //bind item uuid
                var item = $scope.item;
                if(angular.isDefined(item)){
                    $scope.entity.item = item.uuid;
                }

                $scope.loading = true;
                return true;
            }

        // @Override
        self.setAdditionalMessageLabels = self.setAdditionalMessageLabels || function(){
                return ConsumptionFunctions.addMessageLabels();
            }

        /* call-back functions. */
        // handle returned department list
        self.onLoadDepartmentsSuccessful = self.onLoadDepartmentsSuccessful || function(data){
            if(angular.isDefined($scope.entity)){
                $scope.departments = data.results;
                $scope.department = $scope.department || $scope.departments[0];
            }
        }
        
         /* call-back functions. */
        // handle returned department list
        self.onLoadItemsSuccessful = self.onLoadItemsSuccessful || function(data){
            if(angular.isDefined($scope.entity)){
                $scope.items = data.results;
                $scope.item = $scope.item || $scope.items[0];
            }
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
