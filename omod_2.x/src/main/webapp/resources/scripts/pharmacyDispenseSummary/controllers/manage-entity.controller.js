
(function () {
    'use strict';

    var base = angular.module('app.genericManageController');
    base.controller("ManageARVPharmacyDispenseController", ManageARVPharmacyDispenseController);
    ManageARVPharmacyDispenseController.$inject = ['$injector', '$scope', '$filter',
        'EntityRestFactory', 'CssStylesFactory', 'PaginationService',
        'ARVPharmacyDispenseModel', 'CookiesService', 'ARVPharmacyDispenseRestfulService', 'ARVPharmacyDispenseFunctions'];

    function ManageARVPharmacyDispenseController($injector, $scope, $filter,
            EntityRestFactory, CssStylesFactory, PaginationService, ARVPharmacyDispenseModel,
            CookiesService, ARVPharmacyDispenseRestfulService, ARVPharmacyDispenseFunctions) {

        var self = this;

        var entity_name = emr.message("openhmis.inventory.pharmacyConsumptionSummary.name");
        var REST_ENTITY_NAME = "pharmacyDispenseSummary";

        // @Override
        self.getModelAndEntityName = self.getModelAndEntityName
                || function () {
                    self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME,
                            entity_name);
                    self.checkPrivileges(TASK_MANAGE_METADATA);
                }

        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope || function () {
           // self.loadDepartments();
          // self.loadItems();
            $scope.searchDispenseSummarys = self.searchDispenseSummarys;
        
            //	$scope.searchItemsByName = self.searchItemsByName;
            	$scope.searchField = CookiesService.get('searchField') || $scope.searchField || '';
      //      $scope.department = CookiesService.get('department') || {};
           // $scope.item = CookiesService.get('item') || {};
            $scope.startDate = CookiesService.get('startDate') || {};
            $scope.endDate = CookiesService.get('endDate') || {};

            ARVPharmacyDispenseFunctions.onChangeDatePicker('startDate-display', function (value) {
                $scope.startDate = ARVPharmacyDispenseFunctions.formatDate(value);
                console.log('before format: '+value);
                console.log('after format: '+$scope.startDate);
            });

            ARVPharmacyDispenseFunctions.onChangeDatePicker('endDate-display', function (value) {
                $scope.endDate = ARVPharmacyDispenseFunctions.formatDate(value);;
                 console.log('before format: '+value);
                console.log('after format: '+$scope.startDate);
            });

        }

        self.loadDepartments = self.loadDepartments || function () {
            ARVPharmacyDispenseRestfulService.loadDepartments(self.onLoadDepartmentsSuccessful);
        }

        self.loadItems = self.loadItems || function () {
            ARVPharmacyDispenseyRestfulService.loadItems(self.onLoadItemsSuccessful);
        }

      

        self.searchDispenseSummarys = self.searchDispenseSummarys || function (currentPage) {
               console.log('about to call pharm ARV Dispense controller');
            
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

            var department_uuid;
          //  var item_uuid;

//            if ($scope.department !== null) {
//                department_uuid = $scope.department.uuid;
//            }

//            if ($scope.item !== null) {
//                item_uuid = $scope.item.uuid;
//            }


            // var searchField = $scope.searchField || '';
            
         	// ConsumptionSummaryRestfulService.searchConsumptionSummarys(currentPage, $scope.limit, department_uuid, item_uuid,$scope.startDate,$scope.endDate, $scope.includeRetired, self.onLoadConsumptionSummarysSuccessful)

            ARVPharmacyDispenseRestfulService.searchDispenseSummarys(currentPage, $scope.limit, department_uuid,$scope.startDate,$scope.endDate, $scope.includeRetired, self.onLoadARVPharmacyDispenseSuccessful)
        }

        self.onLoadARVPharmacyDispenseSuccessful = self.onLoadARVPharmacyDispenseSuccessful || function (data) {
            $scope.fetchedEntities = data.results;
            $scope.totalNumOfResults = data.length;
            console.log(data.results);
            console.log(data.results[0]);
        }

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericManageController, self, {
            $scope: $scope,
            $filter: $filter,
            EntityRestFactory: EntityRestFactory,
            PaginationService: PaginationService,
            CssStylesFactory: CssStylesFactory,
            GenericMetadataModel: ARVPharmacyDispenseModel,
            CookiesService: CookiesService
        });
    }
    
})();
