
(function () {
    'use strict';

    var base = angular.module('app.genericManageController');
    base.controller("ManageConsumptionSummaryController", ManageConsumptionSummaryController);
    ManageConsumptionSummaryController.$inject = ['$injector', '$scope', '$filter',
        'EntityRestFactory', 'CssStylesFactory', 'PaginationService',
        'ConsumptionSummaryModel', 'CookiesService', 'ConsumptionSummaryRestfulService', 'ConsumptionSummaryFunctions'];

    function ManageConsumptionSummaryController($injector, $scope, $filter,
            EntityRestFactory, CssStylesFactory, PaginationService, ConsumptionSummaryModel,
            CookiesService, ConsumptionSummaryRestfulService, ConsumptionSummaryFunctions) {

        var self = this;

        var entity_name = emr.message("openhmis.inventory.consumptionSummary.name");
        var REST_ENTITY_NAME = "consumptionSummary";

        // @Override
        self.getModelAndEntityName = self.getModelAndEntityName
                || function () {
                    self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME,
                            entity_name);
                    self.checkPrivileges(TASK_MANAGE_METADATA);
                }

        self.bindExtraVariablesToScope = self.bindExtraVariablesToScope || function () {
            self.loadDepartments();
            self.loadItems();
            $scope.searchConsumptionSummarys = self.searchConsumptionSummarys;
            //	$scope.searchItemsByName = self.searchItemsByName;
            //	$scope.searchField = CookiesService.get('searchField') || $scope.searchField || '';
            $scope.department = CookiesService.get('department') || {};
           // $scope.item = CookiesService.get('item') || {};
            $scope.startDate = CookiesService.get('startDate') || {};
            $scope.endDate = CookiesService.get('endDate') || {};

            ConsumptionSummaryFunctions.onChangeDatePicker('startDate-display', function (value) {
                $scope.startDate = ConsumptionSummaryFunctions.formatDate(value);
                console.log('before format: '+value);
                console.log('after format: '+$scope.startDate);
            });

            ConsumptionSummaryFunctions.onChangeDatePicker('endDate-display', function (value) {
                $scope.endDate = ConsumptionSummaryFunctions.formatDate(value);;
                 console.log('before format: '+value);
                console.log('after format: '+$scope.startDate);
            });

        }

        self.loadDepartments = self.loadDepartments || function () {
            ConsumptionSummaryRestfulService.loadDepartments(self.onLoadDepartmentsSuccessful);
        }

        self.loadItems = self.loadItems || function () {
            ConsumptionSummaryRestfulService.loadItems(self.onLoadItemsSuccessful);
        }



//		self.searchItemsByName = self.searchItemsByName || function(currentPage){
//				// reset current page when the search field is cleared
//				if($scope.searchField === undefined || $scope.searchField === ''){
//					currentPage = 1;
//					$scope.currentPage = currentPage;
//				}
//				self.searchItems(currentPage);
//			}



function checkParameters(parameterObject) {
            var objectKeys = Object.keys(parameterObject);
            for (var i = 0; i < objectKeys.length; i++) {
                var name = objectKeys[i];
                var value = parameterObject[objectKeys[i]];
                if (!value || value === {} ) {
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


        self.searchConsumptionSummarys = self.searchConsumptionSummarys || function (currentPage) {
            
             var parametersAreValid = checkParameters({
                "startDate": $scope.startDate,
                "endDate": $scope.endDate
            });
            
            
            if(parametersAreValid){
                   //	CookiesService.set('searchField', $scope.searchField);
            CookiesService.set('startIndex', $scope.startIndex);
            CookiesService.set('limit', $scope.limit);
            CookiesService.set('includeRetired', $scope.includeRetired);
            CookiesService.set('currentPage', currentPage);
            CookiesService.set('department', $scope.department);
           // CookiesService.set('item', $scope.item);
            CookiesService.set('startDate', $scope.startDate);
            CookiesService.set('endDate', $scope.endDate);

            var department_uuid;
          //  var item_uuid;

            if ($scope.department !== null) {
                department_uuid = $scope.department.uuid;
            }

//            if ($scope.item !== null) {
//                item_uuid = $scope.item.uuid;
//            }


            //var searchField = $scope.searchField || '';
            
         //    ConsumptionSummaryRestfulService.searchConsumptionSummarys(currentPage, $scope.limit, department_uuid, item_uuid,$scope.startDate,$scope.endDate, $scope.includeRetired, self.onLoadConsumptionSummarysSuccessful)

            ConsumptionSummaryRestfulService.searchConsumptionSummarys(currentPage, $scope.limit, department_uuid,$scope.startDate,$scope.endDate, $scope.includeRetired, self.onLoadConsumptionSummarysSuccessful); 
                
            }else{
                
                alert('select a startDate and endDate to continue!');
            }
            
        
        }

        self.onLoadConsumptionSummarysSuccessful = self.onLoadConsumptionSummarysSuccessful || function (data) {
            $scope.fetchedEntities = data.results;
            $scope.totalNumOfResults = data.length;
        }

        self.onLoadDepartmentsSuccessful = self.onLoadDepartmentsSuccessful || function (data) {
            $scope.departments = data.results;
        }


        self.onLoadItemsSuccessful = self.onLoadItemsSuccessful || function (data) {
            $scope.items = data.results;
        }

        /* ENTRY POINT: Instantiate the base controller which loads the page */
        $injector.invoke(base.GenericManageController, self, {
            $scope: $scope,
            $filter: $filter,
            EntityRestFactory: EntityRestFactory,
            PaginationService: PaginationService,
            CssStylesFactory: CssStylesFactory,
            GenericMetadataModel: ConsumptionSummaryModel,
            CookiesService: CookiesService
        });
    }
})();
