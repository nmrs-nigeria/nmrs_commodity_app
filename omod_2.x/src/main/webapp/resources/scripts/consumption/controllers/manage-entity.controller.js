
(function() {
	'use strict';

	var base = angular.module('app.genericManageController');
	base.controller("ManageConsumptionController", ManageConsumptionController);
	ManageConsumptionController.$inject = ['$injector', '$scope', '$filter',
			'EntityRestFactory', 'CssStylesFactory', 'PaginationService',
			'ConsumptionModel', 'CookiesService', 'ConsumptionRestfulService'];

	function ManageConsumptionController($injector, $scope, $filter,
			EntityRestFactory, CssStylesFactory, PaginationService, ConsumptionModel,
			CookiesService, ConsumptionRestfulService) {

		var self = this;

		var entity_name = emr.message("openhmis.inventory.consumption.name");
		var REST_ENTITY_NAME = "consumption";

		// @Override
		self.getModelAndEntityName = self.getModelAndEntityName
				|| function() {
					self.bindBaseParameters(INVENTORY_MODULE_NAME, REST_ENTITY_NAME,
						entity_name);
					 self.checkPrivileges(TASK_MANAGE_METADATA);
				}

		self.bindExtraVariablesToScope = self.bindExtraVariablesToScope || function() {
				
				self.loadDepartments();
                                self.loadItems();
				$scope.searchConsumptions = self.searchConsumptions;
			//	$scope.searchItemsByName = self.searchItemsByName;
			//	$scope.searchField = CookiesService.get('searchField') || $scope.searchField || '';
				$scope.department = CookiesService.get('department') || {};
                $scope.item = CookiesService.get('item') || {};
                $scope.deletePopupDialog = self.deletePopupDialog;
                $scope.selectedEntity = '';
                $scope.deleteConsumption = self.deleteConsumption;                
			}

		self.loadDepartments = self.loadDepartments || function(){
				ConsumptionRestfulService.loadDepartments(self.onLoadDepartmentsSuccessful);
			}
                        
                        self.loadItems = self.loadItems || function(){
				ConsumptionRestfulService.loadItems(self.onLoadItemsSuccessful);
			}

//		self.searchItemsByName = self.searchItemsByName || function(currentPage){
//				// reset current page when the search field is cleared
//				if($scope.searchField === undefined || $scope.searchField === ''){
//					currentPage = 1;
//					$scope.currentPage = currentPage;
//				}
//				self.searchItems(currentPage);
//			}

		self.searchConsumptions = self.searchConsumptions || function(currentPage){
			//	CookiesService.set('searchField', $scope.searchField);
				CookiesService.set('startIndex', $scope.startIndex);
				CookiesService.set('limit', $scope.limit);
				CookiesService.set('includeRetired', $scope.includeRetired);
				CookiesService.set('currentPage', currentPage);
				CookiesService.set('department', $scope.department);
                                CookiesService.set('item', $scope.item);
                                

				var department_uuid;
                                var item_uuid;
                                
				if($scope.department !== null){
					department_uuid = $scope.department.uuid;
				}
                                
                                if($scope.item !== null){
					item_uuid = $scope.item.uuid;
				}


				//var searchField = $scope.searchField || '';

				ConsumptionRestfulService.searchConsumptions(currentPage, $scope.limit, department_uuid,item_uuid, $scope.includeRetired, self.onLoadConsumptionsSuccessful)
			}

		self.onLoadConsumptionsSuccessful = self.onLoadConsumptionsSuccessful || function(data){
				$scope.fetchedEntities = data.results;
				$scope.totalNumOfResults = data.length;
			}

		self.onLoadDepartmentsSuccessful = self.onLoadDepartmentsSuccessful || function(data){
				$scope.departments = data.results;
			}
                        
                        
		self.onLoadItemsSuccessful = self.onLoadItemsSuccessful || function(data){
				$scope.items = data.results;
			}

		 self.deletePopupDialog = self.deletePopupDialog || function (popupid, entuuid) {
                console.log("in manage controller");
				console.log('popup id: ' + popupid);
                console.log('entity uuid: ' + entuuid);      
                var dialog = emr.setupConfirmationDialog({
                	selector: '#' + popupid,
                	actions: {
                		confirm: function ($scope) {	     
                            console.log("after confirm");
                            var deleteParam = entuuid;
                            console.log(deleteParam);
                            ConsumptionRestfulService.deleteConsumption(deleteParam, self.deleteConsumptionSuccessful);
                           // ConsumptionRestfulService.setBaseUrl(INVENTORY_MODULE_NAME);   
	                        dialog.close();
	                    },                      		
                    	cancel: function () {
                        	dialog.close();
                    	}
                	}
            	});
            	dialog.show();
            }
            
            self.deleteConsumption = self.deleteConsumption || function (selectEnt) {
				console.log("in manage controller");
				var deleteParam = selectEnt;
				console.log(deleteParam);
				ConsumptionRestfulService.deleteConsumption(deleteParam,self.deleteConsumptionSuccessful);
				ConsumptionRestfulService.setBaseUrl(INVENTORY_MODULE_NAME);
			}

            self.deleteConsumptionSuccessful = self.deleteConsumptionSuccessful || function(data){
                console.log(data.results);
            }
            
		/* ENTRY POINT: Instantiate the base controller which loads the page */
		$injector.invoke(base.GenericManageController, self, {
			$scope : $scope,
			$filter : $filter,
			EntityRestFactory : EntityRestFactory,
			PaginationService : PaginationService,
			CssStylesFactory : CssStylesFactory,
			GenericMetadataModel : ConsumptionModel,
			CookiesService : CookiesService
		});
	}
})();
